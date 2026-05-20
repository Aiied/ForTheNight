package TastingNote;

import javax.swing.*;
import java.awt.*;

public class TastingNotePage extends JFrame {
    public TastingNotePage() {
        setTitle("다이어리");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("다이어리 페이지", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
