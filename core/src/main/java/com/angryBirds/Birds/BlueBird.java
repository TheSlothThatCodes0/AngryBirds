package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class BlueBird extends Bird {

    public BlueBird(Main game) {
        super(game);
        loadTexture();
    }

    @Override
    protected void loadTexture() {
        birdTexture = game.assets.get("blue_bird.png", Texture.class);
    }
}
