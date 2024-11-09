package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Pig extends Image{
    protected Main game;
    protected Texture pigTexture;
    private Body body;

    public Pig(Main game, float width, float height) {
        super();
        this.game = game;
        setSize(width, height);
    }
    protected void loadTexture(String textureFile) {
        pigTexture= game.assets.get(textureFile, Texture.class);
        updateTexture();
    }

    protected void updateTexture() {
        setDrawable(new TextureRegionDrawable(pigTexture));
    }
    public void dispose() {
        pigTexture.dispose();
    }

    public float getPigWidth(){
        return getWidth();
    }

    public void setPigWidth(float width){
        setSize(width, getHeight());
    }

    public void setPigHeight(float height){
        setSize(getWidth(), height);
    }

    public float getPigHeight(){
        return getHeight();
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
