package Ui.component;

import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.FontMetrics;

public class StyledTabbedPane extends JTabbedPane {
    public StyledTabbedPane() {
        setFont(ThemeFonts.boldRaw(12));
        setFocusable(false);
        setOpaque(false);
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        setUI(new RoundedTabUI());
    }

    private static class RoundedTabUI extends BasicTabbedPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabAreaInsets = new Insets(ScreenScale.scale(6), ScreenScale.scale(12), ScreenScale.scale(10), ScreenScale.scale(12));
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            tabInsets = new Insets(ScreenScale.scale(10), ScreenScale.scale(14), ScreenScale.scale(14), ScreenScale.scale(14));
        }

        @Override
        protected int calculateTabWidth(int tabPlacement, int tabIndex, java.awt.FontMetrics metrics) {
            return Math.max(ScreenScale.scale(58), super.calculateTabWidth(tabPlacement, tabIndex, metrics) + ScreenScale.scale(18));
        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            return Math.max(ScreenScale.scale(66), super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + ScreenScale.scale(28));
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = x + (w / 2);
            int wireTop = ScreenScale.scale(14);
            int socketWidth = ScreenScale.scale(18);
            int socketHeight = ScreenScale.scale(14);
            int socketX = centerX - (socketWidth / 2);
            int socketY = y + ScreenScale.scale(12);
            int bulbSize = ScreenScale.scale(28);
            int bulbX = centerX - (bulbSize / 2);
            int bulbY = socketY + socketHeight - ScreenScale.scale(2);

            g2.setColor(ThemeColors.TAB_WIRE);
            g2.drawLine(centerX, wireTop, centerX, socketY + ScreenScale.scale(2));

            g2.setColor(ThemeColors.TAB_SOCKET);
            g2.fillRoundRect(socketX, socketY, socketWidth, socketHeight, ScreenScale.scale(8), ScreenScale.scale(8));
            g2.setColor(ThemeColors.TAB_SOCKET_HIGHLIGHT);
            g2.drawRoundRect(socketX, socketY, socketWidth, socketHeight, ScreenScale.scale(8), ScreenScale.scale(8));
            if (isSelected) {
                int glowSize = bulbSize + ScreenScale.scale(14);
                int glowX = centerX - (glowSize / 2);
                int glowY = bulbY - ScreenScale.scale(6);
                g2.setPaint(new RadialGradientPaint(
                        centerX,
                        bulbY + bulbSize / 2f,
                        glowSize / 2f,
                        new float[]{0f, 0.65f, 1f},
                        new java.awt.Color[]{ThemeColors.ACCENT_GLOW, new java.awt.Color(255, 208, 110, 35), new java.awt.Color(255, 208, 110, 0)}
                ));
                g2.fillOval(glowX, glowY, glowSize, glowSize);
            }

            g2.setPaint(new RadialGradientPaint(
                    centerX,
                    bulbY + ScreenScale.scale(10),
                    bulbSize / 1.7f,
                    new float[]{0f, 0.72f, 1f},
                    isSelected
                            ? new java.awt.Color[]{ThemeColors.ACCENT_BULB_INNER, ThemeColors.ACCENT_BULB_OUTER, ThemeColors.TAB_BULB_EDGE}
                            : new java.awt.Color[]{ThemeColors.TAB_BULB_DEFAULT_INNER, ThemeColors.TAB_BULB_DEFAULT_OUTER, ThemeColors.TAB_BULB_EDGE}
            ));
            g2.fillOval(bulbX, bulbY, bulbSize, bulbSize);

            g2.setColor(new Color(255, 255, 255, isSelected ? 130 : 70));
            g2.fillOval(bulbX + ScreenScale.scale(8), bulbY + ScreenScale.scale(5), ScreenScale.scale(8), ScreenScale.scale(10));
            g2.setColor(ThemeColors.TAB_BULB_EDGE);
            g2.drawOval(bulbX, bulbY, bulbSize, bulbSize);
            g2.dispose();
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, java.awt.FontMetrics metrics,
                                 int tabIndex, String title, java.awt.Rectangle textRect, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(font);
            g2.setColor(isSelected ? ThemeColors.ACCENT_TEXT_SELECTED : ThemeColors.TAB_TEXT_DEFAULT);

            Rectangle bounds = getTabBounds(tabPane, tabIndex);
            int bulbSize = ScreenScale.scale(28);
            int textCenterX = bounds.x + bounds.width / 2;
            int textCenterY = bounds.y + ScreenScale.scale(12) + ScreenScale.scale(14) - ScreenScale.scale(2) + bulbSize / 2;
            FontMetrics fontMetrics = g2.getFontMetrics();
            int textX = textCenterX - (fontMetrics.stringWidth(title) / 2);
            int textY = textCenterY + (fontMetrics.getAscent() / 2) - ScreenScale.scale(2);
            g2.drawString(title, textX, textY);
            g2.dispose();
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, java.awt.Rectangle[] rects,
                                           int tabIndex, java.awt.Rectangle iconRect,
                                           java.awt.Rectangle textRect, boolean isSelected) {
            // keep tab design clean by skipping the default focus paint
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
            // remove the default rectangular tab outline
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.dispose();
        }
    }
}
