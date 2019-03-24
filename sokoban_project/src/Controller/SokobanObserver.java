package Controller;

public interface SokobanObserver {
    void instanceReadSuccess();
    void instanceReadFail();
    void solutionNotFound();
    void refreshTable(int SolutionCount);
}
