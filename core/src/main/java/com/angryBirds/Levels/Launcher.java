//package com.angryBirds.Levels;
//
//import com.angryBirds.Birds.Bird;
//import com.angryBirds.Main;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//
//public class Launcher extends Image {
//    private Main game;
//    private World world;
//    private Bird currentBird;
//    private Vector2 launchPosition;
//    private Vector2 releasePosition;
//    private float maxLaunchForce = 1000f;
//    private float launchForce = 0f;
//    private float launchAngle = 45f;
//
//    public Launcher(Main game, World world, Vector2 launchPosition) {
//        super(new Texture("launcher.png"));
//        this.game = game;
//        this.world = world;
//        this.launchPosition = launchPosition;
//        setPosition(launchPosition.x, launchPosition.y);
//
//        addListener(new ClickListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                releasePosition = new Vector2(x, y);
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                slingshot();
//            }
//        });
//    }
//
//    public void setCurrentBird(Bird bird) {
//        currentBird = bird;
//        currentBird.setPosition(launchPosition.x, launchPosition.y);
//    }
//
//    private void slingshot() {
//        if (currentBird != null) {
//            float dx = releasePosition.x - launchPosition.x;
//            float dy = releasePosition.y - launchPosition.y;
//
//            float distance = (float) Math.sqrt(dx * dx + dy * dy);
//            launchForce = Math.min(distance / 100 * maxLaunchForce, maxLaunchForce);
//
//            float angle = (float) Math.atan2(dy, dx);
//            launchAngle = (float) Math.toDegrees(angle);
//
//            BodyDef bdef = new BodyDef();
//            bdef.type = BodyDef.BodyType.DynamicBody;
//            bdef.position.set(launchPosition.x / game.PPM, launchPosition.y / game.PPM);
//
//            Body body = world.createBody(bdef);
//
//            FixtureDef fdef = new FixtureDef();
//            PolygonShape shape = new PolygonShape();
//            shape.setAsBox(currentBird.getBirdWidth() / 2 / game.PPM, currentBird.getBirdHeight() / 2 / game.PPM);
//
//            fdef.shape = shape;
//            fdef.density = 1f;
//            fdef.restitution = 0.5f;
//            fdef.friction = 0.3f;
//
//            body.createFixture(fdef).setUserData(currentBird);
//
//            body.applyForceToCenter(new Vector2((float) Math.cos(angle) * launchForce, (float) Math.sin(angle) * launchForce), true);
//
//            currentBird.setBody(body);
//            currentBird = null;
//        }
//    }
//}
