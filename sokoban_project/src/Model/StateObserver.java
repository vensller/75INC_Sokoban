package Model;

public interface StateObserver {
    void stateCreated();
    void stateVisited(String stateLog);
}
