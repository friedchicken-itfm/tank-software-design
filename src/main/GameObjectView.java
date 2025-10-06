package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameObject;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public abstract class GameObjectView {
    protected final GameObject gameObject;
    protected final TextureRegion textureRegion;
    protected final Rectangle rectangle;

    public GameObjectView(GameObject gameObject, TextureRegion textureRegion) {
        this.gameObject = gameObject;
        this.textureRegion = textureRegion;
        this.rectangle = createBoundingRectangle(textureRegion);
    }

    public abstract void draw(Batch batch);

    public Rectangle getRectangle() {
        return rectangle;
    }
}