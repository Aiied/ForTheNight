package Ui.panel;

import Ui.util.AppPaths;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

public class BackgroundPanel extends JPanel {
    private static final Image BACKGROUND_IMAGE = new ImageIcon(AppPaths.BACKGROUND_IMAGE).getImage();

    public BackgroundPanel() {
        setOpaque(false);
    }

    public BackgroundPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = BACKGROUND_IMAGE.getWidth(this);
        int imageHeight = BACKGROUND_IMAGE.getHeight(this);

        if (imageWidth <= 0 || imageHeight <= 0) {
            super.paintComponent(g);
            return;
        }

        double scale = Math.max((double) panelWidth / imageWidth, (double) panelHeight / imageHeight);
        int drawWidth = (int) Math.ceil(imageWidth * scale);
        int drawHeight = (int) Math.ceil(imageHeight * scale);
        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g.drawImage(BACKGROUND_IMAGE, x, y, drawWidth, drawHeight, this);
        super.paintComponent(g);
    }
}
