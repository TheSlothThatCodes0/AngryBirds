package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
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
            // Double current velocity
            Vector2 vel = body.getLinearVelocity();
            body.setLinearVelocity(vel.x * SPEED_MULTIPLIER, vel.y * SPEED_MULTIPLIER);
            specialAbilityUsed = true; // Ensure the ability is marked as used
            System.out.println("YellowBird special ability triggered: " + vel + " -> " + body.getLinearVelocity());
        }
    }
}