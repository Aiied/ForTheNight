package TastingNote;

import Ui.buttons.BackButton;
import Ui.buttons.AbstractActionButton;
import Ui.icon.ImageScaler;
import Ui.icon.StarIconFactory;
import Ui.panel.BackgroundPanel;
import Ui.text.TastingNoteStrings;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.theme.ThemeSpacing;
import Ui.util.AppPaths;

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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewNotePage extends JFrame {
    private static final float STAR_ON_ALPHA = 1.0f;
    private final JTextField nameField = new JTextField();
    private final JTextField aromaField = new JTextField();
    private final JTextField tasteField = new JTextField();
    private final JTextField finishField = new JTextField();
    private final JTextArea detailArea = new JTextArea(8, 28);
    private final JLabel imagePreviewLabel = new JLabel(TastingNoteStrings.NO_IMAGE_SELECTED, SwingConstants.CENTER);
    private final JLabel imageNameLabel = new JLabel(TastingNoteStrings.NO_IMAGE_SELECTED);
    private final JButton[] ratingButtons = new JButton[5];
    private final ImageIcon starOnIcon = StarIconFactory.createStarIcon(ScreenScale.scale(30), STAR_ON_ALPHA);
    private final ImageIcon starOffIcon = StarIconFactory.createStarIcon(ScreenScale.scale(30), 1.0f, ThemeColors.TEXT_WHITE);
    private final JFrame previousPage;
    private final TastingNote editingNote;
    private final int editingIndex;
    private int selectedScore = 0;
    private File selectedImageFile;
    private String currentImagePath = "";

    public NewNotePage(JFrame previousPage) {
        this(previousPage, null, -1);
    }

    public NewNotePage(JFrame previousPage, TastingNote editingNote, int editingIndex) {
        this.previousPage = previousPage;
        this.editingNote = editingNote;
        this.editingIndex = editingIndex;

        setTitle(isEditMode() ? TastingNoteStrings.EDIT_NOTE_TITLE : TastingNoteStrings.NEW_NOTE_TITLE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel(new BorderLayout()));

        JButton backButton = new BackButton();
        backButton.addActionListener(e -> {
            returnToPreviousPage();
        });

        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeSpacing.scale(ThemeSpacing.SPACE_12),
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN),
                0,
                ThemeSpacing.scale(ThemeSpacing.PAGE_MARGIN)
        ));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel(
                isEditMode() ? TastingNoteStrings.EDIT_NOTE_HEADER : TastingNoteStrings.WRITE_NOTE_HEADER,
                SwingConstants.CENTER
        );
        title.setForeground(ThemeColors.TEXT_WHITE);
        title.setFont(ThemeFonts.bold(28));

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(true);
        formPanel.setBackground(ThemeColors.SURFACE_CARD_OVERLAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeSpacing.scale(ThemeSpacing.SPACE_20),
                ThemeSpacing.scale(ThemeSpacing.SPACE_20),
                ThemeSpacing.scale(ThemeSpacing.SPACE_20),
                ThemeSpacing.scale(ThemeSpacing.SPACE_20)
        ));
        formPanel.setMaximumSize(ScreenScale.dimension(
                ThemeSizes.TASTING_NOTE_FORM_WIDTH,
                ThemeSizes.TASTING_NOTE_FORM_HEIGHT
        ));

        formPanel.add(createFieldBlock(TastingNoteStrings.FIELD_NAME, nameField));
        formPanel.add(createFieldBlock(TastingNoteStrings.FIELD_AROMA, aromaField));
        formPanel.add(createFieldBlock(TastingNoteStrings.FIELD_TASTE, tasteField));
        formPanel.add(createFieldBlock(TastingNoteStrings.FIELD_FINISH, finishField));
        formPanel.add(createImageBlock());
        formPanel.add(createRatingBlock());
        formPanel.add(createAreaBlock(TastingNoteStrings.FIELD_DETAIL_REVIEW, detailArea));

        JButton saveButton = new AbstractActionButton(
                isEditMode() ? TastingNoteStrings.UPDATE_BUTTON : TastingNoteStrings.SAVE_BUTTON,
                ScreenScale.scale(16)
        ) { };
        saveButton.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.NOTE_SAVE_BUTTON_WIDTH,
                ThemeSizes.NOTE_SAVE_BUTTON_HEIGHT
        ));
        saveButton.addActionListener(e -> saveNote());

        JPanel savePanel = new JPanel();
        savePanel.setOpaque(false);
        savePanel.add(saveButton);
        formPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_10));
        formPanel.add(savePanel);

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        title.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        centerPanel.add(title);
        centerPanel.add(ThemeSpacing.verticalGap(ThemeSpacing.SPACE_18));
        centerPanel.add(Box.createVerticalGlue());
        formPanel.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        if (isEditMode()) {
            populateForm(editingNote);
        } else {
            setScore(0);
        }
        setVisible(true);
    }

    private JPanel createFieldBlock(String label, JTextField field) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0, ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(14));

        field.setFont(ThemeFonts.plain(14));
        field.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.TASTING_NOTE_FIELD_WIDTH,
                ThemeSizes.TASTING_NOTE_FIELD_HEIGHT
        ));

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(field, BorderLayout.CENTER);
        return block;
    }

    private JPanel createAreaBlock(String label, JTextArea area) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0, ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(14));

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(ThemeFonts.plain(14));

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(ScreenScale.dimension(
                ThemeSizes.TASTING_NOTE_DETAIL_AREA_WIDTH,
                ThemeSizes.TASTING_NOTE_DETAIL_AREA_HEIGHT
        ));

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(scrollPane, BorderLayout.CENTER);
        return block;
    }

    private JPanel createImageBlock() {
        JPanel block = new JPanel(new BorderLayout(0, ThemeSpacing.scale(ThemeSpacing.SPACE_8)));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0, ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0));

        JLabel titleLabel = new JLabel(TastingNoteStrings.FIELD_PHOTO);
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(14));

        JButton selectImageButton = new AbstractActionButton(TastingNoteStrings.CHOOSE_IMAGE, ScreenScale.scale(13)) { };
        selectImageButton.addActionListener(e -> chooseImage());

        JButton removeImageButton = new AbstractActionButton(TastingNoteStrings.REMOVE_IMAGE, ScreenScale.scale(13)) { };
        removeImageButton.addActionListener(e -> clearSelectedImage());

        JPanel topRow = new JPanel(new BorderLayout(ThemeSpacing.scale(ThemeSpacing.SPACE_10), 0));
        topRow.setOpaque(false);
        imageNameLabel.setForeground(ThemeColors.TEXT_MUTED);
        imageNameLabel.setFont(ThemeFonts.plain(13));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeSpacing.scale(ThemeSpacing.SPACE_8), 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(selectImageButton);
        buttonPanel.add(removeImageButton);
        topRow.add(buttonPanel, BorderLayout.WEST);
        topRow.add(imageNameLabel, BorderLayout.CENTER);

        int previewWidth = ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_WIDTH);
        int previewHeight = ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_HEIGHT);
        imagePreviewLabel.setPreferredSize(ThemeSizes.scaledTastingNotePreview());
        imagePreviewLabel.setOpaque(true);
        imagePreviewLabel.setBackground(ThemeColors.SURFACE_INPUT_ALT);
        imagePreviewLabel.setForeground(ThemeColors.TEXT_MUTED);
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(ThemeColors.BORDER_SUBTLE));

        block.add(titleLabel, BorderLayout.NORTH);
        block.add(topRow, BorderLayout.CENTER);
        block.add(imagePreviewLabel, BorderLayout.SOUTH);
        return block;
    }

    private JPanel createRatingBlock() {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        block.setBorder(BorderFactory.createEmptyBorder(ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0, ThemeSpacing.scale(ThemeSpacing.SPACE_6), 0));

        JLabel titleLabel = new JLabel(TastingNoteStrings.FIELD_RATING);
        titleLabel.setForeground(ThemeColors.TEXT_WHITE);
        titleLabel.setFont(ThemeFonts.bold(14));

        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeSpacing.scale(ThemeSpacing.SPACE_4), 0));
        starsPanel.setOpaque(false);

        for (int i = 0; i < ratingButtons.length; i++) {
            final int score = i + 1;
            JButton starButton = new JButton("\u2606");
            starButton.setFocusPainted(false);
            starButton.setBorderPainted(false);
            starButton.setContentAreaFilled(false);
            starButton.setOpaque(false);
            starButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            starButton.setFont(ThemeFonts.bold(24));
            starButton.setPreferredSize(ScreenScale.dimension(
                    ThemeSizes.RATING_BUTTON_SIZE,
                    ThemeSizes.RATING_BUTTON_SIZE
            ));
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
                ratingButtons[i].setForeground(filled ? ThemeColors.ACCENT_STAR : ThemeColors.TEXT_WHITE);
            }
        }
    }

    private void saveNote() {
        String name = nameField.getText().trim();
        String detail = detailArea.getText().trim().replace("\r\n", " ").replace("\n", " ");

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, TastingNoteStrings.NAME_REQUIRED);
            return;
        }

        String savedImagePath = saveSelectedImage(name);
        if (selectedImageFile != null && savedImagePath == null) {
            return;
        }

        String noteDate = isEditMode() ? editingNote.getDate() : LocalDate.now().toString();
        String finalImagePath = savedImagePath == null ? currentImagePath : savedImagePath;
        TastingNote note = new TastingNote(
                name,
                noteDate,
                parseCommaSeparated(aromaField.getText()),
                parseCommaSeparated(tasteField.getText()),
                parseCommaSeparated(finishField.getText()),
                selectedScore,
                detail,
                finalImagePath
        );

        try {
            if (isEditMode()) {
                TastingNoteList.replaceTastingNote(AppPaths.TASTING_NOTES_FILE, editingIndex, note);
                JOptionPane.showMessageDialog(this, TastingNoteStrings.UPDATED);
                returnToPreviousPage();
                return;
            }

            appendTastingNote(note);
            JOptionPane.showMessageDialog(this, TastingNoteStrings.SAVED);
            clearForm();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, TastingNoteStrings.SAVE_FAILED);
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nameField.setText("");
        aromaField.setText("");
        tasteField.setText("");
        finishField.setText("");
        detailArea.setText("");
        selectedImageFile = null;
        currentImagePath = "";
        imageNameLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
        imagePreviewLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
        imagePreviewLabel.setIcon(null);
        setScore(0);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(TastingNoteStrings.SELECT_TASTING_NOTE_PHOTO);
        chooser.setFileFilter(new FileNameExtensionFilter(TastingNoteStrings.IMAGE_FILES, "jpg", "jpeg", "png", "gif", "bmp", "webp"));
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        if (file == null || !file.isFile()) {
            return;
        }

        selectedImageFile = file;
        currentImagePath = "";
        imageNameLabel.setText(file.getName());

        ImageIcon previewIcon = ImageScaler.loadScaledIcon(
                file.getAbsolutePath(),
                ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_WIDTH),
                ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_HEIGHT)
        );
        imagePreviewLabel.setText(previewIcon == null ? TastingNoteStrings.PREVIEW_UNAVAILABLE : "");
        imagePreviewLabel.setIcon(previewIcon);
    }

    private String saveSelectedImage(String whiskyName) {
        if (selectedImageFile == null) {
            return null;
        }

        try {
            Path imageDir = Path.of(AppPaths.TASTING_NOTE_IMAGE_DIR);
            Files.createDirectories(imageDir);

            String extension = getFileExtension(selectedImageFile.getName());
            String safeName = sanitizeFileName(whiskyName);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = safeName + "_" + timestamp + extension;
            Path targetPath = imageDir.resolve(fileName);

            Files.copy(selectedImageFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString().replace('\\', '/');
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, TastingNoteStrings.IMAGE_SAVE_FAILED);
            e.printStackTrace();
            return null;
        }
    }

    private void populateForm(TastingNote note) {
        nameField.setText(note.getWhiskyName());
        aromaField.setText(joinNotes(note.getAroma()));
        tasteField.setText(joinNotes(note.getTaste()));
        finishField.setText(joinNotes(note.getFinish()));
        detailArea.setText(note.getDetailReview());
        currentImagePath = note.getImagePath() == null ? "" : note.getImagePath();
        selectedImageFile = null;
        setScore(note.getScore());
        updatePreviewFromCurrentImage();
    }

    private void updatePreviewFromCurrentImage() {
        if (currentImagePath == null || currentImagePath.isBlank()) {
            imageNameLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
            imagePreviewLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
            imagePreviewLabel.setIcon(null);
            return;
        }

        File imageFile = new File(currentImagePath);
        imageNameLabel.setText(imageFile.getName());
        ImageIcon previewIcon = ImageScaler.loadScaledIcon(
                currentImagePath,
                ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_WIDTH),
                ScreenScale.scale(ThemeSizes.TASTING_NOTE_PREVIEW_HEIGHT)
        );
        imagePreviewLabel.setText(previewIcon == null ? TastingNoteStrings.PREVIEW_UNAVAILABLE : "");
        imagePreviewLabel.setIcon(previewIcon);
    }

    private void clearSelectedImage() {
        selectedImageFile = null;
        currentImagePath = "";
        imageNameLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
        imagePreviewLabel.setText(TastingNoteStrings.NO_IMAGE_SELECTED);
        imagePreviewLabel.setIcon(null);
    }

    private ArrayList<String> parseCommaSeparated(String text) {
        ArrayList<String> values = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return values;
        }

        for (String part : text.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    private String joinNotes(List<String> notes) {
        ArrayList<String> filtered = new ArrayList<>();
        for (String note : notes) {
            if (note != null && !note.isBlank()) {
                filtered.add(note.trim());
            }
        }
        return String.join(", ", filtered);
    }

    private void appendTastingNote(TastingNote note) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AppPaths.TASTING_NOTES_FILE, true))) {
            writer.write(note.toStorageLine());
            writer.newLine();
        }
    }

    private boolean isEditMode() {
        return editingNote != null && editingIndex >= 0;
    }

    private void returnToPreviousPage() {
        if (previousPage != null) {
            previousPage.setVisible(true);
        }
        dispose();
    }

    private String sanitizeFileName(String text) {
        String sanitized = text == null ? "" : text.trim().replaceAll("[^a-zA-Z0-9-_]+", "_");
        return sanitized.isBlank() ? TastingNoteStrings.DEFAULT_FILE_NAME : sanitized;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return ".png";
        }

        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            return ".png";
        }
        return fileName.substring(lastDot);
    }
}
