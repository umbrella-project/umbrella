package drivers.ryu;

import drivers.controller.Controller;
import drivers.ryu.topostore.impl.RyuTopoStore;

public class RyuController extends Controller {

    public RyuController()
    {
        topoStore = new RyuTopoStore();

        topoStore.fetchTopo();






    }
}
