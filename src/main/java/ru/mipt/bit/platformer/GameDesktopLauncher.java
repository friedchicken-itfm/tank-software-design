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
import ru.mipt.bit.platformer.commands.Command;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.controller.AIController;
import ru.mipt.bit.platformer.controller.HealthBarController;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.LevelGeneratorFactory;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.CollisionDetector;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.TankView;
import ru.mipt.bit.platformer.view.TreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    // Графические компоненты
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private Texture blueTankTexture;
    private Texture greenTreeTexture;
    private Texture redTankTexture;

    // Модели (работаем через абстракции)
    private Movable player;
    private List<ru.mipt.bit.platformer.model.Obstacle> obstacles = new ArrayList<>();
    private List<Movable> allTanks = new ArrayList<>();
    private List<Tank> aiTanks = new ArrayList<>();

    // Представления
    private TankView playerView;
    private List<TankView> aiTankViews = new ArrayList<>();
    private List<TreeView> obstacleViews;
    
    // Контроллеры
    private InputHandler inputHandler;
    private List<AIController> aiControllers = new ArrayList<>();
    private HealthBarController healthBarController;

    // Утилиты
    private CollisionDetector collisionDetector;
    private int levelWidth;
    private int levelHeight;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Создаем генератор уровня в зависимости от конфигурации
        LevelGenerator levelGenerator = createLevelGenerator();
        LevelData levelData = levelGenerator.generateLevel();
        
        levelWidth = levelData.getWidth();
        levelHeight = levelData.getHeight();

        // Загрузка уровня и настройка рендеринга
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Загрузка текстур
        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");
        redTankTexture = new Texture("images/tank_red.png");

        // Создание детектора коллизий
        collisionDetector = new CollisionDetector();

        // Создание списка всех танков
        List<Tank> allTankObjects = new ArrayList<>();

        // Создание игрока
        Tank playerTank = new Tank(levelData.getPlayerStart(), GameConfig.MOVEMENT_SPEED);
        allTanks.add(playerTank);
        allTankObjects.add(playerTank);
        player = playerTank;

        // Создание AI танков
        aiTanks = levelData.getAiTanks();
        for (Tank aiTank : aiTanks) {
            allTanks.add(aiTank);
            allTankObjects.add(aiTank);
        }

        obstacles = levelData.getObstacles();

        // Создание контроллера здоровья
        healthBarController = new HealthBarController(allTankObjects);

        // Создание представлений
        playerView = new TankView(playerTank, new TextureRegion(blueTankTexture));
        healthBarController.addView(playerView, playerTank);
        
        // Создание представлений для AI танков
        for (Tank aiTank : aiTanks) {
            TankView aiTankView = new TankView(aiTank, new TextureRegion(redTankTexture));
            aiTankViews.add(aiTankView);
            healthBarController.addView(aiTankView, aiTank);
        }

        // Создание представлений для препятствий
        obstacleViews = obstacles.stream()
                .filter(obstacle -> obstacle instanceof Tree)
                .map(obstacle -> new TreeView((Tree) obstacle, new TextureRegion(greenTreeTexture)))
                .collect(Collectors.toList());

        // Создание контроллеров для AI танков
        for (Tank aiTank : aiTanks) {
            aiControllers.add(new AIController(obstacles, allTanks, collisionDetector, levelWidth, levelHeight));
        }

        // Создание обработчика ввода для игрока
        inputHandler = new InputHandler(playerTank, obstacles, allTanks, collisionDetector, 
                                      levelWidth, levelHeight, healthBarController);

        // Начальное позиционирование
        moveRectangleAtTileCenter(groundLayer, playerView.getRectangle(), playerTank.getCoordinates());
        for (TankView aiTankView : aiTankViews) {
            Tank aiTank = (Tank) aiTankView.gameObject;
            moveRectangleAtTileCenter(groundLayer, aiTankView.getRectangle(), aiTank.getCoordinates());
        }
        obstacleViews.forEach(view -> moveRectangleAtTileCenter(groundLayer, view.getRectangle(), view.gameObject.getCoordinates()));
    }

    private LevelGenerator createLevelGenerator() {
        if (GameConfig.LEVEL_GENERATOR_TYPE == LevelGeneratorFactory.GeneratorType.RANDOM) {
            return LevelGeneratorFactory.createGenerator(
                LevelGeneratorFactory.GeneratorType.RANDOM,
                String.valueOf(GameConfig.RANDOM_LEVEL_WIDTH),
                String.valueOf(GameConfig.RANDOM_LEVEL_HEIGHT),
                String.valueOf(GameConfig.RANDOM_OBSTACLE_DENSITY),
                String.valueOf(GameConfig.AI_TANK_COUNT)
            );
        } else {
            return LevelGeneratorFactory.createGenerator(
                LevelGeneratorFactory.GeneratorType.FILE,
                GameConfig.LEVEL_FILE_NAME
            );
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обработка ввода игрока
        inputHandler.handleInput();

        // Обновление AI танков (только живых)
        for (int i = 0; i < aiControllers.size(); i++) {
            Tank aiTank = aiTanks.get(i);
            if (aiTank.isAlive() && !aiTank.isMoving()) {
                AIController controller = aiControllers.get(i);
                Command command = controller.getNextCommand(aiTank);
                command.execute(aiTank);
            }
        }

        // Обновление состояния всех танков
        for (Movable tank : allTanks) {
            tank.update(deltaTime);
        }

        // Обновление графического представления игрока
        Tank playerTank = (Tank) player;
        if (playerTank.isAlive()) {
            tileMovement.moveRectangleBetweenTileCenters(playerView.getRectangle(), 
                player.getCoordinates(), player.getDestinationCoordinates(), player.getMovementProgress());
        }

        // Обновление графического представления AI танков
        for (int i = 0; i < aiTankViews.size(); i++) {
            TankView aiTankView = aiTankViews.get(i);
            Tank aiTank = aiTanks.get(i);
            if (aiTank.isAlive()) {
                tileMovement.moveRectangleBetweenTileCenters(aiTankView.getRectangle(), 
                    aiTank.getCoordinates(), aiTank.getDestinationCoordinates(), aiTank.getMovementProgress());
            }
        }

        levelRenderer.render();

        batch.begin();
        
        // Отрисовка игрока
        if (((Tank) player).isAlive()) {
            GameObjectView playerViewToDraw = healthBarController.getView(0);
            if (playerViewToDraw != null) {
                playerViewToDraw.draw(batch);
            }
        }
        
        // Отрисовка AI танков
        for (int i = 0; i < aiTankViews.size(); i++) {
            Tank aiTank = aiTanks.get(i);
            if (aiTank.isAlive()) {
                GameObjectView aiTankViewToDraw = healthBarController.getView(i + 1); // +1 потому что 0 - игрок
                if (aiTankViewToDraw != null) {
                    aiTankViewToDraw.draw(batch);
                }
            }
        }
        
        // Отрисовка препятствий
        obstacleViews.forEach(view -> view.draw(batch));
        
        batch.end();
    }

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        redTankTexture.dispose();
        level.dispose();
        batch.dispose();
        if (healthBarController != null) {
            healthBarController.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Обработка изменения размера окна
    }

    @Override
    public void pause() {
        // Пауза игры
    }

    @Override
    public void resume() {
        // Возобновление игры
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        config.setTitle("Tank Game");
        config.setResizable(false);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
    private Map<GameObject, GameObjectView> views = new HashMap<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameEngine = new GameEngine();
        
        // 1. ПОДПИСЫВАЕМСЯ: Графика слушает Логику
        gameEngine.addListener(this);

        // 2. Инициализация уровня (из LevelGenerator)
        // Важно: LevelGenerator теперь должен не создавать View, а просто возвращать список GameObject,
        // или мы добавляем их в engine вручную.
        
        Tank playerTank = new Tank(new GridPoint2(1, 1), 3);
        // Это вызовет onObjectAdded и автоматически создаст View!
        gameEngine.addGameObject(playerTank); 
        
        // Генерация деревьев и врагов...
        // gameEngine.addGameObject(new Tree(...));
    }

    // --- Реализация LevelListener (Observer) ---

    @Override
    public void onObjectAdded(GameObject object) {
        if (object instanceof Tank) {
            views.put(object, new TankView((Tank) object)); // или TextureRegion
        } else if (object instanceof Bullet) {
            // Создаем картинку для пули
            // views.put(object, new BulletView((Bullet) object)); 
        } else if (object instanceof Tree) {
            views.put(object, new TreeView((Tree) object));
        }
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        // Удаляем графическое представление
        GameObjectView view = views.remove(object);
        if (view != null) {
            view.dispose(); // Если нужно освободить ресурсы
        }
    }

    // --- Игровой цикл ---

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        // 1. Создание команд (InputHandler)
        // Если нажат пробел -> new ShootCommand(playerTank, gameEngine).execute();
        
        // 2. Выполнение команд (уже произошло выше при нажатии)
        
        // 3. Логический тик
        gameEngine.updateState(); 
        
        // 4. Отрисовка
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        
        // Рисуем все активные views
        for (GameObjectView view : views.values()) {
            view.draw(batch); // Предполагаем, что у View есть метод draw
        }
        
        batch.end();
    }
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. Получаем готовый бин GameEngine из контейнера
        // Spring уже создал его внутри себя
        this.gameEngine = context.getBean(GameEngine.class);
        
        // 3. Получаем игрока (Танк), которого создал Spring
        Tank playerTank = context.getBean(Tank.class);
        
        // --- КОНЕЦ ИЗМЕНЕНИЙ SPRING ---

        // Настраиваем связь Observer (Графика слушает Логику)
        // Важно сделать это ДО добавления объектов в движок, чтобы отрисовался начальный танк
        gameEngine.addListener(this);

        // Добавляем игрока в движок.
        // Так как мы подписались строчкой выше, сработает onObjectAdded и танк нарисуется.
        gameEngine.addGameObject(playerTank);

        // Если у тебя есть генерация уровня (деревья, враги), её тоже лучше вызывать здесь
        // или через отдельный бин LevelGenerator.
        // initLevel(gameEngine); 
    }

    // ... Остальной код (onObjectAdded, onObjectRemoved, render) остается БЕЗ ИЗМЕНЕНИЙ ...
    
    @Override
    public void onObjectAdded(GameObject object) {
        if (object instanceof Tank) {
            views.put(object, new TankView((Tank) object));
        } else if (object instanceof Bullet) {
            // views.put(object, new BulletView((Bullet) object));
        } else if (object instanceof Tree) {
             views.put(object, new TreeView((Tree) object));
        }
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        GameObjectView view = views.remove(object);
        if (view != null) view.dispose();
    }

    @Override
    public void render() {
        // ... твой старый код рендера ...
        // gameEngine.updateState(); и отрисовка
        
        // (Для примера, чтобы код был валидным, скопирую структуру)
        float deltaTime = Gdx.graphics.getDeltaTime();
        gameEngine.updateState(); 
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (GameObjectView view : views.values()) {
            view.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (GameObjectView view : views.values()) {
            view.dispose();
        }
    }
