package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.config.GameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {
    private final int width;
    private final int height;
    private final float obstacleDensity;
    private final int aiTankCount;
    private final Random random;

    public RandomLevelGenerator(int width, int height, float obstacleDensity, int aiTankCount) {
        this.width = width;
        this.height = height;
        this.obstacleDensity = obstacleDensity;
        this.aiTankCount = aiTankCount;
        this.random = new Random();
    }

    @Override
    public LevelData generateLevel() {
        List<Obstacle> obstacles = new ArrayList<>();
        List<Tank> aiTanks = new ArrayList<>();
        GridPoint2 playerStart = null;

        // Генерируем препятствия
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextFloat() < obstacleDensity) {
                    obstacles.add(new Tree(new GridPoint2(x, y)));
                }
            }
        }

        // Генерируем случайную позицию игрока
        playerStart = findFreePosition(obstacles, aiTanks);

        // Генерируем AI танки
        for (int i = 0; i < aiTankCount; i++) {
            GridPoint2 aiTankPos = findFreePosition(obstacles, aiTanks);
            if (aiTankPos != null) {
                aiTanks.add(new Tank(aiTankPos, GameConfig.MOVEMENT_SPEED));
            }
        }

        return new LevelData(playerStart, obstacles, aiTanks, width, height);
    }

    private GridPoint2 findFreePosition(List<Obstacle> obstacles, List<Tank> tanks) {
        int attempts = 0;
        while (attempts < 100) { // Защита от бесконечного цикла
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            GridPoint2 candidate = new GridPoint2(x, y);
            
            boolean collision = obstacles.stream()
                    .anyMatch(obstacle -> obstacle.getCoordinates().equals(candidate));
            
            if (!collision) {
                boolean tankCollision = tanks.stream()
                        .anyMatch(tank -> tank.getCoordinates().equals(candidate));
                if (!tankCollision) {
                    return candidate;
                }
            }
            attempts++;
        }
        throw new RuntimeException("Could not find free position for tank");
    }
}