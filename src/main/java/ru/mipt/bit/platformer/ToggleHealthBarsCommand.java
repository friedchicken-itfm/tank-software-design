package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.controller.HealthBarController;

public class ToggleHealthBarsCommand implements Command {
    private final HealthBarController healthBarController;

    public ToggleHealthBarsCommand(HealthBarController healthBarController) {
        this.healthBarController = healthBarController;
    }

    @Override
    public void execute() {
        healthBarController.toggleHealthBars();
    }

    @Override
    public void undo() {
        // Для toggle команды undo это то же самое, что execute
        execute();
    }
}