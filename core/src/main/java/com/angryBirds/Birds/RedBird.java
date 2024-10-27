package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class RedBird extends Bird {
    public RedBird(Main game, float x, float y) {
        super(game, x, y);
    }

    @Override
    protected void loadTexture() {
        birdTexture = game.assets.get("red_bird.png", Texture.class);
        updateTexture();
    }
}
