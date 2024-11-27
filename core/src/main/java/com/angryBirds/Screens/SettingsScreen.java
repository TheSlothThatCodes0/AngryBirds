package com.angryBirds.Screens;

import com.angryBirds.Main;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingsScreen implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private float WORLD_WIDTH = 1920;
    private float WORLD_HEIGHT = 1080;

    private Texture woodBoardTexture;
    private Texture backButton;
    private Texture creditsButton;
    private Rectangle backBounds;
    private Rectangle creditsBounds;
    private Rectangle sliderBar;
    private Rectangle sliderKnob;
    private final float SLIDER_ROTATION = 0;
    private Vector2 sliderCenter;
    private float volume = 1.0f;
    private boolean isDraggingSlider = false;
    private final float BUTTON_SIZE = 100;
    private final float SLIDER_WIDTH = 600;
    private final float SLIDER_HEIGHT = 25;
    private final float KNOB_SIZE = 40;
    private final float CREDITS_BUTTON_WIDTH = 100;
    private final float CREDITS_BUTTON_HEIGHT = 100;

    private musicControl mc;

    public SettingsScreen(Main game) { //  constructor
        this.game = game;

        this.mc = musicControl.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2);

        woodBoardTexture = game.assets.get("settingsScreenBG1.jpg", Texture.class);
        backButton = game.assets.get("backArrow.png", Texture.class);
        creditsButton = game.assets.get("woodButton.png", Texture.class);

        backBounds = new Rectangle(10, WORLD_HEIGHT - BUTTON_SIZE - 10,  // setting the area for click detection
            BUTTON_SIZE, BUTTON_SIZE);
        creditsBounds = new Rectangle((WORLD_WIDTH - CREDITS_BUTTON_WIDTH) / 2,  // setting the area for click detection
            WORLD_HEIGHT * 0.45f, CREDITS_BUTTON_WIDTH, CREDITS_BUTTON_HEIGHT);

        float sliderX = (WORLD_WIDTH - SLIDER_WIDTH) / 2;
        float sliderY = WORLD_HEIGHT * 0.6f;
        sliderBar = new Rectangle(sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT); // setting the area for click detection
        sliderKnob = new Rectangle(sliderX + (SLIDER_WIDTH * volume) - (KNOB_SIZE/2), // setting the area for click detection
            sliderY - (KNOB_SIZE - SLIDER_HEIGHT)/2, KNOB_SIZE, KNOB_SIZE);

        sliderCenter = new Vector2(sliderX + SLIDER_WIDTH/2, sliderY + SLIDER_HEIGHT/2);  // vloume slider
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(woodBoardTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        game.batch.draw(backButton, backBounds.x, backBounds.y,
            backBounds.width, backBounds.height);
        game.batch.draw(creditsButton, creditsBounds.x , creditsBounds.y,
            creditsBounds.width, creditsBounds.height);

        font.draw(game.batch, "Volume: " + (int)(volume * 100) + "%", // vloume slider bar
            sliderBar.x, sliderBar.y + 70);

        game.batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(0.4f, 0.2f, 0.1f, 1)); // Dark brown
        shapeRenderer.translate(sliderCenter.x, sliderCenter.y, 0);
        shapeRenderer.rotate(0, 0, 1, SLIDER_ROTATION);  // roatting the slider if neccesary( depends upon the asset)
        shapeRenderer.rect(-SLIDER_WIDTH/2, -SLIDER_HEIGHT/2, SLIDER_WIDTH, SLIDER_HEIGHT);

        shapeRenderer.setColor(new Color(0.6f, 0.3f, 0.1f, 1)); // Lighter brown
        float knobRelativeX = (sliderKnob.x - sliderCenter.x) + (KNOB_SIZE/2);
        shapeRenderer.rect(knobRelativeX - (KNOB_SIZE/2), -KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);

        shapeRenderer.identity();

        shapeRenderer.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            Vector2 rotatedPoint = rotatePoint(
                new Vector2(touchPos.x, touchPos.y),
                sliderCenter,
                SLIDER_ROTATION
            );

            if (backBounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new MainMenu(game));
            } else if (creditsBounds.contains(touchPos.x, touchPos.y)) {
                 game.setScreen(new EndCredit(game)); // Will be implemented later
            } else if (containsInRotatedSpace(sliderKnob, rotatedPoint)) {
                isDraggingSlider = true;
            }
        }

        if (Gdx.input.isTouched() && isDraggingSlider) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            Vector2 rotatedPoint = rotatePoint(
                new Vector2(touchPos.x, touchPos.y),
                sliderCenter,
                SLIDER_ROTATION
            );

            float newX = rotatedPoint.x - (sliderKnob.width / 2);
            newX = Math.max(sliderBar.x, Math.min(sliderBar.x + sliderBar.width - sliderKnob.width, newX));
            sliderKnob.x = newX;

            volume = (sliderKnob.x - sliderBar.x) / (sliderBar.width - sliderKnob.width);
            volume = Math.max(0, Math.min(1, volume));

            volume = (sliderKnob.x - sliderBar.x) / (sliderBar.width - sliderKnob.width);
            volume = Math.max(0, Math.min(1, volume));
            mc.setVolume(volume);
            Gdx.app.getPreferences("GamePrefs").putFloat("masterVolume", volume).flush();

            Gdx.app.getPreferences("GamePrefs").putFloat("masterVolume", volume).flush();
        }

        if (!Gdx.input.isTouched()) {
            isDraggingSlider = false;
        }
    }

    private Vector2 rotatePoint(Vector2 point, Vector2 center, float degrees) {
        float rad = (float) Math.toRadians(-degrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        Vector2 translated = new Vector2(point.x - center.x, point.y - center.y);
        Vector2 rotated = new Vector2(
            translated.x * cos - translated.y * sin,
            translated.x * sin + translated.y * cos
        );

        return new Vector2(rotated.x + center.x, rotated.y + center.y);
    }

    private boolean containsInRotatedSpace(Rectangle rect, Vector2 point) {
        return rect.contains(point.x, point.y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }

    @Override
    public void show() {
        volume = Gdx.app.getPreferences("GamePrefs").getFloat("masterVolume", 1.0f);
        sliderKnob.x = sliderBar.x + (sliderBar.width - sliderKnob.width) * volume;
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
