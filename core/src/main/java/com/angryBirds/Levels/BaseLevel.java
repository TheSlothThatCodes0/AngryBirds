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

    private World world;
    private Array<Body> birdBodies;
    private Array<Body> blockBodies;
    private Array<Body> pigBodies;

    public BaseLevel(Main game) {
        this.game = game;

        world = new World(new Vector2(0, -9.8f), true);
        birdBodies = new Array<>();
        blockBodies = new Array<>();
        pigBodies = new Array<>();


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

        loadNavigationButtonTextures();
        setupNavigationButtons();
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
        createBirdBody(bird);
    }

    protected void addBlock(Block block) {
        blocks.add(block);
        stage.addActor(block);
        createBlockBody(block);
    }

    protected void addPig(Pig pig) {
        pigs.add(pig);
        stage.addActor(pig);
        createPigBody(pig);
    }

    private void createBirdBody(Bird bird) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(bird.getX() / game.PPM, bird.getY() / game.PPM);

        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bird.getBirdWidth() / 2 / game.PPM, bird.getBirdHeight() / 2 / game.PPM);

        fdef.shape = shape;
        fdef.density = 1f;
        fdef.restitution = 0.5f;
        fdef.friction = 0.3f;

        body.createFixture(fdef).setUserData(bird);
        birdBodies.add(body);
        bird.setBody(body);
    }

    private void createBlockBody(Block block) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(block.getX() / game.PPM, block.getY() / game.PPM);

        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(block.getBlockWidth() / 2 / game.PPM, block.getBlockHeight() / 2 / game.PPM);

        fdef.shape = shape;
        fdef.density = 1f;
        fdef.restitution = 0.5f;
        fdef.friction = 0.3f;

        body.createFixture(fdef).setUserData(block);
        blockBodies.add(body);
        block.setBody(body);
    }

    private void createPigBody(Pig pig) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(pig.getX() / game.PPM, pig.getY() / game.PPM);

        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(pig.getPigWidth() / 2 / game.PPM, pig.getPigHeight() / 2 / game.PPM);

        fdef.shape = shape;
        fdef.density = 1f;
        fdef.restitution = 0.5f;
        fdef.friction = 0.3f;

        body.createFixture(fdef).setUserData(pig);
        pigBodies.add(body);
        pig.setBody(body);
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

        if (levelSelectButton.isPressed()) {
            game.setScreen(new WinScreen(game));
        }
        if (restartButton.isPressed()) {
            game.setScreen(new LossScreen(game));
        }

        world.step(delta, 6, 2);

        for (Body body : birdBodies) {
            Bird bird = (Bird) body.getUserData();
            bird.setPosition((body.getPosition().x * game.PPM) - bird.getBirdWidth() / 2,
                (body.getPosition().y * game.PPM) - bird.getBirdHeight() / 2);
            bird.setRotation((float) Math.toDegrees(body.getAngle()));
        }

        for (Body body : blockBodies) {
            Block block = (Block) body.getUserData();
            block.setPosition((body.getPosition().x * game.PPM) - block.getBlockWidth() / 2,
                (body.getPosition().y * game.PPM) - block.getBlockHeight() / 2);
            block.setRotation((float) Math.toDegrees(body.getAngle()));
        }

        for (Body body : pigBodies) {
            Pig pig = (Pig) body.getUserData();
            pig.setPosition((body.getPosition().x * game.PPM) - pig.getPigWidth() / 2,
                (body.getPosition().y * game.PPM) - pig.getPigHeight() / 2);
            pig.setRotation((float) Math.toDegrees(body.getAngle()));
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

        world.dispose();
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
