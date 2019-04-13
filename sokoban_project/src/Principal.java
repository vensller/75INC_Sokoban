import Controller.SokobanController;
import View.SokobanForm;

import javax.swing.*;

public class Principal {

    public static void main(String[] args) {
        if (args.length > 0){
            SokobanController controller = new SokobanController();
            controller.readInstancesFromPath("/home/ivens/Github/75INC_Sokoban/dataset");
            controller.run();
        }else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new SokobanForm().setVisible(true);
        }
    }

}
