package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject implements Movable {
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private final float movementSpeed;
    private int health;
    private final int maxHealth;

    public Tank(GridPoint2 initialCoordinates, float movementSpeed) {
        super(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
        this.maxHealth = (int) (Math.random() * 21) + 80; // Случайное здоровье от 80 до 100
        this.health = maxHealth;
    }

    public Tank(GridPoint2 initialCoordinates, float movementSpeed, int health) {
        super(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
        this.maxHealth = health;
        this.health = health;
    }

    // Остальные методы остаются без изменений...

    // Новые методы для управления здоровьем
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public float getHealthPercentage() {
        return (float) health / maxHealth;
    }
    public void move(Direction direction, List<Obstacle> obstacles, List<Movable> tanks, 
                CollisionDetector collisionDetector, int levelWidth, int levelHeight) {
    if (!isMoving() && isAlive()) {
        this.direction = direction;
        GridPoint2 nextTile = new GridPoint2(coordinates.x + direction.getDx(), 
                                           coordinates.y + direction.getDy());
        
        // Проверка границ уровня
        if (nextTile.x < 0 || nextTile.x >= levelWidth || 
            nextTile.y < 0 || nextTile.y >= levelHeight) {
            return;
        }
        
        // Проверка коллизий
        if (!collisionDetector.wouldCollide(nextTile, obstacles, this, tanks)) {
            this.destinationCoordinates.set(nextTile);
            this.movementProgress = 0f;
        }
    }
}
}