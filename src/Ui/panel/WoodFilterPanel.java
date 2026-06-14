package Ui.panel;

import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

public class WoodFilterPanel extends JPanel {
    public WoodFilterPanel() {
        this(null);
    }

    public WoodFilterPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = ScreenScale.scale(18);

        g2.setColor(ThemeColors.SHADOW_SOFT);
        g2.fillRoundRect(
                ScreenScale.scale(4),
                ScreenScale.scale(6),
                width - ScreenScale.scale(4),
                height - ScreenScale.scale(2),
                arc,
                arc
        );

        g2.setColor(ThemeColors.SURFACE_PANEL);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        int plankHeight = Math.max(ScreenScale.scale(56), height / 6);
        for (int y = 0; y < height; y += plankHeight) {
            int section = y / Math.max(1, plankHeight);
            Color plankTone = section % 2 == 0
                    ? new Color(24, 24, 24, 70)
                    : new Color(44, 44, 44, 55);
            g2.setColor(plankTone);
            g2.fillRoundRect(
                    ScreenScale.scale(6),
                    y + ScreenScale.scale(6),
                    width - ScreenScale.scale(12),
                    Math.min(plankHeight, height - y) - ScreenScale.scale(8),
                    ScreenScale.scale(14),
                    ScreenScale.scale(14)
            );
        }

        for (int y = ScreenScale.scale(18); y < height; y += ScreenScale.scale(7)) {
            int alpha = (y / Math.max(1, ScreenScale.scale(7))) % 3 == 0 ? 22 : 10;
            g2.setColor(new Color(92, 92, 92, alpha));
            g2.drawLine(ScreenScale.scale(10), y, width - ScreenScale.scale(10), y);
        }

        g2.setColor(new Color(255, 255, 255, 20));
        g2.drawRoundRect(1, 1, width - 3, ScreenScale.scale(54), arc, arc);

        g2.setColor(ThemeColors.BORDER_PANEL);
        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
        g2.setColor(ThemeColors.BORDER_HIGHLIGHT);
        g2.drawRoundRect(1, 1, width - 3, height - 3, arc - 2, arc - 2);
        g2.dispose();

        super.paintComponent(g);
    }

    public static void styleFilterLabel(JLabel label) {
        label.setForeground(ThemeColors.TEXT_WHITE);
        label.setFont(ThemeFonts.bold(12));
    }

    public static <T> void styleComboBox(JComboBox<T> box) {
        box.setBackground(ThemeColors.SURFACE_INPUT);
        box.setForeground(new Color(224, 224, 224));
        box.setFocusable(false);
        box.setBorder(BorderFactory.createLineBorder(ThemeColors.BORDER_SUBTLE));
    }

    public static void styleActionButton(JButton button) {
        button.setFocusPainted(false);
        button.setForeground(ThemeColors.TEXT_PRIMARY);
        button.setBackground(new Color(40, 24, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 120, 46)),
                BorderFactory.createEmptyBorder(
                        ScreenScale.scale(8),
                        ScreenScale.scale(12),
                        ScreenScale.scale(8),
                        ScreenScale.scale(12)
                )
        ));
    }
}
