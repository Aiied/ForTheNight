import Pairing.PairingPage;
import Search.SearchPage;
import TastingNote.TastingNotePage;
import Ui.BackgroundPanel;
import Ui.ImageScaler;
import Ui.Button.PairingButton;
import Ui.Button.SearchButton;
import Ui.Button.TastingNoteButton;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

public class MainPage extends JFrame {
    private static final String LOGO_PATH = "src/assets/image/Ui/LOGO.png";
    private static final int LOGO_WIDTH = 640;
    private static final int LOGO_HEIGHT = 280;

    public MainPage() {
        setTitle("Main");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new BackgroundPanel(new BorderLayout());
        panel.add(createLogoPanel(), BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton diaryButton = new TastingNoteButton();
        JButton searchButton = new SearchButton();
        JButton pairingButton = new PairingButton();
        diaryButton.setAlignmentX(CENTER_ALIGNMENT);
        searchButton.setAlignmentX(CENTER_ALIGNMENT);
        pairingButton.setAlignmentX(CENTER_ALIGNMENT);

        diaryButton.addActionListener(e -> {
            new TastingNotePage(this);
            setVisible(false);
        });

        searchButton.addActionListener(e -> {
            new SearchPage(this);
            setVisible(false);
        });

        pairingButton.addActionListener(e -> {
            new PairingPage(this);
            setVisible(false);
        });

        menuPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        menuPanel.add(diaryButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(searchButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(pairingButton);
        menuPanel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createEmptyBorder(8, 50, 18, 50));
        panel.add(menuPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(LOGO_WIDTH, LOGO_HEIGHT));
        logoLabel.setOpaque(false);

        ImageIcon logoIcon = ImageScaler.loadScaledIcon(LOGO_PATH, LOGO_WIDTH, LOGO_HEIGHT);
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("For The Night");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
        }

        logoPanel.add(logoLabel, BorderLayout.CENTER);
        return logoPanel;
    }
}
