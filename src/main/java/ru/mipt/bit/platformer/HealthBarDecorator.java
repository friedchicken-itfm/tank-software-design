package ru.mipt.bit.platformer.view.decorators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.view.GameObjectView;

public class HealthBarDecorator extends GameObjectView {
    private final GameObjectView decoratedView;
    private final Tank tank;
    private final ShapeRenderer shapeRenderer;

    public HealthBarDecorator(GameObjectView decoratedView, Tank tank) {
        super(decoratedView.gameObject, decoratedView.textureRegion);
        this.decoratedView = decoratedView;
        this.tank = tank;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch) {
        // Сначала рисуем обычное представление
        decoratedView.draw(batch);

        // Затем рисуем полоску здоровья
        if (tank.isAlive()) {
            drawHealthBar(batch);
        }
    }

    private void drawHealthBar(Batch batch) {
        Rectangle rect = getRectangle();
        
        // Завершаем batch для использования ShapeRenderer
        batch.end();
        
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        
        // Фон полоски здоровья
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(rect.x - 2, rect.y + rect.height + 2, rect.width + 4, 6);
        
        // Основная полоска здоровья
        float healthPercentage = tank.getHealthPercentage();
        if (healthPercentage > 0.6f) {
            shapeRenderer.setColor(Color.GREEN);
        } else if (healthPercentage > 0.3f) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.RED);
        }
        
        float healthWidth = rect.width * healthPercentage;
        shapeRenderer.rect(rect.x, rect.y + rect.height + 3, healthWidth, 4);
        shapeRenderer.end();
        
        // Возобновляем batch
        batch.begin();
    }

    @Override
    public Rectangle getRectangle() {
        return decoratedView.getRectangle();
    }

    @Override
    public void dispose() {
        decoratedView.dispose();
        shapeRenderer.dispose();
    }
}