package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    public RedBird(Main game, float x, float y, World world) {
        super(game, x, y, world);
    }

    @Override
    public void loadTexture() {
        birdTexture = game.assets.get("red_bird.png", Texture.class);
        updateTexture();
    }

    @Override
    public void triggerSpecialAbility() {
        if (body != null && isLaunched && !specialAbilityUsed) {
            float centerX = body.getPosition().x;
            float centerY = body.getPosition().y;

            setSize(getWidth() * 2, getHeight() * 2);

            for (Fixture fixture : body.getFixtureList()) {
                CircleShape shape = (CircleShape) fixture.getShape();
                shape.setRadius(shape.getRadius() * 2);
            }

            body.resetMassData();
            body.setTransform(centerX, centerY, body.getAngle());
        }
    }
}
