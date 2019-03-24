package Model;

import busca.Antecessor;
import busca.Estado;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SokobanState implements Estado, Antecessor {

    private String[][] map;
    private int rowPos;
    private int columnPos;

    private boolean findPlayerPosition(){
        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[x].length; y++)
                if (map[x][y].equals("@")){
                    rowPos = x;
                    columnPos = y;
                    return true;
                }

        return false;
    }

    private boolean positionIsBlocked(int row, int column, String[][] cloneMap){
        return cloneMap[row][column].equals("#")
                || (cloneMap[row][column].equals("$"))
                || (cloneMap[row][column].equals(".$"));
    }

    private boolean havePossibilityToFindPoint(int row, int column, String[][] cloneMap){
        return !((cloneMap[row + 1][column].equals("#") && ((cloneMap[row][column - 1].equals("#")) || (cloneMap[row][column + 1].equals("#")))) //Linha abaixo e coluna esquerda ou direita tem parede
                || (cloneMap[row - 1][column].equals("#") && ((cloneMap[row][column - 1].equals("#")) || (cloneMap[row][column + 1].equals("#"))))); //linha acima e coluna esquerda ou direita tem parede
    }

    private boolean isDeadLock(int row, int column, String[][] cloneMap){
        boolean isAtPoint = cloneMap[row][column].equals(".$");
        boolean canMovement = (cloneMap[row - 1][column].equals(" ") || cloneMap[row - 1][column].equals("@") || cloneMap[row - 1][column].equals(".@"))
                           && (cloneMap[row + 1][column].equals(" ") || cloneMap[row + 1][column].equals("@") || cloneMap[row + 1][column].equals(".@"))
                           && (cloneMap[row][column - 1].equals(" ") || cloneMap[row][column - 1].equals("@") || cloneMap[row][column - 1].equals(".@"))
                           && (cloneMap[row][column + 1].equals(" ") || cloneMap[row][column + 1].equals("@") || cloneMap[row][column + 1].equals(".@"));


        boolean deadLock = !isAtPoint && !canMovement && !havePossibilityToFindPoint(row, column, cloneMap);

        return deadLock;
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

            canChange = !isDeadLock(newRow, newColumn, cloneMap);
        }

        return canChange;
    }

    private boolean changePlayerPos(int newRow, int newColumn, EnumMov movement, String[][] cloneMap){
        boolean canChange = false;
        String newPosAux = cloneMap[newRow][newColumn];
        String newPos = "@";

        if (newPosAux.equals(" "))
            canChange = true;

        if (newPosAux.equals(".")){
            canChange = true;
            newPos = ".@";
        }

        if (newPosAux.equals("$"))
            canChange = changeBoxPos(newRow, newColumn, movement, cloneMap);

        if (canChange) {
            cloneMap[newRow][newColumn] = newPos;
            cloneMap[rowPos][columnPos] = " ";
        }

        return canChange;
    }

    private void movePlayerUp(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((rowPos > 0)
                && (changePlayerPos(rowPos -1, columnPos, EnumMov.UP, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerDown(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((rowPos + 1 < map.length)
                && (changePlayerPos(rowPos + 1, columnPos, EnumMov.DOWN, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerLeft(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((columnPos > 0)
                && (changePlayerPos(rowPos, columnPos - 1, EnumMov.LEFT, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerRight(List<Estado> list){
        String[][] cloneMap = Arrays.stream(map).map(r -> r.clone()).toArray(String[][]::new);
        if ((columnPos + 1 < map[0].length)
                && (changePlayerPos(rowPos, columnPos + 1, EnumMov.RIGHT, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    public SokobanState(String[][] map){
        this.map = map;
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
        List<Estado> suc = new LinkedList<Estado>();

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
    public List<Estado> antecessores() {
        return sucessores();
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
}
