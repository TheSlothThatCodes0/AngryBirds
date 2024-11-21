package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class BlueBird extends Bird {
    public BlueBird(Main game, float x, float y,World world) {
        super(game, x, y, world);
    }

    @Override
    public void loadTexture() {
        birdTexture = game.assets.get("blue_bird.png", Texture.class);
        updateTexture();
    }
}
