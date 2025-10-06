package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.Tank;

public class TankView extends GameObjectView {

    public TankView(Tank tank, TextureRegion textureRegion) {
        super(tank, textureRegion);
    }

    @Override
    public void draw(Batch batch) {
        Tank tank = (Tank) this.gameObject;
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, tank.getDirection().getRotation());
    }
}