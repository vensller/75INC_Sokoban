package Model;

public enum EnumAlg {
    BREADTH_FIRST("Largura"),
    DEPTH("Profundidade"),
    ITERATIVEDEPTH("Profundidade iterativa"),
    ASTAR("A*");

    public final String label;

    private EnumAlg(String label){
        this.label = label;
    }
}