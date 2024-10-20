package com.angryBirds.Pigs;

import com.angryBirds.Main;

public class NormalPig extends Pig {

    public NormalPig(Main game, String type) {
        super(game, getWidth(type), getHeight(type));  // Set size based on type
        loadTexture(getTexture(type));  // Load texture based on type
    }

    private static float getWidth(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 100;  // Big pig width
        } else if (type.equalsIgnoreCase("small")) {
            return 60;   // Small pig width
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }

    private static float getHeight(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 100;  // Big pig height
        } else if (type.equalsIgnoreCase("small")) {
            return 60;   // Small pig height
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }

    private static String getTexture(String type) {
        if (type.equalsIgnoreCase("big")) {
            return "big_pig.png";  // Big pig texture
        } else if (type.equalsIgnoreCase("small")) {
            return "small_pig.png";  // Small pig texture
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }
}
