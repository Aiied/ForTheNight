package Ui.component;

import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public abstract class BaseSearchField extends JTextField {
    private final String placeholder;

    protected BaseSearchField(String placeholder, int width, int height) {
        this.placeholder = placeholder;
        setFont(ThemeFonts.plain(16));
        setPreferredSize(ScreenScale.dimension(width, height));
        setMinimumSize(ScreenScale.dimension(width, height));
        setMaximumSize(ScreenScale.dimension(width, height));
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(
                ScreenScale.scale(8),
                ScreenScale.scale(14),
                ScreenScale.scale(8),
                ScreenScale.scale(14)
        ));
        setCaretColor(ThemeColors.TEXT_PRIMARY);
        setSelectionColor(ThemeColors.WOOD_SELECTION);
        setSelectedTextColor(ThemeColors.TEXT_WHITE);
        setPlaceholder();

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (BaseSearchField.this.placeholder.equals(getText())) {
                    setText("");
                    setForeground(ThemeColors.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isBlank()) {
                    setPlaceholder();
                }
            }
        });
    }

    public void onChange(Runnable action) {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                action.run();
            }
        });
    }

    public String getSearchKeyword() {
        String keyword = getText() == null ? "" : getText().trim();
        if (placeholder.equalsIgnoreCase(keyword)) {
            return "";
        }
        return keyword;
    }

    private void setPlaceholder() {
        setText(placeholder);
        setForeground(ThemeColors.TEXT_PLACEHOLDER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = ScreenScale.scale(18);

        g2.setColor(ThemeColors.WOOD_FILL);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        paintWoodGrain(g2, width, height, arc);

        g2.setColor(new java.awt.Color(255, 255, 255, 28));
        g2.drawRoundRect(1, 1, width - 3, height / 2, arc, arc);

        g2.setColor(ThemeColors.WOOD_BORDER_OUTER);
        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
        g2.setColor(ThemeColors.WOOD_BORDER_INNER);
        g2.drawRoundRect(1, 1, width - 3, height - 3, arc - 2, arc - 2);

        g2.dispose();
        super.paintComponent(g);
    }

    private void paintWoodGrain(Graphics2D g2, int width, int height, int arc) {
        for (int x = 6; x < width; x += 14) {
            int alpha = 16 + (x % 28);
            g2.setColor(new java.awt.Color(
                    ThemeColors.WOOD_GRAIN_DARK.getRed(),
                    ThemeColors.WOOD_GRAIN_DARK.getGreen(),
                    ThemeColors.WOOD_GRAIN_DARK.getBlue(),
                    alpha
            ));
            g2.drawRoundRect(x, 2, 3, height - 5, arc, arc);
        }

        for (int y = height / 3; y < height; y += ScreenScale.scale(8)) {
            int alpha = y % 2 == 0 ? 18 : 10;
            g2.setColor(new java.awt.Color(
                    ThemeColors.WOOD_GRAIN_LIGHT.getRed(),
                    ThemeColors.WOOD_GRAIN_LIGHT.getGreen(),
                    ThemeColors.WOOD_GRAIN_LIGHT.getBlue(),
                    alpha
            ));
            g2.drawLine(ScreenScale.scale(10), y, width - ScreenScale.scale(10), y + 1);
        }
    }
}
