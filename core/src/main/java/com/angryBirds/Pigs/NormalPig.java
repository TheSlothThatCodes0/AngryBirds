package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.physics.box2d.World;

public class NormalPig extends Pig {

    private static final float BIG_PIG_HEALTH = 200f;
    private static final float SMALL_PIG_HEALTH = 100f;

    public NormalPig(Main game, String type, float x, float y, World world) {
        super(game, getWidth(type), getHeight(type), world, x, y);

        if (type.equalsIgnoreCase("big")) {
            this.health = BIG_PIG_HEALTH;
            this.maxHealth = BIG_PIG_HEALTH;
        } else if (type.equalsIgnoreCase("small")) {
            this.health = SMALL_PIG_HEALTH;
            this.maxHealth = SMALL_PIG_HEALTH;
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }

        loadTexture(getTexture(type));
    }

    private static float getWidth(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 80;  //100
        } else if (type.equalsIgnoreCase("small")) {
            return 40;   //60
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }

    private static float getHeight(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 80;  //100
        } else if (type.equalsIgnoreCase("small")) {
            return 40;   //60
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }

    private static String getTexture(String type) {
        if (type.equalsIgnoreCase("big")) {
            return "pig1.png";
        } else if (type.equalsIgnoreCase("small")) {
            return "pig2.png";
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }
}
