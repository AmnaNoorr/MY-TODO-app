package ui;

import model.TaskV5Filtered;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class TodoFrame extends JFrame {
    private java.util.List<TaskV5Filtered> tasks = new ArrayList<>();
    private TaskTableModel tableModel;
    private JTable table;
    private static final String SAVE_FILE = "todo_tasks_v5_filtered.ser";

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JComboBox<String> categoryBox;
    private JComboBox<String> filterBox;
    private JSpinner dateSpinner;

    public TodoFrame() {
        super("My TODO");

        // --- 🎨 Theme Colors ---
        Color bgMain = new Color(245, 245, 240);
        Color accent = new Color(168, 112, 78);
        Color panelBg = new Color(250, 250, 245);
        Color textColor = new Color(30, 30, 30);
        Color buttonBg = new Color(168, 112, 78);
        Color buttonFg = Color.WHITE;
        Color borderColor = new Color(200, 200, 190);

        // --- Table and Model ---
        tableModel = new TaskTableModel(tasks);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // --- Buttons ---
        JButton saveBtn = new JButton(" Save");
        JButton loadBtn = new JButton(" Load");
        JButton editBtn = new JButton(" Edit");
        JButton deleteBtn = new JButton(" Remove");
        JButton showCompletedBtn = new JButton(" Show Completed");
        JButton showAllBtn = new JButton(" Show All");
        JButton filterBtn = new JButton("Apply Filter");
        JButton addBtn = new JButton(" Add Task");

        // --- Filter box ---
        filterBox = new JComboBox<>(new String[]{"ALL", "WORK", "PERSONAL", "STUDY", "OTHER"});

        // --- Top panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(saveBtn);
        topPanel.add(loadBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);
        topPanel.add(showCompletedBtn);
        topPanel.add(showAllBtn);
        topPanel.add(new JLabel(" | Filter by Category: "));
        topPanel.add(filterBox);
        topPanel.add(filterBtn);

        // --- Form panel ---
        titleField = new JTextField(15);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        priorityBox = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
        categoryBox = new JComboBox<>(new String[]{"WORK", "PERSONAL", "STUDY", "OTHER"});
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(descriptionArea));
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityBox);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);
        formPanel.add(new JLabel("Deadline:"));
        formPanel.add(dateSpinner);

        // --- Bottom panel ---
        JLabel statusLabel = new JLabel("Ready");
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(addBtn, BorderLayout.SOUTH);

        // --- Layout setup ---
        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 🎨 Apply styling ---
        getContentPane().setBackground(bgMain);
        topPanel.setBackground(panelBg);
        bottomPanel.setBackground(panelBg);
        formPanel.setBackground(panelBg);
        scrollPane.getViewport().setBackground(Color.WHITE);
        table.setBackground(Color.WHITE);
        table.setForeground(textColor);

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(borderColor),
                        "New Task",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        accent),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // --- 🧩 Table header & selection colors ---
        table.setGridColor(borderColor);
        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(224, 242, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // responsive columns

        // --- 🔘 Button styling ---
        JButton[] buttons = {saveBtn, loadBtn, editBtn, deleteBtn, showCompletedBtn, showAllBtn, filterBtn, addBtn};
        for (JButton btn : buttons) {
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // --- 🧠 Listeners ---
        addBtn.addActionListener(e -> addTask());
        deleteBtn.addActionListener(e -> removeTask());
        editBtn.addActionListener(e -> editTask());
        saveBtn.addActionListener(e -> saveTasks(statusLabel));
        loadBtn.addActionListener(e -> loadTasks());
        filterBtn.addActionListener(e -> applyFilter());
        showCompletedBtn.addActionListener(e -> showCompletedTasks());
        showAllBtn.addActionListener(e -> refreshList());

        // --- 🎨 Custom Cell Renderer ---
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                TaskV5Filtered task = ((TaskTableModel) table.getModel()).getTaskAt(row);

                c.setForeground(Color.BLACK);
                c.setFont(c.getFont().deriveFont(Font.PLAIN));

                if (task.isCompleted()) {
                    c.setForeground(Color.GRAY);
                    Font font = c.getFont().deriveFont(Font.ITALIC);
                    Map<TextAttribute, Object> attrs = new HashMap<>(font.getAttributes());
                    attrs.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                    c.setFont(font.deriveFont(attrs));
                }

                // Priority color
                if (column == 3) {
                    switch (task.getPriority()) {
                        case "HIGH" -> c.setForeground(new Color(220, 20, 60));
                        case "MEDIUM" -> c.setForeground(new Color(255, 140, 0));
                        case "LOW" -> c.setForeground(new Color(34, 139, 34));
                    }
                }

                // Deadline highlighting
                if (column == 5 && task.getDeadline() != null) {
                    LocalDate today = LocalDate.now();
                    if (task.getDeadline().isBefore(today)) {
                        c.setForeground(Color.RED);
                    } else if (task.getDeadline().isBefore(today.plusDays(2))) {
                        c.setForeground(new Color(255, 165, 0));
                    }
                }

                return c;
            }
        });

        // --- Frame settings (Full Screen + Responsive) ---
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full-screen (maximized)
        setUndecorated(false); // true = borderless fullscreen
        setMinimumSize(new Dimension(900, 600)); // keep it usable when resized
        loadTasks();
        setVisible(true);
    }

    // ---------------- Task Methods ----------------
    private void addTask() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String desc = descriptionArea.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        String category = (String) categoryBox.getSelectedItem();
        Date date = (Date) dateSpinner.getValue();
        LocalDate deadline = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        tasks.add(new TaskV5Filtered(title, desc, priority, category, deadline));
        refreshList();
        titleField.setText("");
        descriptionArea.setText("");
    }

    private void removeTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task first", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        tasks.remove(tableModel.getTaskAt(row));
        refreshList();
    }

    private void editTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task first", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TaskV5Filtered t = tableModel.getTaskAt(row);

        JTextField titleF = new JTextField(t.getTitle());
        JTextArea descF = new JTextArea(t.getDescription(), 3, 20);
        JComboBox<String> priF = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
        priF.setSelectedItem(t.getPriority());
        JComboBox<String> catF = new JComboBox<>(new String[]{"WORK", "PERSONAL", "STUDY", "OTHER"});
        catF.setSelectedItem(t.getCategory());
        JSpinner dateF = new JSpinner(new SpinnerDateModel());
        dateF.setEditor(new JSpinner.DateEditor(dateF, "dd/MM/yyyy"));
        if (t.getDeadline() != null)
            dateF.setValue(Date.from(t.getDeadline().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Title:")); panel.add(titleF);
        panel.add(new JLabel("Description:")); panel.add(new JScrollPane(descF));
        panel.add(new JLabel("Priority:")); panel.add(priF);
        panel.add(new JLabel("Category:")); panel.add(catF);
        panel.add(new JLabel("Deadline:")); panel.add(dateF);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            t.setTitle(titleF.getText().trim());
            t.setDescription(descF.getText().trim());
            t.setPriority((String) priF.getSelectedItem());
            t.setCategory((String) catF.getSelectedItem());
            t.setDeadline(((Date) dateF.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            refreshList();
        }
    }

    private void applyFilter() {
        String selectedCategory = (String) filterBox.getSelectedItem();
        java.util.List<TaskV5Filtered> filtered = new ArrayList<>();
        for (TaskV5Filtered t : tasks) {
            if (selectedCategory.equals("ALL") || t.getCategory().equals(selectedCategory)) {
                filtered.add(t);
            }
        }
        tableModel.setTasks(filtered);
    }

    private void showCompletedTasks() {
        java.util.List<TaskV5Filtered> completed = new ArrayList<>();
        for (TaskV5Filtered t : tasks) if (t.isCompleted()) completed.add(t);
        tableModel.setTasks(completed);
    }

    private void refreshList() {
        tableModel.setTasks(tasks);
    }

    private void saveTasks(JLabel statusLabel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(tasks);
            statusLabel.setText("✅ Tasks saved successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            tasks = (java.util.List<TaskV5Filtered>) ois.readObject();
            refreshList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
