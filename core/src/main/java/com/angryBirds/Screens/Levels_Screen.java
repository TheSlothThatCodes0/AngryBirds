package com.angryBirds.Screens;

import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Levels.Level_1;
import com.angryBirds.Main;
import com.angryBirds.Utils.CustomButton;
import com.angryBirds.Utils.SaveData;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Constructor;

import static com.angryBirds.Levels.BaseLevel.loadSavedGameFile;

public class Levels_Screen implements Screen {
    private Main game;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;

    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    private Texture backgroundTexture;
    private Texture ground_shape;
    private Texture launcher1;
    private Texture launcher2;
    private Texture rbird;
    private Texture portal_T;

    private Image background;
    private Image launch1;
    private Image launch2;
    private Image redBird;
    private Image portal_I1;
    private Image portal_I2;
    private Image portal_I3;
    private Texture exitButtonTexture;
    private Texture exitButtonTexture_Clicked;
    private Rectangle exitBounds;
    private final float EXIT_SIZE = 100;
    private boolean isExitHovered = false;
    private CustomButton exitButton;
    private Screen PrevScreen;


    public Levels_Screen(Main game, Screen prev) { // constructor
        this.game = game;
        this.PrevScreen = prev;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        exitButtonTexture = game.assets.get("exitButton1.png", Texture.class);
        exitButtonTexture_Clicked = game.assets.get("exitButton1_pressed.png", Texture.class);
        exitBounds = new Rectangle(10, WORLD_HEIGHT - EXIT_SIZE - 10, EXIT_SIZE, EXIT_SIZE);

        exitButton = new CustomButton(game, exitBounds, exitButtonTexture, exitButtonTexture_Clicked, exitButtonTexture, () -> {
            game.setScreen(prev);
        });

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        loadTextures();
        setupStage();
        stage.addActor(exitButton); // Add exit button to the stage

//
//        mc.crossFade("audio/theme_1.mp3",0.3f);
    }

    private void loadTextures() { // loading texture files
        backgroundTexture = new Texture("level_background1.jpg");
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
        portal_T = new Texture("portal.png");
        rbird = new Texture("red_bird.png");
    }

    private void setupStage() { // wrapping textures to images and setting up images
        background = new Image(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        launch1 = new Image(launcher1);
        launch1.setPosition(240, 200);
        launch1.setScaling(Scaling.fit);

        launch2 = new Image(launcher2);
        launch2.setPosition(327, 340);
        launch2.setScaling(Scaling.fit);

        redBird = new Image(rbird);
        redBird.setPosition(320, 400);
        redBird.setScaling(Scaling.fit);

        portal_I1 = new Image(portal_T);
        portal_I1.setPosition(1500, 250);
        portal_I1.setScaling(Scaling.fit);

        portal_I1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {// event listener for button
                createLevel1();
            }
        });

        portal_I2 = new Image(portal_T);
        portal_I2.setPosition(1100, 300 + launch1.getHeight() - 100);
        portal_I2.setScaling(Scaling.fit);

        portal_I3 = new Image(portal_T);
        portal_I3.setPosition(600, 300 + launch1.getHeight() + 125);
        portal_I3.setScaling(Scaling.fit);

        float rw = redBird.getWidth();
        float rh = redBird.getHeight();
        float sf = 0.4f;
        redBird.setSize(rw * sf, rh * sf);

        float pw = portal_I1.getWidth();
        float ph = portal_I1.getHeight();
        float sf_p = 0.4f;
        portal_I1.setSize(pw * sf_p, ph * sf_p);
        portal_I2.setSize(pw * sf_p, ph * sf_p);
        portal_I3.setSize(pw * sf_p, ph * sf_p);

        stage.addActor(background);
        stage.addActor(launch2);
        stage.addActor(redBird);
        stage.addActor(launch1);
        stage.addActor(portal_I1);
        stage.addActor(portal_I2);
        stage.addActor(portal_I3);

        // Add saved game portal if save exists
        SaveData savedGame = loadSavedGameFile();
        if (savedGame != null) {
            Image savedGamePortal = new Image(portal_T);
            savedGamePortal.setPosition(900, 250);
            savedGamePortal.setScaling(Scaling.fit);

            savedGamePortal.setSize(pw * sf_p, ph * sf_p);

            savedGamePortal.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    loadSavedGame();
                }
            });

            stage.addActor(savedGamePortal);
        } else {
            System.out.println("No saved game found");
        }
    }

    private void loadSavedGame() {
        SaveData savedGame = BaseLevel.loadSavedGameFile();
        if (savedGame != null) {
            try {
                // Create the level instance with loadingFromSave = true
                Class<?> levelClass = Class.forName("com.angryBirds.Levels." + savedGame.levelName);
                Constructor<?> constructor = levelClass.getConstructor(Main.class, boolean.class);
                BaseLevel level = (BaseLevel) constructor.newInstance(game, true);
                level.loadFromSaveData(savedGame);
                game.setScreen(level);
            } catch (Exception e) {
                System.out.println("Error loading level: " + e.getMessage());
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // for clearing up the previous stage
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        isExitHovered = exitBounds.contains(mousePos.x, mousePos.y);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        exitButton.workHover(game, isExitHovered, mousePos);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            exitButton.workClick(game, touchPos);
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { // full screen
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    private void createLevel1() {
        game.setScreen(new Level_1(game, false));
    } // function for creating the new level

    @Override
    public void dispose() { // disposing the textures and stage
        stage.dispose();
        backgroundTexture.dispose();
        ground_shape.dispose();
        launcher1.dispose();
        launcher2.dispose();
        portal_T.dispose();
    }
}
