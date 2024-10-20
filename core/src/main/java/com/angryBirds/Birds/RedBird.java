package com.angryBirds.Birds;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class RedBird extends Bird {

    public RedBird(Main game) {
        super(game);
        loadTexture();
    }

    @Override
    protected void loadTexture() {
        birdTexture = game.assets.get("red_bird.png", Texture.class);
    }
}
