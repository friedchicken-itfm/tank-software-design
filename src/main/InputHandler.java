package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.List;

public class InputHandler {
    private final Movable movable;
    private final List<Obstacle> obstacles;

    public InputHandler(Movable movable, List<Obstacle> obstacles) {
        this.movable = movable;
        this.obstacles = obstacles;
    }

    public void handleInput() {
        if (movable.isMoving()) {
            return;
        }
        
        for (Direction direction : Direction.values()) {
            for (int key : direction.getKeys()) {
                if (Gdx.input.isKeyPressed(key)) {
                    movable.move(direction, obstacles);
                    return;
                }
            }
        }
    }
}