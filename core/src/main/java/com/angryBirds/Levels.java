package com.angryBirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Levels implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    // textures
    private Texture backgroundTexture;
    private Texture ground_shape;
    private Texture launcher1;
    private Texture launcher2;

    public Levels(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        loadTextures();
    }

    private void loadTextures() {
        backgroundTexture = new Texture("level_background.png");
        ground_shape = new Texture("ground_shape.png");
        launcher1 = new Texture("launcher1.png");
        launcher2 = new Texture("launcher2.png");
    }

    @Override
    public void show() {
            }

    @Override
    public void render(float delta) {
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        game.batch.end();
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
        backgroundTexture.dispose();
        ground_shape.dispose();
        launcher1.dispose();
        launcher2.dispose();
    }
}


