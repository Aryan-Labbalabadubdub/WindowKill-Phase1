package controller.constants;

public enum WaveConstants {
    MIN_ENEMY_SPAWN_RADIUS, MAX_ENEMY_SPAWN_RADIUS, NUMBER_OF_WAVES;

    public int getValue() {
        return switch (this) {

            case MIN_ENEMY_SPAWN_RADIUS -> 500;
            case MAX_ENEMY_SPAWN_RADIUS -> 1000;
            case NUMBER_OF_WAVES -> 3;
        };
    }
}
