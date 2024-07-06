package model.collision;

import model.MotionPanelModel;
import model.characterModels.EpsilonModel;
import model.characterModels.GeoShapeModel;
import model.entityModel.AttackTypes;
import model.entityModel.Entity;
import model.movement.Direction;
import model.projectileModels.BulletModel;

import java.util.ArrayList;

import static controller.constants.ImpactConstants.IMPACT_RADIUS;
import static controller.constants.ImpactConstants.IMPACT_SCALE;
import static model.MotionPanelModel.mainMotionPanelModel;
import static model.Utils.*;

public final class Collision implements Runnable {
    private static Collision INSTANCE = null;

    private Collision() {
    }

    public static Collision getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Collision();
        return INSTANCE;
    }

    public static void emitImpactWave(CollisionState state) {
        for (GeoShapeModel shapeModel : GeoShapeModel.allShapeModelsList) {
            if (shapeModel instanceof BulletModel) continue;
            float distance = (float) shapeModel.getMovement().getAnchor().distance(state.collisionPoint);
            float scale = (distance > IMPACT_RADIUS.getValue()) ? 0 : (IMPACT_SCALE.getValue() * IMPACT_RADIUS.getValue() - distance) / IMPACT_RADIUS.getValue();
            Direction direction = new Direction(relativeLocation(shapeModel.getMovement().getAnchor(), state.collisionPoint));
            float rotate = 0;
            if (shapeModel == state.collidable1) {
                direction = state.directionOfCollidable1;
                scale *= state.scale1;
                rotate = scale * state.torque1;
            }
            if (shapeModel == state.collidable2) {
                direction = state.directionOfCollidable2;
                scale *= state.scale2;
                rotate = scale * state.torque2;
            }
            shapeModel.getMovement().impact(direction, scale);
            if (rotate != 0) shapeModel.getMovement().angularSpeed = rotate;
        }
    }

    public static void evaluateDamage(CollisionState state) {
        if (state.collidable1 instanceof Entity && state.collidable2 instanceof Entity && state.collisionPoint != null) {
            if (((Entity) state.collidable1).vulnerable
                    && (state.collidable2 instanceof BulletModel || (state.collidable2.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                    (state.collidable1 instanceof EpsilonModel || state.collidable2 instanceof EpsilonModel)))) {
                ((Entity) state.collidable2).damage((Entity) state.collidable1, AttackTypes.MELEE);
            }
            if (((Entity) state.collidable2).vulnerable
                    && (state.collidable1 instanceof BulletModel || (state.collidable1.isGeometryVertex(toCoordinate(state.collisionPoint)) != null &&
                    (state.collidable1 instanceof EpsilonModel || state.collidable2 instanceof EpsilonModel)))) {
                ((Entity) state.collidable1).damage((Entity) state.collidable2, AttackTypes.MELEE);
            }
        }
    }

    @Override
    public void run() {
        Collidable.CreateAllGeometries();
        ArrayList<CollisionState> collisionStates = new ArrayList<>();
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            for (int j = i + 1; j < Collidable.collidables.size(); j++) {
                CollisionState state;
                state = Collidable.collidables.get(i).checkCollision(Collidable.collidables.get(j));
                if (state != null) collisionStates.add(state);
            }
        }
        for (CollisionState state : collisionStates) {
            if (state.collidable1 instanceof BulletModel) ((BulletModel) state.collidable1).eliminate();
            if (state.collidable2 instanceof BulletModel) ((BulletModel) state.collidable2).eliminate();
            evaluateDamage(state);
            if ((state.collidable1 instanceof MotionPanelModel && state.collidable2 instanceof BulletModel) ||
                    (state.collidable2 instanceof MotionPanelModel && state.collidable1 instanceof BulletModel))
                mainMotionPanelModel.extend(roundPoint(state.collisionPoint));
            else emitImpactWave(state);
        }
    }
}