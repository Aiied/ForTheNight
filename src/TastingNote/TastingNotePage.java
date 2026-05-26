package TastingNote;

import Ui.Button.BackButton;
import Ui.BackgroundPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

public class TastingNotePage extends JFrame {
    public TastingNotePage() {
        this(null);
    }

    public TastingNotePage(JFrame previousPage) {
        setTitle("TastingNote");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel(new BorderLayout()));

        JButton backButton = new BackButton();
        backButton.addActionListener(e -> {
            if (previousPage != null) {
                previousPage.setVisible(true);
            }
            dispose();
        });

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel label = new JLabel("TastingNote", SwingConstants.CENTER);
        label.setForeground(java.awt.Color.WHITE);

        JButton newNoteButton = new JButton("New Note");
        newNoteButton.setFocusPainted(false);
        newNoteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newNoteButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        newNoteButton.setPreferredSize(new Dimension(180, 44));
        newNoteButton.addActionListener(e -> {
            new NewNotePage(this);
            setVisible(false);
        });

        JPanel bottomPanel = new BackgroundPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 32, 0));
        bottomPanel.add(newNoteButton);

        add(topPanel, BorderLayout.NORTH);
        add(label, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}

