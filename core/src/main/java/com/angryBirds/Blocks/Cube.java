package com.angryBirds.Blocks;

import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class Cube extends Block {

    public Cube(Main game, String material) {
        super(game, 100, 100);  // Cube dimensions (100x100)
        loadTexture(material);
    }

    @Override
    protected void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("cube_stone.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("cube_wood.png", Texture.class);
                break;
            case "metal":
                blockTexture = game.assets.get("cube_metal.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }
}
