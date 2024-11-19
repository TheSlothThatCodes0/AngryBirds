package com.angryBirds.Birds;

import com.angryBirds.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.angryBirds.Main.PPM;

public abstract class Bird extends Image {
    protected Main game;
    protected Texture birdTexture;
    protected float birdWidth = 100;
    protected float birdHeight = 100;
    protected Body body;
    protected World world;
    public boolean isLaunched = false;
    public boolean isDragging = false;

    private static final float DISAPPEAR_TIME = 5.0f; // 7 seconds
    private float timeSinceLaunch = 0f;

    protected static final short CATEGORY_GROUND = 0x0001;
    protected static final short CATEGORY_BLOCKS = 0x0002;
    protected static final short CATEGORY_BIRDS = 0x0004;
    protected static final short CATEGORY_PIGS = 0x0008;

    public Bird(Main game, float x, float y, World world) {
        super();
        this.game = game;
        this.world = world;
        loadTexture();
        setSize(birdWidth, birdHeight);
        setPosition(x, y);
        createPhysicsBody(x, y);
    }

    protected void createPhysicsBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                (x + birdWidth / 2) / PPM,
                (y + birdHeight / 2) / PPM);

        body = world.createBody(bodyDef);

        Shape shape;
        if (this instanceof YellowBird) {
            // Triangular hitbox for yellow bird
            PolygonShape polygonShape = new PolygonShape();
            Vector2[] vertices = new Vector2[3];
            vertices[0] = new Vector2(-birdWidth / (2 * PPM), -birdHeight / (2 * PPM));
            vertices[1] = new Vector2(birdWidth / (2 * PPM), -birdHeight / (2 * PPM));
            vertices[2] = new Vector2(0, birdHeight / (2 * PPM));
            polygonShape.set(vertices);
            shape = polygonShape;
        } else {
            // Circular hitbox for red and blue birds
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(birdWidth / (2 * PPM));
            shape = circleShape;
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5.0f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.4f;
        fixtureDef.filter.categoryBits = CATEGORY_BIRDS;
        fixtureDef.filter.maskBits = -1;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();

        // Initially deactivate physics
        body.setActive(false);

        // Fix the bird in place initially
        if (!isLaunched) {
            body.setAngularVelocity(0);
            body.setLinearVelocity(0, 0);
            body.setFixedRotation(true); // Prevent rotation
            body.setAwake(false); // Put the body to sleep
        }
    }

    public void launch(float velocityX, float velocityY) {
        isLaunched = true;
        body.setAwake(true);
        body.setFixedRotation(false);
        body.setLinearVelocity(velocityX, velocityY);
        body.setGravityScale(1); // Enable gravity after launch
        timeSinceLaunch = 0f; // Reset timer on launch
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        
        // Update disappear timer if launched
        if (isLaunched) {
            timeSinceLaunch += delta;
            if (timeSinceLaunch >= DISAPPEAR_TIME) {
                dispose();
                remove(); // Remove from stage
                return;
            }
        }

        if (body != null) {
            if (!body.isActive()) {
                // Keep bird fixed at current visual position when physics are disabled
                Vector2 position = new Vector2(
                    (getX() + getWidth()/2) / PPM,
                    (getY() + getHeight()/2) / PPM
                );
                body.setTransform(position, 0);
            } else if (!isDragging) {
                // Update visual position from physics body
                float centerX = body.getPosition().x * PPM;
                float centerY = body.getPosition().y * PPM;
                float x = centerX - (getWidth() / 2);
                float y = centerY - (getHeight() / 2);
                setPosition(x, y);
                setRotation((float) Math.toDegrees(body.getAngle()));
            }
            setOrigin(getWidth() / 2, getHeight() / 2);
        }
    }

    protected abstract void loadTexture();

    protected void updateTexture() {
        setDrawable(new TextureRegionDrawable(birdTexture));
    }

    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        if (birdTexture != null) {
            birdTexture.dispose();
        }
    }

    public Body getBody() {
        return body;
    }
}
