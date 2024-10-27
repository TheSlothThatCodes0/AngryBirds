package com.angryBirds.Screens;

import com.angryBirds.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WinScreen implements Screen{
    private Main game;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    private Texture backgroundTexture;
    private Image background;

    protected Texture levelSelectTexture;
    protected ImageButton levelSelectButton;

    public WinScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        loadTextures();
        setupStage();
        loadNavigationButtonTextures();
        setupNavigationButtons();
    }

    private void loadTextures() {
        backgroundTexture = new Texture("winScreen.png");

    }

    private void setupStage(){
        background = new Image(backgroundTexture);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        stage.addActor(background);
    }

    private void loadNavigationButtonTextures() {
        try {
            levelSelectTexture = new Texture("check_square_color_cross.png");

        } catch (Exception e) {
            Gdx.app.error("BaseLevel", "Error loading navigation button textures", e);
        }
    }

    private void setupNavigationButtons() {
        float padding = 20f;

        ImageButton.ImageButtonStyle levelSelectStyle = new ImageButton.ImageButtonStyle();
        levelSelectStyle.imageUp = new TextureRegionDrawable(new TextureRegion(levelSelectTexture));

        levelSelectButton = new ImageButton(levelSelectStyle);

        float buttonScale = 1f;
        levelSelectButton.setSize(levelSelectTexture.getWidth() * buttonScale,
            levelSelectTexture.getHeight() * buttonScale);

        levelSelectButton.setPosition(WORLD_WIDTH-2*padding, WORLD_HEIGHT - levelSelectButton.getHeight() - padding);

        stage.addActor(levelSelectButton);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(v);
        stage.draw();
        if (levelSelectButton.isPressed()) {
            game.setScreen(new MainMenu(game));
        }
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
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
    }
}
