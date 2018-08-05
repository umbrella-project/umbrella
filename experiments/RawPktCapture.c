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
#include <pthread.h>

int	count = 0;

void	*captureThreadFn (void *);

int	main (
		int	argc,
		char	*argv[]
	     )
{
	pthread_t	captureThread;
	int		err;

	err = pthread_create(&captureThread, NULL, captureThreadFn, NULL);

	sleep(15);

	printf("%d\n", count);

	return 0;
}

void	*captureThreadFn (
		void	*arg
		)
{

	int	sock;
	char	*pkt;
	struct	sockaddr_ll sockRecvAddr;
	struct	iphdr *ip;
	socklen_t recvAlen;
	int	err;

	sock = socket(AF_PACKET, SOCK_RAW, htons(0x0800));
	if (sock == -1) {
		perror("socket()");
		exit(1);
	}

	pkt = (char *)malloc(1518);
	if(!pkt) {
		perror("malloc()");
		exit(1);
	}

	while(1) {

		recvAlen = sizeof(sockRecvAddr);
		err = recvfrom(sock, pkt, 1514, 0, (struct sockaddr *)&sockRecvAddr, &recvAlen);
		if(err < 0) {
			perror("recvfrom()");
			exit(1);
		}

		ip = (struct iphdr *)(pkt + 14);

		if(ip->protocol != 0x89) {
			continue;
		}

		count++;
	}

	return NULL;
}
