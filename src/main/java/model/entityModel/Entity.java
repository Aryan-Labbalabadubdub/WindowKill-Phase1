package model.entityModel;

import model.characterModels.GeoShapeModel;

import java.util.HashMap;

import static controller.UserInterfaceController.*;
import static model.characterModels.GeoShapeModel.allShapeModelsList;
import static model.collision.Collidable.collidables;

public abstract class Entity {
    public int health;
    public int fullHealth;
    public boolean vulnerable;
    public HashMap<AttackTypes, Integer> damageSize = new HashMap<>();

    protected abstract String getModelId();

    protected abstract String getMotionPanelId();

    public void damage(Entity entity, AttackTypes attackType) {
        if (entity.vulnerable) {
            entity.health -= damageSize.get(attackType);
            if (entity.health <= 0) {
                entity.eliminate();
                playDownSoundEffect();
            } else playHitSoundEffect();
        }
    }

    public void eliminate() {
        if (this instanceof GeoShapeModel) {
            allShapeModelsList.remove(this);
            collidables.remove(this);
            eliminateView(getModelId(), getMotionPanelId());
        }
    }

    public void addHealth(int units) {
        this.health = Math.min(fullHealth, health + units);
    }

}
