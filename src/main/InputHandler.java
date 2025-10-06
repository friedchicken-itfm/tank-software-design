package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class InputHandler {
    private final Tank playerTank;
    private final List<Tree> obstacles;

    public InputHandler(Tank playerTank, List<Tree> obstacles) {
        this.playerTank = playerTank;
        this.obstacles = obstacles;
    }

    public void handleInput() {
        if (playerTank.isMoving()) {
            return; // Не обрабатываем новый ввод, пока танк движется
        }
        
        for (Direction direction : Direction.values()) {
            for (int key : direction.getKeys()) {
                if (Gdx.input.isKeyPressed(key)) {
                    playerTank.move(direction, obstacles);
                    return; // Выходим после первого же нажатия
                }
            }
        }
    }
}