package controller.constants;

import static controller.constants.Variables.SIZE_SCALE;

public enum ViewConstants {
    VERTEX_RADIUS, VERTEX_OFFSET, BASE_PAINT_OPACITY,
    EPSILON_FACTOR, TRIGORATH_FACTOR, SQUARANTINE_FACTOR, BULLET_FACTOR;

    public float getValue() {
        return switch (this) {
            case VERTEX_RADIUS -> SIZE_SCALE * 6;
            case VERTEX_OFFSET -> (VERTEX_RADIUS.getValue() + 1) / 2;
            case BASE_PAINT_OPACITY -> 0.5f;
            case EPSILON_FACTOR -> SIZE_SCALE * 50;
            case TRIGORATH_FACTOR -> SIZE_SCALE * 70;
            case SQUARANTINE_FACTOR -> SIZE_SCALE * 60;
            case BULLET_FACTOR -> SIZE_SCALE * 20;
        };
    }
}
