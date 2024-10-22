package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.angryBirds.Pigs.Pig;
import com.angryBirds.Screens.PauseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    // Arrays to hold game objects
    protected Array<Image> birds;
    protected Array<Image> blocks;
    protected Array<Image> pigs;

    // World dimensions
    protected final float WORLD_WIDTH = 1920;
    protected final float WORLD_HEIGHT = 1080;

    // Launcher
    protected Texture launcher1;
    protected Texture launcher2;
    protected Image launch1;
    protected Image launch2;
    Image level_bg;
//    protected Texture pauseButtonTexture;
//    protected Image pauseButton;

    // Pause screen
//    protected Screen pauseScreen = new PauseScreen(game);


    public BaseLevel(Main game) {
        this.game = game;

        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        // Setup stage
        stage = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(stage);

        // Initialize arrays
        birds = new Array<>();
        blocks = new Array<>();
        pigs = new Array<>();

        // Load launcher textures and setup
        loadLauncherTextures();
        setupLauncher();
        setupPauseButton();
    }

    // Load launcher textures
    private void loadLauncherTextures() {
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
            }

//    private void loadPauseButtonTexture() {
//        pauseButtonTexture = new Texture("pauseButton.png");
//    }

    // Setup launcher position and sizes
    private void setupLauncher() {
        float sf = 0.5f;

        launch1 = new Image(launcher1);
        launch1.setSize(launch1.getWidth()*sf, launch1.getHeight()*sf);
        launch1.setPosition(255, 215);

        launch2 = new Image(launcher2);
        launch2.setSize(launch2.getWidth()*sf, launch2.getHeight()*sf);
        launch2.setPosition(300, 285);
    }

    private void setupPauseButton() {
        float buttonScaleFactor = 0.3f;

//        pauseButton = new Image(pauseButtonTexture);
//        pauseButton.setSize(pauseButton.getWidth() * buttonScaleFactor, pauseButton.getHeight() * buttonScaleFactor);
//        pauseButton.setPosition(WORLD_WIDTH - pauseButton.getWidth() - 20, WORLD_HEIGHT - pauseButton.getHeight() - 20);

        // Add click listener to switch to pause screen
//        pauseButton.addListener(new ClickListener() {///////////////////////////////////////
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//
//                game.setScreen(pauseScreen);
//            }
//        });

//        stage.addActor(pauseButton);
    }

    // Abstract method for initializing game objects in specific levels
    protected abstract void initializeGameObjects();

    protected void setBackground(String backgroundTexturePath) {
        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));
        level_bg = new Image(backgroundTexture);
        level_bg.setSize(WORLD_WIDTH, WORLD_HEIGHT);
    }

    // Helper methods for adding objects
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

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Render background
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw launchers
        game.batch.draw(launcher1, launch1.getX(), launch1.getY(), launch1.getWidth(), launch1.getHeight());
        game.batch.draw(launcher2, launch2.getX(), launch2.getY(), launch2.getWidth(), launch2.getHeight());

        game.batch.end();

        stage.act(delta);
        stage.draw();

        // Render all game objects
//        renderGameObjects();
    }

//    private void renderGameObjects() {
//        // Render birds
//        for (Bird bird : birds) {
//            bird.render();
//        }
//
//        // Render blocks
//        for (Block block : blocks) {
//            block.render();
//        }
//
//        // Render pigs
//        for (Pig pig : pigs) {
//            pig.render();
//        }
//    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        launcher1.dispose();
        launcher2.dispose();
//        pauseButtonTexture.dispose();

        stage.dispose();

//        for (Bird bird : birds) {
//            bird.dispose();
//        }

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
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
