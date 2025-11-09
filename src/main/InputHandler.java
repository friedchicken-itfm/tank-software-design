package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.commands.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.controller.HealthBarController;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.util.CollisionDetector;

import java.util.List;

public class InputHandler {
    private final Movable playerTank;
    private final List<Obstacle> obstacles;
    private final List<Movable> allTanks;
    private final CollisionDetector collisionDetector;
    private final int levelWidth;
    private final int levelHeight;
    private final HealthBarController healthBarController;

    public InputHandler(Movable playerTank, List<Obstacle> obstacles, List<Movable> allTanks,
                       CollisionDetector collisionDetector, int levelWidth, int levelHeight,
                       HealthBarController healthBarController) {
        this.playerTank = playerTank;
        this.obstacles = obstacles;
        this.allTanks = allTanks;
        this.collisionDetector = collisionDetector;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.healthBarController = healthBarController;
    }

    public void handleInput() {
        // Обработка переключения полосок здоровья
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            new ToggleHealthBarsCommand(healthBarController).execute();
        }

        // Обработка движения игрока (если танк игрока жив)
        if (playerTank.isMoving() || !((Tank) playerTank).isAlive()) {
            return;
        }
        
        for (Direction direction : Direction.values()) {
            for (int key : direction.getKeys()) {
                if (Gdx.input.isKeyPressed(key)) {
                    ((Tank) playerTank).move(direction, obstacles, allTanks, collisionDetector, levelWidth, levelHeight);
                    return;
                }
            }
        }
    }
}