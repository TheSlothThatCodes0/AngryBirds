package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.angryBirds.Main.PPM;

import java.lang.reflect.Array;

public abstract class Block extends Image {
    protected Main game;
    protected Texture blockTexture;
    protected float blockWidth;
    protected float blockHeight;

    protected Body body;
    protected World world;

    protected static final short CATEGORY_GROUND = 0x0001;
    protected static final short CATEGORY_BLOCKS = 0x0002;
    protected static final short CATEGORY_BIRDS = 0x0004;
    protected static final short CATEGORY_PIGS = 0x0008;

    protected static final short MASK_GROUND = -1; // Collide with everything
    protected static final short MASK_BLOCKS = -1; // Collide with everything
    protected static final short MASK_BIRDS = -1; // Collide with everything
    protected static final short MASK_PIGS = -1;

    public Block(Main game, float width, float height, World world) {
        super();
        this.game = game;
        this.world = world;
        this.blockWidth = width;
        this.blockHeight = height;
        setSize(blockWidth, blockHeight);
    }

    protected void createPhysicsBody(float x, float y, float density, float friction, float restitution, String type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type.equals("ground") ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;

        // Convert screen coordinates to physics world coordinates
        // Add half the width/height to center the body on the sprite
        bodyDef.position.set(
                (x + blockWidth / 2) / PPM,
                (y + blockHeight / 2) / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        if(type.equalsIgnoreCase("triangle")){
            Vector2[] triangleVertices = new Vector2[3];
            triangleVertices[0] = new Vector2(-blockWidth/(2*PPM), -blockHeight/(2*PPM));  // bottom left
             triangleVertices[1] = new Vector2(blockWidth/(2*PPM), -blockHeight/(2*PPM));   // bottom right
             triangleVertices[2] = new Vector2(0, blockHeight/(2*PPM));
            shape.set(triangleVertices);
        }
        else{
            shape.setAsBox(
                blockWidth / (2 * PPM), // half-width
                blockHeight / (2 * PPM) // half-height
            );
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = type.equals("ground") ? CATEGORY_GROUND : CATEGORY_BLOCKS;
        fixtureDef.filter.maskBits = -1;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            // Get the center position from physics body
            float centerX = body.getPosition().x * PPM;
            float centerY = body.getPosition().y * PPM;

            // Calculate the position to center the texture on the physics body
            float x = centerX - (getWidth() / 2); // Subtract half width to center horizontally
            float y = centerY - (getHeight() / 2); // Subtract half height to center vertically

            // Update visual position
            setPosition(x, y);

            // Update rotation around the center
            setRotation((float) Math.toDegrees(body.getAngle()));
            setOrigin(getWidth() / 2, getHeight() / 2); // Set rotation origin to center

            // Debug output for positions below ground
            if (y < 0) {
                System.out.println("Warning: Block position below ground! Physics pos: " +
                        body.getPosition().y + ", Screen pos: " + y);
            }
        }
    }

    protected abstract void loadTexture(String material);

    protected void updateTexture() {
        setDrawable(new TextureRegionDrawable(blockTexture));
    }

    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }

        if (blockTexture != null) {
            blockTexture.dispose();
            blockTexture = null;
        }
    }
}
