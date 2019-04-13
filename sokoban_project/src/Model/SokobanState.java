package Model;

import busca.Estado;
import busca.Heuristica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SokobanState implements Estado, Heuristica {

    private String[][] map;
    private int rowPos;
    private int columnPos;
    private StateObserver observer;
    private List<SokobanState> visited;
    private List<Coordinate> boxes;
    private List<Coordinate> objectives;

    private boolean findPlayerPosition(){
        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[x].length; y++)
                if (map[x][y].equals("+") || map[x][y].equals(".+")){
                    rowPos = x;
                    columnPos = y;
                    return true;
                }

        return false;
    }

    private boolean havePossibilityToFindPoint(int row, int column, String[][] cloneMap){
        return !((cloneMap[row + 1][column].equals("#") && ((cloneMap[row][column - 1].equals("#")) || (cloneMap[row][column + 1].equals("#")))) //Linha abaixo e coluna esquerda ou direita tem parede
                || (cloneMap[row - 1][column].equals("#") && ((cloneMap[row][column - 1].equals("#")) || (cloneMap[row][column + 1].equals("#"))))); //linha acima e coluna esquerda ou direita tem parede
    }

    private boolean changeBoxPos(int rowBox, int columnBox, EnumMov movement, String[][] cloneMap){
        boolean canChange = false;

        int newRow = rowBox;
        int newColumn = columnBox;

        switch (movement){
            case UP:{
                newRow--;
                break;
            }
            case DOWN:{
                newRow++;
                break;
            }
            case LEFT:{
                newColumn--;
                break;
            }
            case RIGHT:{
                newColumn++;
                break;
            }
        }

        String newPosAux = cloneMap[newRow][newColumn];
        String newPos = "$";

        if (newPosAux.equals(" "))
            canChange = true;

        if (newPosAux.equals(".")){
            canChange = true;
            newPos = ".$";
        }

        if (canChange){
            cloneMap[newRow][newColumn] = newPos;
            cloneMap[rowBox][columnBox] = " ";
        }

        return canChange;
    }

    private boolean changePlayerPos(int newRow, int newColumn, EnumMov movement, String[][] cloneMap, String actualPos){
        boolean canChange = false;
        String newPosAux = cloneMap[newRow][newColumn];
        String newPos = "+";
        String oldPos = " ";

        if (actualPos.equals(".+"))
            oldPos = ".";

        if (newPosAux.equals(" "))
            canChange = true;

        if (newPosAux.equals(".")){
            canChange = true;
            newPos = ".+";
        }

        if (newPosAux.equals("$"))
            canChange = changeBoxPos(newRow, newColumn, movement, cloneMap);

        if (canChange) {
            cloneMap[newRow][newColumn] = newPos;
            cloneMap[rowPos][columnPos] = oldPos;
        }

        return canChange;
    }

    private boolean stateVisited(SokobanState state){
        for (SokobanState st : visited)
            if (st.equals(state))
                return true;

        return false;
    }

    private void movePlayerUp(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((rowPos > 0)
                && (changePlayerPos(rowPos -1, columnPos, EnumMov.UP, cloneMap, cloneMap[rowPos][columnPos]))) {
            SokobanState state = new SokobanState(cloneMap, observer, visited);

            if (!stateVisited(state) && !state.containDeadLock()) {
                list.add(state);
                visited.add(state);
            }
        }
    }

    private boolean containDeadLock(){
        for (Coordinate coord : boxes){
            if (map[coord.getX() + 1][coord.getY()].equals("#")
                    && (map[coord.getX()][coord.getY() - 1]).equals("#")
                        || map[coord.getX()][coord.getY() + 1].equals("#"))
                return true;

            if (map[coord.getX() - 1][coord.getY()].equals("#")
                    && (map[coord.getX()][coord.getY() + 1].equals("#")
                        || map[coord.getX()][coord.getY() + 1].equals("#")))
                return true;
        }

        return false;
    }

    private void movePlayerDown(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((rowPos + 1 < map.length)
                && (changePlayerPos(rowPos + 1, columnPos, EnumMov.DOWN, cloneMap, cloneMap[rowPos][columnPos]))){
            SokobanState state = new SokobanState(cloneMap, observer, visited);

            if (!stateVisited(state)) {
                list.add(state);
                visited.add(state);
            }
        }
    }

    private void movePlayerLeft(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((columnPos > 0)
                && (changePlayerPos(rowPos, columnPos - 1, EnumMov.LEFT, cloneMap, cloneMap[rowPos][columnPos]))) {
            SokobanState state = new SokobanState(cloneMap, observer, visited);

            if (!stateVisited(state)) {
                list.add(state);
                visited.add(state);
            }
        }
    }

    private void movePlayerRight(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((columnPos + 1 < map[0].length)
                && (changePlayerPos(rowPos, columnPos + 1, EnumMov.RIGHT, cloneMap, cloneMap[rowPos][columnPos]))) {
            SokobanState state = new SokobanState(cloneMap, observer, visited);

            if (!stateVisited(state)) {
                list.add(state);
                visited.add(state);
            }
        }
    }

    private String log(){
        String result = "";
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y < map[0].length; y++)
                result += map[x][y];

            result += "\n";
        }

        return result;
    }

    private void createBoxesList(){
        boxes = new ArrayList<>();

        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[x].length; y++)
                if (map[x][y].equals("$"))
                    boxes.add(new Coordinate(x, y));
    }

    private void createObjectives(){
        objectives = new ArrayList<>();

        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[x].length; y++)
                if (map[x][y].equals(".") || map[x][y].equals(".+"))
                    objectives.add(new Coordinate(x, y));
    }

    public SokobanState(String[][] map, StateObserver observer, List<SokobanState> visited){
        this.map = map;
        this.observer = observer;
        this.visited = visited;
        observer.stateCreated();
        createBoxesList();
        createObjectives();
    }

    @Override
    public String getDescricao() {
        return "https://en.wikipedia.org/wiki/Sokoban";
    }

    @Override
    public boolean ehMeta() {
        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[x].length; y++)
                if (map[x][y].equals("$") || map[x][y].equals("."))
                    return false;

        return true;
    }

    @Override
    public int custo() {
        return 1;
    }

    @Override
    public List<Estado> sucessores() {
        visited.add(this);
        observer.stateVisited(log());
        List<Estado> suc = new LinkedList<>();

        if (findPlayerPosition()) {
            movePlayerUp(suc);
            movePlayerDown(suc);
            movePlayerLeft(suc);
            movePlayerRight(suc);
        }

        return suc;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof SokobanState) {
            for (int x = 0; x < map.length; x++)
                for (int y = 0; y < map[x].length; y++)
                    if (!map[x][y].equals(((SokobanState) o).map[x][y]))
                        return false;
        }else return false;

        return true;
    }

    @Override
    public String toString() {
        String result = "";

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++)
                result += map[x][y] +",";

            result += ("\n");
        }

        return result;
    }

    @Override
    public int h() {
        int totalDistance = 0;

        for (Coordinate box : boxes){
            Coordinate obj = findBoxObjective(box);

            if (obj != null)
                totalDistance += Math.abs(box.getX() - obj.getX()) + Math.abs(box.getY() - obj.getY());
        }

        return totalDistance;
    }

    private Coordinate findBoxObjective(Coordinate box){
        Coordinate objective = null;
        double distance = Double.MAX_VALUE;

        for (Coordinate obj : objectives){
            double dist = Math.hypot(Math.abs(obj.getX() - box.getX()), Math.abs(obj.getY() - box.getY()));

            if (dist < distance){
                objective = obj;
                distance = dist;
            }
        }

        return objective;
    }
}
