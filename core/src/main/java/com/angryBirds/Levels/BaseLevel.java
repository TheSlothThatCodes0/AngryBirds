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

    public boolean loadingFromSave = false;

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
    protected Matrix4 debugMatrix;
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

        this.mc = musicControl.getInstance();
        mc.crossFade("audio/theme_3.mp3", 0.7f);

        loadNavigationButtonTextures();
        setupNavigationButtons();
        blocksToDestroy = new Array<>();

    }

    protected void checkWinCondition() {
        boolean allPigsDead = true;
        for (Image x : pigs) {
            if (((Pig) x).health > 0) {
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

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

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
                new Vector2(300, 105),
                launcher1, launcher2);
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

        for (Image image : birds) {
            if (image instanceof Bird) {
                Bird bird = (Bird) image;
                saveData.birds.add(new SaveData.GameObjectData(
                        bird.getClass().getSimpleName(),
                        "",
                        bird.getX(),
                        bird.getY(),
                        bird.getRotation()));
            }
        }

        for (Image image : blocks) {
            if (image instanceof Block) {
                Block block = (Block) image;
                saveData.blocks.add(new SaveData.GameObjectData(
                        block.getClass().getSimpleName(),
                        block.getMaterial(),
                        block.getX(), 
                        block.getY(),
                        block.getRotation()));
            }
        }

        for (Image image : pigs) {
            if (image instanceof Pig) {
                Pig pig = (Pig) image;
                saveData.pigs.add(new SaveData.GameObjectData(
                        pig.getClass().getSimpleName(),
                        "",
                        pig.getX(), 
                        pig.getY(),
                        pig.getRotation()));
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

    public void pauseGame() {
        isPaused = true;
        Gdx.input.setInputProcessor(null);
    }

    public void resumeGame() {
        isPaused = false;
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        float actualDelta = isPaused ? 0 : delta;

        ScreenUtils.clear(1, 1, 1, 1);

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

        stage.act(actualDelta);
        stage.draw();

        if (!isPaused) {
            checkEndCondition();
        }
    }

    private void cleanupDestroyedBlocks() {
        blocksToDestroy.clear();

        for (Image image : blocks) {
            if (image instanceof Block) {
                Block block = (Block) image;
                if (block.isMarkedForDestruction()) {
                    blocksToDestroy.add(block);
                }
            }
        }

        for (Block block : blocksToDestroy) {
            blocks.removeValue(block, true);
            block.remove();
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
        if (world != null) {
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for (Body body : bodies) {
                world.destroyBody(body);
            }
            world.dispose();
        }
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
            if (((Pig) x).health > 0) {
                allPigsDead = false;
                break;
            }
        }
        for (Image x : birds) {
            if (!((Bird) x).isDead) {
                allBirdsInactive = false;
                break;
            }
        }
        if (allPigsDead) {
            game.setScreen(new WinScreen(game));
        } else if (allBirdsInactive && !allPigsDead) {
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
