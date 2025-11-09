package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TankHealthTest {

    @Test
    void testTankHealthInitialization() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        
        assertTrue(tank.isAlive());
        assertTrue(tank.getHealth() >= 80 && tank.getHealth() <= 100);
        assertEquals(tank.getHealth(), tank.getMaxHealth());
    }

    @Test
    void testTankTakeDamage() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 100);
        
        tank.takeDamage(30);
        assertEquals(70, tank.getHealth());
        assertTrue(tank.isAlive());
        
        tank.takeDamage(80);
        assertEquals(0, tank.getHealth());
        assertFalse(tank.isAlive());
    }

    @Test
    void testHealthPercentage() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 100);
        
        assertEquals(1.0f, tank.getHealthPercentage());
        
        tank.takeDamage(25);
        assertEquals(0.75f, tank.getHealthPercentage());
        
        tank.takeDamage(75);
        assertEquals(0.0f, tank.getHealthPercentage());
    }
}