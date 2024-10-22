package com.angryBirds.Pigs;

import com.angryBirds.Main;

public class NormalPig extends Pig {

    public NormalPig(Main game, String type,float x,float y) {
        super(game, getWidth(type), getHeight(type));
        loadTexture(getTexture(type));
        setPosition(x,y);
    }

    private static float getWidth(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 100;
        } else if (type.equalsIgnoreCase("small")) {
            return 60;
        } else {
            throw new IllegalArgumentException("Invalid pig type: " + type);
        }
    }

    private static float getHeight(String type) {
        if (type.equalsIgnoreCase("big")) {
            return 100;
        } else if (type.equalsIgnoreCase("small")) {
            return 60;
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
