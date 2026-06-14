package Ui.component;

import Ui.buttons.BackButton;
import Ui.panel.BackgroundPanel;
import Ui.theme.ScreenScale;
import Ui.theme.ThemeColors;
import Ui.theme.ThemeFonts;
import Ui.theme.ThemeSizes;
import Ui.util.BaseList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

public abstract class AbstractSearchListPage<T> extends JFrame {
    private static final String TAB_ITEMS_PROPERTY = "tabItems";
    private static final String TAB_LOADED_PROPERTY = "tabLoaded";

    private final JFrame previousPage;
    protected final BaseSearchField nameField;
    protected final JTabbedPane listTabs;

    protected AbstractSearchListPage(
            String title,
            JFrame previousPage,
            String searchPlaceholder,
            int searchWidth,
            int searchHeight
    ) {
        this.previousPage = previousPage;
        this.nameField = new BaseSearchField(searchPlaceholder, searchWidth, searchHeight) { };
        this.listTabs = new StyledTabbedPane();

        setTitle(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ChangeListener tabLoadListener = e -> ensureSelectedTabLoaded();
        this.listTabs.addChangeListener(tabLoadListener);
    }

    protected final void initializeSearchPage() {
        JPanel topPanel = new BackgroundPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(12),
                ScreenScale.scale(16),
                0,
                ScreenScale.scale(16)
        ));

        if (previousPage != null) {
            BackButton backButton = new BackButton();
            backButton.addActionListener(e -> {
                previousPage.setVisible(true);
                dispose();
            });
            topPanel.add(backButton, BorderLayout.WEST);
        }

        JComponent topCenter = createTopCenterComponent();
        if (topCenter != null) {
            topPanel.add(topCenter, BorderLayout.CENTER);
        }

        nameField.onChange(this::refreshList);

        JPanel centerPanel = new BackgroundPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        centerPanel.add(createSearchRow());
        centerPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));

        listTabs.setAlignmentX(CENTER_ALIGNMENT);
        if (getTabbedPanePreferredWidth() > 0 || getTabbedPanePreferredHeight() > 0) {
            listTabs.setPreferredSize(ScreenScale.dimension(
                    getTabbedPanePreferredWidth(),
                    getTabbedPanePreferredHeight()
            ));
        }
        centerPanel.add(listTabs);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JComponent eastPanel = createEastPanel();
        if (eastPanel != null) {
            add(eastPanel, BorderLayout.EAST);
        }

        JComponent southPanel = createSouthPanel();
        if (southPanel != null) {
            add(southPanel, BorderLayout.SOUTH);
        }

        refreshList();
        setVisible(true);
    }

    protected final void refreshList() {
        refreshList(filterItems());
    }

    protected final void refreshList(List<T> items) {
        listTabs.removeAll();
        List<List<T>> tabs = BaseList.partition(items, getItemsPerTab());

        if (tabs.isEmpty()) {
            listTabs.addTab("1", createListScrollPane(createEmptyMessagePanel()));
            listTabs.setEnabledAt(0, false);
            return;
        }

        for (int i = 0; i < tabs.size(); i++) {
            JScrollPane scrollPane = createDeferredTabScrollPane(tabs.get(i));
            listTabs.addTab(String.valueOf(i + 1), scrollPane);
        }

        ensureSelectedTabLoaded();
    }

    private JScrollPane createDeferredTabScrollPane(List<T> tabItems) {
        JPanel placeholder = new BackgroundPanel();
        placeholder.setOpaque(false);
        JScrollPane scrollPane = createListScrollPane(placeholder);
        scrollPane.putClientProperty(TAB_ITEMS_PROPERTY, tabItems);
        scrollPane.putClientProperty(TAB_LOADED_PROPERTY, Boolean.FALSE);
        return scrollPane;
    }

    @SuppressWarnings("unchecked")
    private void ensureSelectedTabLoaded() {
        int selectedIndex = listTabs.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= listTabs.getTabCount()) {
            return;
        }

        java.awt.Component component = listTabs.getComponentAt(selectedIndex);
        if (!(component instanceof JScrollPane scrollPane)) {
            return;
        }

        Object loaded = scrollPane.getClientProperty(TAB_LOADED_PROPERTY);
        if (Boolean.TRUE.equals(loaded)) {
            return;
        }

        Object rawItems = scrollPane.getClientProperty(TAB_ITEMS_PROPERTY);
        if (!(rawItems instanceof List<?> rawList)) {
            return;
        }

        JPanel tabPanel = createTabPanel();
        for (Object rawItem : rawList) {
            tabPanel.add(createItemPanel((T) rawItem));
            tabPanel.add(Box.createRigidArea(ScreenScale.dimension(0, 10)));
        }

        scrollPane.setViewportView(tabPanel);
        scrollPane.putClientProperty(TAB_LOADED_PROPERTY, Boolean.TRUE);
    }

    protected JPanel createTabPanel() {
        JPanel tabPanel = new BackgroundPanel();
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
        tabPanel.setBorder(BorderFactory.createEmptyBorder(
                ScreenScale.scale(16),
                ScreenScale.scale(16),
                ScreenScale.scale(16),
                ScreenScale.scale(16)
        ));
        return tabPanel;
    }

    protected JScrollPane createListScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(ScreenScale.scale(16));
        return scrollPane;
    }

    protected JPanel createEmptyMessagePanel() {
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel emptyLabel = new JLabel(getEmptyMessage());
        emptyLabel.setForeground(ThemeColors.TEXT_WHITE);
        emptyLabel.setFont(ThemeFonts.bold(16));
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(ScreenScale.scale(40), 0, 0, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(emptyLabel);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    protected JPanel createSearchRow() {
        JComponent accessory = createSearchAccessoryComponent();
        nameField.setAlignmentX(CENTER_ALIGNMENT);

        if (accessory == null) {
            return wrapCentered(nameField);
        }

        JPanel searchRow = new JPanel();
        searchRow.setOpaque(false);
        searchRow.setLayout(new BoxLayout(searchRow, BoxLayout.X_AXIS));
        searchRow.setMaximumSize(ScreenScale.dimension(
                ThemeSizes.SEARCH_ROW_MAX_WIDTH,
                ThemeSizes.SEARCH_ROW_MAX_HEIGHT
        ));
        searchRow.setAlignmentX(CENTER_ALIGNMENT);

        nameField.setAlignmentY(CENTER_ALIGNMENT);
        accessory.setAlignmentY(CENTER_ALIGNMENT);

        searchRow.add(nameField);
        searchRow.add(Box.createRigidArea(ScreenScale.dimension(ThemeSizes.SEARCH_ACCESSORY_GAP, 0)));
        searchRow.add(accessory);
        return searchRow;
    }

    private JPanel wrapCentered(JComponent component) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        component.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(component);
        return panel;
    }

    protected int getTabbedPanePreferredWidth() {
        return 0;
    }

    protected int getTabbedPanePreferredHeight() {
        return 0;
    }

    protected JComponent createTopCenterComponent() {
        return null;
    }

    protected JComponent createSearchAccessoryComponent() {
        return null;
    }

    protected JComponent createEastPanel() {
        return null;
    }

    protected JComponent createSouthPanel() {
        return null;
    }

    protected abstract int getItemsPerTab();

    protected abstract String getEmptyMessage();

    protected abstract List<T> filterItems();

    protected abstract JPanel createItemPanel(T item);
}
