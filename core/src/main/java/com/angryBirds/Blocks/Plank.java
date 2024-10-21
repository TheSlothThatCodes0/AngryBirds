package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;

public class Plank extends Block {

    public Plank(Main game, String material,float x, float y) {
        super(game, 300, 50);
        loadTexture(material);
        setPosition(x, y);
    }

    @Override
    protected void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("s_p.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("w_p.png", Texture.class);
                break;
            case "ice":
                blockTexture = game.assets.get("g_p.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
        updateTexture();
    }
}
