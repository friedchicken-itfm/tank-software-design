@Override
public void create() {
    batch = new SpriteBatch();

    // Создаем генератор уровня в зависимости от конфигурации
    LevelGenerator levelGenerator;
    if (GameConfig.LEVEL_GENERATOR_TYPE == LevelGeneratorFactory.GeneratorType.RANDOM) {
        levelGenerator = LevelGeneratorFactory.createGenerator(
            LevelGeneratorFactory.GeneratorType.RANDOM,
            String.valueOf(GameConfig.RANDOM_LEVEL_WIDTH),
            String.valueOf(GameConfig.RANDOM_LEVEL_HEIGHT),
            String.valueOf(GameConfig.RANDOM_OBSTACLE_DENSITY)
        );
    } else {
        levelGenerator = LevelGeneratorFactory.createGenerator(
            LevelGeneratorFactory.GeneratorType.FILE,
            GameConfig.LEVEL_FILE_NAME
        );
    }

    // Генерируем уровень
    LevelData levelData = levelGenerator.generateLevel();

    // Загрузка уровня и настройка рендеринга
    level = new TmxMapLoader().load("level.tmx");
    levelRenderer = createSingleLayerMapRenderer(level, batch);
    TiledMapTileLayer groundLayer = getSingleLayer(level);
    tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

    // Загрузка текстур
    blueTankTexture = new Texture("images/tank_blue.png");
    greenTreeTexture = new Texture("images/greenTree.png");

    // Создание моделей через абстракции (используем данные уровня)
    player = new Tank(levelData.getPlayerStart(), GameConfig.MOVEMENT_SPEED);
    obstacles = levelData.getObstacles();

    // Создание представлений
    playerView = new TankView((Tank) player, new TextureRegion(blueTankTexture));
    obstacleViews = obstacles.stream()
            .filter(obstacle -> obstacle instanceof Tree)
            .map(obstacle -> new TreeView((Tree) obstacle, new TextureRegion(greenTreeTexture)))
            .collect(Collectors.toList());

    // Создание обработчика ввода (работает с абстракциями)
    inputHandler = new InputHandler(player, obstacles);

    // Начальное позиционирование
    obstacleViews.forEach(view -> moveRectangleAtTileCenter(groundLayer, view.getRectangle(), view.gameObject.getCoordinates()));
}