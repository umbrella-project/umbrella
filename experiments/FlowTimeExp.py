import sys
import time
from functools import partial
from mininet.net import Mininet
from mininet.node import OVSKernelSwitch, RemoteController
from mininet.topo import Topo
from mininet.util import irange
from subprocess import call


class LinearTopo(Topo):
    def __init__(self, N, **params):

        Topo.__init__(self, params)

        # hosts = [ self.addHost('h%s' % h) for h in irange(1, N) ]


        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        switches = [self.addSwitch('s%s' % s) for s in irange(1, N)]

        last = None;
        for s in switches:
            if last:
                self.addLink(last, s)
            last = s

        # i = 0
        # for h in hosts:
        #    self.addLink(h, switches[i])
        #    i = i + 1

        self.addLink(switches[0], h1)
        self.addLink(switches[N - 1], h2)


if __name__ == "__main__":

    if len(sys.argv) < 2:
        exit()

    cname = sys.argv[1]
    print 'Controller: ', cname

    N = 3

    if len(sys.argv) > 2:
        N = int(sys.argv[2])

    print "Generating a linear topology of %s hosts" % N

    topo = LinearTopo(N)

    net = Mininet(topo=topo, switch=OVSKernelSwitch, controller=partial(RemoteController, ip='127.0.0.1', port=6633),
                  autoSetMacs=True)

    net.start()

    time.sleep(3)

    net.pingAll()

    hosts = net.hosts;

    h1 = None
    hN = None
    for h in hosts:
        if h.name == 'h1':
            h1 = h

        if h.name == 'h2':
            hN = h

    switches = net.switches

    for s in switches:
        s.dpctl('del-flows', 'ip,nw_proto=137')

    call(['java', '-cp', '../target/sddf-1.0-SNAPSHOT-jar-with-dependencies.jar', 'apps.FlowTimeExperiment',
          '%s' % cname])

    # CLI(net)

    hN.cmd('./capture > capture-output &')
    h1.cmd("./generator %s &" % 2)

    time.sleep(2)

    call(['java', '-cp', '../target/sddf-1.0-SNAPSHOT-jar-with-dependencies.jar', 'apps.FlowTimeExperiment',
          '%s' % cname, '%s' % N])

    time.sleep(15)

    received = hN.cmd('cat capture-output')

    print 'Received: ', received

    time.sleep(30)

    # CLI(net)
    net.stop()
