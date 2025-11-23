package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // 1. Бин Движка (GameEngine)
    // Spring создаст его один раз (Singleton) и будет выдавать всем, кто попросит.
    @Bean
    public GameEngine gameEngine() {
        return new GameEngine();
    }

    // 2. Бин Игрока (Tank)
    // Мы можем сразу настроить его начальные параметры здесь.
    @Bean
    public Tank playerTank() {
        return new Tank(new GridPoint2(1, 1), 3); // Координаты (1,1), Здоровье 3
    }
    
    // 3. Бин Генератора уровня (если он у тебя есть отдельным классом)
    // Пример внедрения зависимости: мы передаем gameEngine внутрь генератора
    /*
    @Bean
    public LevelGenerator levelGenerator(GameEngine engine) {
        return new LevelGenerator(engine); 
    }
    */
}