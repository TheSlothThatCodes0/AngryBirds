package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import static com.angryBirds.Main.PPM;

public class Ground extends Block {

    public Ground(Main game,String material, float width, float height, World world) {
        super(game, 1920 / PPM, 50,world);
        setPosition(width, height);
        float density = 100000.0f;
        float friction = 0.3f;
        float restitution = 0.1f;
        createPhysicsBody(width, height, density, friction, restitution,"ground");
        loadTexture(material);
    }

    @Override
    protected void loadTexture(String material) {
        blockTexture = game.assets.get("ground.png", Texture.class);
        updateTexture();  // Make sure this is called after setting the texture
//        switch (material.toLowerCase()) {
//            case "stone":
//                blockTexture = game.assets.get("s_b.png", Texture.class);
//                break;
//            case "wood":
//                blockTexture = game.assets.get("w_b.png", Texture.class);
//                break;
//            case "ice":
//                blockTexture = game.assets.get("g_b.png", Texture.class);
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid material: " + material);
//        }
//        updateTexture();  // Make sure this is called after setting the texture
    }
}
