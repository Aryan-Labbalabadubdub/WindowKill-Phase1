package controller.constants;

import static controller.constants.Variables.GAME_SPEED;

public enum ShrinkConstants {
    SHRINK_DELAY, SHRINK_SPEED, EXTEND_SPEED, DEFORM_SPEED_SENSITIVITY, SHRINK_THRESHOLD_SENSITIVITY,
    SHRINK_DECELERATION, DEFORM_VELOCITY, DEFORM_SENSITIVITY, EXTENSION_LENGTH, MINIMIZE_DELAY;

    public float getValue() {
        return switch (this) {
            case SHRINK_DELAY -> (int) (900 / GAME_SPEED);
            case SHRINK_SPEED -> 0.0028F * GAME_SPEED;
            case EXTEND_SPEED -> 0.2F * GAME_SPEED;
            case DEFORM_SPEED_SENSITIVITY -> 0.01f;
            case SHRINK_THRESHOLD_SENSITIVITY -> 0.01F;
            case SHRINK_DECELERATION -> -0.0001F * GAME_SPEED;
            case DEFORM_VELOCITY -> 0.03F * GAME_SPEED;
            case DEFORM_SENSITIVITY -> 5;
            case EXTENSION_LENGTH -> 30;
            case MINIMIZE_DELAY -> 250;
        };
    }
}
