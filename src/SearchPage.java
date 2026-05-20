import javax.swing.*;
import java.awt.*;
class SearchPage extends JFrame {

    public SearchPage() {
        setTitle("검색");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("검색 페이지", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
