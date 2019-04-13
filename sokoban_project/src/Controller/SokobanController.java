package Controller;

import Model.EnumAlg;
import Model.Instance;
import Model.SokobanState;
import Model.StateObserver;
import Utilities.InstanceReader;
import Utilities.SolutionWriter;
import busca.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SokobanController implements StateObserver {

    private List<Instance> instances;
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
        System.out.println(countStateCreated);
    }

    private void notifyStateVisited(String stateLog){
        System.out.println(stateLog);
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

    public void readInstancesFromPath(String path){
        instances = InstanceReader.readInstancesFromPath(path);

        if (instances != null) {
            instance = instances.get(0);
            notifyReadSuccess();
        }
        else notifyReadFail();
    }

    public void runGame(int alg){
        EnumAlg algorithm = EnumAlg.values()[alg];
        countStateCreated = 0;
        countStateVisited = 0;
        Nodo nodo = null;

        if (instance != null) {
            switch (algorithm) {
                case BREADTH_FIRST:
                    nodo = new BuscaLargura(new MostraStatusConsole()).busca(new SokobanState(instance.getMap(), this, new ArrayList<>()));
                    break;
                case DEPTH:
                    nodo = new BuscaProfundidade(new MostraStatusConsole()).busca(new SokobanState(instance.getMap(), this, new ArrayList<>()));
                    break;
                case ITERATIVEDEPTH:
                    nodo = new BuscaIterativo(new MostraStatusConsole()).busca(new SokobanState(instance.getMap(), this, new ArrayList<>()));
                    break;
                case ASTAR:
                    nodo = new AEstrela(new MostraStatusConsole()).busca(new SokobanState(instance.getMap(), this, new ArrayList<>()));
                    break;
            }

            if (nodo != null) {
                SolutionWriter.writeLog("Solução encontrada!");
                solution = InstanceReader.readInstanceFromText(nodo.montaCaminho());
                notifyRefreshScreen(solution.size());
            } else {
                SolutionWriter.writeLog("Solução não encontrada!");
                notifySolutionNotFound();
            }
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
    public void stateVisited(String stateLog) {
        countStateVisited++;
        notifyStateVisited(stateLog);
    }

    public void run() {
        for (Instance i : instances){
            instance = i;
            SolutionWriter.writeLog("Level " + instances.indexOf(i) + ". Busca em Largura");
            runGame(0);
            SolutionWriter.writeLog("Level " + instances.indexOf(i) + ". Busca em Profundidade");
            runGame(1);
            SolutionWriter.writeLog("Level " + instances.indexOf(i) + ". Busca em Profundidade Iterativa");
            runGame(2);
            SolutionWriter.writeLog("Level " + instances.indexOf(i) + ". A*");
            runGame(3);
        }
    }
}
