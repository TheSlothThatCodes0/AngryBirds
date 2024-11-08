package com.angryBirds;

import com.angryBirds.Screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public static final float PPM = 5;
    public SpriteBatch batch;
    public AssetManager assets;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        setScreen(new SplashScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
        getScreen().dispose();
    }
}
