package Controller;

import Model.EnumAlg;
import Model.Instance;
import Model.SokobanState;
import Model.StateObserver;
import Utilities.InstanceReader;
import busca.*;

import java.util.ArrayList;
import java.util.List;

public class SokobanController implements StateObserver {

    private Instance instance;
    private List<SokobanObserver> observers;
    private List<Instance> solution;
    private int countStateCreated;
    private int countStateVisited;

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

    private void notifyStateCreated(){
        for (SokobanObserver obs : observers)
            obs.stateCreated(countStateCreated);
    }

    private void notifyStateVisited(){
        for (SokobanObserver obs : observers)
            obs.stateVisited(countStateVisited);
    }

    private void notifyCleanLog(){
        for (SokobanObserver obs : observers)
            obs.cleanLog();
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
        countStateCreated = 0;
        countStateVisited = 0;
        notifyCleanLog();
        Nodo nodo = null;

        if (instance != null) {
            switch (algorithm) {
                case BREADTH_FIRST:
                    nodo = new BuscaLargura().busca(new SokobanState(instance.getMap(), this));
                    break;
                case DEPTH:
                    nodo = new BuscaProfundidade().busca(new SokobanState(instance.getMap(), this));
                    break;
                case ITERATIVEDEPTH:
                    nodo = new BuscaIterativo().busca(new SokobanState(instance.getMap(), this));
                    break;
                case ASTAR:
                    nodo = new AEstrela().busca(new SokobanState(instance.getMap(), this));
                    break;
            }

            if (nodo != null) {
                solution = InstanceReader.readInstanceFromText(nodo.montaCaminho());
                notifyRefreshScreen(solution.size());
            } else notifySolutionNotFound();
        }
    }

    public void changeSolution(int solutionIndex){
        instance = solution.get(solutionIndex);
    }

    @Override
    public void stateCreated() {
        countStateCreated++;
        notifyStateCreated();
    }

    @Override
    public void stateVisited() {
        countStateVisited++;
        notifyStateVisited();
    }
}
