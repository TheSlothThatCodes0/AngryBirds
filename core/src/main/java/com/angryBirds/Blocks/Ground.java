package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import static com.angryBirds.Main.PPM;

public class Ground extends Block {
    public Ground(Main game, String material, float x, float y, World world) {
        super(game, 4000, 60, world, material);
        setPosition(x, y);

        float density = 1f;
        float friction = 0.4f;
        float restitution = 0.1f;

        createPhysicsBody(x, y, density, friction, restitution, "ground");
        setVisible(false);
    }

    @Override
    public void loadTexture(String material) {
    }
}
