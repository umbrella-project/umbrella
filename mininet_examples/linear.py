from mininet.net import Mininet
from mininet.topo import Topo
from mininet.cli import CLI
from mininet.node import RemoteController


class LinearTopo(Topo):
    def __init__(self):

        Topo.__init__(self)

        linear = []
        hosts = []
        n = 100
        for i in range(0, n):
            sw = self.addSwitch("s%s" % i)
            linear.append(sw)
            if i == 0 or i == n - 1:
                host = self.addHost("h" + str(i))
                hosts.append(host)
                self.addLink(sw, host, intfName1=host + 'eth0')

        for i in range(1, n):
            self.addLink(linear[i], linear[i - 1])


if __name__ == '__main__':
    topo = LinearTopo()
    net = Mininet(topo, autoSetMacs=True, xterms=False, controller=RemoteController)
    net.addController('c', ip='127.0.0.1')  # localhost:127.0.0.1 vm-to-mac:10.0.2.2 server-to-mac:128.112.93.28
    print "\nHosts configured with IPs, switches pointing to OpenVirteX at 128.112.93.28 port 6633\n"
    net.start()
    CLI(net)
    net.stop()
