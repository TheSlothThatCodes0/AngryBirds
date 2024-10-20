package com.angryBirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Levels implements Screen {
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

    private Image background;
    private Image test_image;

    public Levels(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        loadTextures();
        setupStage();
    }

    private void loadTextures() {
        backgroundTexture = new Texture("level_background.png");
//        ground_shape = new Texture("ground_shape.png");
        launcher1 = new Texture("launcher1.png");
        launcher2 = new Texture("launcher2.png");
//        backgroundTexture = new Texture("game_background.png");
//        background = new Image(backgroundTexture);
//        background.setSize(800, 480);
//
//        test_image = new Image(launcher1);
////        test_image.setSize(100,100);
//        test_image.setPosition(50,50);
//        test_image.setScaling(Scaling.fit);
//
//        stage.addActor(background);
//        stage.addActor(test_image);
    }

    private void setupStage(){
        background = new Image(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        test_image = new Image(launcher1);
        test_image.setPosition(100, 200);
//        test_image.setSize(10,10);
        test_image.setScaling(Scaling.fit);

        stage.addActor(background);
        stage.addActor(test_image);
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

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        ground_shape.dispose();
        launcher1.dispose();
        launcher2.dispose();
    }
}


