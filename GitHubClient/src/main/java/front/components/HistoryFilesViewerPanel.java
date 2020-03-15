package front.components;

import javax.swing.JPanel;
import java.awt.*;

public class HistoryFilesViewerPanel extends JPanel {

    private int width, height;

    //create viewer interface
    public HistoryFilesViewerPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLUE);
    }

}
