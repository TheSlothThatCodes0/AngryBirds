package com.angryBirds.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.angryBirds.Main;

public abstract class Pig {
    protected Main game;
    protected Texture pigTexture;
    protected Rectangle pigBounds;
    protected float pigWidth;
    protected float pigHeight;

    public Pig(Main game, float width, float height) {
        this.game = game;
        this.pigWidth = width;
        this.pigHeight = height;

        // Set the pig's initial position and bounds
        float initialX = 300;  // X position (you can adjust)
        float initialY = 200;  // Y position (you can adjust)
        pigBounds = new Rectangle(initialX, initialY, pigWidth, pigHeight);
    }

    // Concrete method to load the texture
    protected void loadTexture(String textureFile) {
        pigTexture = game.assets.get(textureFile, Texture.class);
    }

    public void render() {
        game.batch.begin();
        game.batch.draw(pigTexture, pigBounds.x, pigBounds.y, pigBounds.width, pigBounds.height);
        game.batch.end();
    }

    public void dispose() {
        pigTexture.dispose();
    }

    public void setPosition(float x, float y) {
        pigBounds.setPosition(x, y);
    }
}
