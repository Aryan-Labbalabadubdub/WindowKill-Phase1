package model.characterModels;

import controller.UserInputHandler;
import model.collision.Collidable;
import model.entityModel.AttackTypes;
import model.movement.Direction;
import model.projectileModels.Long_Ranged;

import java.awt.*;

import static controller.UserInputHandler.getMouseLocation;
import static controller.UserInterfaceController.*;
import static controller.constants.EntityConstants.EPSILON_HEALTH;
import static controller.constants.EntityConstants.PointConstants.EPSILON_CENTER;
import static controller.constants.MovementConstants.EPSILON_SPEED;
import static controller.constants.Variables.*;
import static model.Utils.*;

public final class EpsilonModel extends GeoShapeModel implements Long_Ranged {
    public static boolean MOVE_UP_IND_SAVE;
    public static boolean MOVE_DOWN_IND_SAVE;
    public static boolean MOVE_LEFT_IND_SAVE;
    public static boolean MOVE_RIGHT_IND_SAVE;
    public static boolean SHOOT_IND_SAVE;
    private static EpsilonModel INSTANCE;

    private EpsilonModel(String motionPanelId) {
        super(new Point(0, 0), EPSILON_VERTICES, EPSILON_HEALTH.getValue());
        this.isCircular = true;
        this.motionPanelId = motionPanelId;
        this.anchorSave = deepClone(EPSILON_CENTER.getValue());
        assert anchorSave != null;
        createEpsilon(modelId, roundPoint(anchorSave), motionPanelId);
        moveShapeModel(getMotionPanelCenterLocation(getMainMotionPanelId()));
        movement.setAnchor(getMotionPanelCenterLocation(getMainMotionPanelId()));
        damageSize.put(AttackTypes.MELEE, EPSILON_MELEE_DAMAGE);
        damageSize.put(AttackTypes.RANGED, EPSILON_RANGED_DAMAGE);
        activateMovement();
    }

    public static EpsilonModel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new EpsilonModel(getMainMotionPanelId());
        return INSTANCE;
    }

    public static void flushINSTANCE() {
        INSTANCE = null;
    }

    public void activateMovement() {
        movement.angularSpeed = 0;
        movement.speed = EPSILON_SPEED.getValue();
        movement.speedSave = EPSILON_SPEED.getValue();

        movement.getMoveListeners().clear();
        movement.getMoveListeners().add(e -> {
            movement.setDirection(new Direction(0, Direction.DirectionOrientation.positive) {{
                isDownside = true;
            }});
            if (UserInputHandler.MOVE_UP_IND) {
                MOVE_UP_IND_SAVE = true;
                movement.dampenDecelerations();
                movement.updateAnchor();
            } else if (MOVE_UP_IND_SAVE) {
                MOVE_UP_IND_SAVE = false;
                movement.decelerate(movement.direction.clone());
            }

            movement.setDirection(new Direction(0, Direction.DirectionOrientation.positive) {{
                isUpside = true;
            }});
            if (UserInputHandler.MOVE_DOWN_IND) {
                MOVE_DOWN_IND_SAVE = true;
                movement.dampenDecelerations();
                movement.updateAnchor();
            } else if (MOVE_DOWN_IND_SAVE) {
                MOVE_DOWN_IND_SAVE = false;
                movement.decelerate(movement.direction.clone());
            }

            movement.setDirection(new Direction(0, Direction.DirectionOrientation.negative));
            if (UserInputHandler.MOVE_LEFT_IND) {
                MOVE_LEFT_IND_SAVE = true;
                movement.dampenDecelerations();
                movement.updateAnchor();
            } else if (MOVE_LEFT_IND_SAVE) {
                MOVE_LEFT_IND_SAVE = false;
                movement.decelerate(movement.direction.clone());
            }

            movement.setDirection(new Direction(0, Direction.DirectionOrientation.positive));
            if (UserInputHandler.MOVE_RIGHT_IND) {
                MOVE_RIGHT_IND_SAVE = true;
                movement.dampenDecelerations();
                movement.updateAnchor();
            } else if (MOVE_RIGHT_IND_SAVE) {
                MOVE_RIGHT_IND_SAVE = false;
                movement.decelerate(movement.direction.clone());
            }
            movement.move();

            Point mouseLocation = getMouseLocation();
            Point relativeMouse = (Point) relativeLocation(mouseLocation, roundPoint(getMovement().getAnchor()));
            float mouseAngle = calculateAngle(relativeMouse);

            if (UserInputHandler.SHOOT_IND) {
                SHOOT_IND_SAVE = true;
                shoot(this, new Direction(relativeMouse), damageSize.get(AttackTypes.RANGED));
                UserInputHandler.SHOOT_IND = false;
            }

            rotateShapeModel(totalRotation - mouseAngle);
        });
    }

    @Override
    public boolean collide(Collidable collidable) {
        return true;
    }

    @Override
    public String getMotionPanelId() {
        return motionPanelId;
    }
}
