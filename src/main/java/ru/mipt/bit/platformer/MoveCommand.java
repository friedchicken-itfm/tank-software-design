package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.util.CollisionDetector;
import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

public class MoveCommand implements Command {
    private final Direction direction;
    private final List<Obstacle> obstacles;
    private final List<Movable> tanks;
    private final CollisionDetector collisionDetector;
    private final int levelWidth;
    private final int levelHeight;

    public MoveCommand(Direction direction, List<Obstacle> obstacles, List<Movable> tanks, 
                      CollisionDetector collisionDetector, int levelWidth, int levelHeight) {
        this.direction = direction;
        this.obstacles = obstacles;
        this.tanks = tanks;
        this.collisionDetector = collisionDetector;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    @Override
    public void execute(Movable movable) {
        if (canExecute(movable)) {
            ((Tank) movable).move(direction, obstacles, tanks);
        }
    }

    @Override
    public boolean canExecute(Movable movable) {
        GridPoint2 currentPos = movable.getCoordinates();
        GridPoint2 nextPos = new GridPoint2(currentPos.x + direction.getDx(), currentPos.y + direction.getDy());
        
        // Проверка границ уровня
        if (nextPos.x < 0 || nextPos.x >= levelWidth || nextPos.y < 0 || nextPos.y >= levelHeight) {
            return false;
        }
        
        // Проверка препятствий
        if (collisionDetector.wouldCollideWithObstacle(nextPos, obstacles)) {
            return false;
        }
        
        // Проверка других танков (текущая и следующая позиция)
        return !collisionDetector.wouldCollideWithTanks(movable, nextPos, tanks);
    }
}