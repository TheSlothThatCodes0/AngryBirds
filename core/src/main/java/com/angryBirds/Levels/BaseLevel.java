package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.angryBirds.Pigs.Pig;
import com.angryBirds.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseLevel implements Screen {
    protected Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Texture backgroundTexture;
    protected Stage stage;

    protected Array<Image> birds; // array for all the bird images
    protected Array<Image> blocks;  // array for all the block images
    protected Array<Image> pigs; // array for all the pigs images

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

    public BaseLevel(Main game) { // constructor
        this.game = game;

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

        loadNavigationButtonTextures();
        setupNavigationButtons();
    }

    private void loadBackgroundTextures() { // loading background texture
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

    private void loadLauncherTextures() {  // setting up the launcher
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
    }

    private void loadPauseButtonTexture() {
        pauseButtonTexture = new Texture("pauseButton.png");
    }

    private void setupLauncher() {  // function for setting up launcher
        float sf = 0.5f;

        launch1 = new Image(launcher1);
        launch1.setSize(launch1.getWidth() * sf, launch1.getHeight() * sf);
        launch1.setPosition(255, 35);

        launch2 = new Image(launcher2);
        launch2.setSize(launch2.getWidth() * sf, launch2.getHeight() * sf);
        launch2.setPosition(300, 105);

        stage.addActor(launch1);
        stage.addActor(launch2);
    }

    private void setupPauseButton() {
        float buttonScaleFactor = 0.3f;

        pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(pauseButton.getWidth() * buttonScaleFactor, pauseButton.getHeight() * buttonScaleFactor);
        pauseButton.setPosition(WORLD_WIDTH - pauseButton.getWidth() - 20, WORLD_HEIGHT - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, BaseLevel.this));
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

    protected void addBird(Bird bird) { // function for adding bird object
        birds.add(bird);
        stage.addActor(bird);
    }

    protected void addBlock(Block block) { // function for adding block object
        blocks.add(block);
        stage.addActor(block);
    }

    protected void addPig(Pig pig) { // function for adding pig object
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

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        stateTime += delta;
        if (stateTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % backgroundFrames.length;
            stateTime = 0;
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (backgroundFrames != null && backgroundFrames[currentFrame] != null) {
            game.batch.begin();
            game.batch.draw(backgroundFrames[currentFrame], 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            game.batch.end();
        }

        if (levelSelectButton.isPressed()) {
            game.setScreen(new WinScreen(game));
        }
        if (restartButton.isPressed()) {
            game.setScreen(new LossScreen(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        if (backgroundFrames != null) {
            for (Texture frame : backgroundFrames) {
                if (frame != null) {
                    frame.dispose();
                }
            }
        }

        launcher1.dispose();
        launcher2.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        pauseButtonTexture.dispose();

        stage.dispose();

        if (levelSelectTexture != null) levelSelectTexture.dispose();
        if (restartTexture != null) restartTexture.dispose();

        for (Image bird : birds) {
            if (bird instanceof Bird) {
                ((Bird) bird).dispose();
            }
        }

        for (Image block : blocks) {
            if (block instanceof Block) {
                ((Block) block).dispose();
            }
        }

        for (Image pig : pigs) {
            if (pig instanceof Pig) {
                ((Pig) pig).dispose();
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
