package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class Bird {
    protected Main game;
    protected Texture birdTexture;
    protected Rectangle birdBounds;
    protected float birdWidth = 100;
    protected float birdHeight = 100;

    public Bird(Main game) {
        this.game = game;
        // Set the bird's initial position and bounds
        float initialX = 200;  // X position (you can adjust)
        float initialY = 200;  // Y position (you can adjust)
        birdBounds = new Rectangle(initialX, initialY, birdWidth, birdHeight);
    }

    // Abstract method to be implemented by each specific bird type
    protected abstract void loadTexture();

    public void render() {
        game.batch.begin();
        game.batch.draw(birdTexture, birdBounds.x, birdBounds.y, birdBounds.width, birdBounds.height);
        game.batch.end();
    }

    public void dispose() {
        birdTexture.dispose();
    }

    public void setPosition(float x, float y) {
        birdBounds.setPosition(x, y);
    }
}
