package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.view.TankView;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static org.junit.jupiter.api.Assertions.*;

class HealthBarControllerTest {

    @Test
    void testHealthBarToggle() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        TankView tankView = new TankView(tank, new TextureRegion());
        
        HealthBarController controller = new HealthBarController(java.util.Arrays.asList(tank));
        controller.addView(tankView, tank);
        
        assertFalse(controller.areHealthBarsVisible());
        
        controller.toggleHealthBars();
        assertTrue(controller.areHealthBarsVisible());
        
        controller.toggleHealthBars();
        assertFalse(controller.areHealthBarsVisible());
    }
}