package sokoban;

public class Board {
    private Tile[][] grid;
    private int playerX, playerY;
    private int boxesNotOnGoal;
    private int totalBoxes;
    private int totalGoals;

    public Board(String[] levelData) {
        loadLevel(levelData);
    }

    private void loadLevel(String[] levelData) {

        int rows = levelData.length;
        int cols = levelData[0].length();
        grid = new Tile[rows][cols];

        boxesNotOnGoal = 0;
        totalBoxes = 0;
        totalGoals = 0;
        playerX = -1;
        playerY = -1;


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Tile.FLOOR;
            }
        }


        for (int i = 0; i < rows; i++) {
            String row = levelData[i];
            for (int j = 0; j < Math.min(row.length(), cols); j++) {
                char c = row.charAt(j);
                switch(c) {
                    case '#':
                        grid[i][j] = Tile.WALL;
                        break;

                    case '@':
                        grid[i][j] = Tile.PLAYER;
                        playerX = i;
                        playerY = j;
                        break;

                    case '$':
                        grid[i][j] = Tile.BOX;
                        totalBoxes++;
                        boxesNotOnGoal++;
                        break;

                    case '.':
                        grid[i][j] = Tile.GOAL;
                        totalGoals++;
                        break;

                    case '+':
                        grid[i][j] = Tile.PLAYER_ON_GOAL;
                        playerX = i;
                        playerY = j;
                        totalGoals++;
                        break;

                    case '*':
                        grid[i][j] = Tile.BOX_ON_GOAL;
                        totalBoxes++;
                        totalGoals++;

                        break;
                }
            }
        }


        if (playerX == -1 || playerY == -1) {
            throw new RuntimeException("Level missing player position!");
        }
    }

    public boolean movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (!isValidPosition(newX, newY)) {
            return false;
        }

        // Handle box pushing
        if (isBoxAt(newX, newY)) {
            return pushBox(newX, newY, dx, dy);
        }

        movePlayerTo(newX, newY);
        return true;
    }

    private boolean pushBox(int boxX, int boxY, int dx, int dy) {
        int newBoxX = boxX + dx;
        int newBoxY = boxY + dy;


        if (!isValidPosition(newBoxX, newBoxY) || isBoxAt(newBoxX, newBoxY)) {
            return false;
        }

        Tile currentBoxTile = grid[boxX][boxY];
        Tile targetTile = grid[newBoxX][newBoxY];

        if (currentBoxTile == Tile.BOX_ON_GOAL) {
            boxesNotOnGoal++;
        }

        if (targetTile == Tile.GOAL || targetTile == Tile.PLAYER_ON_GOAL) {
            // Moving onto goal
            grid[newBoxX][newBoxY] = Tile.BOX_ON_GOAL;
            boxesNotOnGoal--;
        } else {
            grid[newBoxX][newBoxY] = Tile.BOX;
        }

        // Clear old box position
        if (currentBoxTile == Tile.BOX_ON_GOAL) {
            grid[boxX][boxY] = Tile.GOAL;
        } else {
            grid[boxX][boxY] = Tile.FLOOR;
        }

        // Move player to box's old position
        movePlayerTo(boxX, boxY);
        return true;
    }

    private void movePlayerTo(int x, int y) {

        if (grid[playerX][playerY] == Tile.PLAYER) {
            grid[playerX][playerY] = Tile.FLOOR;
        } else if (grid[playerX][playerY] == Tile.PLAYER_ON_GOAL) {
            grid[playerX][playerY] = Tile.GOAL;
        }


        if (grid[x][y] == Tile.GOAL) {
            grid[x][y] = Tile.PLAYER_ON_GOAL;
        } else {
            grid[x][y] = Tile.PLAYER;
        }

        playerX = x;
        playerY = y;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < grid.length &&
                y >= 0 && y < grid[0].length &&
                grid[x][y] != Tile.WALL;
    }

    private boolean isBoxAt(int x, int y) {
        return grid[x][y] == Tile.BOX || grid[x][y] == Tile.BOX_ON_GOAL;
    }


    public boolean isWin() {
        return boxesNotOnGoal == 0;
    }

    public Tile[][] getGrid() {
        return grid;
    }


    public int getBoxesLeft() {
        return boxesNotOnGoal;
    }

    public int getTotalBoxes() {
        return totalBoxes;
    }
}