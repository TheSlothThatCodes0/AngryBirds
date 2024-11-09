
package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Bird extends Image {
    protected Main game;
    protected Texture birdTexture;
    protected float birdWidth = 100;
    protected float birdHeight = 100;
    private Body body;

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

    public float getBirdWidth(){
        return birdWidth;
    }

    public float getBirdHeight(){
        return birdHeight;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
