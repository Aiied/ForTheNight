package Ui.buttons;

import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BackButton extends JButton {
    private static final int ARC = 18;

    private boolean hovered;

    public BackButton() {
        super("BACK");

        setPreferredSize(ThemeSizes.scaledBackButton());
        setMinimumSize(ThemeSizes.scaledBackButton());
        setMaximumSize(ThemeSizes.scaledBackButton());
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(ThemeFonts.boldRaw(12));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    public BackButton(JFrame currentPage, JFrame previousPage) {
        this();

        setEnabled(previousPage != null);
        addActionListener(e -> {
            if (previousPage != null) {
                previousPage.setVisible(true);
            }
            if (currentPage != null) {
                currentPage.dispose();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        g2.setColor(hovered ? ThemeColors.SURFACE_BUTTON_HOVER : ThemeColors.SURFACE_BUTTON);
        g2.fillRoundRect(1, 1, width - 2, height - 2, ARC, ARC);

        g2.setColor(ThemeColors.BORDER_ACCENT);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(1, 1, width - 3, height - 3, ARC, ARC);

        paintArrow(g2, 18, height / 2);
        paintText(g2, width, height);

        g2.dispose();
    }

    private void paintArrow(Graphics2D g2, int x, int y) {
        g2.setColor(ThemeColors.TEXT_PRIMARY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + 8, y - 7, x, y);
        g2.drawLine(x, y, x + 8, y + 7);
        g2.drawLine(x, y, x + 20, y);
    }

    private void paintText(Graphics2D g2, int width, int height) {
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int textWidth = metrics.stringWidth(getText());
        int x = width - textWidth - 16;
        int y = (height - metrics.getHeight()) / 2 + metrics.getAscent();

        g2.setColor(ThemeColors.TEXT_PRIMARY);
        g2.setFont(getFont());
        g2.drawString(getText(), x, y);
    }
}
