package front.components;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    public Button(String text) {
        setText(text);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    Button(int x, int y, int width, int height, String text, Font font) {
        setBounds(x, y, width, height);
        setFont(font);
        setText(text);
    }

}
