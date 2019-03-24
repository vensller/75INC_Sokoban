package Controller;

import Model.EnumAlg;
import Model.Instance;
import Model.SokobanState;
import Utilities.InstanceReader;
import busca.BuscaLargura;
import busca.Nodo;

import java.util.ArrayList;
import java.util.List;

public class SokobanController {

    private Instance instance;
    private List<SokobanObserver> observers;
    private List<Instance> solution;

    private void notifyReadSuccess(){
        for (SokobanObserver obs : observers)
            obs.instanceReadSuccess();
    }

    private void notifyReadFail(){
        for (SokobanObserver obs : observers)
            obs.instanceReadFail();
    }

    private void notifySolutionNotFound(){
        for (SokobanObserver obs : observers)
            obs.solutionNotFound();
    }

    private void notifyRefreshScreen(int solutionCount){
        for (SokobanObserver obs : observers)
            obs.refreshTable(solutionCount);
    }

    public SokobanController(){
        observers = new ArrayList<>();
    }

    public void observe(SokobanObserver observer){
        observers.add(observer);
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
        instance = InstanceReader.readInstanceFromFile(archive);

        if (instance != null)
            notifyReadSuccess();
        else notifyReadFail();
    }

    public void runGame(int alg){
        EnumAlg algorithm = EnumAlg.values()[alg];

        if (instance != null)
            switch (algorithm){
                case BREADTH_FIRST:{
                    Nodo nodo = new BuscaLargura().busca(new SokobanState(instance.getMap()));

                    if (nodo != null) {
                        solution = InstanceReader.readInstanceFromText(nodo.montaCaminho());
                        notifyRefreshScreen(solution.size());

                    }else notifySolutionNotFound();

                    break;
                }
                case DEPTH:{
                    break;
                }
                case BIDIRECTIONAL:{
                    break;
                }
                case MOUNTAINCLIMB:{
                    break;
                }
                case ASTAR:{
                    break;
                }
            }
    }

    public void changeSolution(int solutionIndex){
        instance = solution.get(solutionIndex);
    }
}
