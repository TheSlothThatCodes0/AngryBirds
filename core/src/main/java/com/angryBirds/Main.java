package com.angryBirds;

import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Screens.SplashScreen;
import com.angryBirds.Utils.musicControl;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public static final float PPM = 5;
    public SpriteBatch batch;
    public AssetManager assets;
    private musicControl mc;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        BaseLevel.clearSavedGame();

        mc = musicControl.getInstance();
        mc.loadMusic("audio/theme_1.mp3");
        mc.fadeIn(3.0f);

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
        mc.dispose();
    }
}
