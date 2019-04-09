package View;

import Controller.SokobanController;
import Controller.SokobanObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SokobanForm extends JFrame implements SokobanObserver {

    private JButton btnChooseArch;
    private JPanel rootPanel;
    private JComboBox cbSearchAlg;
    private JTable tblGame;
    private JButton btnSearch;
    private JTextArea txaAlgorithm;
    private JLabel lblArchive;
    private JScrollPane scrollPane;
    private TableRenderer tableRenderer;
    private TableModel tableModel;
    private SokobanController controller;

    public SokobanForm(){
        add(rootPanel);
        setTitle("Sokoban Game");
        setSize(800, 600);
        initData();
        initListeners();
    }

    private void initData(){
        cleanLog();
        controller = new SokobanController();
        controller.observe(this);
        tableRenderer = new TableRenderer();
        tableModel = new TableModel(controller);
        tblGame.setDefaultRenderer(Object.class, tableRenderer);

        String[] algorithms = controller.getAlgorithms();
        cbSearchAlg.removeAllItems();

        for (String alg : algorithms){
            cbSearchAlg.addItem(alg);
        }
    }

    private void initListeners(){
        btnChooseArch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                int retorno = fileChooser.showOpenDialog(tblGame);

                if (retorno == JFileChooser.APPROVE_OPTION){
                    cleanLog();
                    lblArchive.setText(fileChooser.getSelectedFile().getName());
                    controller.readInstance(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.runGame(cbSearchAlg.getSelectedIndex());
            }
        });
    }

    @Override
    public void instanceReadSuccess() {
        tableModel.dataChanged();
        tblGame.setModel(tableModel);
        JOptionPane.showMessageDialog(this, "Success to read the instance!");
    }

    @Override
    public void instanceReadFail() {
        JOptionPane.showMessageDialog(this, "Fail to read the instance, please choose another!");
        lblArchive.setText("");
    }

    @Override
    public void solutionNotFound() {
        JOptionPane.showMessageDialog(this, "Failed to found a solution!");
    }

    @Override
    public void refreshTable(int solutionCount) {
        tableModel.dataChanged();
        tblGame.setModel(tableModel);
        Thread t = new Thread(){
            @Override
            public void run(){
                for (int x = 0; x < solutionCount; x++){
                    controller.changeSolution(x);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }
        };
        t.start();
    }

    @Override
    public void stateCreated(int countState) {
        txaAlgorithm.append("New state created. Created " + countState + " states.\n");
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    @Override
    public void stateVisited(int countState) {
        txaAlgorithm.append("New state visited. Visited " + countState + " states.\n");
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    @Override
    public void cleanLog() {
        txaAlgorithm.setText("Algorithm Result: \n");
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}
