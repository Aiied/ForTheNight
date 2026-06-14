package Ui.icon;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageScaler {
    private ImageScaler() {
    }

    public static ImageIcon loadScaledIcon(String path, int targetWidth, int targetHeight) {
        if (path == null || path.isBlank() || targetWidth <= 0 || targetHeight <= 0) {
            return null;
        }

        try {
            BufferedImage source = ImageIO.read(new File(path));
            if (source == null) {
                return null;
            }

            BufferedImage scaled = scalePreserveAspect(source, targetWidth, targetHeight);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }

    private static BufferedImage scalePreserveAspect(BufferedImage source, int boxWidth, int boxHeight) {
        double widthRatio = (double) boxWidth / source.getWidth();
        double heightRatio = (double) boxHeight / source.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = Math.max(1, (int) Math.round(source.getWidth() * ratio));
        int scaledHeight = Math.max(1, (int) Math.round(source.getHeight() * ratio));
        return scaleHighQuality(source, scaledWidth, scaledHeight);
    }

    private static BufferedImage scaleHighQuality(BufferedImage source, int targetWidth, int targetHeight) {
        BufferedImage current = source;
        int currentWidth = source.getWidth();
        int currentHeight = source.getHeight();

        while (currentWidth / 2 >= targetWidth && currentHeight / 2 >= targetHeight) {
            currentWidth /= 2;
            currentHeight /= 2;
            current = resizeOnce(current, currentWidth, currentHeight);
        }

        if (currentWidth != targetWidth || currentHeight != targetHeight) {
            current = resizeOnce(current, targetWidth, targetHeight);
        }

        return current;
    }

    private static BufferedImage resizeOnce(BufferedImage source, int width, int height) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.drawImage(source, 0, 0, width, height, null);
        g2.dispose();
        return target;
    }
}
