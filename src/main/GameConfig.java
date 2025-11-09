package ru.mipt.bit.platformer.config;

import ru.mipt.bit.platformer.level.LevelGeneratorFactory;

public class GameConfig {
    public static final float MOVEMENT_SPEED = 0.4f;
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 1024;
    
    // Настройки уровня
    public static final LevelGeneratorFactory.GeneratorType LEVEL_GENERATOR_TYPE = 
        LevelGeneratorFactory.GeneratorType.FILE; // или RANDOM
    
    // Параметры для случайного генератора
    public static final int RANDOM_LEVEL_WIDTH = 10;
    public static final int RANDOM_LEVEL_HEIGHT = 8;
    public static final float RANDOM_OBSTACLE_DENSITY = 0.2f;
    
    // Параметры для файлового генератора
    public static final String LEVEL_FILE_NAME = "level1.txt";
    
    private GameConfig() {
        // Utility class
    }
}