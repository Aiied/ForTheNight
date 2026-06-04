package TastingNote;

import Ui.Button.BackButton;
import Ui.BackgroundPanel;
import Ui.StarIconFactory;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class NewNotePage extends JFrame {
    private static final String NOTE_FILE_PATH = "src/assets/Flie(txt)/tastingNotes.txt";
    private static final float STAR_ON_ALPHA = 1.0f;
    private final JTextField nameField = new JTextField();
    private final JTextField aromaField = new JTextField();
    private final JTextField tasteField = new JTextField();
    private final JTextField finishField = new JTextField();
    private final JTextArea detailArea = new JTextArea(8, 28);
    private final JButton[] ratingButtons = new JButton[5];
    private final ImageIcon starOnIcon = StarIconFactory.createStarIcon(30, STAR_ON_ALPHA);
    private final ImageIcon starOffIcon = StarIconFactory.createStarIcon(30, 1.0f, Color.WHITE);
    private int selectedScore = 0;

    public NewNotePage(JFrame previousPage) {
        setTitle("New Note");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel(new BorderLayout()));

        JButton backButton = new BackButton();
        backButton.addActionListener(e -> {
            if (previousPage != null) {
                previousPage.setVisible(true);
            }
            dispose();
        });

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Write New Note", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(true);
        formPanel.setBackground(new Color(18, 18, 18, 220));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setMaximumSize(new Dimension(760, 600));

        formPanel.add(createFieldBlock("Name", nameField));
        formPanel.add(createFieldBlock("Aroma (comma separated)", aromaField));
        formPanel.add(createFieldBlock("Taste (comma separated)", tasteField));
        formPanel.add(createFieldBlock("Finish (comma separated)", finishField));
        formPanel.add(createRatingBlock());
        formPanel.add(createAreaBlock("Detailed Review", detailArea));

        JButton saveButton = new JButton("Save");
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(140, 42));
        saveButton.addActionListener(e -> saveNote());

        JPanel savePanel = new JPanel();
        savePanel.setOpaque(false);
        savePanel.add(saveButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(savePanel);

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        title.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        centerPanel.add(title);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        centerPanel.add(Box.createVerticalGlue());
        formPanel.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setScore(0);
        setVisible(true);
    }

    private JPanel createFieldBlock(String label, JTextField field) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(620, 34));

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(field, BorderLayout.CENTER);
        return block;
    }

    private JPanel createAreaBlock(String label, JTextArea area) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(620, 150));

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(scrollPane, BorderLayout.CENTER);
        return block;
    }

    private JPanel createRatingBlock() {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel titleLabel = new JLabel("Rating");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        starsPanel.setOpaque(false);

        for (int i = 0; i < ratingButtons.length; i++) {
            final int score = i + 1;
            JButton starButton = new JButton("\u2606");
            starButton.setFocusPainted(false);
            starButton.setBorderPainted(false);
            starButton.setContentAreaFilled(false);
            starButton.setOpaque(false);
            starButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            starButton.setFont(new Font("SansSerif", Font.BOLD, 24));
            starButton.setPreferredSize(new Dimension(40, 40));
            starButton.addActionListener(e -> setScore(score));
            ratingButtons[i] = starButton;
            starsPanel.add(starButton);
        }

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(starsPanel, BorderLayout.CENTER);
        return block;
    }

    private void setScore(int score) {
        selectedScore = Math.max(0, Math.min(score, ratingButtons.length));

        for (int i = 0; i < ratingButtons.length; i++) {
            boolean filled = i < selectedScore;
            if (starOnIcon != null && starOffIcon != null) {
                ratingButtons[i].setText("");
                ratingButtons[i].setIcon(filled ? starOnIcon : starOffIcon);
            } else {
                ratingButtons[i].setText(filled ? "\u2605" : "\u2606");
                ratingButtons[i].setForeground(filled ? new Color(242, 196, 74) : Color.WHITE);
            }
        }
    }

    private void saveNote() {
        String name = nameField.getText().trim();
        String aroma = aromaField.getText().trim();
        String taste = tasteField.getText().trim();
        String finish = finishField.getText().trim();
        String detail = detailArea.getText().trim().replace("\r\n", " ").replace("\n", " ");

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        String today = LocalDate.now().toString();
        String line = String.join("|", name, today, aroma, taste, finish, Integer.toString(selectedScore), detail);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOTE_FILE_PATH, true))) {
            writer.write(line);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Saved.");
            clearForm();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed.");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nameField.setText("");
        aromaField.setText("");
        tasteField.setText("");
        finishField.setText("");
        detailArea.setText("");
        setScore(0);
    }
}
