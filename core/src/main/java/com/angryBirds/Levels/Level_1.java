package com.angryBirds.Levels;

import com.angryBirds.Birds.*;
import com.angryBirds.Blocks.*;
import com.angryBirds.Main;
import com.angryBirds.Pigs.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Level_1 extends BaseLevel {

    private Texture launcher1;
    private Texture launcher2;
    private Image launch1;
    private Image launch2;

    public Level_1(Main game) {
        super(game);
        setBackground("level_background.png");
        initializeGameObjects();
    }

    @Override
    protected void initializeGameObjects() {
        // Adding birds at specific positions
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");


        addBird(new RedBird(game), 100, 100);
        addBird(new BlueBird(game), 150, 100);

        // Adding blocks at specific positions
        addBlock(new Cube(game, "stone"), 500, 200);
        addBlock(new Triangle(game, "wood"), 600, 200);
//
//        // Adding pigs at specific positions
//        addPig(new NormalPig(game, "big"), 850, 350);
    }
}
