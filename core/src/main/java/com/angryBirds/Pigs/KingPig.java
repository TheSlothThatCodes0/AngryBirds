package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.physics.box2d.World;

public class KingPig extends Pig {

    private static final float KING_PIG_HEALTH = 300f;

    public KingPig(Main game, float x, float y, World world) {
        super(game, 150, 150, world, x, y);
        this.health = KING_PIG_HEALTH;
        this.maxHealth = KING_PIG_HEALTH; 
        loadTexture();
    }

    protected void loadTexture() {
        super.loadTexture("pig3.png");
    }
}
