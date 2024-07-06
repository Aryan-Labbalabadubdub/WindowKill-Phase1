package model;

import model.characterModels.EpsilonModel;
import model.characterModels.GeoShapeModel;
import model.characterModels.SquarantineModel;
import model.characterModels.TrigorathModel;
import model.movement.Direction;
import view.menu.MainMenu;
import view.menu.TopMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static controller.UserInterfaceController.exitGame;
import static controller.UserInterfaceController.getMainMotionPanelId;
import static controller.constants.Variables.WAVE_ENEMY_COUNT;
import static controller.constants.WaveConstants.*;
import static model.Utils.multiplyPoint;
import static model.Utils.roundPoint;
import static view.containers.GlassFrame.getGlassFrame;

public class WaveManager {
    public final ArrayList<Integer> waveCount = WAVE_ENEMY_COUNT;
    private final ArrayList<GeoShapeModel> waveEntities = new ArrayList<>();

    public static TopMessage.MessageType getMessageType(int i) {
        return switch (i) {
            case 0 -> TopMessage.MessageType.STRING_ZERO;
            case 1 -> TopMessage.MessageType.STRING_ONE;
            case 2 -> TopMessage.MessageType.STRING_TWO;
            case 3 -> TopMessage.MessageType.STRING_THREE;
            case 4 -> TopMessage.MessageType.STRING_FOUR;
            case 5 -> TopMessage.MessageType.STRING_FIVE;
            case 6 -> TopMessage.MessageType.STRING_SIX;
            default -> null;
        };
    }    public final ArrayList<ActionListener> waves = new ArrayList<>(List.of(e -> wave1(), e -> wave2(), e -> wave3(), e -> finish()));

    public void start() {
        waveCheck(0).start();
    }

    public void wave1() {
        waveCheck(1).start();

    }

    public void wave2() {
        waveCheck(2).start();
    }

    public void wave3() {
        waveCheck(3).start();
    }

    public void finish() {
        exitGame();
        MainMenu.flushINSTANCE();
        MainMenu.getINSTANCE().togglePanel();
    }

    public void lockEnemies() {
        for (GeoShapeModel model : waveEntities) {
            if (!(model instanceof EpsilonModel)) {
                model.getMovement().lockOnTarget(EpsilonModel.getINSTANCE().modelId);
            }
        }
    }

    public void randomSpawn(int wave) {
        Random random = new Random();
        for (int i = 0; i < waveCount.get(wave); i++) {
            Point location = new Point(roundPoint(multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                    random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
            GeoShapeModel model;
            if (wave == 0) model = new SquarantineModel(location, getMainMotionPanelId());
            else {
                model = switch (random.nextInt(0, 2)) {
                    case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                    case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                    default -> null;
                };
            }
            if (model != null) waveEntities.add(model);
        }
    }

    public Timer waveCheck(int wave) {
        randomSpawn(wave);
        lockEnemies();
        Timer waveTimer = new Timer(10, null);
        waveTimer.addActionListener(e -> {
            boolean waveFinished = true;
            for (GeoShapeModel shapeModel : waveEntities) {
                if (shapeModel.health > 0) {
                    waveFinished = false;
                    break;
                }
            }
            if (waveFinished) {
                waveTimer.stop();
                waveEntities.clear();
                if (getMessageType(wave) != null) getGlassFrame().showMessage(getMessageType(NUMBER_OF_WAVES.getValue() - wave));
                if (waves.size() > wave) waves.get(wave).actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            }
        });
        return waveTimer;
    }


}
