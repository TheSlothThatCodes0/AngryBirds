package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class KingPig extends Pig {

    public KingPig(Main game,float x,float y) {
        super(game, 150, 150);  // King Pig size (adjust as needed)
        loadTexture();
    }

    protected void loadTexture() {
        super.loadTexture("pig3.png");  // load King Pig texture
    }
}
