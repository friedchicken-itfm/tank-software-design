package ru.mipt.bit.platformer.commands;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.CollisionDetector;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class MoveCommandTest {

    @Test
    void testMoveWithinBounds() {
        CollisionDetector detector = new CollisionDetector();
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        
        MoveCommand command = new MoveCommand(Direction.RIGHT, 
            Collections.emptyList(), Collections.emptyList(), 
            detector, 10, 10);
        
        assertTrue(command.canExecute(tank));
    }

    @Test
    void testMoveOutOfBounds() {
        CollisionDetector detector = new CollisionDetector();
        Tank tank = new Tank(new GridPoint2(0, 0), 0.4f);
        
        MoveCommand command = new MoveCommand(Direction.LEFT, 
            Collections.emptyList(), Collections.emptyList(), 
            detector, 10, 10);
        
        assertFalse(command.canExecute(tank));
    }

    @Test
    void testMoveIntoObstacle() {
        CollisionDetector detector = new CollisionDetector();
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Tree tree = new Tree(new GridPoint2(2, 1));
        
        MoveCommand command = new MoveCommand(Direction.RIGHT, 
            Arrays.asList(tree), Collections.emptyList(), 
            detector, 10, 10);
        
        assertFalse(command.canExecute(tank));
    }
}