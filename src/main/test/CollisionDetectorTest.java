package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class CollisionDetectorTest {

    @Test
    void testObstacleCollision() {
        CollisionDetector detector = new CollisionDetector();
        Tree tree = new Tree(new GridPoint2(2, 2));
        
        assertTrue(detector.wouldCollideWithObstacle(new GridPoint2(2, 2), 
                   Arrays.asList(tree)));
        assertFalse(detector.wouldCollideWithObstacle(new GridPoint2(3, 3), 
                    Arrays.asList(tree)));
    }

    @Test
    void testTankCollision() {
        CollisionDetector detector = new CollisionDetector();
        Tank tank1 = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank tank2 = new Tank(new GridPoint2(2, 2), 0.4f);
        
        assertTrue(detector.wouldCollideWithTanks(tank1, new GridPoint2(2, 2), 
                   Arrays.asList(tank1, tank2)));
        assertFalse(detector.wouldCollideWithTanks(tank1, new GridPoint2(3, 3), 
                    Arrays.asList(tank1, tank2)));
    }
}