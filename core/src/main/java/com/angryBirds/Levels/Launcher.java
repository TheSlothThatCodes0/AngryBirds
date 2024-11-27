package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import static java.lang.Float.max;
import static java.lang.Float.min;

public class Launcher {
    private static final float MAX_DRAG_DISTANCE = 50f;
    private static final float FORCE_MULTIPLIER = 50f;
    private static final float PPM = 5f;
    private static final float BIRD_OFFSET_X = 15f; // Offset from launcher center
    private static final float BIRD_OFFSET_Y = 180f; // Height between launcher parts

    private final Vector2 launchPoint;
    private final World world;
    private final Stage stage;

    private final Image launcherBase;
    private final Image launcherTop;

    private Bird selectedBird;
    private Vector2 dragStart;
    private Vector2 dragCurrent;
    private boolean isDragging;

    private Sound catap;

    public Launcher(World world, Stage stage, Vector2 launchPoint, Texture launcher1, Texture launcher2) {
        this.world = world;
        this.stage = stage;
        this.launchPoint = launchPoint;

        float sf = 0.5f;
        launcherBase = new Image(launcher1);
        launcherBase.setSize(launcherBase.getWidth() * sf, launcherBase.getHeight() * sf);
        launcherBase.setPosition(255, 35);
        launcherBase.setZIndex(1); // Base layer

        launcherTop = new Image(launcher2);
        launcherTop.setSize(launcherTop.getWidth() * sf, launcherTop.getHeight() * sf);
        launcherTop.setPosition(300, 105);
        launcherTop.setZIndex(3); // Top layer

        stage.addActor(launcherBase);
        stage.addActor(launcherTop);

        setupInputListener();
        catap = Gdx.audio.newSound(Gdx.files.internal("audio/launch_sound.mp3"));
    }

    float temp_x,temp_y;

    private void setupInputListener() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 touchPoint = new Vector2(x, y);
                Actor hit = stage.hit(x, y, true);

                if (hit instanceof Bird && !((Bird) hit).isLaunched) {
                    Bird bird = (Bird) hit;

                    if (selectedBird == null) {
                        selectedBird = bird;
                        selectedBird.remove();

                        float initialX = launcherBase.getX() + launcherBase.getWidth() / 2 + BIRD_OFFSET_X;
                        float initialY = launcherBase.getY() + BIRD_OFFSET_Y;
                        selectedBird.setPosition(
                                initialX - selectedBird.getWidth() / 2,
                                initialY - selectedBird.getHeight() / 2);

                        temp_x = initialX - selectedBird.getWidth() / 2;
                        temp_y = initialY - selectedBird.getHeight() / 2;

                        selectedBird.getBody().setActive(false);

                        launcherBase.setZIndex(1);
                        selectedBird.setZIndex(2);
                        launcherTop.setZIndex(3);

                        stage.addActor(selectedBird);

                    } else if (hit == selectedBird) {
                        selectedBird.isDragging = true;
                        dragStart = touchPoint;
                        dragCurrent = touchPoint;
                        isDragging = true;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isDragging && selectedBird != null) {
                    if((((x-temp_x)*(x-temp_x) + (y-temp_y)*(y-temp_y))<160000) && y>80){
                        dragCurrent = new Vector2(x, y);
                        updateBirdPosition();
                    }
                    else {
                        float angle = MathUtils.atan2(y - temp_y, x - temp_x);
                        float boundaryX = temp_x + 400 * MathUtils.cos(angle);
                        float boundaryY = temp_y + 400 * MathUtils.sin(angle);

                        if (boundaryY < 80) {
                            boundaryY = 80;
                        }

                        dragCurrent = new Vector2(boundaryX, boundaryY);
                        updateBirdPosition();

                    }
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragging && selectedBird != null) {
                    selectedBird.isDragging = false;
                    selectedBird.getBody().setActive(true); // Enable physics
                    launchBird();
                    isDragging = false;
                    selectedBird = null;
                }
            }
        });
    }

    private void updateBirdPosition() {
        if (selectedBird == null)
            return;

        selectedBird.setPosition(
                dragCurrent.x - selectedBird.getWidth() / 2,
                dragCurrent.y - selectedBird.getHeight() / 2);

        selectedBird.getBody().setTransform(
                dragCurrent.x / PPM,
                dragCurrent.y / PPM,
                selectedBird.getBody().getAngle());

        launcherTop.setRotation(0);
    }

        private void launchBird() {
        if (selectedBird == null) return;

        Vector2 launchVector = new Vector2(
            launcherBase.getX() + launcherBase.getWidth()/2 + BIRD_OFFSET_X,
            launcherBase.getY() + BIRD_OFFSET_Y
        ).sub(dragCurrent);

        float force = Math.min(launchVector.len(), MAX_DRAG_DISTANCE) * FORCE_MULTIPLIER;
        float angle = launchVector.angle();

        float velocityX = force * MathUtils.cosDeg(angle) / PPM;
        float velocityY = force * MathUtils.sinDeg(angle) / PPM;

        CollisionHandler.enableDamage();
        catap.play(20.0f);
        selectedBird.launch(velocityX, velocityY);

        Gdx.app.log("Launcher", String.format("Bird launched with velocity (%.2f, %.2f)",
                   velocityX, velocityY));
    }
}
