package apps.tests;

import drivers.ryu.RyuController;

public class TestRyuTopology {

    public static void main(String[] args) {

        RyuController ryuController = new RyuController();

        ryuController.printTopology();

    }






}
