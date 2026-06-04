package Ui;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class StyledTabbedPane extends JTabbedPane {
    private static final Color TAB_SELECTED_BG = new Color(186, 126, 52);
    private static final Color TAB_DEFAULT_BG = new Color(35, 35, 35);
    private static final Color TAB_BORDER = new Color(88, 88, 88);
    private static final Color TEXT_SELECTED = Color.WHITE;
    private static final Color TEXT_DEFAULT = new Color(210, 210, 210);

    public StyledTabbedPane() {
        setFont(new Font("SansSerif", Font.BOLD, 12));
        setFocusable(false);
        setOpaque(false);
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        setUI(new RoundedTabUI());
    }

    private static class RoundedTabUI extends BasicTabbedPaneUI {
        @Override
        protected int calculateTabWidth(int tabPlacement, int tabIndex, java.awt.FontMetrics metrics) {
            return Math.max(40, super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 18);
        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            return Math.max(28, super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 6);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isSelected ? TAB_SELECTED_BG : TAB_DEFAULT_BG);
            g2.fillRoundRect(x + 2, y + 2, w - 4, h - 5, 12, 12);
            g2.setColor(TAB_BORDER);
            g2.drawRoundRect(x + 2, y + 2, w - 4, h - 5, 12, 12);
            g2.dispose();
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, java.awt.FontMetrics metrics,
                                 int tabIndex, String title, java.awt.Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            g.setColor(isSelected ? TEXT_SELECTED : TEXT_DEFAULT);
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, java.awt.Rectangle[] rects,
                                           int tabIndex, java.awt.Rectangle iconRect,
                                           java.awt.Rectangle textRect, boolean isSelected) {
            // keep tab design clean by skipping the default focus paint
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // remove default content border for a cleaner dark surface
        }
    }
}
