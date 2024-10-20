package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class Triangle extends Block {

    public Triangle(Main game, String material) {
        super(game, 120, 100);  // Triangle dimensions (can be adjusted)
        loadTexture(material);
    }

    @Override
    protected void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("s_t.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("w_t.png", Texture.class);
                break;
            case "ice":
                blockTexture = game.assets.get("g_t.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }
}
