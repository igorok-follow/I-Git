package front.components;

import front.MainWindow;

import javax.swing.*;

public class NewRepositoryWindow {

    private int width, height;

    NewRepositoryWindow(MainWindow owner, int width, int height) {
        JWindow window = new JWindow(owner);
        window.setSize(width, height);
        window.setVisible(true);
    }

}
