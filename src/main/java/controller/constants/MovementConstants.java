package controller.constants;

import java.util.concurrent.TimeUnit;

import static controller.constants.Variables.*;

public enum MovementConstants {
    EPSILON_SPEED, ANGULAR_SPEED_BOUND, DEFAULT_ANGULAR_DECAY,
    MAX_SAFE_ROTATION, DEFAULT_SPEED, BULLET_SPEED, DEFAULT_DECELERATION,
    DECELERATION_DECAY, DECELERATION_SENSITIVITY, DAMPEN_FACTOR, POSITION_UPDATE_INTERVAL;

    public float getValue() {
        return switch (this) {
            case EPSILON_SPEED -> SIZE_SCALE * GAME_SPEED * 80f / UPS;
            case ANGULAR_SPEED_BOUND -> 400f / UPS;
            case DEFAULT_ANGULAR_DECAY -> 1 - (0.5f * SIZE_SCALE * GAME_SPEED / UPS);
            case MAX_SAFE_ROTATION -> 240f / UPS;
            case DEFAULT_SPEED -> SIZE_SCALE * GAME_SPEED * 30f / UPS;
            case BULLET_SPEED -> SIZE_SCALE * GAME_SPEED * 300f / UPS;
            case DEFAULT_DECELERATION -> SIZE_SCALE * GAME_SPEED * (-10f) / (UPS * UPS);
            case DECELERATION_DECAY -> 1 - (2f * SIZE_SCALE * GAME_SPEED / UPS);
            case DECELERATION_SENSITIVITY -> 0.000001f / UPS;
            case DAMPEN_FACTOR -> 1 + GAME_SPEED * 0.015f;
            case POSITION_UPDATE_INTERVAL -> TimeUnit.SECONDS.toNanos(1) / (GAME_SPEED * UPS);
        };
    }
}
