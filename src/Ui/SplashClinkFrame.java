package Ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

public class SplashClinkFrame extends JFrame {
    private static final int DURATION_MS = 2600;
    private final long startTimeMs = System.currentTimeMillis();
    private final Runnable onComplete;
    private final Timer timer;

    public SplashClinkFrame(Runnable onComplete) {
        this.onComplete = onComplete;
        setTitle("ForTheNight");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new ClinkPanel());

        timer = new Timer(16, e -> {
            long elapsed = System.currentTimeMillis() - startTimeMs;
            repaint();
            if (elapsed >= DURATION_MS) {
                ((Timer) e.getSource()).stop();
                dispose();
                if (this.onComplete != null) {
                    this.onComplete.run();
                }
            }
        });

        setVisible(true);
        timer.start();
    }

    private class ClinkPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            float t = Math.min(1f, (System.currentTimeMillis() - startTimeMs) / (float) DURATION_MS);

            g2.setColor(new Color(8, 8, 12));
            g2.fillRect(0, 0, w, h);

            int centerX = w / 2;
            int centerY = h / 2 + 40;
            int leftStartX = -180;
            int rightStartX = w + 180;
            int impactGap = 65;

            int leftX = (int) (leftStartX + (centerX - impactGap - leftStartX) * easeOut(t * 1.35f));
            int rightX = (int) (rightStartX + (centerX + impactGap - rightStartX) * easeOut(t * 1.35f));

            float tiltLeft = (float) Math.toRadians(-22 + 14 * easeOut(t * 1.2f));
            float tiltRight = (float) Math.toRadians(22 - 14 * easeOut(t * 1.2f));

            drawGlass(g2, leftX, centerY, tiltLeft);
            drawGlass(g2, rightX, centerY, tiltRight);

            float impactPhase = clamp((t - 0.52f) / 0.18f);
            if (impactPhase > 0f) {
                drawImpact(g2, centerX, centerY - 70, impactPhase);
            }

            g2.setColor(new Color(255, 244, 214, (int) (255 * clamp((t - 0.2f) / 0.45f))));
            g2.setFont(new Font("SansSerif", Font.BOLD, 50));
            String text = "FOR THE NIGHT";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, centerX - textWidth / 2, centerY + 220);

            g2.dispose();
        }

        private void drawGlass(Graphics2D g2, int x, int y, float angle) {
            Graphics2D g = (Graphics2D) g2.create();
            g.translate(x, y);
            g.rotate(angle);

            Path2D bowl = new Path2D.Float();
            bowl.moveTo(-46, -92);
            bowl.curveTo(-58, -18, -38, 34, 0, 46);
            bowl.curveTo(38, 34, 58, -18, 46, -92);
            bowl.closePath();

            g.setColor(new Color(255, 255, 255, 35));
            g.fill(bowl);
            g.setColor(new Color(245, 236, 220, 180));
            g.setStroke(new BasicStroke(3f));
            g.draw(bowl);

            g.setColor(new Color(232, 186, 95, 160));
            g.fillOval(-24, -30, 48, 22);
            g.setColor(new Color(240, 200, 120, 180));
            g.drawOval(-24, -30, 48, 22);

            g.setColor(new Color(220, 200, 170, 160));
            g.fillRoundRect(-5, 44, 10, 34, 8, 8);
            g.fillOval(-25, 74, 50, 12);

            g.dispose();
        }

        private void drawImpact(Graphics2D g2, int x, int y, float phase) {
            Graphics2D g = (Graphics2D) g2.create();
            int alpha = (int) (220 * (1f - phase));
            g.setColor(new Color(255, 240, 200, Math.max(alpha, 0)));
            g.setStroke(new BasicStroke(3f));
            int radius = 18 + (int) (120 * phase);
            g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

            for (int i = 0; i < 10; i++) {
                double a = i * (Math.PI * 2d / 10d);
                int r1 = 18;
                int r2 = 65 + (int) (100 * phase);
                int x1 = x + (int) (Math.cos(a) * r1);
                int y1 = y + (int) (Math.sin(a) * r1);
                int x2 = x + (int) (Math.cos(a) * r2);
                int y2 = y + (int) (Math.sin(a) * r2);
                g.drawLine(x1, y1, x2, y2);
            }
            g.dispose();
        }

        private float easeOut(float value) {
            float v = clamp(value);
            return 1f - (1f - v) * (1f - v) * (1f - v);
        }

        private float clamp(float value) {
            if (value < 0f) {
                return 0f;
            }
            return Math.min(value, 1f);
        }
    }
}
