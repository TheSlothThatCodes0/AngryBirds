package com.angryBirds.Screens;

import com.angryBirds.Main;
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

    // Textures
    private final Texture backgroundTexture;
    private final Texture resumeButtonTexture;
    private final Texture saveButtonTexture;
    private final Texture exitButtonTexture;

    // World dimensions (matching BaseLevel)
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1080;

    public PauseScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        // Setup camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        // Setup stage
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture("settBack.png");
        resumeButtonTexture = new Texture("button_square_depth_flat.png");
        saveButtonTexture = new Texture("button_square_depth_flat.png");
        exitButtonTexture = new Texture("button_square_depth_flat.png");

        // Setup UI elements
        setupUI();
    }

//    private void setupUI() {
//        // Background
//        Image background = new Image(backgroundTexture);
//        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
//        stage.addActor(background);
//
//        // Button scaling and positioning
//        float buttonScale = 2f;
//        float buttonSpacing = 50f;
//        float startY = WORLD_HEIGHT / 2 + 300;
//
//        // Resume button
//        Image resumeButton = new Image(resumeButtonTexture);
//        resumeButton.setSize(resumeButton.getWidth() * buttonScale, resumeButton.getHeight() * buttonScale);
////        resumeButton.setPosition(
////            (WORLD_WIDTH - resumeButton.getWidth()) / 2,
////            startY
////        );
//        resumeButton.setPosition(
//            (WORLD_WIDTH /10),
//            startY
//        );
//        resumeButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(previousScreen);
//            }
//        });
//
//        // Save button (functionality to be added later)
//        Image saveButton = new Image(saveButtonTexture);
//        saveButton.setSize(saveButton.getWidth() * buttonScale, saveButton.getHeight() * buttonScale);
//        saveButton.setPosition(
//            (WORLD_WIDTH /10),
//            startY - (resumeButton.getHeight() + buttonSpacing)
//        );
//        saveButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // Save functionality to be implemented
//            }
//        });
//
//        // Exit button
//        Image exitButton = new Image(exitButtonTexture);
//        exitButton.setSize(exitButton.getWidth() * buttonScale, exitButton.getHeight() * buttonScale);
//        exitButton.setPosition(
//            WORLD_WIDTH /10,
//            startY - 2 * (exitButton.getHeight() + buttonSpacing)
//        );
//        exitButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new MainMenu(game));
//            }
//        });
//
//        // Add buttons to stage
//        stage.addActor(resumeButton);
//        stage.addActor(saveButton);
//        stage.addActor(exitButton);
//    }
private void setupUI() {
    // Background
    Image background = new Image(backgroundTexture);
    background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
    stage.addActor(background);

    // Button scaling and spacing
    float buttonScale = 2.5f; // Increase button scale for larger size
    float buttonSpacing = 80f; // Increase spacing between buttons
    float buttonY = (WORLD_HEIGHT / 2) - 100; // Slightly below center

    // Resume button
    Image resumeButton = new Image(resumeButtonTexture);
    resumeButton.setSize(resumeButton.getWidth() * buttonScale, resumeButton.getHeight() * buttonScale);
    resumeButton.setPosition(
        (WORLD_WIDTH - (3 * resumeButton.getWidth() + 2 * buttonSpacing)) / 2,
        buttonY
    );
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
        buttonY
    );
    saveButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            // Save functionality to be implemented
        }
    });

    // Exit button
    Image exitButton = new Image(exitButtonTexture);
    exitButton.setSize(exitButton.getWidth() * buttonScale, exitButton.getHeight() * buttonScale);
    exitButton.setPosition(
        saveButton.getX() + saveButton.getWidth() + buttonSpacing,
        buttonY
    );
    exitButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new MainMenu(game));
        }
    });

    // Add buttons to stage
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
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        resumeButtonTexture.dispose();
        saveButtonTexture.dispose();
        exitButtonTexture.dispose();
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
