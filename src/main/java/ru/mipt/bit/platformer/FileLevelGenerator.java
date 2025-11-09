package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

public class FileLevelGenerator implements LevelGenerator {
    private final String levelFileName;

    public FileLevelGenerator(String levelFileName) {
        this.levelFileName = levelFileName;
    }

    @Override
    public LevelData generateLevel() {
        List<Obstacle> obstacles = new ArrayList<>();
        GridPoint2 playerStart = null;

        FileHandle fileHandle = Gdx.files.internal("levels/" + levelFileName);
        if (!fileHandle.exists()) {
            throw new RuntimeException("Level file not found: " + levelFileName);
        }

        String[] lines = fileHandle.readString().split("\\r?\\n");
        int height = lines.length;
        int width = 0;

        // Обрабатываем строки сверху вниз (в файле первая строка - верх уровня)
        for (int y = height - 1; y >= 0; y--) {
            String line = lines[height - 1 - y].trim();
            width = Math.max(width, line.length());

            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                GridPoint2 position = new GridPoint2(x, y);

                switch (cell) {
                    case 'T':
                        obstacles.add(new Tree(position));
                        break;
                    case 'X':
                        if (playerStart != null) {
                            throw new RuntimeException("Multiple player start positions found");
                        }
                        playerStart = position;
                        break;
                    case '_':
                        // Пустая клетка - ничего не делаем
                        break;
                    default:
                        throw new RuntimeException("Unknown cell character: " + cell);
                }
            }
        }

        if (playerStart == null) {
            throw new RuntimeException("No player start position (X) found in level file");
        }

        return new LevelData(playerStart, obstacles, width, height);
    }
}