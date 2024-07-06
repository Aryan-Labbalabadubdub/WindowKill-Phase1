package model.projectileModels;

import model.characterModels.GeoShapeModel;
import model.collision.Collidable;
import model.movement.Direction;

import static controller.UserInterfaceController.playShootSoundEffect;
import static model.Utils.roundPoint;

public interface Long_Ranged extends Collidable {
    String getMotionPanelId();

    default void shoot(GeoShapeModel shooter, Direction direction, int damage) {
        new BulletModel(roundPoint(getAnchor()), getMotionPanelId(), damage) {
            @Override
            public boolean collide(Collidable collidable) {
                return !(collidable instanceof BulletModel) && collidable != shooter;
            }
        }.getMovement().setDirection(direction);
        playShootSoundEffect();
    }
}
