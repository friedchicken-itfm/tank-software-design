package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.Tree;

public class TreeView extends GameObjectView {
    public TreeView(Tree tree, TextureRegion textureRegion) {
        super(tree, textureRegion);
    }

    @Override
    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, 0f);
    }
}