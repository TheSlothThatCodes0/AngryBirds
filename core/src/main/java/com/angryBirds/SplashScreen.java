package com.angryBirds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class SplashScreen implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture splashImage;
    private Texture loadingBird;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;
    private float elapsed = 0f;
    private static final float MINIMUM_SPLASH_TIME = 5f; // Minimum time to show splash screen
    private boolean assetsLoaded = false;

    public SplashScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        // Load splash image immediately
        splashImage = new Texture("splashScreen.png");
        loadingBird = new Texture("loading.png");

        // Queue all other assets
        queueAssets();
    }

    private void queueAssets() {
        game.assets.load("MainMenuBG.jpg", Texture.class);
        game.assets.load("settings.png", Texture.class);
        game.assets.load("exitButton.png", Texture.class);
        game.assets.load("playButton.png", Texture.class);
        game.assets.load("level_background.png", Texture.class);
        game.assets.load("red_bird.png", Texture.class);
        game.assets.load("blue_bird.png", Texture.class);
        game.assets.load("yellow_bird.png", Texture.class);
        game.assets.load("g_b.png",Texture.class);
        game.assets.load("g_p.png",Texture.class);
        game.assets.load("g_t.png",Texture.class);
        game.assets.load("s_b.png",Texture.class);
        game.assets.load("s_p.png",Texture.class);
        game.assets.load("s_t.png",Texture.class);
        game.assets.load("w_p.png",Texture.class);
        game.assets.load("w_b.png",Texture.class);
        game.assets.load("w_t.png",Texture.class);
        game.assets.load("pig1.png", Texture.class);
        game.assets.load("pig2.png", Texture.class);
        game.assets.load("pig3.png", Texture.class);

    }

    @Override
    public void render(float delta) {
        // Update timer
        elapsed += delta;

        // Update asset loading
        assetsLoaded = game.assets.update(); // Returns true when all assets are loaded

        // Only proceed to main menu if both conditions are met:
        // 1. Minimum splash time has elapsed
        // 2. All assets are loaded
        if (elapsed >= MINIMUM_SPLASH_TIME && assetsLoaded) {
            game.setScreen(new MainMenu(game));
            return;
        }

        // Clear screen
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Draw splash screen
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(splashImage, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // draw loading bird in bottom center
        float birdWidth = 200;
        float birdHeight = 100;
        float birdX = (WORLD_WIDTH - birdWidth) / 2;
        float birdY = 50; // Distance from bottom
        game.batch.draw(loadingBird, birdX, birdY, birdWidth, birdHeight);

        // loading progress
        float progress = game.assets.getProgress();

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
    }

    @Override
    public void dispose() {
        splashImage.dispose();
        loadingBird.dispose();
    }

    // Other required Screen interface methods
    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

}
