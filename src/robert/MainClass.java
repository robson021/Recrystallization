package robert;

import robert.gui.MainFrame;

import javax.swing.*;

/**
 * Created by robert on 03.06.16.
 */
public class MainClass {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainFrame.getFrame().setVisible(true));
    }
}
