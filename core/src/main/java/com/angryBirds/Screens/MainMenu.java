package com.angryBirds.Screens;

import com.angryBirds.Main;
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

    private Texture backgroundTexture;
    private Texture settingsButton;
    private Texture exitButton;
    private Texture playButton;

    private Rectangle settingsBounds;
    private Rectangle exitBounds;
    private Rectangle playBounds;

    private final float SETTINGS_SIZE = 100;
    private final float EXIT_SIZE = 100;
    private final float PLAY_WIDTH = 500;// Earlier it was 400
    private final float PLAY_HEIGHT = 200;

    public MainMenu(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        backgroundTexture = game.assets.get("MainMenuBG1.jpg", Texture.class);
        settingsButton = game.assets.get("settings.png", Texture.class);
        exitButton = game.assets.get("exitButton1.png", Texture.class);
        playButton = game.assets.get("playButton1.png", Texture.class);

        settingsBounds = new Rectangle(WORLD_WIDTH - SETTINGS_SIZE - 10, WORLD_HEIGHT - SETTINGS_SIZE - 10,
            SETTINGS_SIZE, SETTINGS_SIZE);
        exitBounds = new Rectangle(10, WORLD_HEIGHT - EXIT_SIZE - 10,
            EXIT_SIZE, EXIT_SIZE);
        playBounds = new Rectangle((WORLD_WIDTH - PLAY_WIDTH) / 2,
            (WORLD_HEIGHT - PLAY_HEIGHT) / 10, // Modifiying the 2
            PLAY_WIDTH, PLAY_HEIGHT);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        game.batch.draw(settingsButton, settingsBounds.x, settingsBounds.y,
            settingsBounds.width, settingsBounds.height);
        game.batch.draw(exitButton, exitBounds.x, exitBounds.y,
            exitBounds.width, exitBounds.height);
        game.batch.draw(playButton, playBounds.x, playBounds.y,
            playBounds.width, playBounds.height);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (exitBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
            } else if (settingsBounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new SettingsScreen(game));
            } else if (playBounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new Levels_Screen(game));
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
