package front.components;

import javax.swing.*;
import java.awt.*;

class Label extends JLabel {

    Label(int x, int y, int width, int height, String text, Font font) {
        setBounds(x, y, width, height);
        setText(text);
        setFont(font);
    }

}
