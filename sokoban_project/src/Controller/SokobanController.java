package Controller;

import Model.EnumAlg;
import Model.Instance;

public class SokobanController {

    private Instance instance;

    public String returnImageName(int row, int column){
        if (instance != null)
            return instance.getMap()[row][column];
        else return "";
    }

    public int returnRowCount(){
        if (instance != null)
            return instance.getMap().length;
        else return 0;
    }

    public int returnColumnCount(){
        if (instance != null)
            return instance.getMap()[0].length;
        else return 0;
    }

    public String[] getAlgorithms() {
        String[] algorithms = new String[EnumAlg.values().length];

        for (EnumAlg e : EnumAlg.values())
            algorithms[e.ordinal()] = e.label;

        return algorithms;
    }
}
