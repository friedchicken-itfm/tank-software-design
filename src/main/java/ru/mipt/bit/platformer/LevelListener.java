package ru.mipt.bit.platformer;

public interface LevelListener {
    // Вызывается, когда в логику добавляется новый объект (танк, пуля, дерево)
    void onObjectAdded(GameObject object);
    
    // Вызывается, когда объект исчезает (уничтожен, вылетел за карту)
    void onObjectRemoved(GameObject object);
}