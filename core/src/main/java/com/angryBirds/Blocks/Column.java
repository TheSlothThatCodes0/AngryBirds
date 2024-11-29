package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Column extends Block {

    public Column(Main game, String material, float x, float y, World world) {
        super(game, 25, 200, world, material);
        loadTexture(material);
        setPosition(x, y);
        setInitialHealth(material);
        float density = getDensity(material);
        float friction = getFriction(material);
        float restitution = getRestitution(material);

        createPhysicsBody(x, y, density, friction, restitution, "column");
    }

    public Column(Main game, String material, float x, float y, World world, float rotation) {
        super(game, 25, 200, world, material);
        loadTexture(material);
        setPosition(x, y);
        setInitialHealth(material);
        float density = getDensity(material);
        float friction = getFriction(material);
        float restitution = getRestitution(material);
        setRotation(rotation);
        createPhysicsBody(x, y, density, friction, restitution, "column");
        body.setTransform(body.getPosition(), (float) Math.toRadians(rotation));
    }



    private float getDensity(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                return 2.0f;
            case "wood":
                return 0.5f;
            case "ice":
                return 0.8f;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }

    private float getFriction(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                return 0.4f;
            case "wood":
                return 0.3f;
            case "ice":
                return 0.1f;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }

    private float getRestitution(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                return 0.1f;
            case "wood":
                return 0.2f;
            case "ice":
                return 0.3f;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
    }

    @Override
    public void loadTexture(String material) {
        switch (material.toLowerCase()) {
            case "stone":
                blockTexture = game.assets.get("sh_p.png", Texture.class);
                break;
            case "wood":
                blockTexture = game.assets.get("wh_p.png", Texture.class);
                break;
            case "ice":
                blockTexture = game.assets.get("gh_p.png", Texture.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid material: " + material);
        }
        updateTexture();
    }
}
