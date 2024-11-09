package com.angryBirds.Blocks;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.angryBirds.Main.PPM;



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
    protected static final short MASK_BIRDS = -1;  // Collide with everything
    protected static final short MASK_PIGS = -1;

    public Block(Main game, float width, float height, World world) {
        super();
        this.game = game;
        this.world = world;
        this.blockWidth = width;
        this.blockHeight = height;
        setSize(blockWidth, blockHeight);
    }

    protected void createPhysicsBody(float x, float y, float density, float friction, float restitution) {
        System.out.println("Creating physics body at x:" + x + " y:" + y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // No PPM scaling in position since the coordinates are already in pixels
        bodyDef.position.set(x/PPM, y/PPM);
        bodyDef.bullet = true;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        // Make physics body match visual size
        float halfWidth = blockWidth / (2 * PPM);
        float halfHeight = blockHeight / (2 * PPM);
        shape.setAsBox(halfWidth, halfHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = CATEGORY_BLOCKS;
        // Make sure blocks collide with ground
        fixtureDef.filter.maskBits = CATEGORY_GROUND | CATEGORY_BLOCKS;

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(this);

        // Debug output
        System.out.println("Physics body created with width:" + blockWidth + " height:" + blockHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            // Update visual position based on physics body
            float x = body.getPosition().x * PPM - blockWidth/2;
            float y = body.getPosition().y * PPM - blockHeight/2;
            setPosition(x, y);
            setRotation((float) Math.toDegrees(body.getAngle()));

            // Debug output
            if (y < 0) {
                System.out.println("Block at position below ground! y:" + y);
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
        }
        if (blockTexture != null) {
            blockTexture.dispose();
        }
    }
}
