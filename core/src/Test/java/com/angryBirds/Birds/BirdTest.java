package com.angryBirds.Birds;

import com.angryBirds.GdxTestRunner;
import com.angryBirds.Main;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GdxTestRunner.class)
public class BirdTest {
    private World world;
    private TestBird bird;
    private static final float DELTA = 0.001f;

    private class TestBird extends Bird {
        public TestBird(Main game, float x, float y, World world) {
            super(game, x, y, world);
        }

        @Override
        public void loadTexture() {
        }

        @Override
        public void triggerSpecialAbility() {
        }
    }

    @BeforeEach
    void setUp() {
        world = new World(new Vector2(0, -9.81f), true);
        bird = new TestBird(null, 100, 100, world);
    }

    @Test
    void testBirdInitialization() {
        assertFalse(bird.isLaunched);
        assertFalse(bird.isDragging);
        assertFalse(bird.isDead);
        assertNotNull(bird.getBody());
    }

    @Test
    void testBirdLaunch() {
        float velocityX = 10f;
        float velocityY = 10f;
        
        bird.launch(velocityX, velocityY);
        
        assertTrue(bird.isLaunched);
        assertTrue(bird.getBody().isAwake());
        assertFalse(bird.getBody().isFixedRotation());
        
        Vector2 actualVelocity = bird.getBody().getLinearVelocity();
        assertEquals(velocityX, actualVelocity.x, DELTA);
        assertEquals(velocityY, actualVelocity.y, DELTA);
    }

    @Test
    void testPhysicsBodyProperties() {
        assertEquals(5.0f, bird.getBody().getFixtureList().first().getDensity(), DELTA);
        assertEquals(0.2f, bird.getBody().getFixtureList().first().getFriction(), DELTA);
        assertEquals(0.4f, bird.getBody().getFixtureList().first().getRestitution(), DELTA);
    }

    @Test
    void testBirdDisposal() {
        assertNotNull(bird.getBody());
        bird.dispose();
        assertNull(bird.getBody());
    }

    @Test
    void testInitialBodyState() {
        assertFalse(bird.getBody().isActive());
        assertEquals(0, bird.getBody().getAngularVelocity(), DELTA);
        Vector2 velocity = bird.getBody().getLinearVelocity();
        assertEquals(0, velocity.x, DELTA);
        assertEquals(0, velocity.y, DELTA);
        assertTrue(bird.getBody().isFixedRotation());
    }

    @AfterEach
    void tearDown() {
        bird.dispose();
        world.dispose();
        GdxTestRunner.cleanUp();
    }
}