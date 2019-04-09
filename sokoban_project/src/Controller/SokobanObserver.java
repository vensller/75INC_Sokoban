package Controller;

public interface SokobanObserver {
    void instanceReadSuccess();
    void instanceReadFail();
    void solutionNotFound();
    void refreshTable(int SolutionCount);
    void stateCreated(int countState);
    void stateVisited(int countState);
    void cleanLog();
}
