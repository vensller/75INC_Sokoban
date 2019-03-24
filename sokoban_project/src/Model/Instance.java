package Model;

public class Instance {

    private String[][] map;

    public Instance(int lines, int columns){
        map = new String[lines][columns];
    }

    public String[][] getMap() {
        return map;
    }

    public int getLines(){
        return map.length;
    }

    public int getColumns(){
        return map[0].length;
    }
}
