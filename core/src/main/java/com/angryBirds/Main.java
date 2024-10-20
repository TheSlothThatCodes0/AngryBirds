package com.angryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;
    public AssetManager assets;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
        getScreen().dispose();
    }
}
