package com.angryBirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    // Textures
    private Texture backgroundTexture;
    private Texture settingsButton;
    private Texture exitButton;
    private Texture playButton;

    // Button rectangles for click detection
    private Rectangle settingsBounds;
    private Rectangle exitBounds;
    private Rectangle playBounds;

    // Button dimensions
    private final float SETTINGS_SIZE = 100;
    private final float EXIT_SIZE = 100;
    private final float PLAY_WIDTH = 400;
    private final float PLAY_HEIGHT = 200;

    public MainMenu(Main game) {
        this.game = game;

        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        // Load textures
        backgroundTexture = game.assets.get("MainMenuBG.jpg", Texture.class);
        settingsButton = game.assets.get("settings.png", Texture.class);
        exitButton = game.assets.get("exitButton.png", Texture.class);
        playButton = game.assets.get("playButton.png", Texture.class);

        // Initialize button bounds
        settingsBounds = new Rectangle(WORLD_WIDTH - SETTINGS_SIZE - 10, WORLD_HEIGHT - SETTINGS_SIZE - 10,
            SETTINGS_SIZE, SETTINGS_SIZE);
        exitBounds = new Rectangle(10, WORLD_HEIGHT - EXIT_SIZE - 10,
            EXIT_SIZE, EXIT_SIZE);
        playBounds = new Rectangle((WORLD_WIDTH - PLAY_WIDTH) / 2,
            (WORLD_HEIGHT - PLAY_HEIGHT) / 2,
            PLAY_WIDTH, PLAY_HEIGHT);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Draw background
        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw buttons
        game.batch.draw(settingsButton, settingsBounds.x, settingsBounds.y,
            settingsBounds.width, settingsBounds.height);
        game.batch.draw(exitButton, exitBounds.x, exitBounds.y,
            exitBounds.width, exitBounds.height);
        game.batch.draw(playButton, playBounds.x, playBounds.y,
            playBounds.width, playBounds.height);

        game.batch.end();

        // Handle input
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (exitBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
            } else if (settingsBounds.contains(touchPos.x, touchPos.y)) {
                // Will be implemented later to switch to settings screen
            } else if (playBounds.contains(touchPos.x, touchPos.y)) {
                // Will be implemented later to switch to game screen
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
    }

    @Override
    public void dispose() {
        // Don't dispose of assets here as they're managed by the asset manager
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
