package com.angryBirds.Levels;

import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Birds.YellowBird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Blocks.Cube;
import com.angryBirds.Blocks.Ground;
import com.angryBirds.Blocks.Plank;
import com.angryBirds.Blocks.Triangle;
import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Pigs.KingPig;
import com.angryBirds.Pigs.NormalPig;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Level_1 extends BaseLevel {

    public Level_1(Main game, boolean loadingFromSave) {
        super(game, loadingFromSave);
        if (!loadingFromSave) {
            initializeGameObjects();
        }

    }

    @Override
    protected void initializeGameObjects() {
        float ground_height = 35f;
        float xOffset = 250; // Offset to move everything left

        // Birds - keep these at original position
        addBird(new RedBird(game, 100, ground_height, world));
        addBird(new BlueBird(game, 150, ground_height, world));
        addBird(new YellowBird(game, 200, ground_height, world));

        // Block sizes
        float cube_size = 140f;
        float plank_height = 50f;
        float triangle_size = 120f;

        // Ground
        addBlock(new Ground(game, "stone", 0, 0, world));

        // Base layer
        addBlock(new Cube(game, "stone", 1400 - xOffset, ground_height, world));
        addBlock(new Cube(game, "stone", 1600 - xOffset, ground_height, world));
        // Add small pig in middle of base
        addPig(new NormalPig(game, "small", 1500 - xOffset, ground_height + 30, world));

        // Second layer
        float second_layer = ground_height + cube_size;
        addBlock(new Cube(game, "stone", 1400 - xOffset, second_layer, world));
        addBlock(new Cube(game, "stone", 1600 - xOffset, second_layer, world));

        // First plank
        float first_plank = second_layer + cube_size;
        addBlock(new Plank(game, "wood", 1400 - xOffset, first_plank, world));

        // Third layer
        float third_layer = first_plank + plank_height;
        addBlock(new Cube(game, "stone", 1400 - xOffset, third_layer, world));
        addBlock(new Cube(game, "stone", 1600 - xOffset, third_layer, world));
        // Add small pig on top of plank
        addPig(new NormalPig(game, "small", 1500 - xOffset, third_layer + 30, world));

        // Second plank
        float second_plank = third_layer + cube_size;
        addBlock(new Plank(game, "wood", 1400 - xOffset, second_plank, world));

        // Top center cube and king pig
        float top_cube = second_plank + plank_height;
        addBlock(new Cube(game, "stone", 1500 - xOffset, top_cube, world));
        addPig(new KingPig(game, 1500 - xOffset, top_cube + cube_size + 30, world));

        // Triangle decorations
        addBlock(new Triangle(game, "wood", 1395 - xOffset, top_cube, world));
        addBlock(new Triangle(game, "wood", 1595 - xOffset, top_cube, world));

        System.out.println("Level 1 initialized with blocks and pigs");
    }

    @Override
    public void render(float delta) {
        // Call parent render which handles debug rendering
        super.render(delta);

        // Add any Level_1 specific rendering here
        // Your existing rendering code without the debug renders
        game.batch.begin();
        // Your existing rendering code
        game.batch.end();

    }
}
