package controller;

import model.MotionPanelModel;
import model.characterModels.EpsilonModel;
import model.characterModels.GeoShapeModel;
import model.collision.Collidable;
import model.entityModel.Skill;
import view.BulletView;
import view.charaterViews.EpsilonView;
import view.charaterViews.GeoShapeView;
import view.charaterViews.SquarantineView;
import view.charaterViews.TrigorathView;
import view.containers.MotionPanelView;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static controller.AudioHandler.clips;
import static model.MotionPanelModel.allMotionPanelModelsList;
import static model.MotionPanelModel.mainMotionPanelModel;
import static model.Utils.*;
import static model.characterModels.GeoShapeModel.allShapeModelsList;
import static view.charaterViews.GeoShapeView.allShapeViewsList;
import static view.containers.MotionPanelView.allMotionPanelViewsList;
import static view.containers.MotionPanelView.mainMotionPanelView;

public abstract class UserInterfaceController {
    public static void createEpsilon(String modelId, Point anchor, String motionPanelId) {
        EpsilonView view = new EpsilonView(anchor, findMotionPanelView(motionPanelId));
        view.setViewId(modelId);
    }

    public static void createTrigorath(String modelId, Point anchor, String motionPanelId) {
        TrigorathView view = new TrigorathView(anchor, findMotionPanelView(motionPanelId));
        view.setViewId(modelId);
    }

    public static void createSquarantine(String modelId, Point anchor, String motionPanelId) {
        SquarantineView view = new SquarantineView(anchor, findMotionPanelView(motionPanelId));
        view.setViewId(modelId);
    }

    public static void createBullet(String modelId, Point referenceAnchor, String motionPanelId) {
        BulletView view = new BulletView(referenceAnchor, findMotionPanelView(motionPanelId));
        view.setViewId(modelId);
    }

    public static void createMotionPanel(String modelId, Point2D dimension, Point2D location) {
        MotionPanelView view = new MotionPanelView(pointToDimension(dimension), roundPoint(location));
        view.setVisible(true);
        view.setViewId(modelId);
    }

    public static void eliminateView(String modelId, String motionPanelId) {
        GeoShapeView shapeView = findView(modelId);
        assert shapeView != null;
        allShapeViewsList.remove(shapeView);
        Objects.requireNonNull(findMotionPanelView(motionPanelId)).shapeViews.remove(shapeView);
    }

    public static void fireSkill() {
        if (Skill.activeSkill != null) Skill.activeSkill.fire();
    }

    public static boolean isGameRunning() {
        return GameLoop.getINSTANCE().isRunning();
    }

    public static void toggleGameRunning() {
        GameLoop.getINSTANCE().toggleGameLoop();
    }

    public static void exitGame() {
        GameLoop.getINSTANCE().forceExitGame();
        EpsilonModel.flushINSTANCE();
        for (MotionPanelView motionPanelView : allMotionPanelViewsList) {
            motionPanelView.shapeViews.clear();
            motionPanelView.setVisible(false);
        }
        mainMotionPanelModel = null;
        mainMotionPanelView = null;
        allMotionPanelViewsList.clear();
        allMotionPanelModelsList.clear();
        allShapeModelsList.clear();
        allShapeViewsList.clear();
        Collidable.collidables.clear();
    }

