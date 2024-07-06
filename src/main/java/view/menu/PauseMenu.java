package view.menu;

import javax.swing.*;

import static controller.AudioHandler.setAllVolumes;
import static controller.UserInterfaceController.exitGame;
import static controller.UserInterfaceController.toggleGameRunning;
import static controller.constants.DimensionConstants.PAUSE_MENU_DIMENSION;
import static controller.constants.UIConstants.*;
import static controller.constants.UIMessageConstants.*;
import static controller.constants.Variables.SOUND_SCALE;
import static view.containers.GlassFrame.getGlassFrame;

public class PauseMenu extends PanelB implements TopElement {
    private static PauseMenu INSTANCE;

    private PauseMenu() {
        super(PAUSE_MENU_DIMENSION.getValue());
        ButtonB resume = new ButtonB(ButtonB.ButtonType.small_menu_button, "RESUME", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                PauseMenu.getINSTANCE().togglePanel();
                toggleGameRunning();
            });
        }};
        SliderB volumeSlider = new SliderB(this, MIN_VOLUME.getValue(), MAX_VOLUME.getValue(), SOUND_SCALE, VOLUME_SLIDER_NAME.getValue());
        volumeSlider.addChangeListener(e -> {
            SOUND_SCALE = volumeSlider.getPreciseValue();
            setAllVolumes();
        });
        ButtonB exit = new ButtonB(ButtonB.ButtonType.small_menu_button, "EXIT", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                int action = JOptionPane.showConfirmDialog(getINSTANCE(), EXIT_GAME_MESSAGE.getValue(), EXIT_GAME_TITLE.getValue()
                        , JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (action == JOptionPane.YES_OPTION) {
                    exitGame();
                    PauseMenu.getINSTANCE().togglePanel();
                    MainMenu.flushINSTANCE();
                    MainMenu.getINSTANCE().togglePanel();
                }
            });
        }};
        constraints.gridwidth = 2;
        add(resume, false, true);
        constraints.gridwidth = 1;
        constraints.gridy++;
        horizontalBulkAdd(java.util.List.of(volumeSlider.labelButton, volumeSlider));
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add(exit, false, true);
    }

    public static PauseMenu getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new PauseMenu();
        return INSTANCE;
    }

    @Override
    public void repaint() {
        super.repaint();
        pinOnTop();
    }

    @Override
    public JFrame getFrame() {
        return getGlassFrame();
    }
}
