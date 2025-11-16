package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        super("My TODO - Welcome");

        // 🎨 Theme Colors
        Color accent = new Color(168, 112, 78); // Teal
        Color bg = new Color(245, 245, 240);   // Soft beige

        // 🏷️ Title
        JLabel title = new JLabel("Welcome to My TODO", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 38));
        title.setForeground(accent);

        // 💬 Subtitle
        JLabel subtitle = new JLabel("Plan smarter. Stay organized. Get things done.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(80, 80, 80));

        // 🚀 Start Button
        JButton startButton = new JButton("→ Open My Tasks");
        startButton.setBackground(accent);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // 🪞 Center layout panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bg);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(title);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(subtitle);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel);

        // 🖥 Fullscreen window like TodoFrame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bg);

        // 🎬 Button Action — open TodoFrame manually
        startButton.addActionListener((ActionEvent e) -> openTodo());
    }

    private void openTodo() {
        new TodoFrame().setVisible(true);
        dispose(); // Close welcome screen
    }
}