    public static void playGameTheme(Container container) {
        AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.GAME_THEME, container);
    }

    public static void playMenuTheme() {
        AtomicReference<Boolean> atomicReference = new AtomicReference<>(false);
        ActionListener actionListener = e -> atomicReference.set(isGameRunning());
        AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.MENU_THEME, actionListener, atomicReference);
    }

    public static void playHitSoundEffect() {
        AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.HIT);
    }

    public static void playDownSoundEffect() {
        AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.DOWN);
    }

    public static void playShootSoundEffect() {
        AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.SHOOT);
    }

    public static float playCountdownEffect(int i) {
        return AudioHandler.playSoundEffect(AudioHandler.SoundEffectType.COUNTDOWN, i);
    }

    public static void safeExitGame() {
        for (Clip clip : new ArrayList<>(clips.keySet())) {
            clip.stop();
            clips.remove(clip);
        }
        System.exit(0);
    }

    public static HashMap<String, Integer> getSkillCategoryInfo() {
        HashMap<String, Integer> out = new HashMap<>();
        for (Skill.SkillType type : Skill.SkillType.values()) {
            int acquired = 0;
            for (Skill skill : Skill.skillSet.get(type)) if (skill.acquired) acquired++;
            out.put(type.name(), acquired);
        }
        return out;
    }

    public static HashMap<String, ArrayList<String>> getSkillSet() {
        HashMap<String, ArrayList<String>> out = new HashMap<>();
        for (Skill.SkillType type : Skill.skillSet.keySet()) {
            ArrayList<String> skillNames = new ArrayList<>();
            for (Skill skill : Skill.skillSet.get(type)) skillNames.add(skill.name);
            out.put(type.name(), skillNames);
        }
        return out;
    }

    public static HashMap<String, Boolean> getSkillsAcquiredState() {
        HashMap<String, Boolean> out = new HashMap<>();
        for (Skill.SkillType type : Skill.skillSet.keySet()) {
            for (Skill skill : Skill.skillSet.get(type)) {
                out.put(skill.name, skill.acquired);
            }
        }
        return out;
    }

    public static HashMap<String, Integer> getSkillsCosts() {
        HashMap<String, Integer> out = new HashMap<>();
        for (Skill.SkillType type : Skill.skillSet.keySet()) {
            for (Skill skill : Skill.skillSet.get(type)) {
                out.put(skill.name, skill.cost);
            }
        }
        return out;
    }

    public static String getActiveSkill() {
        if (Skill.activeSkill == null) return null;
        return Skill.activeSkill.name;
    }

    public static void setActiveSkill(String skillName) {
        if (Objects.requireNonNull(findSkill(skillName)).acquired) Skill.activeSkill = findSkill(skillName);
    }

    public static boolean purchaseSkill(String skillName) {
        //TODO check for enough XP
        Objects.requireNonNull(findSkill(skillName)).acquired = true;
        return true;
    }

    public static Skill findSkill(String name) {
        for (Skill.SkillType type : Skill.skillSet.keySet()) {
            for (Skill skill : Skill.skillSet.get(type)) {
                if (skill.name.equals(name)) return skill;
            }
        }
        return null;
    }

    public static ArrayList<Point> getGeoShapeVertices(String viewId) {
        GeoShapeModel model = findModel(viewId);
        assert model != null;
        Point2D motionPanelLocation = Objects.requireNonNull(findMotionPanelModel(model.motionPanelId)).location;
        ArrayList<Point> out = new ArrayList<>();
        for (Point2D point2D : new ArrayList<>(model.vertices)) out.add(roundPoint(relativeLocation(point2D, motionPanelLocation)));
        return out;
    }

    public static int[] getMotionPanelProperties(String motionPanelId) {
        MotionPanelModel model = findMotionPanelModel(motionPanelId);
        assert model != null;
        Point location = roundPoint(model.location);
        Point dimension = roundPoint(model.dimension);
        return new int[]{location.x, location.y, dimension.x, dimension.y};
    }

    public static float getHealthScale(String viewId) {
        GeoShapeModel model = findModel(viewId);
        if (model != null && model.vulnerable) {
            if (model.fullHealth == 0) return 1;
            return (float) model.health / model.fullHealth;
        }
        return 1;
    }

    public synchronized static GeoShapeView findView(String modelId) {
        for (GeoShapeView shapeView : allShapeViewsList) {
            if (shapeView.getViewId().equals(modelId)) return shapeView;
        }
        return null;
    }

    public synchronized static GeoShapeModel findModel(String viewId) {
        for (GeoShapeModel shapeModel : allShapeModelsList) {
            if (shapeModel.getModelId().equals(viewId)) return shapeModel;
        }
        return null;
    }

    public synchronized static MotionPanelModel findMotionPanelModel(String motionPanelId) {
        for (MotionPanelModel motionPanelModel : allMotionPanelModelsList) {
            if (motionPanelModel.getModelId().equals(motionPanelId)) return motionPanelModel;
        }
        return null;
    }

    public synchronized static MotionPanelView findMotionPanelView(String motionPanelId) {
        for (MotionPanelView motionPanelView : allMotionPanelViewsList) {
            if (motionPanelView.getViewId().equals(motionPanelId)) return motionPanelView;
        }
        return null;
    }

    public static void moveGeoShape(String modelId, Point2D newAnchorLocation) {
        GeoShapeView view = findView(modelId);
        if (view != null) view.moveShapeView(roundPoint(newAnchorLocation));
    }

    public static void rotateGeoShape(String modelId, double angle) {
        GeoShapeView view = findView(modelId);
        if (view != null) view.rotateShapeView(angle);
    }

    public static String getMainMotionPanelId() {
        return mainMotionPanelModel.getModelId();
    }

    public static Point2D getMotionPanelCenterLocation(String motionPanelId) {
        MotionPanelModel motionPanelModel = findMotionPanelModel(motionPanelId);
        assert motionPanelModel != null;
        return addUpPoints(motionPanelModel.location, multiplyPoint(motionPanelModel.dimension, 1 / 2F));
    }
}