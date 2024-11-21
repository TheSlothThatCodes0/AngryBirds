package com.angryBirds.Screens;

import com.angryBirds.Main;
import com.angryBirds.Utils.CustomButton;
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
    private Texture settingsButtonTexture;
    private Texture exitButtonTexture;
    private Texture playButtonTexture;
    private Texture exitButtonTexture_Clicked;
    private Texture playButtonTexture_clicked;

    private Rectangle settingsBounds;
    private Rectangle exitBounds;
    private Rectangle playBounds;

    private final float SETTINGS_SIZE = 100;
    private final float EXIT_SIZE = 100;
    private final float PLAY_WIDTH = 500;
    private final float PLAY_HEIGHT = 200;

    private boolean isSettingsHovered = false;
    private boolean isPlayHovered = false;
    private boolean isExitHovered = false;
    private final float HOVER_SCALE = 1.2f;
    private boolean isExiting = false;

    private CustomButton playButton;
    private CustomButton exitButton;
    private CustomButton settingsButton;

    public MainMenu(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        backgroundTexture = game.assets.get("MainMenuBG1.jpg", Texture.class);
        settingsButtonTexture = game.assets.get("settings.png", Texture.class);
        exitButtonTexture = game.assets.get("exitButton1.png", Texture.class);
        exitButtonTexture_Clicked = game.assets.get("exitButton1_pressed.png", Texture.class);
        playButtonTexture = game.assets.get("playButton1.png", Texture.class);
        playButtonTexture_clicked = game.assets.get("playButton1_clicked.png", Texture.class);



        settingsBounds = new Rectangle(WORLD_WIDTH - SETTINGS_SIZE - 10, WORLD_HEIGHT - SETTINGS_SIZE - 10,
            SETTINGS_SIZE, SETTINGS_SIZE);
        exitBounds = new Rectangle(10, WORLD_HEIGHT - EXIT_SIZE - 10,
            EXIT_SIZE, EXIT_SIZE);
        playBounds = new Rectangle((WORLD_WIDTH - PLAY_WIDTH) / 2,
            (WORLD_HEIGHT - PLAY_HEIGHT) / 10,
            PLAY_WIDTH, PLAY_HEIGHT);//////////////////////////////////////////////////////////////////////////////

        playButton = new CustomButton(game,playBounds, playButtonTexture, playButtonTexture_clicked,playButtonTexture, () -> {
            game.setScreen(new Levels_Screen(game));
        });

        exitButton = new CustomButton(game, exitBounds, exitButtonTexture, exitButtonTexture_Clicked, exitButtonTexture, () -> {
            Gdx.app.exit();
        });

        settingsButton = new CustomButton(game,settingsBounds, settingsButtonTexture, settingsButtonTexture,settingsButtonTexture, () -> {
            game.setScreen(new SettingsScreen(game));
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        isSettingsHovered = settingsBounds.contains(mousePos.x, mousePos.y);
        isPlayHovered = playButton.isHover(mousePos);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        playButton.workHover(game,isPlayHovered,mousePos);/////////////////////////////////////////////////////////////////////////////////

        // Draw exit button with different texture when exiting
        exitButton.workHover(game,isExitHovered,mousePos);/////////////////////////////////////////////////////////////////////////////////

        settingsButton.workHover(game,isSettingsHovered,mousePos);/////////////////////////////////////////////////////////////////////////////////



        // Draw settings button with hover effect
//        if (isSettingsHovered) {
//            float scaledWidth = settingsBounds.width * HOVER_SCALE;
//            float scaledHeight = settingsBounds.height * HOVER_SCALE;
//            float offsetX = (scaledWidth - settingsBounds.width) / 2;
//            float offsetY = (scaledHeight - settingsBounds.height) / 2;
//
//            game.batch.draw(settingsButton,
//                settingsBounds.x - offsetX,
//                settingsBounds.y - offsetY,
//                scaledWidth,
//                scaledHeight);
//        } else {
//            game.batch.draw(settingsButton, settingsBounds.x, settingsBounds.y,
//                settingsBounds.width, settingsBounds.height);
//        }

        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            playButton.workClick(game,touchPos);///////////////////////////////////////////////////////////////////////////////////////
            exitButton.workClick(game,touchPos);///////////////////////////////////////////////////////////////////////////////////////
            settingsButton.setScale(1.5f);
            settingsButton.workClick(game,touchPos);///////////////////////////////////////////////////////////////////////////////////////

//            if (exitBounds.contains(touchPos.x, touchPos.y) && !isExiting) {
//                // Start exit sequence
//                isExiting = true;
//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        Gdx.app.exit();
//                    }
//                }, 0.3f);
//            }

        if (settingsBounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new SettingsScreen(game));
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
