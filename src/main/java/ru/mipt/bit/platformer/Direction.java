package ru.mipt.bit.platformer;

import static com.badlogic.gdx.Input.Keys.*;

public enum Direction {
    UP(0, 1, 90f, W, UP),
    DOWN(0, -1, -90f, S, DOWN),
    LEFT(-1, 0, 180f, A, LEFT),
    RIGHT(1, 0, 0f, D, RIGHT);

    private final int dx;
    private final int dy;
    private final float rotation;
    private final int[] keys;

    Direction(int dx, int dy, float rotation, int... keys) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
        this.keys = keys;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getRotation() {
        return rotation;
    }

    public int[] getKeys() {
        return keys;
    }
}