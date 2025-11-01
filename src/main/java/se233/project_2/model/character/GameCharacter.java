package se233.project_2.model.character;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.project_2.Launcher;
import se233.project_2.model.AnimatedSprite;
import se233.project_2.model.platFrom;
import se233.project_2.view.GameStage;

import java.util.ArrayList;
import java.util.List;


public class GameCharacter extends Pane {
    private List<platFrom> platFromList =  new ArrayList<>();
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private int characterWidth;
    private int characterHeight;
    private int standingHeight;
    private int crouchingHeight;

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

    public GameCharacter( int x, int y, int width, int height, KeyCode leftKey, KeyCode rightKey, KeyCode upKey,KeyCode proneKey, KeyCode shootKey) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.standingHeight = height;
        this.crouchingHeight = (int)(height * 0.6);
        this.characterHeight = this.standingHeight;

        this.lives = 999;

        this.characterImg = new Image(Launcher.class.getResourceAsStream("/se233/project_2/player.png"));
        this.imageView = new AnimatedSprite(characterImg,6,6,1, 0,0, width,height);
        this.imageView.setFitWidth((int) (width * 1.2));
        this.imageView.setFitHeight((int) (height * 1.2));

        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = proneKey;
        this.shootKey = shootKey ;
        this.getChildren().addAll(this.imageView);
        setScaleX(1);
    }

    public void moveLeft() {
        if (isCrouching || isDead) return;
        setScaleX(-1);
        isMoveLeft = true;
        isMoveRight = false;
    }

    public void moveRight() {
        if (isCrouching || isDead) return;
        setScaleX(1);
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

    public void setPlatforms(List<platFrom> platforms) {
        this.platFromList = platforms;
    }

    public void shoot() {
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
        } else if( x + characterWidth >= GameStage.WIDTH) {
            x = GameStage.WIDTH - characterWidth;
        }
    }

    public void checkReachFloor() {
        if (isDead) return;
        if (isFalling) {
            boolean onPlatform = false;

            // ตรวจสอบการชนกับแพลตฟอร์ม
            for (platFrom p : platFromList) {
                // ตรวจสอบว่าตัวละครอยู่เหนือแพลตฟอร์มในแนวนอน
                boolean xOverlap = (x + characterWidth > p.getX()) && (x < p.getX() + p.getwidth());

                // ตรวจสอบว่าตัวละครกำลังจะตกถึงแพลตฟอร์ม
                boolean yFutureOverlap = (y + characterHeight + yVelocity) >= p.getY();
                boolean yCurrentlyAbove = (y + characterHeight) <= p.getY();

                if (xOverlap && yCurrentlyAbove && yFutureOverlap) {
                    y = p.getY() - this.characterHeight;
                    isFalling = false;
                    canJump = true;
                    yVelocity = 0;
                    onPlatform = true;
                    break;
                }
            }

            // ถ้าไม่ได้อยู่บนแพลตฟอร์มใดๆ ให้ตรวจสอบการชนกับพื้น
            if (!onPlatform) {
                if (y >= GameStage.GROUND - this.characterHeight) {
                    // ถึงพื้นแล้ว
                    y = GameStage.GROUND - this.characterHeight;
                    isFalling = false;
                    canJump = true;
                    yVelocity = 0;
                } else {
                    // ยังไม่ถึงพื้นและไม่ได้อยู่บนแพลตฟอร์ม = ต้องตกลงมา
                    if (!isFalling && !isJumping) {
                        isFalling = true;
                        canJump = false;
                    }
                }
            }
        }

        // ตรวจสอบว่าตัวละครยังคงอยู่บนแพลตฟอร์มหรือไม่ (สำหรับกรณีที่เดินออกจากขอบ)
        if (!isFalling && !isJumping && canJump) {
            boolean stillOnPlatform = false;

            for (platFrom p : platFromList) {
                boolean xOverlap = (x + characterWidth > p.getX()) && (x < p.getX() + p.getwidth());
                boolean yOnPlatform = Math.abs((y + characterHeight) - p.getY()) <= 2; // ให้ความผิดพลาดเล็กน้อย

                if (xOverlap && yOnPlatform) {
                    stillOnPlatform = true;
                    break;
                }
            }

            // ตรวจสอบว่ายังอยู่บนพื้นหรือไม่
            boolean onGround = Math.abs((y + characterHeight) - GameStage.GROUND) <= 2;

            // ถ้าไม่ได้อยู่บนแพลตฟอร์มและไม่ได้อยู่บนพื้น ให้เริ่มตก
            if (!stillOnPlatform && !onGround) {
                isFalling = true;
                canJump = false;
            }
        }
    }

    public void update() {
        moveX();
        moveY();
        checkReachGameWall();
        checkReachHighest();
        checkReachFloor();

    }



    public void respawn() {
        this.x = this.startX;
        this.y = this.startY;

        this.characterHeight = this.standingHeight;
        this.imageView.setFitWidth(this.characterWidth);
        this.imageView.setFitHeight((int)(this.standingHeight * 1.2));

        this.isMoveLeft = false;
        this.isMoveRight = false;
        this.isFalling = true;
        this.canJump = false;
        this.isJumping = false;
        this.isCrouching = false;

        this.isDead = false;
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

    public int getLives() {
        return lives;
    }
}