package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Main;
import com.angryBirds.Pigs.Pig;
import com.angryBirds.Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseLevel implements Screen {

    protected static final short CATEGORY_GROUND = 0x0001;  // 1 in binary
    protected static final short CATEGORY_BLOCKS = 0x0002;  // 2 in binary
    protected static final short CATEGORY_BIRDS = 0x0004;   // 4 in binary
    protected static final short CATEGORY_PIGS = 0x0008;    // 8 in binary

//    protected static final short MASK_GROUND = -1; // Collide with everything
//    protected static final short MASK_BLOCKS = -1; // Collide with everything
//    protected static final short MASK_BIRDS = -1;  // Collide with everything
//    protected static final short MASK_PIGS = -1;   // Collide with everything


    protected Main game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Texture backgroundTexture;
    protected Stage stage;

    protected Array<Image> birds;
    protected Array<Image> blocks;
    protected Array<Image> pigs;

    protected final float WORLD_WIDTH = 1920;
    protected final float WORLD_HEIGHT = 1080;

    protected Texture launcher1;
    protected Texture launcher2;
    protected Image launch1;
    protected Image launch2;
    protected Image level_bg;

    protected Texture[] backgroundFrames;
    protected int currentFrame;
    protected float stateTime;
    protected float frameDuration = 0.1f;

    protected Texture pauseButtonTexture;
    protected Image pauseButton;

    protected Texture levelSelectTexture;
    protected Texture restartTexture;
    protected ImageButton levelSelectButton;
    protected ImageButton restartButton;
    protected Skin skin;

    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    protected static final float PPM = 100f;

    public BaseLevel(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        birds = new Array<>();
        blocks = new Array<>();
        pigs = new Array<>();
        loadBackgroundTextures();
        loadLauncherTextures();
        loadPauseButtonTexture();
        setupLauncher();
        setupPauseButton();

        world = new World(new Vector2(0,-0.98f), true);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Log contacts for debugging
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                Gdx.app.log("Contact", "Collision between bodies: " +
                    bodyA.getUserData() + " and " + bodyB.getUserData());
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

        loadNavigationButtonTextures();
        setupNavigationButtons();
        createGround();
    }

    private void loadBackgroundTextures() {
        backgroundFrames = new Texture[8];
        try {
            for (int i = 0; i < backgroundFrames.length; i++) {
                String filename = "frame_" + i + "_delay-0.1s.png";
                if (Gdx.files.internal(filename).exists()) {
                    backgroundFrames[i] = new Texture(Gdx.files.internal(filename));
                    Gdx.app.log("BaseLevel", "Loaded texture: " + filename);
                } else {
                    Gdx.app.error("BaseLevel", "Cannot find texture: " + filename);
                }
            }
        } catch (Exception e) {
            Gdx.app.error("BaseLevel", "Error loading textures", e);
        }
    }

    private void loadLauncherTextures() {
        launcher1 = new Texture("launcher_1.png");
        launcher2 = new Texture("launcher_2.png");
    }

    private void loadPauseButtonTexture() {
        pauseButtonTexture = new Texture("pauseButton.png");
    }

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        // Place ground at bottom of screen
        groundBodyDef.position.set((WORLD_WIDTH/2)/PPM, (35/2)/PPM);

        Body groundBody = world.createBody(groundBodyDef);
        groundBody.setUserData("ground");

        PolygonShape groundBox = new PolygonShape();
        // Make ground span entire width
        groundBox.setAsBox(WORLD_WIDTH/(2*PPM), 35/(2*PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = CATEGORY_GROUND;
        fixtureDef.filter.maskBits = CATEGORY_BLOCKS | CATEGORY_BIRDS | CATEGORY_PIGS;

        groundBody.createFixture(fixtureDef);
        groundBox.dispose();

        // Debug output
        System.out.println("Ground created at y:" + (35/2)/PPM);
    }


    private void setupLauncher() {
        float sf = 0.5f;

        launch1 = new Image(launcher1);
        launch1.setSize(launch1.getWidth() * sf, launch1.getHeight() * sf);
        launch1.setPosition(255, 35);

        launch2 = new Image(launcher2);
        launch2.setSize(launch2.getWidth() * sf, launch2.getHeight() * sf);
        launch2.setPosition(300, 105);

        stage.addActor(launch1);
        stage.addActor(launch2);
    }

    private void setupPauseButton() {
        float buttonScaleFactor = 0.3f;

        pauseButton = new Image(pauseButtonTexture);
        pauseButton.setSize(pauseButton.getWidth() * buttonScaleFactor, pauseButton.getHeight() * buttonScaleFactor);
        pauseButton.setPosition(WORLD_WIDTH - pauseButton.getWidth() - 20, WORLD_HEIGHT - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, BaseLevel.this));
            }
        });

        stage.addActor(pauseButton);
    }

    protected abstract void initializeGameObjects();

    protected void setBackground(String backgroundTexturePath) {
        backgroundTexture = new Texture(Gdx.files.internal(backgroundTexturePath));
        level_bg = new Image(backgroundTexture);
        level_bg.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        stage.addActor(level_bg);
        level_bg.toBack();
    }

    protected void addBird(Bird bird) {
        birds.add(bird);
        stage.addActor(bird);
    }

    protected void addBlock(Block block) {
        blocks.add(block);
        stage.addActor(block);
    }

    protected void addPig(Pig pig) {
        pigs.add(pig);
        stage.addActor(pig);
    }

    private void loadNavigationButtonTextures() {
        try {
            levelSelectTexture = new Texture("button_square_depth_gradient.png");
            restartTexture = new Texture("button_square_gradient.png");
        } catch (Exception e) {
            Gdx.app.error("BaseLevel", "Error loading navigation button textures", e);
        }
    }

    private void setupNavigationButtons() {
        float padding = 20f;

        ImageButton.ImageButtonStyle levelSelectStyle = new ImageButton.ImageButtonStyle();
        levelSelectStyle.imageUp = new TextureRegionDrawable(new TextureRegion(levelSelectTexture));

        ImageButton.ImageButtonStyle restartStyle = new ImageButton.ImageButtonStyle();
        restartStyle.imageUp = new TextureRegionDrawable(new TextureRegion(restartTexture));

        levelSelectButton = new ImageButton(levelSelectStyle);
        restartButton = new ImageButton(restartStyle);

        float buttonScale = 0.3f;
        levelSelectButton.setSize(levelSelectTexture.getWidth() * buttonScale,
            levelSelectTexture.getHeight() * buttonScale);
        restartButton.setSize(restartTexture.getWidth() * buttonScale,
            restartTexture.getHeight() * buttonScale);

        levelSelectButton.setPosition(padding, WORLD_HEIGHT - levelSelectButton.getHeight() - padding);
        restartButton.setPosition(levelSelectButton.getX() + levelSelectButton.getWidth() + padding,
            levelSelectButton.getY());

        stage.addActor(levelSelectButton);
        stage.addActor(restartButton);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        world.step(1/60f, 6, 2);

        debugRenderer.render(world, camera.combined.cpy().scl(PPM));

        stateTime += delta;
        if (stateTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % backgroundFrames.length;
            stateTime = 0;
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (backgroundFrames != null && backgroundFrames[currentFrame] != null) {
            game.batch.begin();
            game.batch.draw(backgroundFrames[currentFrame], 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            game.batch.end();
        }

        Matrix4 debugMatrix = camera.combined.cpy();
        debugMatrix.scale(PPM, PPM, 1);
        debugRenderer.render(world, debugMatrix);

        if (levelSelectButton.isPressed()) {
            game.setScreen(new WinScreen(game));
        }
        if (restartButton.isPressed()) {
            game.setScreen(new LossScreen(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void dispose() {

        if (world != null) {
            world.dispose();
        }
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }


        if (backgroundFrames != null) {
            for (Texture frame : backgroundFrames) {
                if (frame != null) {
                    frame.dispose();
                }
            }
        }

        launcher1.dispose();
        launcher2.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        pauseButtonTexture.dispose();

        stage.dispose();

        if (levelSelectTexture != null) levelSelectTexture.dispose();
        if (restartTexture != null) restartTexture.dispose();

        for (Image bird : birds) {
            if (bird instanceof Bird) {
                ((Bird) bird).dispose();
            }
        }

        for (Image block : blocks) {
            if (block instanceof Block) {
                ((Block) block).dispose();
            }
        }

        for (Image pig : pigs) {
            if (pig instanceof Pig) {
                ((Pig) pig).dispose();
            }
        }


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
