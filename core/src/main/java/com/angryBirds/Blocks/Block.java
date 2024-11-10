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



    protected void createPhysicsBody(float x, float y, float density, float friction, float restitution, boolean isGround) {
        System.out.println("Creating physics body at x:" + x + " y:" + y);

        BodyDef bodyDef = new BodyDef();
        if(!isGround) {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        } else {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        bodyDef.position.set((x + blockWidth/2) / PPM, (y + blockHeight/2) / PPM);
        bodyDef.fixedRotation = false; // Allow rotation
        bodyDef.bullet = true; // Enable continuous collision detection

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(blockWidth / (2 * PPM), blockHeight / (2 * PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = CATEGORY_BLOCKS;
        fixtureDef.filter.maskBits = -1; // Collide with everything

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(this);

        System.out.println("Block physics body created at: " +
            "x=" + body.getPosition().x + " " +
            "y=" + body.getPosition().y + " " +
            "width=" + (blockWidth/PPM) + " " +
            "height=" + (blockHeight/PPM));
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
//                body.setLinearVelocity(body.getPosition().x * PPM - blockWidth/2,body.getPosition().y * PPM - blockHeight/2);
//                float x1 = body.getPosition().x * PPM - blockWidth/2;
//                float y1 = body.getPosition().y * PPM - blockHeight/2;
//                setPosition(x1, y1);
//                setRotation((float) Math.toDegrees(body.getAngle()));
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
