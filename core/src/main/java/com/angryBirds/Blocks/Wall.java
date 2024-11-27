package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.physics.box2d.World;

public class Wall extends Block {
    public Wall(Main game, float x, float y, World world) {
        super(game, 60, 1080, world, ""); // Set width and height for the right wall
        setPosition(x, y);

        float density = 1f;
        float friction = 0.4f;
        float restitution = 0.0f;

        createPhysicsBody(x, y, density, friction, restitution, "ground");
        setVisible(false); // Make the actor invisible
    }

    @Override
    public void loadTexture(String material) {
        // Empty implementation - no texture needed
    }
}
