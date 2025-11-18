package sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScreen extends JFrame {
    private Board board;
    private GamePanel gamePanel;
    private LevelManager levelManager;
    private JLabel statusLabel;
    private int moveCount = 0;

    public GameScreen(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.board = new Board(levelManager.getCurrentLevelData());

        setTitle("Sokoban - Level " + levelManager.getCurrentLevelNumber());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status bar - FIXED: proper initial text
        statusLabel = new JLabel(getStatusText(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setPreferredSize(new Dimension(0, 30));
        statusLabel.setBackground(new Color(50, 50, 80));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        add(statusLabel, BorderLayout.NORTH);

        // Game panel
        gamePanel = new GamePanel(board);
        add(gamePanel, BorderLayout.CENTER);

        // Controls panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(new Color(40, 40, 60));

        JButton resetBtn = new JButton("Reset Level (R)");
        resetBtn.addActionListener(e -> resetLevel());

        JButton menuBtn = new JButton("Level Select (M)");
        menuBtn.addActionListener(e -> showLevelSelect());

        controlsPanel.add(resetBtn);
        controlsPanel.add(menuBtn);
        add(controlsPanel, BorderLayout.SOUTH);

        // Keyboard controls
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        requestFocusInWindow();
    }

    private String getStatusText() {
        return String.format("Level: %d | Moves: %d | Boxes left: %d/%d",
                levelManager.getCurrentLevelNumber(),
                moveCount,
                board.getBoxesLeft(),
                board.getTotalBoxes()); // Show progress like "2/10"
    }

    private void handleKeyPress(KeyEvent e) {
        int dx = 0, dy = 0;

        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP: dx = -1; break;
            case KeyEvent.VK_DOWN: dx = 1; break;
            case KeyEvent.VK_LEFT: dy = -1; break;
            case KeyEvent.VK_RIGHT: dy = 1; break;
            case KeyEvent.VK_R: resetLevel(); return;
            case KeyEvent.VK_M: showLevelSelect(); return;
            default: return;
        }

        if (board.movePlayer(dx, dy)) {
            moveCount++;
            updateStatus();
            gamePanel.repaint();
            checkWinCondition(); // This triggers win detection
        }
    }

    private void updateStatus() {
        statusLabel.setText(getStatusText());
    }

    private void checkWinCondition() {
        if (board.isWin()) {
            String message = "Level " + levelManager.getCurrentLevelNumber() + " complete!";
            String title = "Congratulations!";

            if (levelManager.isLastLevel()) {
                message += "\n\nYou've completed ALL levels! You're a Sokoban Master!";
                title = "Ultimate Victory!";
            }

            int option = JOptionPane.showOptionDialog(
                    this,
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Next Level", "Level Select"},
                    "Next Level"
            );

            if (option == JOptionPane.YES_OPTION) {
                if (!levelManager.isLastLevel()) {
                    levelManager.nextLevel();
                    // *** إعادة إنشاء Board وGamePanel بشكل كامل ***
                    board = new Board(levelManager.getCurrentLevelData());
                    gamePanel.setBoard(board); // تم تحديث هذه الطريقة

                    // *** تحديث الأبعاد وإعادة الترتيب ***
                    pack(); // *** مهم جداً - يعيد حساب حجم النافذة ***

                    moveCount = 0;
                    setTitle("Sokoban - Level " + levelManager.getCurrentLevelNumber());
                    updateStatus();
                    gamePanel.repaint();

                    // *** التأكد من التركيز على النافذة ***
                    requestFocusInWindow();
                } else {
                    showLevelSelect();
                }
            } else {
                showLevelSelect();
            }
        }
    }
    private void resetLevel() {
        board = new Board(levelManager.getCurrentLevelData());
        gamePanel.setBoard(board);
        moveCount = 0;
        updateStatus();
        gamePanel.repaint();
        requestFocusInWindow();
    }

    private void showLevelSelect() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new LevelSelectionScreen(levelManager).setVisible(true);
        });
    }
}