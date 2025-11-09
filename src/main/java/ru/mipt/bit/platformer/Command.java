package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.model.Movable;

public interface Command {
    void execute(Movable movable);
    boolean canExecute(Movable movable);
}