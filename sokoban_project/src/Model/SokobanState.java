package Model;

import busca.Estado;

import java.util.LinkedList;
import java.util.List;

public class SokobanState implements Estado {

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

    private boolean isDeadLock(String[][] cloneMap){
        return false;
    }

    private boolean changeBoxPos(int rowBox, int columnBox, int newRow, int newColumn, String[][] cloneMap){
        boolean canChange = false;
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

            canChange = !isDeadLock(cloneMap);
        }

        return canChange;
    }

    private boolean changePlayerPos(int newRow, int newColumn, String[][] cloneMap){
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
            canChange = (changeBoxPos(newRow, newColumn, newRow -1, newColumn, cloneMap));

        if (canChange) {
            cloneMap[newRow][newColumn] = newPos;
            cloneMap[rowPos][columnPos] = " ";
        }

        return canChange;
    }

    private void movePlayerUp(List<Estado> list){
        String[][] cloneMap = map.clone();
        if ((rowPos > 0)
                && (changePlayerPos(rowPos -1, columnPos, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerDown(List<Estado> list){
        String[][] cloneMap = map.clone();
        if ((rowPos + 1 < map.length)
                && (changePlayerPos(rowPos + 1, columnPos, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerLeft(List<Estado> list){
        String[][] cloneMap = map.clone();
        if ((columnPos > 0)
                && (changePlayerPos(rowPos, columnPos - 1, cloneMap)))
            list.add(new SokobanState(cloneMap));
    }

    private void movePlayerRight(List<Estado> list){
        String[][] cloneMap = map.clone();
        if ((columnPos + 1 < map[0].length)
                && (changePlayerPos(rowPos, columnPos + 1, cloneMap)))
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

}
