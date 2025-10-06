package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject {
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT; // Начальное направление

    public Tank(GridPoint2 initialCoordinates) {
        super(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    public void move(Direction direction, List<Tree> obstacles) {
        if (!isMoving()) {
            this.direction = direction;
            GridPoint2 nextTile = new GridPoint2(coordinates.x + direction.getDx(), coordinates.y + direction.getDy());

            boolean collision = false;
            for (Tree obstacle : obstacles) {
                if (obstacle.getCoordinates().equals(nextTile)) {
                    collision = true;
                    break;
                }
            }
            if (!collision) {
                this.destinationCoordinates.set(nextTile);
                this.movementProgress = 0f;
            }
        }
    }

    public void update() {
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }
    
    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }
    
    // Геттеры и сеттеры
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }
    
    public float getMovementProgress() {
        return movementProgress;
    }

    public void setMovementProgress(float progress) {
        this.movementProgress = progress;
    }

    public Direction getDirection() {
        return direction;
    }
}