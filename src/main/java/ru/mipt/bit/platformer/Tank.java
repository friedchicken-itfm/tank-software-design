package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank extends GameObject implements Movable {
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private final float movementSpeed;

    public Tank(GridPoint2 initialCoordinates, float movementSpeed) {
        super(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void move(Direction direction, List<Obstacle> obstacles) {
        if (!isMoving()) {
            this.direction = direction;
            GridPoint2 nextTile = new GridPoint2(coordinates.x + direction.getDx(), coordinates.y + direction.getDy());

            boolean collision = obstacles.stream()
                    .anyMatch(obstacle -> obstacle.getCoordinates().equals(nextTile));
                    
            if (!collision) {
                this.destinationCoordinates.set(nextTile);
                this.movementProgress = 0f;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if (isMoving()) {
            movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
            if (isEqual(movementProgress, 1f)) {
                coordinates.set(destinationCoordinates);
            }
        }
    }

    @Override
    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    // Геттеры
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }
    
    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }
    
    public float getMovementSpeed() {
        return movementSpeed;
    }
}