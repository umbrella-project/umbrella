package drivers.ryu;

import drivers.controller.Controller;
import drivers.ryu.flowservice.impl.RyuFlowService;
import drivers.ryu.topostore.impl.RyuTopoStore;

public class RyuController extends Controller {

    public RyuController()
    {
        topoStore = new RyuTopoStore();

        topoStore.fetchTopo();

        flowService = new RyuFlowService();




    }
}
