package Ui.buttons;

import javax.swing.JButton;
import java.awt.Cursor;
import Ui.theme.ThemeFonts;

public abstract class AbstractActionButton extends JButton {
    protected AbstractActionButton(String text, int fontSize) {
        super(text);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(ThemeFonts.boldRaw(fontSize));
    }
}
