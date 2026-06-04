package Ui.Button;

import Ui.StarIconFactory;
import Whisky.FavoriteWhiskyStore;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;

public class FavoriteStarButton extends JButton {
    private final String whiskyName;
    private final ImageIcon selectedIcon;
    private final ImageIcon unselectedIcon;
    private boolean favorite;

    public FavoriteStarButton(String whiskyName, int size) {
        this.whiskyName = whiskyName;
        this.favorite = FavoriteWhiskyStore.isFavorite(whiskyName);
        int iconSize = Math.max(12, size - 6);
        this.selectedIcon = StarIconFactory.createStarIcon(iconSize, 1.0f);
        this.unselectedIcon = StarIconFactory.createStarIcon(iconSize, 1.0f, Color.WHITE);

        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setMargin(new Insets(0, 0, 0, 0));
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setIconTextGap(0);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateVisual();

        addActionListener(e -> {
            favorite = FavoriteWhiskyStore.toggleFavorite(this.whiskyName);
            updateVisual();
        });
    }

    private void updateVisual() {
        if (selectedIcon == null || unselectedIcon == null) {
            setText(favorite ? "\u2605" : "\u2606");
            setForeground(favorite ? new Color(242, 196, 74) : Color.WHITE);
            return;
        }

        setText("");
        setIcon(favorite ? selectedIcon : unselectedIcon);
    }
}
