package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class Plank extends Block {

    public Plank(Main game, String material) {
        super(game, 200, 50);
        loadTexture(material);
    }

    @Override
    protected void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("plank_stone.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("plank_wood.png", Texture.class);
                break;
            case "metal":
                blockTexture = game.assets.get("plank_metal.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }
}
