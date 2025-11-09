package ru.mipt.bit.platformer.level;

public class LevelGeneratorFactory {
    public enum GeneratorType {
        RANDOM,
        FILE
    }

    public static LevelGenerator createGenerator(GeneratorType type, String... params) {
        switch (type) {
            case RANDOM:
                int width = Integer.parseInt(params[0]);
                int height = Integer.parseInt(params[1]);
                float density = Float.parseFloat(params[2]);
                return new RandomLevelGenerator(width, height, density);
            case FILE:
                return new FileLevelGenerator(params[0]);
            default:
                throw new IllegalArgumentException("Unknown generator type: " + type);
        }
    }
}