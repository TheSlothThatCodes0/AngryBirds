package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.angryBirds.Pigs.Pig;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseLevel implements Screen {
    protected Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Texture backgroundTexture;

    // Arrays to hold game objects
    protected Array<Bird> birds;
    protected Array<Block> blocks;
    protected Array<Pig> pigs;

    // World dimensions
    protected final float WORLD_WIDTH = 1920;
    protected final float WORLD_HEIGHT = 1080;

    // Launcher
    protected Texture launcher1;
    protected Texture launcher2;
    protected Image launch1;
    protected Image launch2;

    public BaseLevel(Main game) {
        this.game = game;

        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        // Initialize arrays
        birds = new Array<>();
        blocks = new Array<>();
        pigs = new Array<>();

        // Load launcher textures and setup
        loadLauncherTextures();
        setupLauncher();
    }

    // Load launcher textures
    private void loadLauncherTextures() {
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
    }

    // Setup launcher position and sizes
    private void setupLauncher() {
        launch1 = new Image(launcher1);
        launch1.setPosition(240, 200);
        launch1.setSize(100, 100);

        launch2 = new Image(launcher2);
        launch2.setPosition(327, 340);
        launch2.setSize(100, 100);
    }

    // Abstract method for initializing game objects in specific levels
    protected abstract void initializeGameObjects();

    protected void setBackground(String backgroundTexturePath) {
        backgroundTexture = game.assets.get(backgroundTexturePath, Texture.class);
    }

    // Helper methods for adding objects
    protected void addBird(Bird bird, float x, float y) {
        bird.setPosition(x, y);
        birds.add(bird);
    }

    protected void addBlock(Block block, float x, float y) {
        block.setPosition(x, y);
        blocks.add(block);
    }

    protected void addPig(Pig pig, float x, float y) {
        pig.setPosition(x, y);
        pigs.add(pig);
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

        // Render all game objects
        renderGameObjects();
    }

    private void renderGameObjects() {
        // Render birds
        for (Bird bird : birds) {
            bird.render();
        }

        // Render blocks
        for (Block block : blocks) {
            block.render();
        }

        // Render pigs
        for (Pig pig : pigs) {
            pig.render();
        }
    }

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

        for (Bird bird : birds) {
            bird.dispose();
        }

        for (Block block : blocks) {
            block.dispose();
        }

        for (Pig pig : pigs) {
            pig.dispose();
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
