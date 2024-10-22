package com.angryBirds.Levels;

import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Blocks.Cube;
import com.angryBirds.Blocks.Plank;
import com.angryBirds.Blocks.Triangle;
import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Pigs.NormalPig;

public class Level_1 extends BaseLevel {

    public Level_1(Main game) {
        super(game);
        initializeGameObjects();
    }

    @Override
    protected void initializeGameObjects() {
        int ground_height = 35;
        addBird(new RedBird(this.game, 100, ground_height));
        addBird(new BlueBird(game, 150, ground_height));

        addBlock(new Cube(game, "stone", 1400, ground_height));
        addBlock(new Cube(game, "stone", 1600, ground_height));
        addBlock(new Cube(game, "stone", 1400, ground_height + 100));
        addBlock(new Cube(game, "stone", 1600, ground_height + 100));
        addBlock(new Cube(game, "stone", 1400, ground_height + 100 + 100 + 22));
        addBlock(new Cube(game, "stone", 1600, ground_height + 100 + 100 + 22));
        addBlock(new Cube(game, "stone", 1500, ground_height + 100 + 100 + 100 + 22 + 22));
        addBlock(new Triangle(game, "wood", 1395, ground_height + 100 + 100 + 100 + 22 + 22));
        addBlock(new Triangle(game, "wood", 1595, ground_height + 100 + 100 + 100 + 22 + 22));
        addBlock(new Triangle(game, "wood", 1500, ground_height + 100 + 100 + 100 + 100 + 22 + 22));
        addBlock(new Plank(game,"wood",1400,ground_height+100+100));
        addBlock(new Plank(game,"wood",1400,ground_height+100+100+100+22));

        addPig(new NormalPig(this.game, "big", 850, ground_height));
    }
}
