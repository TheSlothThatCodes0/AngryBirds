package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Triangle extends Block {

    public Triangle(Main game, String material,float x,float y, World world) {
        super(game, 80, 80, world, material); // 120 120
        loadTexture(material);
        setPosition(x,y);
        setInitialHealth(material);
        float density = 1.0f;
        float friction = 0.3f;
        float restitution = 0.1f;

        switch (material.toLowerCase()) {
            case "stone":
                density = 2.0f;
                friction = 0.4f;
                restitution = 0.1f;
                break;
            case "wood":
                density = 0.5f;
                friction = 0.3f;
                restitution = 0.2f;
                break;
            case "ice":
                density = 0.8f;
                friction = 0.1f;
                restitution = 0.3f;
                break;
        }

        createPhysicsBody(x, y, density, friction, restitution,"triangle");
    }

    public Triangle(Main game, String material,float x,float y, World world, float rotation) {
        super(game, 80, 80, world, material); 
        loadTexture(material);
        setPosition(x,y);
        setInitialHealth(material);
        setRotation(rotation);
        body.setTransform(body.getPosition(), (float) Math.toRadians(rotation));
        float density = 1.0f;
        float friction = 0.3f;
        float restitution = 0.1f;

        switch (material.toLowerCase()) {
            case "stone":
                density = 2.0f;
                friction = 0.4f;
                restitution = 0.1f;
                break;
            case "wood":
                density = 0.5f;
                friction = 0.3f;
                restitution = 0.2f;
                break;
            case "ice":
                density = 0.8f;
                friction = 0.1f;
                restitution = 0.3f;
                break;
        }

        createPhysicsBody(x, y, density, friction, restitution,"triangle");
    }

    @Override
    public void loadTexture(String material) {
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
        updateTexture();
    }
}
