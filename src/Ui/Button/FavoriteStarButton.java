package Ui.Button;

import Whisky.FavoriteWhiskyStore;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

public class FavoriteStarButton extends JButton {
    private final String whiskyName;
    private boolean favorite;

    public FavoriteStarButton(String whiskyName, int size) {
        this.whiskyName = whiskyName;
        this.favorite = FavoriteWhiskyStore.isFavorite(whiskyName);

        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("SansSerif", Font.BOLD, Math.max(14, size - 8)));
        updateVisual();

        addActionListener(e -> {
            favorite = FavoriteWhiskyStore.toggleFavorite(this.whiskyName);
            updateVisual();
        });
    }

    private void updateVisual() {
        setText(favorite ? "★" : "☆");
        setForeground(favorite ? new Color(255, 204, 64) : new Color(190, 190, 190));
    }
}
