package controller.constants;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static controller.constants.DimensionConstants.*;

public enum EntityConstants {
    EPSILON_HEALTH, SHOTS_PER_SECOND, SKILL_COOLDOWN_IN_MINUTES,
    TRIGORATH_HEALTH, TRIGORATH_MELEE_DAMAGE, SQUARANTINE_HEALTH, SQUARANTINE_MELEE_DAMAGE, BULLET_HEALTH;

    public int getValue() {
        return switch (this) {
            case EPSILON_HEALTH -> 100;
            case SHOTS_PER_SECOND -> 2;
            case SKILL_COOLDOWN_IN_MINUTES -> 5;
            case TRIGORATH_HEALTH -> 15;
            case TRIGORATH_MELEE_DAMAGE -> 10;
            case SQUARANTINE_HEALTH -> 10;
            case SQUARANTINE_MELEE_DAMAGE -> 6;
            case BULLET_HEALTH -> 0;
        };
    }

    public enum EntityVertices {
        TRIGORATH_VERTICES, SQUARANTINE_VERTICES, BULLET_VERTICES;

        public ArrayList<Point2D> getValue() {
            return switch (this) {

                case TRIGORATH_VERTICES -> new ArrayList<>(
                        List.of(new Point2D.Float(0, 0), new Point2D.Float(0, TRIGORATH_DIMENSION.getValue().height),
                                new Point2D.Float(TRIGORATH_DIMENSION.getValue().width, TRIGORATH_DIMENSION.getValue().height / 2F)));
                case SQUARANTINE_VERTICES -> new ArrayList<>(
                        List.of(new Point2D.Float(0, 0), new Point2D.Float(0, SQUARANTINE_DIMENSION.getValue().height),
                                new Point2D.Float(SQUARANTINE_DIMENSION.getValue().width, SQUARANTINE_DIMENSION.getValue().height),
                                new Point2D.Float(SQUARANTINE_DIMENSION.getValue().width, 0)));
                case BULLET_VERTICES -> new ArrayList<>();
            };
        }
    }

    public enum PointConstants {
        EPSILON_CENTER, BULLET_CENTER;

        public Point2D getValue() {
            return switch (this) {

                case EPSILON_CENTER -> new Point2D.Float(EPSILON_DIMENSION.getValue().width / 2F, EPSILON_DIMENSION.getValue().height / 2F);
                case BULLET_CENTER -> new Point2D.Float(BULLET_DIMENSION.getValue().width / 2F, BULLET_DIMENSION.getValue().height / 2F);
            };
        }
    }
}
