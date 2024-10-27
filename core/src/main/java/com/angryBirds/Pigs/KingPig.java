package com.angryBirds.Pigs;

import com.angryBirds.Main;

public class KingPig extends Pig {

    public KingPig(Main game,float x,float y) {
        super(game, 150, 150);
        loadTexture();
    }

    protected void loadTexture() {
        super.loadTexture("pig3.png");
    }
}
