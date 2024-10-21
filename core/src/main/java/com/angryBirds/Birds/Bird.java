//package com.angryBirds.Birds;
//
//
//import com.angryBirds.Main;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import java.awt.*;
//
//public abstract class Bird extends Image {
//    protected Main game;
//    protected Texture birdTexture;
////    protected Rectangle birdBounds;
//    protected float birdWidth = 100;
//    protected float birdHeight = 100;
//
//    public Bird(Main game) {
//        super();
//        this.game = game;
//        loadTexture();
//        setSize(birdWidth, birdHeight);
//        setPosition(200, 200);
//        // Set the bird's initial position and bounds
////        float initialX = 200;  // X position (you can adjust)
////        float initialY = 200;  // Y position (you can adjust)
////        birdBounds = new Rectangle(initialX, initialY, birdWidth, birdHeight);
//    }
//
//    // Abstract method to be implemented by each specific bird type
//    protected abstract void loadTexture();
//
//    protected void updateTexture() {
//        setDrawable(new TextureRegionDrawable(birdTexture));
//    }
//
////    public void render() {
////        game.batch.begin();
////        game.batch.draw(birdTexture, birdBounds.x, birdBounds.y, birdBounds.width, birdBounds.height);
////        game.batch.end();
////    }
//
//    public void dispose() {
//        birdTexture.dispose();
//    }
//
////    public void setPosition(float x, float y) {
////        birdBounds.setPosition(x, y);
////    }
//}
package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Bird extends Image {
    protected Main game;
    protected Texture birdTexture;
    protected float birdWidth = 100;
    protected float birdHeight = 100;

    public Bird(Main game, float x, float y) {
        super();
        this.game = game;
        loadTexture();
        setSize(birdWidth, birdHeight);
        setPosition(x, y);
    }

    protected abstract void loadTexture();

    protected void updateTexture() {
        setDrawable(new TextureRegionDrawable(birdTexture));
    }

    public void dispose() {
        birdTexture.dispose();
    }
}
