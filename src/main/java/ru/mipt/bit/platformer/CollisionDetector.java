package ru.mipt.bit.platformer.util;

import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;
import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

public class CollisionDetector {
    
    public boolean wouldCollideWithObstacle(GridPoint2 position, List<Obstacle> obstacles) {
        return obstacles.stream()
                .anyMatch(obstacle -> obstacle.getCoordinates().equals(position));
    }
    
    public boolean wouldCollideWithTanks(Movable currentTank, GridPoint2 nextPosition, List<Movable> tanks) {
        for (Movable tank : tanks) {
            if (tank == currentTank) continue;
            
            // Проверяем текущую позицию танка
            if (tank.getCoordinates().equals(nextPosition)) {
                return true;
            }
            
            // Проверяем целевую позицию движущегося танка
            if (tank.isMoving() && tank.getDestinationCoordinates().equals(nextPosition)) {
                return true;
            }
            
            // Проверяем, не занимает ли другой танк текущую позицию (для случая, когда он движется в нашу текущую клетку)
            if (tank.isMoving() && tank.getDestinationCoordinates().equals(currentTank.getCoordinates())) {
                return true;
            }
        }
        return false;
    }
}