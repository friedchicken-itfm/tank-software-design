package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void testTankInitialization() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertFalse(tank.isMoving());
        assertEquals(1.0f, tank.getMovementProgress());
    }

    @Test
    void testTankMovement() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        List<Obstacle> obstacles = new ArrayList<>();
        List<Movable> otherTanks = new ArrayList<>();
        
        tank.move(Direction.RIGHT, obstacles, otherTanks);
        
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void testTankUpdate() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1.0f); // Быстрая скорость для теста
        List<Obstacle> obstacles = new ArrayList<>();
        List<Movable> otherTanks = new ArrayList<>();
        
        tank.move(Direction.RIGHT, obstacles, otherTanks);
        tank.update(0.5f);
        
        assertTrue(tank.getMovementProgress() > 0);
        tank.update(0.6f); // Завершаем движение
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertFalse(tank.isMoving());
    }
}