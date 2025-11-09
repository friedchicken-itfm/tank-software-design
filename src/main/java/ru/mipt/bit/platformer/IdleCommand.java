package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.model.Movable;

public class IdleCommand implements Command {
    @Override
    public void execute(Movable movable) {
        // Ничего не делаем - танк стоит на месте
    }

    @Override
    public boolean canExecute(Movable movable) {
        return true;
    }
}