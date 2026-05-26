package Pairing;

import Ui.Button.BackButton;
import Ui.BackgroundPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class PairingPage extends JFrame {
    public PairingPage() {
        this(null);
    }

    public PairingPage(JFrame previousPage) {
        setTitle("Pairing");
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

        JLabel label = new JLabel("Pairing Page", SwingConstants.CENTER);
        label.setForeground(java.awt.Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(label, BorderLayout.CENTER);

        setVisible(true);
    }
}

