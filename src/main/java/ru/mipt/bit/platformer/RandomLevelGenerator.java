package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {
    private final int width;
    private final int height;
    private final float obstacleDensity;
    private final Random random;

    public RandomLevelGenerator(int width, int height, float obstacleDensity) {
        this.width = width;
        this.height = height;
        this.obstacleDensity = obstacleDensity;
        this.random = new Random();
    }

    @Override
    public LevelData generateLevel() {
        List<Obstacle> obstacles = new ArrayList<>();
        GridPoint2 playerStart = null;

        // Генерируем препятствия
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextFloat() < obstacleDensity) {
                    obstacles.add(new Tree(new GridPoint2(x, y)));
                }
            }
        }

        // Генерируем случайную позицию игрока, которая не занята препятствием
        while (playerStart == null) {
            int playerX = random.nextInt(width);
            int playerY = random.nextInt(height);
            GridPoint2 candidate = new GridPoint2(playerX, playerY);
            
            boolean collision = obstacles.stream()
                    .anyMatch(obstacle -> obstacle.getCoordinates().equals(candidate));
            
            if (!collision) {
                playerStart = candidate;
            }
        }

        return new LevelData(playerStart, obstacles, width, height);
    }
}