import Button.PairingButton;
import Button.SearchButton;
import Button.TastingNoteButton;
import Pairing.PairingPage;
import TastingNote.TastingNotePage;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {
    public MainPage() {
        setTitle("Main");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 20, 20));
        panel.setBackground(java.awt.Color.BLACK);

        JButton diaryButton = new TastingNoteButton();
        JButton searchButton = new SearchButton();
        JButton pairingButton = new PairingButton();

        diaryButton.addActionListener(e -> {
            new TastingNotePage();
            dispose();
        });

        searchButton.addActionListener(e -> {
            new SearchPage();
            dispose();
        });

        pairingButton.addActionListener(e -> {
            new PairingPage();
            dispose();
        });

        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.add(diaryButton);
        panel.add(searchButton);
        panel.add(pairingButton);

        add(panel);
        setVisible(true);
    }
}
