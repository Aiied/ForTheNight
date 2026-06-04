package InitialSetup;

import Ui.BackgroundPanel;
import Ui.Button.BackButton;
import Ui.ScreenScale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TastePreferencePage extends JFrame {
    private static final Color CARD_BACKGROUND = new Color(16, 16, 16, 220);
    private static final Color TEXT_PRIMARY = new Color(245, 235, 220);
    private static final Color TEXT_SECONDARY = new Color(205, 190, 168);
    private static final Color ACCENT = new Color(170, 126, 62);
    private static final Color BUTTON_NORMAL = new Color(22, 22, 22);
    private static final Color BUTTON_HOVER = new Color(38, 30, 22);

    private final JFrame previousPage;
    private final List<TasteQuestion> questions = TasteTestRepository.loadQuestions();
    private final LinkedHashMap<String, TasteResultProfile> results = new LinkedHashMap<>(TasteTestRepository.loadResults());
    private final int[] answers;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JLabel resultTitleLabel = new JLabel("", SwingConstants.CENTER);
    private final JTextArea resultDescriptionArea = new JTextArea();
    private final JLabel resultExamplesLabel = new JLabel("", SwingConstants.CENTER);

    public TastePreferencePage(JFrame previousPage) {
        this.previousPage = previousPage;
        this.answers = new int[Math.max(questions.size(), 1)];

        setTitle("Whisky Taste Test");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel(new BorderLayout()));

        if (questions.isEmpty() || results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Taste test data could not be loaded.");
            openMainPage();
            dispose();
            return;
        }

        add(createTopPanel(), BorderLayout.NORTH);
        cardPanel.setOpaque(false);
        cardPanel.add(createQuestionView(), "question");
        cardPanel.add(createResultView(), "result");
        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(16),
                0,
                ScreenScale.scale(16)
        ));

        BackButton backButton = new BackButton();
        backButton.setEnabled(previousPage != null);
        backButton.addActionListener(e -> {
            if (previousPage != null) {
                previousPage.setVisible(true);
            }
            dispose();
        });

        topPanel.add(backButton, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel createQuestionView() {
        JPanel outerPanel = new BackgroundPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = createTitleLabel("Find Your Whisky Taste");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(ScreenScale.dimension(980, 760));

        JLabel progressLabel = new JLabel("Answer all " + questions.size() + " questions and scroll freely.", SwingConstants.CENTER);
        progressLabel.setForeground(TEXT_SECONDARY);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(18)));
        progressLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setOpaque(false);
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        for (int index = 0; index < questions.size(); index++) {
            questionsPanel.add(createQuestionCard(index, questions.get(index)));
            if (index < questions.size() - 1) {
                questionsPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 16)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(ScreenScale.scale(18));
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(ScreenScale.dimension(900, 520));
        scrollPane.setMaximumSize(ScreenScale.dimension(900, 520));

        JButton showResultButton = createActionButton("SEE RESULT", 170);
        showResultButton.setAlignmentX(CENTER_ALIGNMENT);
        showResultButton.addActionListener(e -> showResult());

        card.add(progressLabel);
        card.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        card.add(scrollPane);
        card.add(Box.createRigidArea(ScreenScale.dimension(0, 20)));
        card.add(showResultButton);

        outerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 18)));
        outerPanel.add(titleLabel);
        outerPanel.add(Box.createVerticalGlue());
        card.setAlignmentX(CENTER_ALIGNMENT);
        outerPanel.add(card);
        outerPanel.add(Box.createVerticalGlue());
        return outerPanel;
    }

    private JPanel createResultView() {
        JPanel outerPanel = new BackgroundPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = createTitleLabel("Your Whisky Match");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(ScreenScale.dimension(920, 680));

        resultTitleLabel.setForeground(TEXT_PRIMARY);
        resultTitleLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(28)));
        resultTitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        resultDescriptionArea.setEditable(false);
        resultDescriptionArea.setFocusable(false);
        resultDescriptionArea.setLineWrap(true);
        resultDescriptionArea.setWrapStyleWord(true);
        resultDescriptionArea.setOpaque(false);
        resultDescriptionArea.setForeground(TEXT_PRIMARY);
        resultDescriptionArea.setFont(new Font("SansSerif", Font.PLAIN, ScreenScale.scale(19)));
        resultDescriptionArea.setBorder(new EmptyBorder(
                ScreenScale.scale(18),
                0,
                ScreenScale.scale(18),
                0
        ));

        resultExamplesLabel.setForeground(TEXT_SECONDARY);
        resultExamplesLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(18)));
        resultExamplesLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton continueButton = createActionButton("CONTINUE", 190);
        continueButton.setAlignmentX(CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> {
            if (previousPage != null) {
                previousPage.dispose();
            }
            openMainPage();
            dispose();
        });

        card.add(resultTitleLabel);
        card.add(resultDescriptionArea);
        card.add(resultExamplesLabel);
        card.add(Box.createRigidArea(ScreenScale.dimension(0, 24)));
        card.add(continueButton);

        outerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 24)));
        outerPanel.add(titleLabel);
        outerPanel.add(Box.createVerticalGlue());
        card.setAlignmentX(CENTER_ALIGNMENT);
        outerPanel.add(card);
        outerPanel.add(Box.createVerticalGlue());
        return outerPanel;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                BorderFactory.createEmptyBorder(
                        ScreenScale.scale(28),
                        ScreenScale.scale(32),
                        ScreenScale.scale(28),
                        ScreenScale.scale(32)
                )
        ));
        return card;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(34)));
        return label;
    }

    private JPanel createQuestionCard(int index, TasteQuestion question) {
        JPanel questionCard = new JPanel();
        questionCard.setOpaque(true);
        questionCard.setBackground(new Color(24, 24, 24, 210));
        questionCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 72, 44), 1),
                BorderFactory.createEmptyBorder(
                        ScreenScale.scale(18),
                        ScreenScale.scale(20),
                        ScreenScale.scale(18),
                        ScreenScale.scale(20)
                )
        ));
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setAlignmentX(CENTER_ALIGNMENT);
        questionCard.setMaximumSize(new Dimension(ScreenScale.scale(860), Integer.MAX_VALUE));

        JLabel numberLabel = new JLabel("Q" + (index + 1));
        numberLabel.setForeground(ACCENT);
        numberLabel.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(15)));
        numberLabel.setAlignmentX(CENTER_ALIGNMENT);

        int questionWidth = ScreenScale.scale(780);
        JTextArea questionText = new JTextArea(question.getQuestion());
        questionText.setEditable(false);
        questionText.setFocusable(false);
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setOpaque(false);
        questionText.setForeground(TEXT_PRIMARY);
        questionText.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(22)));
        questionText.setBorder(new EmptyBorder(ScreenScale.scale(10), 0, ScreenScale.scale(12), 0));
        questionText.setAlignmentX(CENTER_ALIGNMENT);
        questionText.setSize(new Dimension(questionWidth, Short.MAX_VALUE));
        questionText.setMaximumSize(new Dimension(questionWidth, Integer.MAX_VALUE));
        questionText.setPreferredSize(questionText.getPreferredSize());

        JLabel leftOptionLabel = new JLabel(humanizeLabel(question.getLeftLabel()));
        JLabel rightOptionLabel = new JLabel(humanizeLabel(question.getRightLabel()));
        leftOptionLabel.setForeground(TEXT_SECONDARY);
        rightOptionLabel.setForeground(TEXT_SECONDARY);
        leftOptionLabel.setFont(new Font("SansSerif", Font.PLAIN, ScreenScale.scale(15)));
        rightOptionLabel.setFont(new Font("SansSerif", Font.PLAIN, ScreenScale.scale(15)));

        JPanel labelPanel = new JPanel(new BorderLayout(ScreenScale.scale(12), 0));
        labelPanel.setOpaque(false);
        labelPanel.add(leftOptionLabel, BorderLayout.WEST);
        labelPanel.add(rightOptionLabel, BorderLayout.EAST);

        JSlider slider = new JSlider(-3, 3, answers[index]);
        slider.setOpaque(false);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(false);
        slider.setSnapToTicks(true);
        slider.addChangeListener(e -> {
            answers[index] = slider.getValue();
        });

        questionCard.add(numberLabel);
        questionCard.add(questionText);
        questionCard.add(labelPanel);
        questionCard.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        questionCard.add(slider);
        return questionCard;
    }

    private void showResult() {
        LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
        for (String key : results.keySet()) {
            scores.put(key, 0);
        }

        for (int index = 0; index < questions.size(); index++) {
            TasteQuestion question = questions.get(index);
            int answer = answers[index];
            for (String target : question.getTargets()) {
                scores.put(target, scores.getOrDefault(target, 0) + answer);
            }
        }

        TasteResultProfile bestProfile = null;
        int bestScore = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestScore = entry.getValue();
                bestProfile = results.get(entry.getKey());
            }
        }

        if (bestProfile == null) {
            JOptionPane.showMessageDialog(this, "Could not calculate a taste result.");
            return;
        }

        resultTitleLabel.setText(bestProfile.getTitle());
        resultDescriptionArea.setText(bestProfile.getDescription());
        resultDescriptionArea.setCaretPosition(0);
        resultExamplesLabel.setText("Recommended examples: " + bestProfile.getExamples());
        cardLayout.show(cardPanel, "result");
    }

    private JButton createActionButton(String text, int width) {
        JButton button = new JButton(text) {
            private boolean hovered;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = ScreenScale.scale(22);
                int buttonWidth = getWidth();
                int buttonHeight = getHeight();
                g2.setColor(hovered ? BUTTON_HOVER : BUTTON_NORMAL);
                g2.fillRoundRect(1, 1, buttonWidth - 2, buttonHeight - 2, arc, arc);

                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(1, 1, buttonWidth - 3, buttonHeight - 3, arc, arc);

                g2.setFont(getFont());
                g2.setColor(TEXT_PRIMARY);
                FontMetrics metrics = g2.getFontMetrics();
                int textX = (buttonWidth - metrics.stringWidth(getText())) / 2;
                int textY = (buttonHeight - metrics.getHeight()) / 2 + metrics.getAscent();
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("SansSerif", Font.BOLD, ScreenScale.scale(15)));
        Dimension size = ScreenScale.dimension(width, 44);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        return button;
    }

    private String humanizeLabel(String value) {
        String[] words = value.split("_");
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < words.length; index++) {
            if (index > 0) {
                builder.append(' ');
            }
            String word = words[index];
            if (word.isEmpty()) {
                continue;
            }
            builder.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }
        }
        return builder.toString();
    }

    private void openMainPage() {
        try {
            Class<?> mainPageClass = Class.forName("MainPage");
            JFrame mainPage = (JFrame) mainPageClass.getDeclaredConstructor().newInstance();
            mainPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
