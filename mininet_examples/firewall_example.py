from mininet.net import Mininet
from mininet.topo import Topo
from mininet.cli import CLI
from mininet.node import RemoteController


class MultiPath(Topo):
    def __init__(self, **opts):
        "Create a multipath topology."
        Topo.__init__(self, **opts)

        # Add spine switches


        switches = {}
        for s in range(4):
            switches[s] = self.addSwitch('s%s' % (s + 1))
        # Set link speeds to 100Mb/s
        linkopts = dict()

        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        h3 = self.addHost('h3')

        h4 = self.addHost('h4')
        self.addLink(switches[0], switches[1])
        self.addLink(switches[0], switches[2])
        self.addLink(switches[1], switches[3])
        self.addLink(switches[2], switches[3])

        self.addLink(switches[0], h1)
        self.addLink(switches[1], h2)
        self.addLink(switches[2], h3)
        self.addLink(switches[3], h4)


if __name__ == '__main__':
    topo = MultiPath()
    net = Mininet(topo, autoSetMacs=True, xterms=False, controller=RemoteController)
    net.addController('c', ip='127.0.0.1')  # localhost:127.0.0.1 vm-to-mac:10.0.2.2 server-to-mac:128.112.93.28
    print "\nHosts configured with IPs, switches pointing to OpenVirteX at 128.112.93.28 port 6633\n"
    net.start()
    CLI(net)
    net.stop()
