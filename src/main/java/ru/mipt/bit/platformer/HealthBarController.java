package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.view.decorators.HealthBarDecorator;
import ru.mipt.bit.platformer.view.GameObjectView;
import ru.mipt.bit.platformer.model.Tank;
import java.util.ArrayList;
import java.util.List;

public class HealthBarController {
    private boolean healthBarsVisible = false;
    private final List<HealthBarDecorator> healthBarDecorators = new ArrayList<>();
    private final List<GameObjectView> originalViews = new ArrayList<>();
    private final List<Tank> tanks;

    public HealthBarController(List<Tank> tanks) {
        this.tanks = tanks;
    }

    public void addView(GameObjectView view, Tank tank) {
        originalViews.add(view);
        healthBarDecorators.add(new HealthBarDecorator(view, tank));
    }

    public void toggleHealthBars() {
        healthBarsVisible = !healthBarsVisible;
    }

    public boolean areHealthBarsVisible() {
        return healthBarsVisible;
    }

    public GameObjectView getView(int index) {
        if (healthBarsVisible && index < healthBarDecorators.size()) {
            return healthBarDecorators.get(index);
        } else if (index < originalViews.size()) {
            return originalViews.get(index);
        }
        return null;
    }

    public void dispose() {
        for (HealthBarDecorator decorator : healthBarDecorators) {
            decorator.dispose();
        }
    }
}