package Ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public abstract class BaseSearchField extends JTextField {
    private final String placeholder;

    protected BaseSearchField(String placeholder, int width, int height) {
        this.placeholder = placeholder;
        setFont(new Font("SansSerif", Font.PLAIN, 16));
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPlaceholder();

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (BaseSearchField.this.placeholder.equals(getText())) {
                    setText("");
                    setForeground(Color.BLACK);
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
        setForeground(Color.GRAY);
    }
}
