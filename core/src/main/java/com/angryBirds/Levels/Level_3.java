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

public class Level_3 extends BaseLevel {

    public Level_3(Main game, boolean loadingFromSave) {
        super(game, loadingFromSave);
        if (!loadingFromSave) {
            initializeGameObjects();
        }
    }

    @Override
    protected void initializeGameObjects() {
        float ground_height = 35f;
        float xOffset = 250; // Offset to move everything left
        float screen_right = WORLD_WIDTH - 150;

        // Birds - keep these at original position
        addBird(new RedBird(game, 100, ground_height, world));
        addBird(new BlueBird(game, 150, ground_height, world));
        addBird(new YellowBird(game, 200, ground_height, world));

        // Block sizes
        float cube_size = 80f;
        float plank_height = 25f;
        float triangle_size = 80f;
        float column_height = 200f;
        float buffer_size = 10f;
        float base_x = screen_right - 500;
        float base_y = ground_height;

        // Ground
        addBlock(new Ground(game, "stone", 0, 0, world));
        addBlock(new Wall(game, WORLD_WIDTH, buffer_size, world));
        addBlock(new Wall(game, -90, buffer_size, world));

        // Level 3 specific blocks
        addBlock(new Column(game, "stone", base_x, base_y, world));
        addBlock(new Column(game, "stone", base_x + 175, base_y, world));
        addBlock(new Column(game, "stone", base_x + 190, base_y, world));
        addBlock(new Column(game, "stone", base_x + 2*185+ buffer_size, base_y, world));
        addBlock(new Column(game, "stone", base_x + 2*185+ 2*buffer_size, base_y, world));
        addBlock(new Column(game, "stone", base_x + 3*185, base_y, world));

        addBlock(new Plank(game, "wood", base_x, base_y+10 + column_height, world));
        addBlock(new Plank(game, "wood", base_x + 190, base_y + column_height, world));
        addBlock(new Plank(game, "wood", base_x + 190 + 200, base_y + column_height, world));

        addBlock(new Column(game, "stone", base_x + 100, base_y + column_height + plank_height + buffer_size, world));
        addBlock(new Column(game, "stone", base_x + 100 + 185, base_y + column_height + plank_height + 2*buffer_size, world));
        addBlock(new Column(game, "stone", base_x + 100 + 2*185, base_y + column_height + plank_height + buffer_size, world));

        addBlock(new Plank(game, "wood", base_x + 100, base_y + 2*column_height + plank_height + buffer_size, world));
        addBlock(new Plank(game, "wood", base_x + 100 + 185, base_y + 2*column_height + plank_height + buffer_size, world));

        addBlock(new Column(game, "stone", base_x + 100, base_y + 2*column_height + 2*plank_height + buffer_size, world));
        addBlock(new Column(game, "stone", base_x + 100 + 185, base_y + 2*column_height + 2*plank_height + buffer_size, world));
        addBlock(new Column(game, "stone", base_x + 100 + 2*185, base_y + 2*column_height + 2*plank_height + buffer_size, world));

        addBlock(new Plank(game, "wood", base_x + 100, base_y + 3*column_height + 2*plank_height + buffer_size, world));
        addBlock(new Plank(game, "wood", base_x + 100 + 185, base_y + 3*column_height + 2*plank_height + buffer_size, world));

        addBlock(new Column(game, "stone", base_x + 190, base_y + 3*column_height + 3*plank_height + buffer_size, world));
        addBlock(new Column(game, "stone", base_x + 190 + 185, base_y + 3*column_height + 3*plank_height + buffer_size, world));

        addBlock(new Plank(game, "wood", base_x + 190, base_y + 4*column_height + 3*plank_height + buffer_size, world));


        // Level 2: Middle structure
//        addBlock(new Column(game, "stone", base_x + 200, base_y + column_height + plank_height, world));
//        addBlock(new Column(game, "stone", base_x + 400, base_y + column_height + plank_height, world));
//
//        addBlock(new Plank(game, "wood", base_x + 300, base_y + 2 * column_height + plank_height, world));
//
//        // Level 3: Top structure
//        addBlock(new Column(game, "stone", base_x + 300, base_y + 2 * column_height + 2 * plank_height, world));
//        addBlock(new Plank(game, "wood", base_x + 300, base_y + 3 * column_height + 2 * plank_height, world));


        // Pig placements similar to Level_2 strategy
        addPig(new NormalPig(game, "small", base_x + 50 - xOffset, base_y + cube_size + buffer_size, world));

// Adding pigs on top of the columns
        addPig(new NormalPig(game, "small", base_x - xOffset, base_y + column_height + plank_height + buffer_size + cube_size, world));
        addPig(new NormalPig(game, "small", base_x + 3*185 - xOffset, base_y + column_height + plank_height + buffer_size + cube_size, world));

// King Pig in a prominent position (top of the structure)
        addPig(new KingPig(game, base_x + 190 + 185 - xOffset, base_y + 4*column_height + 3*plank_height + buffer_size, world));

// Big Pig in the middle of columns
        addPig(new NormalPig(game, "big", base_x + 285 - xOffset, base_y + 2*column_height + 2*plank_height + buffer_size, world));

        System.out.println("Level 3 initialized with blocks and pigs");
    }

    @Override
    public void render(float delta) {
        // Call parent render which handles debug rendering
        super.render(delta);

        // Add any Level_3 specific rendering here
        // Your existing rendering code without the debug renders
        game.batch.begin();
        // Your existing rendering code
        game.batch.end();
    }
}