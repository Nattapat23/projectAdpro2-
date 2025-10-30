package se233.project_2.model.character;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.project_2.Launcher;
import se233.project_2.model.AnimatedSprite;
import se233.project_2.view.GameStage;

import java.util.concurrent.TimeUnit;


public class GameCharacter extends Pane {
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private int characterWidth;
    private int characterHeight;
    private int standingHeight; // ADDED: ความสูงตอนยืน
    private int crouchingHeight; // ADDED: ความสูงตอนหมอบ

    private int score;
    private int lives;

    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    private KeyCode downKey;
    private KeyCode shootKey;

    int xVelocity = 0;
    int yVelocity = 0;
    int xAcceleration = 1;
    int yAcceleration = 1;
    int xMaxVelocity = 7;
    int yMaxVelocity = 17;

    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;
    boolean isCrouching =  false;
    public boolean isShooting = false;
    private boolean isDead = false;

    private static final int ANIM_IDLE = 0;
    private static final int ANIM_RUN = 1;
    private static final int ANIM_JUMP = 2;
    private static final int ANIM_FALL = 3;
    private static final int ANIM_CROUCH = 4;
    private static final int ANIM_SHOOT_STAND = 5;
    private static final int ANIM_SHOOT_RUN = 6;
    private static final int ANIM_SHOOT_JUMP = 7;
    private static final int ANIM_SHOOT_CROUCH = 8;

    public GameCharacter(int id, int x, int y, String imgName, int count, int column, int row, int width, int height, KeyCode leftKey, KeyCode rightKey, KeyCode upKey,KeyCode proneKey, KeyCode shootKey) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.standingHeight = height; // FIXED: กำหนดค่าความสูงตอนยืน
        this.crouchingHeight = (int)(height * 0.6);
        this.characterHeight = this.standingHeight;

