package Pairing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PairingPage extends JFrame {
    public PairingPage() {
        setTitle("Pairing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Pairing Page", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
