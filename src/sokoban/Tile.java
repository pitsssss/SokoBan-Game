package sokoban;

public enum Tile {
    WALL('#'),
    FLOOR(' '),
    GOAL('.'),
    PLAYER('@'),
    BOX('$'),
    PLAYER_ON_GOAL('+'),
    BOX_ON_GOAL('*');

    private final char symbol;

    Tile(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}