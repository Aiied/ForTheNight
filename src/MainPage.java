import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {
    public MainPage() {
        setTitle("메인 페이지");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 20, 20));

        JButton diaryButton = new JButton("다이어리");
        JButton searchButton = new JButton("검색");

        diaryButton.addActionListener(e -> {
            new TastingNotePage();
            dispose();
        });

        searchButton.addActionListener(e -> {
            new SearchPage();
            dispose();
        });

        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.add(diaryButton);
        panel.add(searchButton);

        add(panel);
        setVisible(true);
    }



}
