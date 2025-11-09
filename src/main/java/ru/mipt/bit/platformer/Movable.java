package ru.mipt.bit.platformer.model;

import java.util.List;

public interface Movable {
    void move(Direction direction, List<Obstacle> obstacles);
    boolean isMoving();
    void update(float deltaTime);
    float getMovementProgress();
    GridPoint2 getCoordinates();
    GridPoint2 getDestinationCoordinates();
}