package Controller;

import Model.EnumAlg;
import Model.Instance;
import Utilities.InstanceReader;

import java.util.ArrayList;
import java.util.List;

public class SokobanController {

    private Instance instance;
    private List<SokobanObserver> observers;

    private void notifyReadSuccess(){
        for (SokobanObserver obs : observers)
            obs.instanceReadSuccess();
    }

    private void notifyReadFail(){
        for (SokobanObserver obs : observers)
            obs.instanceReadFail();
    }

    public SokobanController(){
        observers = new ArrayList<>();
    }

    public void observe(SokobanObserver observer){
        observers.add(observer);
    }

    public void stopObserve(SokobanObserver observer){
        observers.remove(observer);
    }

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

    public void readInstance(String archive){
        instance = InstanceReader.readInstance(archive);

        if (instance != null)
            notifyReadSuccess();
        else notifyReadFail();
    }

    public void runGame(int algorithm){

    }
}
