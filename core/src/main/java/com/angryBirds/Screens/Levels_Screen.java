package com.angryBirds.Screens;

import com.angryBirds.Levels.Level_1;
import com.angryBirds.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Levels_Screen implements Screen {
    private Main game;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;

    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    // textures
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


    public Levels_Screen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        loadTextures();
        setupStage();
    }

    private void loadTextures() {
        backgroundTexture = new Texture("level_background1.jpg");
//        ground_shape = new Texture("ground_shape.png");
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
        portal_T = new Texture("portal.png");
        rbird = new Texture("red_bird.png");
//        backgroundTexture = new Texture("game_background.png");
//        background = new Image(backgroundTexture);
//        background.setSize(800, 480);
//
//        launch1 = new Image(launcher1);
////        launch1.setSize(100,100);
//        launch1.setPosition(50,50);
//        launch1.setScaling(Scaling.fit);
//
//        stage.addActor(background);
//        stage.addActor(launch1);
    }

    private void setupStage(){
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
        portal_I1.setPosition(1500,250);
        portal_I1.setScaling(Scaling.fit);

        portal_I1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                createLevel1();
            }
        });

        portal_I2 = new Image(portal_T);
        portal_I2.setPosition(1100, 300 + launch1.getHeight() - 100);
        portal_I2.setScaling(Scaling.fit);

        portal_I3 = new Image(portal_T);
        portal_I3.setPosition(600, 300 + launch1.getHeight() + 125);
        portal_I3.setScaling(Scaling.fit);

        float rw = redBird.getWidth();  //for changing the size of red bird.
        float rh = redBird.getHeight();
        float sf = 0.4f;
        redBird.setSize(rw*sf, rh*sf);

        float pw = portal_I1.getWidth();
        float ph = portal_I1.getHeight();
        float sf_p = 0.4f;
        portal_I1.setSize(pw*sf_p,ph*sf_p);
        portal_I2.setSize(pw*sf_p, ph*sf_p);
        portal_I3.setSize(pw*sf_p, ph*sf_p);

        stage.addActor(background);
        stage.addActor(launch2);
        stage.addActor(redBird);
        stage.addActor(launch1);
        stage.addActor(portal_I1);
        stage.addActor(portal_I2);
        stage.addActor(portal_I3);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
//        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
////        game.batch.draw(launcher1,20,20,WORLD_WIDTH/10,WORLD_HEIGHT/9);
//        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
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
        game.setScreen(new Level_1(game));
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        ground_shape.dispose();
        launcher1.dispose();
        launcher2.dispose();
        portal_T.dispose();
    }
}


