package Ui;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class StarIconFactory {
    private static final String STAR_IMAGE_PATH = "src/assets/image/Ui/FavoriteStar.png";

    private StarIconFactory() {
    }

    public static ImageIcon createStarIcon(int size, float alpha) {
        return createStarIcon(size, alpha, null);
    }

    public static ImageIcon createStarIcon(int size, float alpha, Color tintColor) {
        BufferedImage sourceImage = loadSourceImage();
        if (sourceImage == null) {
            return null;
        }

        BufferedImage preparedImage = tintColor == null ? sourceImage : tintImage(sourceImage, tintColor);

        Image scaledImage = preparedImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.SrcOver.derive(alpha));
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();

        return new ImageIcon(image);
    }

    private static BufferedImage loadSourceImage() {
        try {
            BufferedImage source = ImageIO.read(new File(STAR_IMAGE_PATH));
            if (source == null) {
                return null;
            }

            BufferedImage transparent = new BufferedImage(
                    source.getWidth(),
                    source.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            for (int y = 0; y < source.getHeight(); y++) {
                for (int x = 0; x < source.getWidth(); x++) {
                    int argb = source.getRGB(x, y);
                    int alpha = (argb >>> 24) & 0xFF;
                    int red = (argb >>> 16) & 0xFF;
                    int green = (argb >>> 8) & 0xFF;
                    int blue = argb & 0xFF;

                    boolean nearBlack = red <= 10 && green <= 10 && blue <= 10;
                    if (alpha == 0 || nearBlack) {
                        transparent.setRGB(x, y, 0x00000000);
                    } else {
                        transparent.setRGB(x, y, argb);
                    }
                }
            }

            return transparent;
        } catch (IOException e) {
            return null;
        }
    }

    private static BufferedImage tintImage(BufferedImage source, Color tintColor) {
        BufferedImage tinted = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int argb = source.getRGB(x, y);
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha == 0) {
                    tinted.setRGB(x, y, 0x00000000);
                    continue;
                }

                int tintedArgb = (alpha << 24)
                        | (tintColor.getRed() << 16)
                        | (tintColor.getGreen() << 8)
                        | tintColor.getBlue();
                tinted.setRGB(x, y, tintedArgb);
            }
        }

        return tinted;
    }
}
