package com.angryBirds.Levels;

import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Blocks.Cube;
import com.angryBirds.Blocks.Plank;
import com.angryBirds.Blocks.Triangle;
import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Pigs.NormalPig;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Level_1 extends BaseLevel {

    public Level_1(Main game) {
        super(game);
        initializeGameObjects();
    }

    @Override
    protected void initializeGameObjects() {
        int ground_height = 35;
//        addBird(new RedBird(this.game, 100, ground_height));
//        addBird(new BlueBird(game, 150, ground_height));
//
//        addBlock(new Cube(game, "stone", 1400, ground_height,world));
//        addBlock(new Cube(game, "stone", 1600, ground_height, world));
//        addBlock(new Cube(game, "stone", 1400, ground_height + 100, world));
//        addBlock(new Cube(game, "stone", 1600, ground_height + 100, world));
//        addBlock(new Cube(game, "stone", 1400, ground_height + 100 + 100 + 22, world));
//        addBlock(new Cube(game, "stone", 1600, ground_height + 100 + 100 + 22, world));
//        addBlock(new Cube(game, "stone", 1500, ground_height + 100 + 100 + 100 + 22 + 22, world));
//        addBlock(new Triangle(game, "wood", 1395, ground_height + 100 + 100 + 100 + 22 + 22, world));
//        addBlock(new Triangle(game, "wood", 1595, ground_height + 100 + 100 + 100 + 22 + 22, world));
//        addBlock(new Triangle(game, "wood", 1500, ground_height + 100 + 100 + 100 + 100 + 22 + 22, world));
//        addBlock(new Plank(game,"wood",1400,ground_height+100+100, world));
//        addBlock(new Plank(game,"wood",1400,ground_height+100+100+100+22, world));
//
//        addPig(new NormalPig(this.game, "big", 1500, ground_height));

        addBlock(new Cube(game, "stone", 800, 500, world));  // Single block higher up
        System.out.println("Block initialized at y=500");

    }
}
