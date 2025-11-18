package sokoban;

public class LevelManager {
    private int currentLevel = 0;

    public String[] getCurrentLevelData() {
        return LevelData.LEVELS[currentLevel];
    }

    public void nextLevel() {

        if (currentLevel < LevelData.LEVELS.length - 1) {
            currentLevel++;
        }
    }

    public void goToLevel(int levelNumber) {
        if (levelNumber >= 1 && levelNumber <= LevelData.LEVELS.length) {

            currentLevel = levelNumber - 1;
        }
    }

    public int getCurrentLevelNumber() {

        return currentLevel + 1;
    }

    public boolean isLastLevel() {

        return currentLevel == LevelData.LEVELS.length - 1;
    }

    public void reset() {

        currentLevel = 0;
    }
}