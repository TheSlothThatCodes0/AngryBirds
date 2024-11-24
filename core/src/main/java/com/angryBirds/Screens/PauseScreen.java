package com.angryBirds.Screens;

import com.angryBirds.Main;
import com.angryBirds.Levels.BaseLevel;
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

public class PauseScreen implements Screen {
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

    public PauseScreen(Main game, Screen previousScreen) { // constructor
        this.game = game;
        this.previousScreen = previousScreen;
        mc = musicControl.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("settBack1.png");
        resumeButtonTexture = new Texture("resumeButtonPS.png");
        saveButtonTexture = new Texture("saveButtonPS.png");
        exitButtonTexture = new Texture("exitButtonPS.png");

        setupUI();
    }

    private void setupUI() { // loading and setting up all the assets
        Image background = new Image(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        stage.addActor(background);

        float buttonScale = 0.75f;
        float buttonSpacing = 80f;
        float buttonY = (WORLD_HEIGHT / 2) - 100;

        Image resumeButton = new Image(resumeButtonTexture);
        resumeButton.setSize(resumeButton.getWidth() * buttonScale, resumeButton.getHeight() * buttonScale);
        resumeButton.setPosition(
                (WORLD_WIDTH - (3 * resumeButton.getWidth() + 2 * buttonSpacing)) / 2,
                buttonY);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                mc.crossFade("audio/theme_1.mp3",3.0f);
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(resumeButton);
        stage.addActor(saveButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() { // disposing of the assets
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
