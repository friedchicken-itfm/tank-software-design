package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    // Текстуры остаются здесь, так как их нужно освобождать в dispose()
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    // Уменьшаем количество сущнойтсей
    private Tank player;
    private Tree tree;
    private final List<Tree> obstacles = new ArrayList<>();


    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        blueTankTexture = new Texture("images/tank_blue.png");
        player = new Tank(new GridPoint2(1, 1), new TextureRegion(blueTankTexture));

        greenTreeTexture = new Texture("images/greenTree.png");
        tree = new Tree(new GridPoint2(1, 3), new TextureRegion(greenTreeTexture));
        obstacles.add(tree);

        
        moveRectangleAtTileCenter(groundLayer, tree.getRectangle(), tree.getCoordinates());
    }

    private void handleInput() {
        
        for (Direction direction : Direction.values()) {
            for (int key : direction.getKeys()) {
                if (Gdx.input.isKeyPressed(key)) {
                    player.move(direction, obstacles);
                    break; 
                }
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        handleInput();

        //обновляем состояние
        player.setMovementProgress(continueProgress(player.getMovementProgress(), deltaTime, MOVEMENT_SPEED));
        player.update();

        // обновляем графику 
        tileMovement.moveRectangleBetweenTileCenters(player.getRectangle(), player.getCoordinates(), player.getDestinationCoordinates(), player.getMovementProgress());

        levelRenderer.render();

        batch.begin();
        // говорим объектам самим себя нарисовать
        player.draw(batch, player.getRotation());
        tree.draw(batch, 0f); 
        batch.end();
    }

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
    }

    
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}