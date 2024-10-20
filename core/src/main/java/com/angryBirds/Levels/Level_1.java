package com.angryBirds.Levels;

import com.angryBirds.Birds.*;
import com.angryBirds.Blocks.*;
import com.angryBirds.Main;
import com.angryBirds.Pigs.*;

public class Level_1 extends BaseLevel {

    public Level_1(Main game) {
        super(game);
        setBackground("level1_background.png");
        initializeGameObjects();
    }

    @Override
    protected void initializeGameObjects() {
        // Adding birds at specific positions
        addBird(new RedBird(game), 100, 100);
        addBird(new BlueBird(game), 150, 100);

        // Adding blocks at specific positions
        addBlock(new Cube(game, "stone"), 500, 300);
        addBlock(new Triangle(game, "wood"), 550, 250);

        // Adding pigs at specific positions
        addPig(new NormalPig(game, "big"), 850, 350);
    }
}
