package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject {
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 initialCoordinates, TextureRegion textureRegion) {
        super(initialCoordinates, textureRegion);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    public void move(Direction direction, List<Tree> obstacles) {
        // двигаемся только если завершили дижение
        if (!isMoving()) {
            this.rotation = direction.getRotation();

            // рассчет следующей клетки
            GridPoint2 nextTile = new GridPoint2(coordinates.x + direction.getDx(), coordinates.y + direction.getDy());

            // проверка столкновения
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
        // обновляем координаты после движения
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }
    
    // геттеры и сетты для главных полей 
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }
    
    public float getMovementProgress() {
        return movementProgress;
    }

    public void setMovementProgress(float progress) {
        this.movementProgress = progress;
    }

    public float getRotation() {
        return rotation;
    }
}