package com.angryBirds.Levels;

import com.angryBirds.Birds.Bird;
import com.angryBirds.Blocks.Block;
import com.angryBirds.Pigs.Pig;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CollisionHandler implements ContactListener {
    private static final float VELOCITY_THRESHOLD = 6.0f;
    private static final float BLOCK_DAMAGE_MULTIPLIER = 0.1f;
    private static final float BIRD_DAMAGE_MULTIPLIER = 1.0f;   
    private static final float MAX_DAMAGE = 50.0f;            
    private static boolean damageEnabled = false;
    // Add constant for block damage
    private static final float BLOCK_HEALTH_DAMAGE_MULTIPLIER = 0.5f;

    public static void enableDamage() {
        damageEnabled = true;
    }

    @Override
    public void beginContact(Contact contact) {

        if (!damageEnabled) {
            return;
        }

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        
        // Skip processing if both bodies are nearly static
        Vector2 velA = bodyA.getLinearVelocity();
        Vector2 velB = bodyB.getLinearVelocity();
        if (velA.len() < VELOCITY_THRESHOLD && velB.len() < VELOCITY_THRESHOLD) {
            return;
        }
        
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        
        // Debug print only for significant collisions
        System.out.println("Significant collision between: " + 
                          userDataA.getClass().getSimpleName() + " and " + 
                          userDataB.getClass().getSimpleName());

        // Check pig collisions both ways
        if (userDataA instanceof Pig) {
            handlePigCollision((Pig)userDataA, userDataB, bodyA, bodyB);
        }
        if (userDataB instanceof Pig) {
            handlePigCollision((Pig)userDataB, userDataA, bodyB, bodyA);
        }

        handleCollision(userDataA, userDataB, bodyA, bodyB);
    }

    private void handlePigCollision(Pig pig, Object otherObject, Body pigBody, Body otherBody) {
        float impactForce = calculateImpactForce(pigBody, otherBody);
        System.out.println("Raw impact force: " + impactForce);

        if (impactForce < VELOCITY_THRESHOLD) {
            return;
        }

        float damage = 0;
        if (otherObject instanceof Bird) {
            damage = Math.min(impactForce * BIRD_DAMAGE_MULTIPLIER, MAX_DAMAGE);
            System.out.println("Bird hit pig - Current Health: " + pig.health + " Damage: " + damage);
        } else if (otherObject instanceof Block) {
            damage = Math.min(impactForce * BLOCK_DAMAGE_MULTIPLIER, MAX_DAMAGE);
            System.out.println("Block hit pig - Current Health: " + pig.health + " Damage: " + damage);
        }

        if (damage > 0) {
            pig.takeDamage(damage);
            System.out.println("Pig health after hit: " + pig.health);
        }
    }

    private void handleCollision(Object objectA, Object objectB, Body bodyA, Body bodyB) {
        float impactForce = calculateImpactForce(bodyA, bodyB);
        
        // Handle Bird -> Block collision
        if (objectA instanceof Bird && objectB instanceof Block) {
            handleBirdBlockCollision((Bird)objectA, (Block)objectB, impactForce);
        }
        // Handle Block -> Bird collision
        else if (objectA instanceof Block && objectB instanceof Bird) {
            handleBirdBlockCollision((Bird)objectB, (Block)objectA, impactForce);
        }
    }

    private void handleBirdBlockCollision(Bird bird, Block block, float impactForce) {
        if (impactForce < VELOCITY_THRESHOLD) return;
        
        float damage = Math.min(impactForce * BLOCK_HEALTH_DAMAGE_MULTIPLIER, MAX_DAMAGE);
        System.out.println("Bird hit block with force: " + impactForce + " causing damage: " + damage);
        block.takeDamage(damage);
    }

    private float calculateImpactForce(Body bodyA, Body bodyB) {
        Vector2 velA = bodyA.getLinearVelocity();
        Vector2 velB = bodyB.getLinearVelocity();
        
        // Calculate relative velocity
        Vector2 relativeVel = new Vector2(velA).sub(velB);
        float impactSpeed = relativeVel.len();
        
        if (impactSpeed < VELOCITY_THRESHOLD) {
            return 0f;
        }
        
        // Scale down impact force calculation
        float massA = bodyA.getMass();
        float massB = bodyB.getMass();
        float scaledImpact = (impactSpeed * Math.min(massA, massB)) * 0.1f; // Scale down by factor of 0.1
        
        // Cap maximum impact force
        return Math.min(scaledImpact, 100.0f);
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}