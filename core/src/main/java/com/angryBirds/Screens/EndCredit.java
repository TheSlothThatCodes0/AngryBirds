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
    private final float SCROLL_SPEED = 100f;

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
            "-------------------------------------\n" +
            "Student 1: Siddharth Yadav : 2023525\n" +
            "Student 2: Shaman Ranjan : 2023498\n" +
            "-------------------------------------\n\n" +
            "Thank you for playing!\n\n" +
            "This game was created as the final Project for the course Advanced Programming for the batch 2027.\n\n"
            + "This game is not affiliated with or endorsed by Rovio Entertainment Corporation.\n\n"
            + "Angry Birds is a trademark of Rovio Entertainment Corporation.\n\n"
            + "The cost of creating this was my sanity.\n\n"
            + "I hope you enjoyed the game\n\n"
            + "I really do not know what to write in the end Credits\n\n"
            + "Also junit sucks.\n\n\n\n\n\n\n\n\n\n\n\n" +
            "Bye.";

        creditsY = -WORLD_HEIGHT / 10;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

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
