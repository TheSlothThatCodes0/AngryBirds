package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.angryBirds.Main.PPM;

public class BlueBird extends Bird {
    public BlueBird(Main game, float x, float y,World world) {
        super(game, x, y, world);
    }

    @Override
    public void loadTexture() {
        birdTexture = game.assets.get("blue_bird.png", Texture.class);
        updateTexture();
    }

    @Override
    public void triggerSpecialAbility() {
        if (body != null && isLaunched && !specialAbilityUsed) {
            // Create two additional birds
            Vector2 pos = body.getPosition();
            Vector2 vel = body.getLinearVelocity();

            // Create first copy going up at an angle
            BlueBird bird1 = new BlueBird(game, pos.x * PPM, pos.y * PPM, world);
            bird1.isLaunched = true;
            bird1.specialAbilityUsed = true;  
            float angle1 = 20f; // Degrees
            Vector2 vel1 = vel.cpy().rotate(angle1);
            bird1.getBody().setTransform(pos, 0);
            bird1.getBody().setLinearVelocity(vel1);
            bird1.getBody().setActive(true);

            // Create second copy going down at an angle
            BlueBird bird2 = new BlueBird(game, pos.x * PPM, pos.y * PPM, world);
            bird2.isLaunched = true;
            bird2.specialAbilityUsed = true;  
            float angle2 = -20f;
            Vector2 vel2 = vel.cpy().rotate(angle2);
            bird2.getBody().setTransform(pos, 0);
            bird2.getBody().setLinearVelocity(vel2);
            bird2.getBody().setActive(true);

            // Add birds to stage
            getStage().addActor(bird1);
            getStage().addActor(bird2);
        }
    }

}
