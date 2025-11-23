package ru.mipt.bit.platformer;

public class ShootCommand implements Command {
    private final Tank tank;
    private final GameEngine gameEngine; // Ссылка на движок, чтобы зарегистрировать пулю

    public ShootCommand(Tank tank, GameEngine gameEngine) {
        this.tank = tank;
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        // 1. Танк создает объект пули
        Bullet bullet = tank.shoot();
        
        // 2. Регистрируем пулю в логическом уровне
        // (Здесь можно добавить проверку, не уперся ли танк в стену, чтобы не создавать пулю в стене)
        gameEngine.addGameObject(bullet);
        
        System.out.println("Tank fired logic!");
    }
}