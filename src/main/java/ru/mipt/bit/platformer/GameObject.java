
package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public abstract class GameObject {
    protected final Rectangle rectangle;
    protected final TextureRegion textureRegion;
    protected final GridPoint2 coordinates;

    public GameObject(GridPoint2 initialCoordinates, TextureRegion textureRegion) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.textureRegion = textureRegion;
        this.rectangle = createBoundingRectangle(textureRegion);
    }

    public void draw(Batch batch, float rotation) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, rotation);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}