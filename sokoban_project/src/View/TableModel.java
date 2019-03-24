package View;

import Controller.SokobanController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class TableModel extends AbstractTableModel {

    private SokobanController controller;
    private HashMap<String, ImageIcon> images;

    public TableModel(SokobanController controller){
        this.controller = controller;
        this.images = new HashMap<>();
        images.put("#", new ImageIcon("images/wall.png"));
        images.put("@", new ImageIcon("images/person.png"));
        images.put("$", new ImageIcon("images/box.png"));
        images.put(" ", new ImageIcon("images/blank.png"));
        images.put(".", new ImageIcon("images/point.png"));
    }

    @Override
    public int getRowCount() {
        return controller.returnRowCount();
    }

    @Override
    public int getColumnCount() {
        return controller.returnColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getImage(rowIndex, columnIndex);
    }

    public ImageIcon getImage(int row, int column){
        String imageName = controller.returnImageName(row, column);
        return images.get(imageName);
    }
}
