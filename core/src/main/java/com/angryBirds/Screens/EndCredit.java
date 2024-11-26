package com.angryBirds.Screens;

import com.angryBirds.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.angryBirds.Utils.musicControl;

public class EndCredit implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private SpriteBatch batch;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;
    private musicControl mc;

    // Credits scrolling variables
    private String creditsText;
    private float creditsY;
    private final float SCROLL_SPEED = 100f; // pixels per second

    public EndCredit(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        font = new BitmapFont();
        font.getData().setScale(3);
        font.setColor(Color.WHITE);
        this.mc = musicControl.getInstance();
        mc.crossFade("audio/dramatic.mp3",0.0f);
        batch = new SpriteBatch();

        creditsText = "Credits\n\n" +
                      "Game Design: John Doe\n" +
                      "Programming: Jane Smith\n" +
                      "Art: Alice Johnson\n" +
                      "Music: Bob Brown\n\n" +
                      "Thank you for playing!\n" +
            "This game was created for educational purposes only.\n"
            + "All rights to the Angry Birds franchise belong to Rovio Entertainment Corporation.\n"
            + "This game is not affiliated with or endorsed by Rovio Entertainment Corporation.\n"
            + "Angry Birds is a trademark of Rovio Entertainment Corporation.\n"
            + "This game was created by me all by me.\n"
            + "I am the best game developer in the world.\n"
            + "I am the best game developer in the world.\n"
            + "I am the best game developer in the world.\n"
            + "The cost of creating this was my sanity.\n";

        // Start the credits text just below the bottom of the screen
        creditsY = -WORLD_HEIGHT / 10;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Update the position of the credits text
        creditsY += SCROLL_SPEED * delta;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, creditsText, 0, creditsY, WORLD_WIDTH, Align.center, true);
        batch.end();

        if(creditsY > 2*WORLD_HEIGHT){
            game.setScreen(new MainMenu(game));
            mc.crossFade("audio/theme_1.mp3",0.0f);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
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
