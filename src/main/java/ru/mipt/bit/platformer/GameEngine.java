package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;

// Это наш "Логический уровень" (Observable)
public class GameEngine {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<LevelListener> listeners = new ArrayList<>();
    
    // Методы подписки
    public void addListener(LevelListener listener) {
        listeners.add(listener);
    }

    public void addGameObject(GameObject object) {
        gameObjects.add(object);
        // Уведомляем всех слушателей (графику), что появился новый объект
        for (LevelListener listener : listeners) {
            listener.onObjectAdded(object);
        }
    }

    public void removeGameObject(GameObject object) {
        gameObjects.remove(object);
        // Уведомляем графику, что объект нужно удалить
        for (LevelListener listener : listeners) {
            listener.onObjectRemoved(object);
        }
    }

    // Основной цикл логики (Tick)
    public void updateState() {
        // 1. Команды и движение (уже выполнены к этому моменту в цикле)
        
        // 2. Проверка коллизий (столкновений)
        // Упрощенная реализация столкновений для примера:
        List<GameObject> toRemove = new ArrayList<>();
        
        for (GameObject obj : gameObjects) {
            if (obj instanceof Bullet) {
                // Логика пули: проверить, попала ли она в кого-то
                // Если попала -> нанести урон, добавить себя и жертву (если умерла) в toRemove
            }
        }
        
        // Удаляем "мертвые" объекты
        for (GameObject obj : toRemove) {
            removeGameObject(obj);
        }
    }
    
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}