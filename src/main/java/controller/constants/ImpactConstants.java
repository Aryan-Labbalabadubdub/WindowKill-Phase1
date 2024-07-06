package controller.constants;

import java.util.concurrent.TimeUnit;

import static controller.constants.Variables.*;

public enum ImpactConstants {
    /**
     * Impact speed shall be higher than movement speed to avoid tunneling <p>
     * Lower values of sensitivity avoid tunneling at cost of computational power
     */
    IMPACT_SPEED, IMPACT_DECELERATION, IMPACT_RADIUS, IMPACT_SCALE, IMPACT_DRIFT_FACTOR, IMPACT_COOLDOWN,
    COLLISION_SENSITIVITY, DETECTION_SENSITIVITY, DIRECTION_SENSITIVITY;

    public float getValue() {
        return switch (this) {
            case IMPACT_SPEED -> SIZE_SCALE * GAME_SPEED * 120f / UPS;
            case IMPACT_DECELERATION -> SIZE_SCALE * GAME_SPEED * (-55f) / (UPS * UPS);
            case IMPACT_RADIUS -> SIZE_SCALE * 100;
            case IMPACT_SCALE -> 2f;
            case IMPACT_DRIFT_FACTOR -> 1.1f;
            case IMPACT_COOLDOWN -> TimeUnit.SECONDS.toNanos(1) / (100f * SIZE_SCALE * GAME_SPEED);
            case COLLISION_SENSITIVITY -> 0.8f * SIZE_SCALE;
            case DETECTION_SENSITIVITY -> 3f;
            case DIRECTION_SENSITIVITY -> 0.00001f;
        };
    }
}
