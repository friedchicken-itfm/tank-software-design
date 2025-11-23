package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet extends GameObject implements Movable {
    private int damage = 1;
    private GridPoint2 direction;
    // Владелец пули, чтобы танк не убил сам себя при выстреле
    private Tank owner; 

    public Bullet(GridPoint2 location, GridPoint2 direction, Tank owner) {
        // Предполагаем, что у GameObject есть конструктор (location)
        super(location); 
        this.direction = direction;
        this.owner = owner;
    }

    public void move() {
        // Логика движения пули (меняет координаты на +direction)
        // Например: coordinates.add(direction);
    }

    public int getDamage() {
        return damage;
    }
    
    public Tank getOwner() {
        return owner;
    }
}