package se233.project_2.Exception;

public class SpriteException extends RuntimeException {
    public SpriteException(String message) {
        super(message);
    }
    public SpriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
