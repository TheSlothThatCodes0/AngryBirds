package com.angryBirds.Levels;

import com.angryBirds.Birds.*;
import com.angryBirds.Blocks.*;
import com.angryBirds.Main;
import com.angryBirds.Pigs.*;
import com.angryBirds.Utils.SaveData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class Saved_Level extends BaseLevel {
    private static final float GROUND_HEIGHT = 35f;
    private static final float BUFFER_SIZE = 10f;

    public Saved_Level(Main game) {
        super(game, true);
        initializeGameObjects();
        loadSavedGame();
    }

    @Override
    protected void initializeGameObjects() {
        addBlock(new Ground(game, "stone", 0, 0, world));
        addBlock(new Wall(game, WORLD_WIDTH, BUFFER_SIZE, world));
        addBlock(new Wall(game, -90, BUFFER_SIZE, world));
    }

    private void loadSavedGame() {
        SaveData savedGame = loadSavedGameFile();
        if (savedGame != null) {
            loadGameObjects(savedGame);
        } else {
            System.out.println("No saved game found");
        }
    }

    private SaveData loadSavedGameFile() {
        try {
            FileHandle file = Gdx.files.local("save.json");
            if (file.exists()) {
                String json = file.readString();
                return new Json().fromJson(SaveData.class, json);
            }
        } catch (Exception e) {
            System.out.println("Error loading save: " + e.getMessage());
        }
        return null;
    }

    private void loadGameObjects(SaveData saveData) {
        // Load Birds
        if (saveData.birds != null) {
            for (SaveData.GameObjectData birdData : saveData.birds) {
                Bird bird = createBird(birdData);
                if (bird != null) {
                    addBird(bird);
                }
            }
        }

        // Load Blocks
        if (saveData.blocks != null) {
            for (SaveData.GameObjectData blockData : saveData.blocks) {
                Block block = createBlock(blockData);
                if (block != null && block.body != null) {
                    addBlock(block);
                }
            }
        }

        // Load Pigs
        if (saveData.pigs != null) {
            for (SaveData.GameObjectData pigData : saveData.pigs) {
                Pig pig = createPig(pigData);
                if (pig != null) {
                    addPig(pig);
                }
            }
        }
    }

    private Bird createBird(SaveData.GameObjectData data) {
        Bird bird = null;
        switch (data.type) {
            case "RedBird":
                bird = new RedBird(game, data.x, data.y, world);
                break;
            case "BlueBird":
                bird = new BlueBird(game, data.x, data.y, world);
                break;
            case "YellowBird":
                bird = new YellowBird(game, data.x, data.y, world);
                break;
        }
        if (bird != null) {
            bird.setRotation(data.angle);
        }
        return bird;
    }

    private Block createBlock(SaveData.GameObjectData data) {
        Block block = null;
        try {
            switch (data.type) {
                case "Cube":
                    block = new Cube(game, data.material, data.x, data.y, world);
                    break;
                case "Plank":
                    block = new Plank(game, data.material, data.x, data.y, world);
                    break;
                case "Triangle":
                    block = new Triangle(game, data.material, data.x, data.y, world);
                    break;
                case "Column":
                    block = new Column(game, data.material, data.x, data.y, world);
                    break;
            }
            if (block != null) {
                block.setRotation(data.angle);
                if (block.body != null) {
                    block.body.setTransform(block.body.getPosition(), (float)Math.toRadians(data.angle));
                }
            }
            return block;
        } catch (Exception e) {
            System.out.println("Error creating block: " + data.type + " - " + e.getMessage());
            return null;
        }
    }

    private Pig createPig(SaveData.GameObjectData data) {
        Pig pig = null;
        switch (data.type) {
            case "NormalPig":
                pig = new NormalPig(game, "small", data.x, data.y, world);
                break;
            case "KingPig":
                pig = new KingPig(game, data.x, data.y, world);
                break;
        }
        if (pig != null) {
            pig.setRotation(data.angle);
        }
        return pig;
    }
}