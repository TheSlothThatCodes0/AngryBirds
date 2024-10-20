package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class KingPig extends Pig {

    public KingPig(Main game) {
        super(game, 150, 150);  // King Pig size (adjust as needed)
        loadTexture();
    }

    protected void loadTexture() {
        pigTexture = game.assets.get("king_pig.png", Texture.class);  // Load King Pig texture
    }
}
