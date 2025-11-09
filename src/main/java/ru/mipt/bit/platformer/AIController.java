package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.commands.Command;
import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.commands.IdleCommand;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.util.CollisionDetector;
import java.util.List;
import java.util.Random;

public class AIController {
    private final List<Obstacle> obstacles;
    private final List<Movable> tanks;
    private final CollisionDetector collisionDetector;
    private final int levelWidth;
    private final int levelHeight;
    private final Random random;

    public AIController(List<Obstacle> obstacles, List<Movable> tanks, 
                       CollisionDetector collisionDetector, int levelWidth, int levelHeight) {
        this.obstacles = obstacles;
        this.tanks = tanks;
        this.collisionDetector = collisionDetector;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.random = new Random();
    }

    public Command getNextCommand(Movable tank) {
        if (tank.isMoving()) {
            return new IdleCommand();
        }

        // Случайно выбираем направление
        Direction[] directions = Direction.values();
        Direction randomDirection = directions[random.nextInt(directions.length)];
        
        MoveCommand moveCommand = new MoveCommand(randomDirection, obstacles, tanks, 
                                                collisionDetector, levelWidth, levelHeight);
        
        if (moveCommand.canExecute(tank)) {
            return moveCommand;
        }
        
        return new IdleCommand();
    }
}