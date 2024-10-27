package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class BlueBird extends Bird {
    public BlueBird(Main game, float x, float y) {
        super(game, x, y);
    }

    @Override
    protected void loadTexture() {
        birdTexture = game.assets.get("blue_bird.png", Texture.class);
        updateTexture();
    }
}
