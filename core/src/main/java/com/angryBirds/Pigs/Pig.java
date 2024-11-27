package com.angryBirds.Pigs;

import com.angryBirds.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.angryBirds.Main.PPM;

public abstract class Pig extends Image {
    protected Main game;
    protected Texture pigTexture;
    protected Body body;
    protected World world;
    private boolean canRoll = false;
    public float health;
    protected float maxHealth;
    protected com.badlogic.gdx.graphics.g2d.BitmapFont healthFont;
    protected static final float DAMAGE_MULTIPLIER = 0.1f;
    public boolean winc = false;

    protected static final short CATEGORY_GROUND = 0x0001;
    protected static final short CATEGORY_BLOCKS = 0x0002;
    protected static final short CATEGORY_BIRDS = 0x0004;
    protected static final short CATEGORY_PIGS = 0x0008;

    private boolean markedForDestruction = false;

    private Sound deathSound;

    public Pig(Main game, float width, float height, World world, float x, float y) {
        super();
        this.game = game;
        this.world = world;
        setSize(width, height);
        setPosition(x, y);
        createPhysicsBody(x, y);

        healthFont = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        healthFont.setColor(1, 0, 0, 1); // Red color

        loadAudio();

    }

    private void loadAudio() {
        try {
            deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/pig_1.mp3"));
        } catch (Exception e) {
            Gdx.app.error("WinScreen", "Error loading audio files", e);
        }
    }

    public void takeDamage(float damage) {
        if (body != null) {
            System.out.println("Taking damage: " + damage);
            health = Math.max(0, health - damage);
            System.out.println("Health after damage: " + health);
            activate(); // Enable rolling when hit

            if (health <= 0) {
                System.out.println("Pig marked for destruction!");
                markedForDestruction = true;
                winc = true;
            }
        }
    }

    protected void createPhysicsBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
            (x + getWidth() / 2) / PPM,
            (y + getHeight() / 2) / PPM
        );

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(getWidth() / (2 * PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f;
        fixtureDef.filter.categoryBits = CATEGORY_PIGS;
        fixtureDef.filter.maskBits = -1;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        circle.dispose();

        body.setActive(true);
        body.setFixedRotation(true);
        canRoll = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (markedForDestruction && body != null) {

            deathSound.play(1.0f);
            dispose();
            return;
        }

        if (body != null) {
            float centerX = body.getPosition().x * PPM;
            float centerY = body.getPosition().y * PPM;
            float x = centerX - (getWidth() / 2);
            float y = centerY - (getHeight() / 2);
            setPosition(x, y);

            if (canRoll) {
                setRotation((float) Math.toDegrees(body.getAngle()));
            }

            setOrigin(getWidth() / 2, getHeight() / 2);

            if (!markedForDestruction && game.batch != null && healthFont != null) {
                game.batch.begin();
                healthFont.draw(game.batch,
                    String.format("%.0f/%.0f", health, maxHealth),
                    getX() + getWidth()/2 - 20,
                    getY() + getHeight() + 20);
                game.batch.end();
            }
        }
    }

    protected void loadTexture(String texturePath) {
        pigTexture = new Texture(texturePath);
        setDrawable(new TextureRegionDrawable(pigTexture));
    }

    public void activate() {
        if (body != null && !canRoll) {
            canRoll = true;
            body.setFixedRotation(false);
            body.setAngularVelocity(0);
        }
    }

    public void dispose() {
        remove();

        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        if (pigTexture != null) {
            pigTexture.dispose();
        }
        if (healthFont != null) {
            healthFont.dispose();
        }
    }

    public float getPigWidth(){
        return getWidth();
    }

    public void setPigWidth(float width){
        setSize(width, getHeight());
    }

    public void setPigHeight(float height){
        setSize(getWidth(), height);
    }

    public float getPigHeight(){
        return getHeight();
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
