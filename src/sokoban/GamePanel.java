package sokoban;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Board board;
    private static final int TILE_SIZE = 50;

    public GamePanel(Board board) {
        this.board = board;
        updatePreferredSize(); // تحديث الأبعاد فور الإنشاء
    }

    public void setBoard(Board board) {
        this.board = board;
        updatePreferredSize(); // تحديث الأبعاد عند تغيير اللوحة
        revalidate(); // *** مهم جداً - يعيد ترتيب المكونات ***
        repaint(); // *** مهم جداً - يعيد رسم المكون ***
    }

    // *** إضافة هذه الطريقة الأساسية ***
    private void updatePreferredSize() {
        if (board != null && board.getGrid() != null) {
            int rows = board.getGrid().length;
            int cols = rows > 0 ? board.getGrid()[0].length : 0;

            setPreferredSize(new Dimension(
                    cols * TILE_SIZE,
                    rows * TILE_SIZE
            ));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board == null || board.getGrid() == null) {
            return;
        }

        Tile[][] grid = board.getGrid();

        // رسم الخلفية أولاً
        g.setColor(new Color(230, 230, 230));
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                drawTile(g, grid[i][j], j * TILE_SIZE, i * TILE_SIZE);
            }
        }
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);

        switch(tile) {
            case WALL:
                g.setColor(new Color(80, 50, 30));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(120, 80, 50));
                g.fillRect(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10);
                break;

            case FLOOR:
                g.setColor(new Color(220, 210, 180));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

            case GOAL:
                g.setColor(new Color(220, 210, 180));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(255, 220, 0));
                g.fillOval(x + 15, y + 15, TILE_SIZE - 30, TILE_SIZE - 30);
                g.setColor(new Color(200, 180, 0));
                g.drawOval(x + 15, y + 15, TILE_SIZE - 30, TILE_SIZE - 30);
                break;

            case PLAYER:
            case PLAYER_ON_GOAL:
                g.setColor(new Color(70, 130, 180));
                g.fillOval(x + 8, y + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                g.setColor(new Color(40, 80, 120));
                g.fillOval(x + 15, y + 10, TILE_SIZE - 30, TILE_SIZE - 30);
                break;

            case BOX:
            case BOX_ON_GOAL:
                g.setColor(new Color(180, 60, 60));
                g.fillRect(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10);
                g.setColor(new Color(220, 100, 100));
                g.fillRect(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
                break;
        }
    }
}