import InitialSetup.taste.TastePreferencePage;
import Pairing.PairingPage;
import Search.SearchPage;
import TastingNote.TastingNotePage;
import Ui.buttons.PairingButton;
import Ui.buttons.SearchButton;
import Ui.buttons.TastingNoteButton;
import Ui.buttons.TasteTestButton;
import Ui.icon.ImageScaler;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.util.AppPaths;

import javax.swing.*;
import java.awt.BorderLayout;

public class MainPage extends JFrame {
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
        JButton tasteTestButton = new TasteTestButton();
        diaryButton.setAlignmentX(CENTER_ALIGNMENT);
        searchButton.setAlignmentX(CENTER_ALIGNMENT);
        pairingButton.setAlignmentX(CENTER_ALIGNMENT);
        tasteTestButton.setAlignmentX(CENTER_ALIGNMENT);

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

        tasteTestButton.addActionListener(e -> {
            new TastePreferencePage(this);
            setVisible(false);
        });

        menuPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 4)));
        menuPanel.add(diaryButton);
        menuPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        menuPanel.add(searchButton);
        menuPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        menuPanel.add(pairingButton);
        menuPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        menuPanel.add(tasteTestButton);
        menuPanel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(8),
                ScreenScale.scale(50),
                ScreenScale.scale(18),
                ScreenScale.scale(50)
        ));
        panel.add(menuPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ScreenScale.scale(6), 0));

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(ThemeSizes.scaledMainLogo());
        logoLabel.setOpaque(false);

        int logoWidth = ScreenScale.scale(ThemeSizes.MAIN_LOGO_WIDTH);
        int logoHeight = ScreenScale.scale(ThemeSizes.MAIN_LOGO_HEIGHT);
        ImageIcon logoIcon = ImageScaler.loadScaledIcon(AppPaths.LOGO_IMAGE, logoWidth, logoHeight);
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("For The Night");
            logoLabel.setForeground(ThemeColors.TEXT_WHITE);
            logoLabel.setFont(ThemeFonts.bold(36));
        }

        logoPanel.add(logoLabel, BorderLayout.CENTER);
        return logoPanel;
    }
}
