package com.angryBirds.Screens;

import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseScreen1 implements Screen {
    private final Main game;
    private final Screen previousScreen;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Stage stage;

    private final Texture backgroundTexture;
    private final Texture resumeButtonTexture;
    private final Texture saveButtonTexture;
    private final Texture exitButtonTexture;

    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1080;

    private musicControl mc;

    public PauseScreen1(Main game, Screen previousScreen) { // constructor
        this.game = game;
        this.previousScreen = previousScreen;
        mc = musicControl.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("settingsScreenBG1.png");
        resumeButtonTexture = new Texture("play.png");
        saveButtonTexture = new Texture("saveGame.png");
        exitButtonTexture = new Texture("cut.png");

        setupUI();
    }

    private void setupUI() { // loading and setting up all the assets
        float pauseScreenWidth = WORLD_WIDTH*1.5f;
        float pauseScreenHeight = WORLD_HEIGHT*1.5f;
        float pauseScreenX = (WORLD_WIDTH - pauseScreenWidth) / 2;
        float pauseScreenY = (WORLD_HEIGHT - pauseScreenHeight - 300) / 2;

        Image background = new Image(backgroundTexture);
        background.setSize(pauseScreenWidth, pauseScreenHeight);
        background.setPosition(pauseScreenX, pauseScreenY);
        stage.addActor(background);

        float buttonScale = 0.75f;
        float buttonSpacing = 20f;
        float buttonY = pauseScreenY + pauseScreenHeight / 2 + 5;

        Image resumeButton = new Image(resumeButtonTexture);
        resumeButton.setSize(resumeButton.getWidth() * buttonScale, resumeButton.getHeight() * buttonScale);
        resumeButton.setPosition(
                pauseScreenX + (pauseScreenWidth - (3 * resumeButton.getWidth() + 2 * buttonSpacing)) / 2,
                buttonY);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousScreen instanceof BaseLevel) {
                    BaseLevel baseLevel = (BaseLevel) previousScreen;
                    baseLevel.resumeGame();
                }
                game.setScreen(previousScreen);
            }
        });

        // Save button
        Image saveButton = new Image(saveButtonTexture);
        saveButton.setSize(saveButton.getWidth() * buttonScale, saveButton.getHeight() * buttonScale);
        saveButton.setPosition(
                resumeButton.getX() + resumeButton.getWidth() + buttonSpacing,
                buttonY);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Get the current game state from BaseLevel
                if (previousScreen instanceof BaseLevel) {
                    BaseLevel level = (BaseLevel) previousScreen;
                    level.saveGameState();
                }
                mc.crossFade("audio/theme_1.mp3",3.0f);
                game.setScreen(new Levels_Screen(game,previousScreen));
            }
        });

        // Exit button
        Image exitButton = new Image(exitButtonTexture);
        exitButton.setSize(exitButton.getWidth() * buttonScale, exitButton.getHeight() * buttonScale);
        exitButton.setPosition(
                saveButton.getX() + saveButton.getWidth() + buttonSpacing,
                buttonY);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(resumeButton);
        stage.addActor(saveButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        // Render the previous screen behind the pause screen
        previousScreen.render(delta);
//        previousScreen.pause();

        if (previousScreen instanceof BaseLevel) {
            BaseLevel baseLevel = (BaseLevel) previousScreen;
            baseLevel.render(0); // Pass 0 as delta to prevent any updates
        }

        // Render the pause screen on top
        // ScreenUtils.clear(0, 0, 0, 0f); // Semi-transparent background
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
        stage.dispose();
        backgroundTexture.dispose();
        resumeButtonTexture.dispose();
        saveButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

    @Override
    public void show() {
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
