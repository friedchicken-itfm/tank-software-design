
package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public class Tree extends GameObject {
    public Tree(GridPoint2 initialCoordinates, TextureRegion textureRegion) {
        super(initialCoordinates, textureRegion);
    }
}