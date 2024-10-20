package com.angryBirds.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.angryBirds.Main;

public abstract class Block {
    protected Main game;
    protected Texture blockTexture;
    protected Rectangle blockBounds;
    protected float blockWidth;
    protected float blockHeight;

    public Block(Main game, float width, float height) {
        this.game = game;
        this.blockWidth = width;
        this.blockHeight = height;

        // Set the block's initial position and bounds
        float initialX = 400;  // X position (you can adjust)
        float initialY = 200;  // Y position (you can adjust)
        blockBounds = new Rectangle(initialX, initialY, blockWidth, blockHeight);
    }

    // Abstract method to load textures based on material
    protected abstract void loadTexture(String material);

    public void render() {
        game.batch.begin();
        game.batch.draw(blockTexture, blockBounds.x, blockBounds.y, blockBounds.width, blockBounds.height);
        game.batch.end();
    }

    public void dispose() {
        blockTexture.dispose();
    }

    public void setPosition(float x, float y) {
        blockBounds.setPosition(x, y);
    }
}
