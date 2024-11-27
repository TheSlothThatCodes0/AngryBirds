package com.angryBirds.Levels;

import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Birds.YellowBird;
import com.angryBirds.Blocks.*;
import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Pigs.KingPig;
import com.angryBirds.Pigs.NormalPig;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Level_2 extends BaseLevel {

    public Level_2(Main game, boolean loadingFromSave) {
        super(game, loadingFromSave);
        if (!loadingFromSave) {
            initializeGameObjects();
        }

    }

    @Override
    protected void initializeGameObjects() {
        float ground_height = 35f;
        float xOffset = 250;

        addBird(new RedBird(game, 100, ground_height, world));
        addBird(new BlueBird(game, 150, ground_height, world));
        addBird(new YellowBird(game, 200, ground_height, world));

        float cube_size = 80f;
        float plank_height = 25f;
        float triangle_size = 80f;
        float column_height = 200f;
        float buffer_size = 10f;

        addBlock(new Ground(game, "stone", 0, 0, world));
        addBlock(new Wall(game, WORLD_WIDTH, buffer_size, world));
        addBlock(new Wall(game, -90, buffer_size, world));


        addBlock(new Column(game, "stone", 1400 - xOffset, ground_height, world));
        addBlock(new Column(game, "stone", 1440 - xOffset, ground_height, world));
        addBlock(new Column(game, "stone", 1480 - xOffset, ground_height, world));
        addBlock(new Column(game, "stone", 1790 - xOffset, ground_height, world));
        addBlock(new Column(game, "stone", 1830 - xOffset, ground_height, world));
        addBlock(new Column(game, "stone", 1870 - xOffset, ground_height, world));

        addBlock(new Plank(game, "wood", 1370 - xOffset, ground_height + column_height + buffer_size, world));
        addBlock(new Plank(game, "wood", 1720 - xOffset, ground_height + column_height + buffer_size, world));

        addBlock(new Cube(game, "stone", 1370 - xOffset, ground_height + column_height + plank_height + buffer_size, world));
        addBlock(new Cube(game, "stone", 1840 - xOffset, ground_height + column_height + plank_height + buffer_size, world));
        addBlock(new Cube(game, "stone", 1370 - xOffset, ground_height + column_height + plank_height  + cube_size+ buffer_size, world));
        addBlock(new Cube(game, "stone", 1840 - xOffset, ground_height + column_height + plank_height  + cube_size+ buffer_size, world));

        addBlock(new Column(game, "wood", 1540 - xOffset, ground_height + column_height + plank_height + buffer_size, world));
        addBlock(new Column(game, "wood", 1730 - xOffset, ground_height + column_height + plank_height + buffer_size, world));

        addBlock(new Plank(game, "wood", 1545 - xOffset, ground_height + 2*column_height + plank_height +buffer_size, world));
        addBlock(new Column(game, "wood", 1540 - xOffset, ground_height + 2*column_height + 2*plank_height + buffer_size, world));
        addBlock(new Column(game, "wood", 1730 - xOffset, ground_height + 2*column_height + 2*plank_height + buffer_size, world));

        addBlock(new Plank(game, "wood", 1545 - xOffset, ground_height + 3*column_height + 2*plank_height +buffer_size, world));

        addBlock(new Triangle(game, "wood", 1545 - xOffset, ground_height + 3*column_height + 3*plank_height + buffer_size, world));
        addBlock(new Triangle(game, "wood", 1670 - xOffset, ground_height + 3*column_height + 3*plank_height + buffer_size, world));

        addPig(new NormalPig(game, "small", 130 - xOffset, ground_height + cube_size + buffer_size, world));
        addPig(new NormalPig(game, "small", 1370 - xOffset, ground_height + column_height + plank_height + buffer_size + 2*cube_size, world));
        addPig(new NormalPig(game, "small", 1870 - xOffset, ground_height + column_height + plank_height + buffer_size + 2*cube_size, world));

        addPig(new KingPig(game, 1600 - xOffset, ground_height + 3 * column_height + 3 * plank_height + buffer_size, world));

        addPig(new NormalPig(game, "big", 1650 - xOffset, ground_height + 2 * column_height + 2 * plank_height + buffer_size, world));

        System.out.println("Level 1 initialized with blocks and pigs");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        game.batch.end();

    }
}
