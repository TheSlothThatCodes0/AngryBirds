package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.physics.box2d.World;

public class Wall extends Block {
    public Wall(Main game, float x, float y, World world) {
        super(game, 60f, 1080f, world,"ground");
        setPosition(x, y);

        float density = 1f;
        float friction = 0.4f;
        float restitution = 0.0f;

        createPhysicsBody(x, y, density, friction, restitution, "ground");
        setVisible(false);
    }

    @Override
    public void loadTexture(String material) {

    }
}