        this.lives = 3;

        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth((int) (width * 1.2));
        this.imageView.setFitHeight((int) (height * 1.2));

        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = proneKey;
        this.shootKey = shootKey ;
        this.getChildren().addAll(this.imageView);
        setScaleX(id % 2 * 2 - 1);
    }
    public void moveLeft() {
        if (isCrouching || isDead) return;
        setScaleX(1);
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        if (isCrouching || isDead) return;
        setScaleX(-1);
        isMoveLeft = false;
        isMoveRight = true;
    }
    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
    }
    public void moveX() {
        if (isDead) return;
        setTranslateX(x);
        if(isMoveLeft) {
            xVelocity = xVelocity>=xMaxVelocity? xMaxVelocity : xVelocity+xAcceleration;
            x = x - xVelocity;
        } else if(isMoveRight) {
            xVelocity = xVelocity>=xMaxVelocity? xMaxVelocity : xVelocity+xAcceleration;
            x = x + xVelocity;
        } else {
            xVelocity = 0;
        }
    }
    public void moveY() {
        if (isDead) return;
        setTranslateY(y);
        if(isFalling) {
            yVelocity = yVelocity >= yMaxVelocity? yMaxVelocity : yVelocity+yAcceleration;
            y = y + yVelocity;
        } else if(isJumping) {
            yVelocity = yVelocity <= 0 ? 0 : yVelocity-yAcceleration;
            y = y - yVelocity;
        }
    }

    public void jump() {
        if (!canJump || isCrouching || isDead) return;

        yVelocity = yMaxVelocity;
        canJump = false;
        isJumping = true;
        isFalling = false;
    }

    public void crouch() {
        if (!canJump || isCrouching || isDead) return;
        if (!isCrouching) {
            this.y = this.y + (standingHeight - crouchingHeight);
            this.setTranslateY(y);
            this.characterHeight = this.crouchingHeight;
            isCrouching = true;
            stop();
        }
    }

    public void stopCrouch() {
        if (isCrouching) {
            this.y = this.y - (standingHeight - crouchingHeight);
            this.setTranslateY(y);
            this.characterHeight = this.standingHeight;
            isCrouching = false;
        }
    }

    public void shoot() {
        // เราจะแค่ตั้งค่าสถานะ
        // Gameloop จะเป็นคนตรวจจับสถานะนี้และสร้างกระสุน
        if (isDead) return;
        isShooting = true;
    }

    public void stopShoot() {
        isShooting = false;
    }
    public void checkReachHighest () {
        if (isDead) return;
        if(isJumping && yVelocity <= 0) {
            isJumping = false;
            isFalling = true;
            yVelocity = 0;
        }
    }
    public void checkReachGameWall() {
        if (isDead) return;
        if(x <= 0) {
            x = 0;
        } else if( x + characterWidth >= GameStage.WIDTH) { // ใช้ characterWidth
            x = GameStage.WIDTH - characterWidth;
        }
    }
    public void checkReachFloor() {
        if (isDead) return;
        if(isFalling && y >= GameStage.GROUND - this.characterHeight) {
            y = GameStage.GROUND - this.characterHeight;
            isFalling = false;
            canJump = true;
            yVelocity = 0;
        }
    }

    public void update() {
        moveX();
        moveY();
        checkReachGameWall();
        checkReachHighest();
        checkReachFloor();
        updateAnimation();
    }

    public void die() {
        if (isDead) return; // ป้องกันการตายซ้ำซ้อน

        this.lives--;
        this.isDead = true;
        this.stop(); // หยุดเคลื่อนที่
        // TODO: เล่นแอนิเมชันตาย (ถ้ามี)
        // imageView.setAnimationRow(ANIM_DEATH);

        // (คุณสามารถเพิ่ม Timer ตรงนี้เพื่อหน่วงเวลาก่อน Respawn)
        // (แต่เพื่อความง่าย เราจะให้ Gameloop สั่ง Respawn)
    }

    public void respawn() {
        this.x = this.startX;
        this.y = this.startY;

        // MODIFIED: ต้องรีเซ็ตความสูงกลับเป็นตอนยืน
        this.characterHeight = this.standingHeight;
        this.imageView.setFitWidth(this.characterWidth);
        this.imageView.setFitHeight(this.standingHeight); // (หรือค่า * 1.2 ตามที่คุณตั้งไว้)

        this.isMoveLeft = false;
        this.isMoveRight = false;
        this.isFalling = true;
        this.canJump = false;
        this.isJumping = false;
        this.isCrouching = false; // ADDED: รีเซ็ตสถานะหมอบด้วย
    }

    private void updateAnimation() {

        if (isDead) {
            // imageView.setAnimationRow(ANIM_DEATH); // (Optional)
            return;
        }
        if (isCrouching) {
            imageView.setAnimationRow(ANIM_CROUCH);
        } else if (isJumping) {
            imageView.setAnimationRow(ANIM_JUMP);
        } else if (isFalling) {
            imageView.setAnimationRow(ANIM_FALL);
        } else if (isMoveLeft || isMoveRight) {
            if (isShooting) {
                // imageView.setAnimationRow(ANIM_SHOOT_RUN); // ถ้ามี
                imageView.setAnimationRow(ANIM_RUN); // ใช้ท่าวิ่งไปก่อน
            } else {
                imageView.setAnimationRow(ANIM_RUN);
            }
        } else {
            if (isShooting) {
                imageView.setAnimationRow(ANIM_SHOOT_STAND);
            } else {
                imageView.setAnimationRow(ANIM_IDLE);
            }
        }
    }

    public KeyCode getLeftKey() {

        return leftKey;
    }
    public KeyCode getRightKey() {

        return rightKey;
    }
    public KeyCode getUpKey() {

        return upKey;
    }
    public AnimatedSprite getImageView() {

        return imageView;
    }

    public KeyCode getShootKey() {
        return shootKey;
    }

    public KeyCode getDownKey() {
        return downKey;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getCharacterWidth() {
        return characterWidth;
    }

    public int getCharacterHeight() {
        return characterHeight;
    }

    public int getScore() {
        return score;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean isMoveRight() {
        return isMoveRight;
    }

    public boolean isMoveLeft() {
        return isMoveLeft;
    }
}