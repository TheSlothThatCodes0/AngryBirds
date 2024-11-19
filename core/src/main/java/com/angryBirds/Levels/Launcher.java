package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

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
    }

    private void setupInputListener() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 touchPoint = new Vector2(x, y);
                Actor hit = stage.hit(x, y, true);

                if (hit instanceof Bird && !((Bird) hit).isLaunched) {
                    Bird bird = (Bird) hit;

                    if (selectedBird == null) {
                        // First click - select and position bird
                        selectedBird = bird;
                        // Remove bird from current position
                        selectedBird.remove();

                        // Position bird between launcher parts
                        float initialX = launcherBase.getX() + launcherBase.getWidth() / 2 + BIRD_OFFSET_X;
                        float initialY = launcherBase.getY() + BIRD_OFFSET_Y;
                        selectedBird.setPosition(
                                initialX - selectedBird.getWidth() / 2,
                                initialY - selectedBird.getHeight() / 2);

                        // Disable physics completely
                        selectedBird.getBody().setActive(false);

                        // Update z-index order
                        launcherBase.setZIndex(1);
                        selectedBird.setZIndex(2);
                        launcherTop.setZIndex(3);

                        // Re-add bird to stage
                        stage.addActor(selectedBird);

                    } else if (hit == selectedBird) {
                        // Second click - start dragging
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
                    dragCurrent = new Vector2(x, y);
                    updateBirdPosition();
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

        // Update bird position directly to cursor position
        selectedBird.setPosition(
                dragCurrent.x - selectedBird.getWidth() / 2,
                dragCurrent.y - selectedBird.getHeight() / 2);

        // Update physics body's position
        selectedBird.getBody().setTransform(
                dragCurrent.x / PPM,
                dragCurrent.y / PPM,
                selectedBird.getBody().getAngle());

        // Remove launcher rotation for now
        launcherTop.setRotation(0);
    }

        private void launchBird() {
        if (selectedBird == null) return;
    
        // Calculate launch vector from current position to launch point
        Vector2 launchVector = new Vector2(
            launcherBase.getX() + launcherBase.getWidth()/2 + BIRD_OFFSET_X,
            launcherBase.getY() + BIRD_OFFSET_Y
        ).sub(dragCurrent);
        
        float force = Math.min(launchVector.len(), MAX_DRAG_DISTANCE) * FORCE_MULTIPLIER;
        float angle = launchVector.angle();
        
        float velocityX = force * MathUtils.cosDeg(angle) / PPM;
        float velocityY = force * MathUtils.sinDeg(angle) / PPM;
        
        CollisionHandler.enableDamage();
        selectedBird.launch(velocityX, velocityY);
        
        Gdx.app.log("Launcher", String.format("Bird launched with velocity (%.2f, %.2f)", 
                   velocityX, velocityY));
    }
}