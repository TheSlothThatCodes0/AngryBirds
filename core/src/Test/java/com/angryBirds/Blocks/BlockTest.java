package com.angryBirds.Blocks;

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
public class BlockTest {
    private World world;
    private TestBlock block;
    private static final float DELTA = 0.001f;
    private static final float TEST_WIDTH = 50f;
    private static final float TEST_HEIGHT = 50f;

    private class TestBlock extends Block {
        public TestBlock(Main game, float width, float height, World world, String material) {
            super(game, width, height, world, material);
            setInitialHealth(material);
            createPhysicsBody(100, 100, 1.0f, 0.5f, 0.3f, "normal");
        }

        @Override
        public void loadTexture(String material) {
            // Empty implementation for testing
        }
    }

    @BeforeEach
    void setUp() {
        world = new World(new Vector2(0, -9.81f), true);
        block = new TestBlock(null, TEST_WIDTH, TEST_HEIGHT, world, "wood");
    }

    @Test
    void testBlockInitialization() {
        assertNotNull(block.body);
        assertEquals("wood", block.getMaterial());
        assertEquals(TEST_WIDTH, block.blockWidth);
        assertEquals(TEST_HEIGHT, block.blockHeight);
    }

    @Test
    void testBlockHealth() {
        float initialHealth = 150f; // WOOD_HEALTH
        float damage = 50f;

        // Test initial health
        assertFalse(block.isMarkedForDestruction());

        // Test damage
        block.takeDamage(damage);
        assertFalse(block.isMarkedForDestruction());

        // Test destruction
        block.takeDamage(initialHealth);
        assertTrue(block.isMarkedForDestruction());
    }

    @Test
    void testDifferentMaterialHealth() {
        TestBlock stoneBlock = new TestBlock(null, TEST_WIDTH, TEST_HEIGHT, world, "stone");
        TestBlock iceBlock = new TestBlock(null, TEST_WIDTH, TEST_HEIGHT, world, "ice");

        float stoneDamage = 150f;
        float iceDamage = 50f;

        stoneBlock.takeDamage(stoneDamage);
        assertFalse(stoneBlock.isMarkedForDestruction()); // Stone has 200 health

        iceBlock.takeDamage(iceDamage);
        assertFalse(iceBlock.isMarkedForDestruction());
        iceBlock.takeDamage(iceDamage);
        assertTrue(iceBlock.isMarkedForDestruction()); // Ice has 100 health
    }

    @Test
    void testPhysicsBodyProperties() {
        assertNotNull(block.body);
        assertNotNull(block.body.getFixtureList().first());

        assertEquals(1.0f, block.body.getFixtureList().first().getDensity(), DELTA);
        assertEquals(0.5f, block.body.getFixtureList().first().getFriction(), DELTA);
        assertEquals(0.3f, block.body.getFixtureList().first().getRestitution(), DELTA);
    }

    @Test
    void testBlockDisposal() {
        assertNotNull(block.body);
        block.dispose();
        assertNull(block.body);
    }

    @Test
    void testGroundBlockCreation() {
        TestBlock groundBlock = new TestBlock(null, TEST_WIDTH, TEST_HEIGHT, world, "wood");
        groundBlock.createPhysicsBody(100, 100, 1.0f, 0.5f, 0.3f, "ground");

        assertTrue(groundBlock.body.isActive());
        assertEquals(Block.CATEGORY_GROUND, groundBlock.body.getFixtureList().first().getFilterData().categoryBits);
    }

    @AfterEach
    void tearDown() {
        if (block != null) {
            block.dispose();
        }
        world.dispose();
        GdxTestRunner.cleanUp();
    }
}
