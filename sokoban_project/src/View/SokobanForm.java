package View;

import Controller.SokobanController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SokobanForm extends JFrame {

    private JButton btnChooserArch;
    private JPanel rootPanel;
    private JComboBox cbSearchAlg;
    private JTable tblGame;
    private JButton searchSolutionButton;
    private JTextArea txaAlgorithm;
    private JLabel lblArchive;
    private TableModel tableModel;
    private SokobanController controller;

    public SokobanForm(){
        add(rootPanel);

        setTitle("Sokoban Game");
        setSize(800, 600);
        controller = new SokobanController();
        tableModel = new TableModel(controller);
        tblGame.setModel(tableModel);

        String[] algorithms = controller.getAlgorithms();
        cbSearchAlg.removeAllItems();
        for (String alg : algorithms){
            cbSearchAlg.addItem(alg);
        }
        btnChooserArch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

}
