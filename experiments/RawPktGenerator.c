#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <net/ethernet.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netpacket/packet.h>

#define	IPLEN	50

int	main (
		int	argc,
		char	*argv[]
	     )
{
	int	sock;
	uint8_t lastHost;
	struct	iphdr *ip;
	struct	timeval t1, t2, td;
	struct	sockaddr_in sockDstAddr;
	int	slpTime;
	int	err;

	if (argc < 2) {
		printf("required argument: number of hosts\n");
		printf("%s <number_of_hosts>\n", argv[0]);
		exit(1);
	}

	lastHost = atoi(argv[1]);

	sock = socket(AF_INET, SOCK_RAW, IPPROTO_RAW);
	if (sock == -1) {
		perror("socket()");
		exit(1);
	}

	ip = (struct iphdr *)malloc(20 + 4 + IPLEN);
	if(!ip) {
		perror("malloc()");
		exit(1);
	}

	gettimeofday(&t1, NULL);

	int i;
	for(i = 0; i < 10000; i++) {

		memset(ip, 0, sizeof(*ip));

		ip->ihl = 0x5;
		ip->version = 0x4;
		ip->tot_len = 20 + IPLEN;
		ip->ttl = 255;
		ip->protocol = 0x89;
		ip->saddr = 0x0100000a;
		ip->daddr = 0x0000000a | ((uint32_t)lastHost << 24);

		memset(&sockDstAddr, 0, sizeof(sockDstAddr));

		sockDstAddr.sin_family = AF_INET;
		sockDstAddr.sin_addr.s_addr = ip->daddr;

		gettimeofday(&t2, NULL);
		timersub(&t2, &t1, &td);

		slpTime = 1000 - ((td.tv_sec * 1000000) + td.tv_usec) - 50;

		if(slpTime > 0) {
			usleep(slpTime);
		}

		gettimeofday(&t1, NULL);

		err = sendto(sock, ip, 20 + 4 + IPLEN, 0, (struct sockaddr *)&sockDstAddr, sizeof(sockDstAddr));
		if(err < 0) {
			perror("sendto()");
			exit(1);
		}
	}

	free(ip);

	return 0;
}
