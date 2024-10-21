package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Block extends Image {
    protected Main game;
    protected Texture blockTexture;
    protected float blockWidth;
    protected float blockHeight;

    public Block(Main game, float width, float height) {
        super();
        this.game = game;
        this.blockWidth = width;
        this.blockHeight = height;
        setSize(blockWidth, blockHeight);
    }

    // abstract method to load textures based on material
    protected abstract void loadTexture(String material);

    protected void updateTexture() {
        setDrawable(new TextureRegionDrawable(blockTexture));
    }

    public void dispose() {
        blockTexture.dispose();
    }
}
