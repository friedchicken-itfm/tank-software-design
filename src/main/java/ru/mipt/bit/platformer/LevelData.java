package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import java.util.List;

public class LevelData {
    private final GridPoint2 playerStart;
    private final List<Obstacle> obstacles;
    private final List<Tank> aiTanks;
    private final int width;
    private final int height;

    public LevelData(GridPoint2 playerStart, List<Obstacle> obstacles, List<Tank> aiTanks, int width, int height) {
        this.playerStart = playerStart;
        this.obstacles = obstacles;
        this.aiTanks = aiTanks;
        this.width = width;
        this.height = height;
    }

    public GridPoint2 getPlayerStart() {
        return playerStart;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Tank> getAiTanks() {
        return aiTanks;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}