package Button;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;

public class TastingNoteButton extends JButton {
    private static final String IMAGE_PATH = "src/assets/image/Ui/TastingNoteButton.png";
    private static final int IMAGE_WIDTH = 360;
    private static final int IMAGE_HEIGHT = 120;

    public TastingNoteButton() {
        super();

        setIcon(createButtonIcon());
        setBackground(Color.BLACK);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);
    }

    private ImageIcon createButtonIcon() {
        ImageIcon icon = new ImageIcon(IMAGE_PATH);
        Image image = icon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

        return new ImageIcon(image);
    }
}
