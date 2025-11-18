package sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelectionScreen extends JFrame {
    private LevelManager levelManager;

    public LevelSelectionScreen(LevelManager levelManager) {
        this.levelManager = levelManager;
        setTitle("Sokoban - Level Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(40, 40, 80));
        JLabel titleLabel = new JLabel("SELECT LEVEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Level buttons panel (grid layout)
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 3, 15, 15));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create level buttons
        for (int i = 1; i <= 10; i++) {
            final int levelNumber = i;
            JButton levelBtn = new JButton("Level " + i);
            levelBtn.setFont(new Font("Arial", Font.BOLD, 18));
            levelBtn.setPreferredSize(new Dimension(150, 60));

            // Color coding by difficulty
            if (i <= 3) levelBtn.setBackground(new Color(100, 200, 100)); // Easy
            else if (i <= 6) levelBtn.setBackground(new Color(255, 200, 100)); // Medium
            else if (i <= 9) levelBtn.setBackground(new Color(255, 150, 100)); // Hard
            else levelBtn.setBackground(new Color(200, 100, 100)); // Pro

            levelBtn.addActionListener(e -> {
                // *** عدم إعادة تعيين LevelManager ***
                // *** بدلاً من ذلك، الذهاب مباشرة للمستوى المختار ***
                levelManager.goToLevel(levelNumber);
                startGame();

            });
            buttonsPanel.add(levelBtn);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(30, 30, 60));
        JLabel footerLabel = new JLabel("Levels: 1-3=Beginner, 4-6=Intermediate, 7-9=Advanced, 10=Pro",
                SwingConstants.CENTER);
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void startGame() {
        this.dispose(); // التخلص من النافذة الحالية

        SwingUtilities.invokeLater(() -> {
            GameScreen gameScreen = new GameScreen(levelManager);
            gameScreen.setVisible(true);
            gameScreen.requestFocusInWindow(); // التأكد من التركيز
        });
    }
}