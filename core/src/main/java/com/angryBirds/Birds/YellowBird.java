package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
    private static final float YELLOW_SPEED_MULTIPLIER = 2.5f;
    private Vector2 lastPosition;
    private float timeSinceBoost = 0;

    public YellowBird(Main game, float x, float y, World world) {
        super(game, x, y, world);
    }

    @Override
    public void loadTexture() {
        birdTexture = game.assets.get("yellow_bird.png", Texture.class);
        updateTexture();
    }

    @Override
    public void triggerSpecialAbility() {
        if (body != null && isLaunched && !specialAbilityUsed) {
            // Get current velocity
            Vector2 currentVel = body.getLinearVelocity();

            lastPosition = new Vector2(body.getPosition());

            body.setAwake(true);
            body.setActive(true);

            float newVelX = currentVel.x * YELLOW_SPEED_MULTIPLIER;
            float newVelY = currentVel.y * YELLOW_SPEED_MULTIPLIER;

            body.setLinearVelocity(newVelX, newVelY);

            Vector2 impulse = new Vector2(newVelX, newVelY).scl(0.1f);
            body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

            body.setAngularVelocity(0);

            specialAbilityUsed = true;
            timeSinceBoost = 0;

            System.out.println("Boost applied - Position: " + lastPosition);
            System.out.println("New velocity set to: (" + newVelX + ", " + newVelY + ")");
        }
    }
}
