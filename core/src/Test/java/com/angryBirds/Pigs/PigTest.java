package com.angryBirds.Pigs;

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
public class PigTest {
    private World world;
    private TestPig pig;
    private static final float DELTA = 0.001f;
    private static final float TEST_WIDTH = 50f;
    private static final float TEST_HEIGHT = 50f;
    private static final float TEST_X = 100f;
    private static final float TEST_Y = 100f;

    private class TestPig extends Pig {
        public TestPig(Main game, float width, float height, World world, float x, float y) {
            super(game, width, height, world, x, y);
            this.health = 100f;
            this.maxHealth = 100f;
        }

        @Override
        protected void loadTexture(String texturePath) {
        }
    }

    @BeforeEach
    void setUp() {
        world = new World(new Vector2(0, -9.81f), true);
        pig = new TestPig(null, TEST_WIDTH, TEST_HEIGHT, world, TEST_X, TEST_Y);
    }

    @Test
    void testPigInitialization() {
        assertNotNull(pig.getBody());
        assertEquals(TEST_WIDTH, pig.getPigWidth(), DELTA);
        assertEquals(TEST_HEIGHT, pig.getPigHeight(), DELTA);
        assertTrue(pig.getBody().isFixedRotation());
    }

    @Test
    void testPigPhysicsProperties() {
        assertNotNull(pig.getBody().getFixtureList().first());
        assertEquals(1.0f, pig.getBody().getFixtureList().first().getDensity(), DELTA);
        assertEquals(0.3f, pig.getBody().getFixtureList().first().getFriction(), DELTA);
        assertEquals(0.2f, pig.getBody().getFixtureList().first().getRestitution(), DELTA);
        assertEquals(Pig.CATEGORY_PIGS, pig.getBody().getFixtureList().first().getFilterData().categoryBits);
    }

    @Test
    void testPigDamage() {
        float initialHealth = pig.health;
        float damage = 30f;
        
        // Test damage
        pig.takeDamage(damage);
        assertEquals(initialHealth - damage, pig.health, DELTA);
        assertFalse(pig.winc);
        
        // Test death
        pig.takeDamage(initialHealth);
        assertEquals(0f, pig.health, DELTA);
        assertTrue(pig.winc);
    }

    @Test
    void testPigActivation() {
        assertTrue(pig.getBody().isFixedRotation());
        
        pig.activate();
        
        assertFalse(pig.getBody().isFixedRotation());
        assertEquals(0f, pig.getBody().getAngularVelocity(), DELTA);
    }

    @Test
    void testPigSizeModification() {
        float newWidth = 75f;
        float newHeight = 85f;
        
        pig.setPigWidth(newWidth);
        pig.setPigHeight(newHeight);
        
        assertEquals(newWidth, pig.getPigWidth(), DELTA);
        assertEquals(newHeight, pig.getPigHeight(), DELTA);
    }

    @Test
    void testPigDisposal() {
        assertNotNull(pig.getBody());
        pig.dispose();
        assertNull(pig.getBody());
    }

    @AfterEach
    void tearDown() {
        if (pig != null) {
            pig.dispose();
        }
        world.dispose();
        GdxTestRunner.cleanUp();
    }
}