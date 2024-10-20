package com.angryBirds.Blocks;

import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class Cube extends Block {

    public Cube(Main game, String material) {
        super(game, 140, 140);  // Cube dimensions (100x100)
        loadTexture(material);
    }

    @Override
    protected void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("s_b.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("w_b.png", Texture.class);
                break;
            case "ice":
                blockTexture = game.assets.get("g_b.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }
}
