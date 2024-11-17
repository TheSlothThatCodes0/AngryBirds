package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import static com.angryBirds.Main.PPM;

public class Ground extends Block {
    public Ground(Main game, String material, float x, float y, World world) {
        super(game, 1920, 60, world);
        setPosition(x, y);
        
        float density = 1f;
        float friction = 0.4f;
        float restitution = 0.1f;
        
        createPhysicsBody(x, y, density, friction, restitution, "ground");
        // Remove loadTexture call
        setVisible(false); // Make the actor invisible
    }

    @Override
    protected void loadTexture(String material) {
        // Empty implementation - no texture needed
    }
}