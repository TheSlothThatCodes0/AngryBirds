package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Birds.YellowBird;
import com.angryBirds.Blocks.*;
import com.angryBirds.Main;
import com.angryBirds.Pigs.KingPig;
import com.angryBirds.Pigs.NormalPig;
import com.angryBirds.Pigs.Pig;
import com.angryBirds.Screens.*;
import com.angryBirds.Utils.SaveData;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Constructor;

public abstract class BaseLevel implements Screen {

    protected static final short CATEGORY_GROUND = 0x0001; // 1 in binary
    protected static final short CATEGORY_BLOCKS = 0x0002; // 2 in binary
    protected static final short CATEGORY_BIRDS = 0x0004; // 4 in binary
    protected static final short CATEGORY_PIGS = 0x0008; // 8 in binary

    protected static final short MASK_GROUND = -1; // Collide with everything
    protected static final short MASK_BLOCKS = -1; // Collide with everything
    protected static final short MASK_BIRDS = -1; // Collide with everything
    public boolean loadingFromSave = false;
    // protected static final short MASK_PIGS = -1; // Collide with everything

    protected Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Texture backgroundTexture;
    protected Stage stage;

    protected Array<Image> birds;
    protected Array<Image> blocks;
    protected Array<Image> pigs;

    protected final float WORLD_WIDTH = 1920;
    protected final float WORLD_HEIGHT = 1080;

    protected Texture launcher1;
    protected Texture launcher2;
    protected Image launch1;
    protected Image launch2;
    protected Image level_bg;

    protected Texture[] backgroundFrames;
    protected int currentFrame;
    protected float stateTime;
    protected float frameDuration = 0.1f;

    protected Texture pauseButtonTexture;
    protected Image pauseButton;

    protected Texture levelSelectTexture;
    protected Texture restartTexture;
    protected ImageButton levelSelectButton;
    protected ImageButton restartButton;
    protected Skin skin;

    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    protected static final float PPM = 5;
    protected Matrix4 debugMatrix; // Add this as class field
    protected Launcher launcher;

    private musicControl mc;
    private Array<Block> blocksToDestroy;
    private boolean isPaused = false;


    public BaseLevel(Main game, boolean loadFromSave) {
        this.loadingFromSave = loadFromSave;
        this.game = game;
        int ground_height = 35;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        birds = new Array<>();
        blocks = new Array<>();
        pigs = new Array<>();
        loadBackgroundTextures();
        loadLauncherTextures();
        loadPauseButtonTexture();
        setupLauncher();
        setupPauseButton();

        world = new World(new Vector2(0, -20f), true);
        world.setContactListener(new CollisionHandler());
        // debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        // debugMatrix = new Matrix4(camera.combined.cpy().scl(PPM));

        this.mc = musicControl.getInstance();
//        mc.fadeOut();
        mc.crossFade("audio/theme_3.mp3",2.0f);

        loadNavigationButtonTextures();
        setupNavigationButtons();
        blocksToDestroy = new Array<>();

    }

    protected void checkWinCondition() {
    boolean allPigsDead = true;
    for (Image x : pigs) {
        if (((Pig)x).health > 0) {
            allPigsDead = false;
            break;
        }
    }
    if (allPigsDead) {
        game.setScreen(new WinScreen(game));
    }
}

    private void loadBackgroundTextures() {
        backgroundFrames = new Texture[8];
        try {
            for (int i = 0; i < backgroundFrames.length; i++) {
                String filename = "frame_" + i + "_delay-0.1s.png";
                if (Gdx.files.internal(filename).exists()) {
                    backgroundFrames[i] = new Texture(Gdx.files.internal(filename));
                    Gdx.app.log("BaseLevel", "Loaded texture: " + filename);
                } else {
                    Gdx.app.error("BaseLevel", "Cannot find texture: " + filename);
                }
            }
        } catch (Exception e) {
            Gdx.app.error("BaseLevel", "Error loading textures", e);
        }
    }

