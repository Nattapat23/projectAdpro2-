package se233.project_2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se233.project_2.model.character.GameCharacter;
import se233.project_2.model.platFrom;
import se233.project_2.view.GameStage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameCharacterTest {

    private GameCharacter character;
    private final int START_X = 30;
    private final int START_Y = 350;
    private final int WIDTH = 75;
    private final int HEIGHT = 80;

    @BeforeEach
    public void setUp() {
        character = new GameCharacter(
                START_X, START_Y, WIDTH, HEIGHT,
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S,
                KeyCode.SPACE, KeyCode.Z
        );
        GameStage.GROUND = 440;
    }

    // ===== Movement Tests =====

    @Test
    public void testInitialPosition() {
        assertEquals(START_X, character.getX(), "Initial X position should match");
        assertEquals(START_Y, character.getY(), "Initial Y position should match");
    }

    @Test
    public void testMoveLeft() {
        int initialX = character.getX();
        character.moveLeft();
        assertTrue(character.isMoveLeft(), "Character should be moving left");
        assertFalse(character.isMoveRight(), "Character should not be moving right");
        assertEquals(-1.0, character.getScaleX(), "Character should face left");
    }

    @Test
    public void testMoveRight() {
        int initialX = character.getX();
        character.moveRight();
        assertTrue(character.isMoveRight(), "Character should be moving right");
        assertFalse(character.isMoveLeft(), "Character should not be moving left");
        assertEquals(1.0, character.getScaleX(), "Character should face right");
    }

    @Test
    public void testStop() {
        character.moveLeft();
        character.stop();
        assertFalse(character.isMoveLeft(), "Character should stop moving left");
        assertFalse(character.isMoveRight(), "Character should stop moving right");
    }

    @Test
    public void testMoveXLeft() {
        character.moveLeft();
        int initialX = character.getX();
        character.moveX();
        assertTrue(character.getX() < initialX, "X position should decrease when moving left");
    }

    @Test
    public void testMoveXRight() {
        character.moveRight();
        int initialX = character.getX();
        character.moveX();
        assertTrue(character.getX() > initialX, "X position should increase when moving right");
    }

    @Test
    public void testCheckReachGameWallLeft() {
        // Move character far left
        for (int i = 0; i < 100; i++) {
            character.moveLeft();
            character.moveX();
            character.checkReachGameWall();
        }
        assertTrue(character.getX() >= 0, "Character should not go past left wall");
    }

    @Test
    public void testCheckReachGameWallRight() {
        // Move character far right
        for (int i = 0; i < 200; i++) {
            character.moveRight();
            character.moveX();
            character.checkReachGameWall();
        }
        assertTrue(character.getX() + character.getCharacterWidth() <= GameStage.WIDTH,
                "Character should not go past right wall");
    }

    // ===== Jump Tests =====

    @Test
    public void testJump() {
        character.jump();
        assertTrue(character.isJumping(), "Character should be jumping");
        assertFalse(character.isFalling(), "Character should not be falling while jumping");
    }

    @Test
    public void testDoubleJump() {
        character.jump();
        character.jump();
        // After two jumps, should not be able to jump again until landing
        character.jump(); // This should not work
        assertTrue(character.isJumping() || character.isFalling(),
                "Character should be in air after double jump");
    }


    @Test
    public void testFalling() {
        character.jump();
        character.checkReachHighest();
        // Simulate reaching highest point
        for (int i = 0; i < 30; i++) {
            character.moveY();
            character.checkReachHighest();
        }
        assertTrue(character.isFalling(), "Character should be falling after reaching peak");
    }

    @Test
    public void testLanding() {
        character.jump();
        // Simulate full jump cycle
        for (int i = 0; i < 50; i++) {
            character.moveY();
            character.checkReachHighest();
            character.checkReachFloor();
        }
        assertFalse(character.isJumping(), "Character should not be jumping after landing");
        assertTrue(character.isCanJump(), "Character should be able to jump after landing");
    }

    // ===== Crouch Tests =====

    @Test
    public void testCrouch() {
        character.crouch();
        // Crouch only works when on ground
        // Need to ensure character is on ground first
        character.checkReachFloor();
    }

    @Test
    public void testStopCrouch() {
        character.crouch();
        int crouchHeight = character.getCharacterHeight();
        character.stopCrouch();
        assertEquals(HEIGHT, character.getCharacterHeight(),
                "Character height should return to standing height");
    }



    @Test
    public void testPlatformCollision() {
        List<platFrom> platforms = new ArrayList<>();
        platforms.add(new platFrom(0, 300, 200, 15));
        character.setPlatforms(platforms);

        // Position character above platform
        for (int i = 0; i < 50; i++) {
            character.moveY();
            character.checkReachFloor();
        }
    }

    @Test
    public void testFallThroughPlatformGap() {
        List<platFrom> platforms = new ArrayList<>();
        platforms.add(new platFrom(0, 300, 100, 15));
        platforms.add(new platFrom(200, 300, 100, 15));
        character.setPlatforms(platforms);

        // Position character in gap
        character.moveRight();
        for (int i = 0; i < 40; i++) {
            character.moveX();
            character.checkReachGameWall();
        }
    }

    // ===== Action Tests =====

    @Test
    public void testShoot() {
        assertFalse(character.isShooting, "Character should not be shooting initially");
        character.shoot();
        assertTrue(character.isShooting, "Character should be shooting after shoot() called");
    }

    @Test
    public void testStopShoot() {
        character.shoot();
        character.stopShoot();
        assertFalse(character.isShooting, "Character should stop shooting");
        assertFalse(character.isSpecialShooting, "Special shooting should also stop");
    }

    @Test
    public void testSpecialShoot() {
        assertFalse(character.isSpecialShooting, "Character should not be special shooting initially");
        character.specialShoot();
        assertTrue(character.isSpecialShooting, "Character should be special shooting");
    }

    @Test
    public void testGetShootKey() {
        assertEquals(KeyCode.SPACE, character.getShootKey(), "Shoot key should be SPACE");
    }

    @Test
    public void testGetSpecialShootKey() {
        assertEquals(KeyCode.Z, character.getSpecialShootKey(), "Special shoot key should be Z");
    }

    // ===== Respawn Tests =====

    @Test
    public void testRespawn() {
        // Move character and change state
        character.moveRight();
        character.moveX();
        character.jump();
        character.shoot();

        int originalX = character.getX();

        // Respawn
        character.respawn();

        assertEquals(START_X, character.getX(), "X should reset to start position");
        assertFalse(character.isMoveLeft(), "Should not be moving left after respawn");
        assertFalse(character.isMoveRight(), "Should not be moving right after respawn");
        assertFalse(character.isShooting, "Should not be shooting after respawn");
        assertFalse(character.isJumping(), "Should not be jumping after respawn");
        assertTrue(character.isFalling(), "Should be falling after respawn");
    }

    // ===== Property Tests =====

    @Test
    public void testGetters() {
        assertEquals(WIDTH, character.getCharacterWidth(), "Width should match");
        assertEquals(HEIGHT, character.getCharacterHeight(), "Height should match");
        assertEquals(KeyCode.A, character.getLeftKey(), "Left key should be A");
        assertEquals(KeyCode.D, character.getRightKey(), "Right key should be D");
        assertEquals(KeyCode.W, character.getUpKey(), "Up key should be W");
        assertEquals(KeyCode.S, character.getDownKey(), "Down key should be S");
    }

    @Test
    public void testLives() {
        assertEquals(10, character.getLives(), "Character should start with 999 lives");
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, character.getScore(), "Initial score should be 0");
    }

    // ===== Animation State Tests =====

    @Test
    public void testUpdateAnimation() {
        // Test that updateAnimation doesn't throw exceptions
        assertDoesNotThrow(() -> character.updateAnimation(),
                "updateAnimation should not throw exception");
    }

    @Test
    public void testUpdate() {
        // Test that update doesn't throw exceptions
        assertDoesNotThrow(() -> character.update(),
                "update should not throw exception");
    }

    // ===== Edge Cases =====

    @Test
    public void testMultipleJumpsLimit() {
        character.jump();
        character.jump();
        character.jump(); // Should not work (exceeds max jumps)
        character.jump(); // Should not work

        // Character should still be in air or falling
        assertTrue(character.isJumping() || character.isFalling(),
                "Character should be in air after max jumps");
    }

    @Test
    public void testMovementWhileInAir() {
        character.jump();
        character.moveLeft();
        assertTrue(character.isMoveLeft(), "Should be able to move left while jumping");

        character.stop();
        character.moveRight();
        assertTrue(character.isMoveRight(), "Should be able to move right while jumping");
    }

    @Test
    public void testCheckReachFloorAtGround() {
        // Ensure character falls to ground
        for (int i = 0; i < 100; i++) {
            character.moveY();
            character.checkReachFloor();
        }

        assertTrue(character.getY() <= GameStage.GROUND - character.getCharacterHeight(),
                "Character should be at or above ground level");
        assertTrue(character.isCanJump(), "Character should be able to jump when on ground");
    }
}