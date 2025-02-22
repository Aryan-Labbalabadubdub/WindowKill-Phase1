package controller;

import model.MotionPanelModel;
import model.Profile;
import model.WaveManager;
import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.collision.Collision;
import model.movement.Movable;
import view.characters.GeoShapeView;
import view.containers.MotionPanelView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static controller.UserInterfaceController.*;
import static controller.constants.ViewConstants.BASE_PAINT_OPACITY;
import static model.MotionPanelModel.*;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static view.containers.GlassFrame.getGlassFrame;
import static view.containers.MotionPanelView.allMotionPanelViewsList;
import static view.containers.MotionPanelView.getMainMotionPanelView;

public final class GameLoop implements Runnable {
    private static GameLoop INSTANCE = null;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean exit = new AtomicBoolean(false);
    private long updateTimeDiffCapture = 0;
    private long frameTimeDiffCapture = 0;
    private long currentTime;
    private long lastFrameTime;
    private long lastUpdateTime;
    private long timeSaveDiffCapture = 0;
    private long timeSave;
    private volatile String fpsUps = "";

    public static void updateView() {
        for (MotionPanelView motionPanelView : allMotionPanelViewsList) {
            int[] properties = getMotionPanelProperties(motionPanelView.getViewId());
            motionPanelView.setBounds(properties[0], properties[1], properties[2], properties[3]);
            for (GeoShapeView shapeView : motionPanelView.shapeViews) {
                shapeView.getRotatedIcon().setOpacity(BASE_PAINT_OPACITY.getValue() + getHealthScale(shapeView.getViewId())*(1- BASE_PAINT_OPACITY.getValue()));
                shapeView.setVertexLocations(getGeoShapeVertices(shapeView.getViewId()));
            }
        }
        getGlassFrame().repaint();
    }

    public static void updateModel() {
        Collision.getINSTANCE().run();
        for (MotionPanelModel motionPanelModel : allMotionPanelModelsList) {
            for (ActionListener actionListener : motionPanelModel.deformationListeners) {
                actionListener.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            }
        }
        for (GeoShapeModel model : allShapeModelsList) {
            for (ActionListener actionListener : model.getMovement().getMoveListeners()) {
                if (model.getMovement().getMoveListeners().contains(actionListener)) {
                    actionListener.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
                }
            }
        }
    }

    public static GameLoop getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GameLoop();
        return INSTANCE;
    }

    @Override
    public void run() {
        running.set(true);
        exit.set(false);
        initializeGame();

        AtomicInteger frames = new AtomicInteger(0), ticks = new AtomicInteger(0);
        AtomicFloat deltaU = new AtomicFloat(0), deltaF = new AtomicFloat(0);
        currentTime = System.nanoTime();
        lastFrameTime = currentTime;
        lastUpdateTime = currentTime;
        timeSave = currentTime;
        float timePerFrame = (float) TimeUnit.SECONDS.toNanos(1) / Profile.getCurrent().getFps();
        float timePerUpdate = (float) TimeUnit.SECONDS.toNanos(1) / Profile.getCurrent().getUps();

        while (!exit.get()) {
            if (running.get()) {
                currentTime = System.nanoTime();
                gameLoopCycle(frames,ticks,deltaF,deltaU,timePerFrame,timePerUpdate);
            }
        }
    }
    public void gameLoopCycle(AtomicInteger frames,AtomicInteger ticks, AtomicFloat deltaF,AtomicFloat deltaU, float timePerFrame, float timePerUpdate){
        if (deltaU.get() >= 1) {
            updateModel();
            ticks.addAndGet(1);
            deltaU.addAndGet(-1);
        }
        if (deltaF.get() >= 1) {
            updateView();
            frames.addAndGet(1);
            deltaF.addAndGet(-1);
        }
        if (currentTime - lastFrameTime > timePerFrame) {
            deltaF.addAndGet((currentTime - lastFrameTime - timePerFrame) / timePerFrame);
            updateView();
            frames.addAndGet(1);
            lastFrameTime = currentTime;
        }
        if (currentTime - lastUpdateTime > timePerUpdate) {
            deltaU.addAndGet((currentTime - lastUpdateTime - timePerUpdate) / timePerUpdate);
            updateModel();
            ticks.addAndGet(1);
            lastUpdateTime = currentTime;
        }
        if (currentTime - timeSave >= TimeUnit.SECONDS.toNanos(1)) {
            fpsUps = "<html>FPS: " + frames + " <br/>UPS:" + ticks+"</html>";
            frames.set(0);
            ticks.set(0);
            timeSave = currentTime;
        }
    }

    public void initializeGame() {
        getMainMotionPanelModel();
        playGameTheme(getMainMotionPanelView());
        EpsilonModel.getINSTANCE();
        UserInputHandler.getINSTANCE().setupInputHandler(getMainMotionPanelView());
        getMainMotionPanelView().requestFocus();
        new WaveManager().start();
    }

    public void forceExitGame() {
        exit.set(true);
    }

    public void toggleGameLoop() {
        if (getMainMotionPanelView() == null || !getMainMotionPanelView().isVisible()) {
            Thread thread=new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
        else {
            if (running.get()) {
                long now = System.nanoTime();
                updateTimeDiffCapture = now - lastUpdateTime;
                frameTimeDiffCapture = now - lastFrameTime;
                timeSaveDiffCapture = now - timeSave;
                UserInputHandler.getINSTANCE().setShootTimeDiffCapture(now - UserInputHandler.getINSTANCE().getLastShootingTime());
                for (Movable movable: Movable.movables) movable.setPositionUpdateTimeDiffCapture(now - movable.getLastPositionUpdateTime());
            }
            if (!running.get()) {
                currentTime = System.nanoTime();
                lastUpdateTime = currentTime - updateTimeDiffCapture;
                lastFrameTime = currentTime - frameTimeDiffCapture;
                timeSave = currentTime - timeSaveDiffCapture;
                UserInputHandler.getINSTANCE().setLastShootingTime(currentTime - UserInputHandler.getINSTANCE().getShootTimeDiffCapture());
                for (Movable movable: Movable.movables) movable.setLastPositionUpdateTime(currentTime - movable.getPositionUpdateTimeDiffCapture());
            }
        }
        running.set(!running.get());
    }

    public boolean isRunning() {return running.get();}

    public boolean isOn() {
        return !exit.get();
    }
    public void setRunning(boolean running){this.running.set(running);}
    public static String getFpsUps(){
        if (INSTANCE!=null && getINSTANCE().isOn() && getINSTANCE().isRunning()) return getINSTANCE().fpsUps;
        return "";
    }
}
