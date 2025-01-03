package com.angryBirds.Screens;

import com.angryBirds.Levels.*;
import com.angryBirds.Main;
import com.angryBirds.Utils.CustomButton;
import com.angryBirds.Utils.SaveData;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Constructor;

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
    private Texture portal_1;
    private Texture portal_2;
    private Texture portal_3;

    private Texture save_portal_T;

    private Image background;
    private Image launch1;
    private Image launch2;
    private Image portal_I1;
    private Image portal_I2;
    private Image portal_I3;
    private Image save_portal;
    private Texture exitButtonTexture;
    private Texture exitButtonTexture_Clicked;
    private Rectangle exitBounds;
    private final float EXIT_SIZE = 100;
    private boolean isExitHovered = false;
    private CustomButton exitButton;
    private Screen PrevScreen;

    private float animationTime = 0;
    private float floatAmplitude = 10f;
    private float floatSpeed = 2f;
    private float baseY1, baseY2, baseY3;

    private float phase1 = 0f;
    private float phase2 = (float) (Math.PI * 2 / 3);
    private float phase3 = (float) (Math.PI * 4 / 3);

    private Image savedGamePortal;
    private float baseSaveY;
    private float phaseSave = (float) (Math.PI); 

    public Levels_Screen(Main game, Screen prev) {
        this.game = game;
        this.PrevScreen = prev;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        exitButtonTexture = game.assets.get("backArrow.png", Texture.class);
        exitButtonTexture_Clicked = game.assets.get("backArrow.png", Texture.class);
        exitBounds = new Rectangle(10, WORLD_HEIGHT - EXIT_SIZE - 10, EXIT_SIZE, EXIT_SIZE);

        exitButton = new CustomButton(game, exitBounds, exitButtonTexture, exitButtonTexture_Clicked, exitButtonTexture,
                () -> {
                    game.setScreen(prev);
                });

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        loadTextures();
        setupStage();
        stage.addActor(exitButton);
    }

    private SaveData loadSavedGameFile() {
        try {
            FileHandle file = Gdx.files.local("save.json");
            if (file.exists()) {
                String json = file.readString();
                return new Json().fromJson(SaveData.class, json);
            }
        } catch (Exception e) {
            System.out.println("Error loading save: " + e.getMessage());
        }
        return null;
    }

    private void loadTextures() {
        backgroundTexture = new Texture("level_background1.jpg");
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
        portal_1 = new Texture("portal1.png");
        portal_2 = new Texture("portal2.png");
        portal_3 = new Texture("portal3.png");
        save_portal_T = new Texture("save_portal.png");
    }

    private void setupStage() {
        background = new Image(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        launch1 = new Image(launcher1);
        launch1.setPosition(240, 200);
        launch1.setScaling(Scaling.fit);

        launch2 = new Image(launcher2);
        launch2.setPosition(327, 340);
        launch2.setScaling(Scaling.fit);

        portal_I1 = new Image(portal_3);
        portal_I1.setPosition(1500, 250);
        portal_I1.setScaling(Scaling.fit);

        portal_I1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLevel1();
            }
        });

        portal_I2 = new Image(portal_2);
        portal_I2.setPosition(1100, 300 + launch1.getHeight() - 100);
        portal_I2.setScaling(Scaling.fit);

        portal_I2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLevel2();
            }
        });

        portal_I3 = new Image(portal_1);
        portal_I3.setPosition(600, 300 + launch1.getHeight() + 125);
        portal_I3.setScaling(Scaling.fit);

        portal_I3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLevel3();
            }
        });

        float pw = portal_I1.getWidth();
        float ph = portal_I1.getHeight();
        float sf_p = 0.4f;
        portal_I1.setSize(pw * sf_p, ph * sf_p);
        portal_I2.setSize(pw * sf_p, ph * sf_p);
        portal_I3.setSize(pw * sf_p, ph * sf_p);

        float commonBaseY = 300 + launch1.getHeight() - 100;
        baseY1 = commonBaseY;
        baseY2 = commonBaseY;
        baseY3 = commonBaseY;

        portal_I1.setPosition(1600, commonBaseY);
        portal_I2.setPosition(1200, commonBaseY);
        portal_I3.setPosition(800, commonBaseY);

        stage.addActor(background);
        stage.addActor(launch2);
        stage.addActor(launch1);
        stage.addActor(portal_I1);
        stage.addActor(portal_I2);
        stage.addActor(portal_I3);

        SaveData savedGame = loadSavedGameFile();
        if (savedGame != null) {
            savedGamePortal = new Image(save_portal_T);
            savedGamePortal.setPosition(1200, 250);
            savedGamePortal.setScaling(Scaling.fit);

            savedGamePortal.setSize(pw * sf_p, ph * sf_p);

            savedGamePortal.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new Saved_Level(game));
                }
            });

            // Set the base Y position
            baseSaveY = 250;
            
            stage.addActor(savedGamePortal);
        } else {
            System.out.println("No saved game found");
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        animationTime += delta;

        float offsetY1 = floatAmplitude * (float) Math.sin(animationTime * floatSpeed + phase1);
        float offsetY2 = floatAmplitude * (float) Math.sin(animationTime * floatSpeed + phase2);
        float offsetY3 = floatAmplitude * (float) Math.sin(animationTime * floatSpeed + phase3);

        portal_I1.setY(baseY1 + offsetY1);
        portal_I2.setY(baseY2 + offsetY2);
        portal_I3.setY(baseY3 + offsetY3);

        if (savedGamePortal != null) {
            float offsetSave = floatAmplitude * (float) Math.sin(animationTime * floatSpeed + phaseSave);
            savedGamePortal.setY(baseSaveY + offsetSave);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
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
    public void resize(int width, int height) {
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
    }

    private void createLevel2() {
        game.setScreen(new Level_2(game, false));
    }

    private void createLevel3() {
        game.setScreen(new Level_3(game, false));
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        ground_shape.dispose();
        launcher1.dispose();
        launcher2.dispose();
        portal_1.dispose();
        portal_2.dispose();
        portal_3.dispose();
    }
}
