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
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.TankView;
import ru.mipt.bit.platformer.view.TreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    // Графические компоненты
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    // Модели
    private Tank player;
    private List<Tree> obstacles = new ArrayList<>();

    // Представления
    private TankView playerView;
    private List<TreeView> obstacleViews;
    
    // Контроллер
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Загрузка уровня и настройка рендеринга
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Загрузка текстур
        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");

        // Создание моделей
        player = new Tank(new GridPoint2(1, 1));
        Tree tree = new Tree(new GridPoint2(1, 3));
        obstacles.add(tree);

        // Создание представлений
        playerView = new TankView(player, new TextureRegion(blueTankTexture));
        obstacleViews = obstacles.stream()
                .map(t -> new TreeView(t, new TextureRegion(greenTreeTexture)))
                .collect(Collectors.toList());

        // Создание обработчика ввода
        inputHandler = new InputHandler(player, obstacles);

        // Начальное позиционирование
        obstacleViews.forEach(view -> moveRectangleAtTileCenter(groundLayer, view.getRectangle(), view.gameObject.getCoordinates()));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обработка ввода
        inputHandler.handleInput();

        // Обновление состояния модели
        player.setMovementProgress(continueProgress(player.getMovementProgress(), deltaTime, MOVEMENT_SPEED));
        player.update();

        // Обновление графического представления
        tileMovement.moveRectangleBetweenTileCenters(playerView.getRectangle(), player.getCoordinates(), player.getDestinationCoordinates(), player.getMovementProgress());

        levelRenderer.render();

        batch.begin();
        playerView.draw(batch);
        obstacleViews.forEach(view -> view.draw(batch));
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