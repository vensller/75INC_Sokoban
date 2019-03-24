package Model;

public enum EnumAlg {
    BREADTH_FIRST("Largura"),
    DEPTH("Profundidade"),
    BIDIRECTIONAL("Bidirecional"),
    MOUNTAINCLIMB("Subida da montanha"),
    ASTAR("A*");

    public final String label;

    private EnumAlg(String label){
        this.label = label;
    }
}