    private void loadLauncherTextures() {
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
    }

    private void loadPauseButtonTexture() {
        pauseButtonTexture = new Texture("pauseButton.png");
    }

    protected void debugPhysicsBodies() {
        StringBuilder debug = new StringBuilder("Physics World Bodies:\n");

        Array<Body> bodies = new Array<>(); // Create an Array to store bodies
        world.getBodies(bodies); // Pass the array to getBodies()

        for (Body body : bodies) {
            debug.append(String.format(
                    "Body: %s, Position: (%.2f, %.2f), Active: %b\n",
                    body.getUserData(),
                    body.getPosition().x,
                    body.getPosition().y,
                    body.isActive()));
        }

        System.out.println(debug.toString());
    }

    private void setupLauncher() {
        launcher = new Launcher(world, stage,
                new Vector2(300, 105), // Launch point coordinates
                launcher1, launcher2 // Pass existing launcher textures
        );
    }

    private void setupPauseButton() {
        float buttonScaleFactor = 0.3f;

        pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(pauseButton.getWidth() * buttonScaleFactor, pauseButton.getHeight() * buttonScaleFactor);
        pauseButton.setPosition(WORLD_WIDTH - pauseButton.getWidth() - 20, WORLD_HEIGHT - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseGame();
                game.setScreen(new PauseScreen1(game, BaseLevel.this));
            }
        });

        stage.addActor(pauseButton);
    }

    protected abstract void initializeGameObjects();

    protected void setBackground(String backgroundTexturePath) {
        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));
        level_bg = new Image(backgroundTexture);
        level_bg.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        stage.addActor(level_bg);
        level_bg.toBack();
    }

    protected void addBird(Bird bird) {
        birds.add(bird);
        stage.addActor(bird);
    }

    protected void addBlock(Block block) {
        blocks.add(block);
        stage.addActor(block);
    }

    protected void addPig(Pig pig) {
        pigs.add(pig);
        stage.addActor(pig);
    }

    private void loadNavigationButtonTextures() {
        try {
            levelSelectTexture = new Texture("button_square_depth_gradient.png");
            restartTexture = new Texture("button_square_gradient.png");
        } catch (Exception e) {
            Gdx.app.error("BaseLevel", "Error loading navigation button textures", e);
        }
    }

    private void setupNavigationButtons() {
        float padding = 20f;

        ImageButton.ImageButtonStyle levelSelectStyle = new ImageButton.ImageButtonStyle();
        levelSelectStyle.imageUp = new TextureRegionDrawable(new TextureRegion(levelSelectTexture));

        ImageButton.ImageButtonStyle restartStyle = new ImageButton.ImageButtonStyle();
        restartStyle.imageUp = new TextureRegionDrawable(new TextureRegion(restartTexture));

        levelSelectButton = new ImageButton(levelSelectStyle);
        restartButton = new ImageButton(restartStyle);

        float buttonScale = 0.3f;
        levelSelectButton.setSize(levelSelectTexture.getWidth() * buttonScale,
                levelSelectTexture.getHeight() * buttonScale);
        restartButton.setSize(restartTexture.getWidth() * buttonScale,
                restartTexture.getHeight() * buttonScale);

        levelSelectButton.setPosition(padding, WORLD_HEIGHT - levelSelectButton.getHeight() - padding);
        restartButton.setPosition(levelSelectButton.getX() + levelSelectButton.getWidth() + padding,
                levelSelectButton.getY());

        stage.addActor(levelSelectButton);
        stage.addActor(restartButton);
    }

    // _____________________________ game saving ___________________________

    public static void clearSavedGame() {
        try {
            FileHandle file = Gdx.files.local("save.json");
            if (file.exists()) {
                file.delete();
                System.out.println("Previous save file deleted");
            }
        } catch (Exception e) {
            System.out.println("Error deleting save file: " + e.getMessage());
        }
    }

    public void saveGameState() {
        clearSavedGame();
        SaveData saveData = new SaveData();
        saveData.levelName = this.getClass().getSimpleName();

        // Save birds
        for (Image image : birds) {
            if (image instanceof Bird) {
                Bird bird = (Bird) image;
                Body body = bird.getBody();
                if (body != null) {
                    saveData.birds.add(new SaveData.GameObjectData(
                        bird.getClass().getSimpleName(),
                        "",
                        body.getPosition().x * PPM,
                        body.getPosition().y * PPM,
                        body.getAngle()));
                }
            }
        }

        for (Image image : blocks) {
            if (image instanceof Block) {
                Block block = (Block) image;
                Body body = block.body;
                if (body != null) {
                    saveData.blocks.add(new SaveData.GameObjectData(
                        block.getClass().getSimpleName(),
                        block.getMaterial(),
                        body.getPosition().x * PPM,
                        body.getPosition().y * PPM,
                        body.getAngle()));
                }
            }
        }

        // Save pigs
        for (Image image : pigs) {
            if (image instanceof Pig) {
                Pig pig = (Pig) image;
                Body body = pig.getBody();
                if (body != null) {
                    saveData.pigs.add(new SaveData.GameObjectData(
                            pig.getClass().getSimpleName(),
                            "",
                            body.getPosition().x * PPM,
                            body.getPosition().y * PPM,
                            body.getAngle()));
                }
            }
        }

        try {
            FileHandle file = Gdx.files.local("save.json");
            String json = new Json().toJson(saveData);
            file.writeString(json, false);
        } catch (Exception e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static SaveData loadSavedGameFile() {
        try {
            FileHandle file = Gdx.files.local("save.json");
            if (file.exists()) {
                String json = file.readString();
                return new Json().fromJson(SaveData.class, json);
            }
        } catch (Exception e) {
            System.out.println("Error loading save: " + e.getMessage());
        }
        return null;
    }

    public void loadFromSaveData(SaveData saveData) {
        this.loadingFromSave = true;
        // Load the background
        setBackground("frame_0_delay-0.1s.png");
        // Load ground
        addBlock(new Ground(game, "stone", 0, 0, world));
        // Load birds
        for (SaveData.GameObjectData birdData : saveData.birds) {
            Bird bird = createBirdFromData(birdData);
            if (bird != null) {
                addBird(bird);
            }
        }
        // Load blocks
        for (SaveData.GameObjectData blockData : saveData.blocks) {
            Block block = createBlockFromData(blockData);
            if (block != null) {
                addBlock(block);
            }
        }
        // Load pigs
        for (SaveData.GameObjectData pigData : saveData.pigs) {
            Pig pig = createPigFromData(pigData);
            if (pig != null) {
                addPig(pig);
            }
        }
    }

    private Bird createBirdFromData(SaveData.GameObjectData data) {
        switch (data.type) {
            case "RedBird":
                return new RedBird(game, data.x, data.y, world);
            case "BlueBird":
                return new BlueBird(game, data.x, data.y, world);
            case "YellowBird":
                return new YellowBird(game, data.x, data.y, world);
            default:
                return null;
        }
    }

    private Block createBlockFromData(SaveData.GameObjectData data) {
        switch (data.type) {
            case "Cube":
                return new Cube(game, data.material, data.x, data.y, world, data.angle);
            case "Plank":
                return new Plank(game, data.material, data.x, data.y, world, data.angle);
            case "Triangle":
                return new Triangle(game, data.material, data.x, data.y, world, data.angle);
            case "Column":
                return new Column(game, data.material, data.x, data.y, world, data.angle);
            case "Wall":
                return new Wall(game, data.x, data.y, world);
            case "Ground":
                return new Ground(game, data.material, data.x, data.y, world);
            default:
                return null;
        }
    }

    private Pig createPigFromData(SaveData.GameObjectData data) {
        switch (data.type) {
            case "NormalPig":
                return new NormalPig(game, "small", data.x, data.y, world);
            case "KingPig":
                return new KingPig(game, data.x, data.y, world);
            default:
                return null;
        }
    }

    public void pauseGame() {
        isPaused = true;
        Gdx.input.setInputProcessor(null);
    }

    public void resumeGame() {
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        // If paused, use a delta of 0 to freeze simulation
        float actualDelta = isPaused ? 0 : delta;

        ScreenUtils.clear(1, 1, 1, 1);

        // Only step the world if not paused
        if (!isPaused) {
            cleanupDestroyedBlocks();
            world.step(1 / 60f, 6, 2);
        }

        stateTime += actualDelta;
        if (stateTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % backgroundFrames.length;
            stateTime = 0;
        }

        camera.update();

        if (backgroundFrames != null && backgroundFrames[currentFrame] != null) {
            game.batch.begin();
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.draw(backgroundFrames[currentFrame], 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            game.batch.end();
        }

        // Draw the stage with controlled delta
        stage.act(actualDelta);
        stage.draw();

        // Only check end conditions if not paused
        if (!isPaused) {
            checkEndCondition();
        }
    }

    private void cleanupDestroyedBlocks() {
        // Clear the array first
        blocksToDestroy.clear();

        // Check for blocks that need to be destroyed
        for (Image image : blocks) {
            if (image instanceof Block) {
                Block block = (Block) image;
                if (block.isMarkedForDestruction()) {
                    blocksToDestroy.add(block);
                }
            }
        }

        // Remove and dispose of the marked blocks
        for (Block block : blocksToDestroy) {
            blocks.removeValue(block, true);
            block.remove(); // Remove from stage
            if (block.body != null) {
                world.destroyBody(block.body);
                block.body = null;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        // First dispose of all physics bodies
        if (world != null) {
            // Remove all bodies first
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for (Body body : bodies) {
                world.destroyBody(body);
            }
            world.dispose();
        }

        // if (debugRenderer != null) {
        //     debugRenderer.dispose();
        // }

        // Then dispose of game objects
        for (Image block : blocks) {
            if (block instanceof Block) {
                ((Block) block).dispose();
            }
        }

        for (Image bird : birds) {
            if (bird instanceof Bird) {
                ((Bird) bird).dispose();
            }
        }

        for (Image pig : pigs) {
            if (pig instanceof Pig) {
                ((Pig) pig).dispose();
            }
        }

        // Finally dispose of textures and stage
        if (backgroundFrames != null) {
            for (Texture frame : backgroundFrames) {
                if (frame != null) {
                    frame.dispose();
                }
            }
        }

        if (launcher1 != null)
            launcher1.dispose();
        if (launcher2 != null)
            launcher2.dispose();
        if (backgroundTexture != null)
            backgroundTexture.dispose();
        if (pauseButtonTexture != null)
            pauseButtonTexture.dispose();
        if (levelSelectTexture != null)
            levelSelectTexture.dispose();
        if (restartTexture != null)
            restartTexture.dispose();

        if (stage != null) {
            stage.dispose();
        }
    }

    protected void checkEndCondition() {
        boolean allPigsDead = true;
        boolean allBirdsInactive = true;
        for (Image x : pigs) {
            if (((Pig)x).health > 0) {
                allPigsDead = false;
                break;
            }
        }
        for(Image x : birds){
            if(!((Bird) x).isDead){
                allBirdsInactive = false;
                break;
            }
        }
        if (allPigsDead) {
//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
////                    game.setScreen(new WinScreen(game));
//                }}, 5);
            game.setScreen(new WinScreen(game));
        }
        else if(allBirdsInactive && !allPigsDead){
//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
//
//                }}, 3);
            game.setScreen(new LossScreen(game));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
