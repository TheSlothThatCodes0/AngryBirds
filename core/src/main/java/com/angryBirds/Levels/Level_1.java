package com.angryBirds.Levels;

import com.angryBirds.Birds.BlueBird;
import com.angryBirds.Birds.RedBird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Blocks.Cube;
import com.angryBirds.Blocks.Ground;
import com.angryBirds.Blocks.Plank;
import com.angryBirds.Blocks.Triangle;
import com.angryBirds.Levels.BaseLevel;
import com.angryBirds.Main;
import com.angryBirds.Pigs.NormalPig;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Level_1 extends BaseLevel {

    public Level_1(Main game) {
        super(game);
        initializeGameObjects();

    }

    // @Override
    // protected void initializeGameObjects() {
    // int ground_height = 35;
    // super.createGround(ground_height);
    ////
    //// addBird(new RedBird(this.game, 100, ground_height));
    //// addBird(new BlueBird(game, 150, ground_height));
    ////
    //// addBlock(new Cube(game, "stone", 1400, ground_height,world));
    //// addBlock(new Cube(game, "stone", 1600, ground_height, world));
    //// addBlock(new Cube(game, "stone", 1400, ground_height + 100, world));
    //// addBlock(new Cube(game, "stone", 1600, ground_height + 100, world));
    //// addBlock(new Cube(game, "stone", 1400, ground_height + 100 + 100 + 22,
    // world));
    //// addBlock(new Cube(game, "stone", 1600, ground_height + 100 + 100 + 22,
    // world));
    //// addBlock(new Cube(game, "stone", 1500, ground_height + 100 + 100 + 100 + 22
    // + 22, world));
    //// addBlock(new Triangle(game, "wood", 1395, ground_height + 100 + 100 + 100 +
    // 22 + 22, world));
    //// addBlock(new Triangle(game, "wood", 1595, ground_height + 100 + 100 + 100 +
    // 22 + 22, world));
    //// addBlock(new Triangle(game, "wood", 1500, ground_height + 100 + 100 + 100 +
    // 100 + 22 + 22, world));
    //// addBlock(new Plank(game,"wood",1400,ground_height+100+100, world));
    //// addBlock(new Plank(game,"wood",1400,ground_height+100+100+100+22, world));
    ////
    //// addPig(new NormalPig(this.game, "big", 1500, ground_height));
    //
    // addBlock(new Cube(game, "stone", 800, 500, world));
    // addBlock(new Cube(game, "ice", 800, 400, world));// Single block higher up
    // System.out.println("Block initialized at y=500");
    //
    // }

        @Override
    protected void initializeGameObjects() {
        float ground_height = 35f;

        // Block sizes
        float cube_size = 140f;    // Cube is 140x140
        float plank_height = 50f;  // Plank is 300x50
        float triangle_size = 120f; // Triangle is 120x120

        // Create ground
        addBlock(new Ground(game, "stone", 0, 0, world));

        // Base layer cubes
        addBlock(new Cube(game, "stone", 1400, ground_height, world));
        addBlock(new Cube(game, "stone", 1600, ground_height, world));

        // Second layer cubes (cube_size above ground + first cube)
        float second_layer = ground_height + cube_size;
        addBlock(new Cube(game, "stone", 1400, second_layer, world));
        addBlock(new Cube(game, "stone", 1600, second_layer, world));

        // First plank between second and third layer
        float first_plank = second_layer + cube_size;
        addBlock(new Plank(game, "wood", 1400, first_plank, world));

        // Third layer cubes (add plank height)
        float third_layer = first_plank + plank_height;
        addBlock(new Cube(game, "stone", 1400, third_layer, world));
        addBlock(new Cube(game, "stone", 1600, third_layer, world));

        // Second plank
        float second_plank = third_layer + cube_size;
        addBlock(new Plank(game, "wood", 1400, second_plank, world));

        // Top center cube
        float top_cube = second_plank + plank_height;
        addBlock(new Cube(game, "stone", 1500, top_cube, world));

        // Triangle decorations (at same height as top cube)
        addBlock(new Triangle(game, "wood", 1395, top_cube, world));
        addBlock(new Triangle(game, "wood", 1595, top_cube, world));

        // Top triangle (above top cube)
        float top_triangle = top_cube + cube_size;
        addBlock(new Triangle(game, "wood", 1500, top_triangle, world));
        addBlock(new Triangle(game, "stone", 1400, top_triangle + 100f, world));

        System.out.println("Level 1 initialized with adjusted block spacing");
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

    // Debug physics body positions if needed
    for (Image block : blocks) {
        if (block instanceof Block) {
            Block b = (Block) block;
            if (b.getY() < 0) {
                System.out.println("Block below ground: " + b.getY());
            }
        }
    }
}
}
