package view.menu;

import java.util.List;

import static controller.AudioHandler.setAllVolumes;
import static controller.constants.DimensionConstants.SETTINGS_MENU_DIMENSION;
import static controller.constants.UIConstants.*;
import static controller.constants.UIMessageConstants.GAME_SPEED_SLIDER_NAME;
import static controller.constants.UIMessageConstants.VOLUME_SLIDER_NAME;
import static controller.constants.Variables.GAME_SPEED;
import static controller.constants.Variables.SOUND_SCALE;

public class SettingsMenu extends PanelB {
    private static SettingsMenu INSTANCE;

    private SettingsMenu() {
        super(SETTINGS_MENU_DIMENSION.getValue());
        SliderB gameSpeedSlider = new SliderB(this, MIN_GAME_SPEED.getValue(), MAX_GAME_SPEED.getValue(), GAME_SPEED, GAME_SPEED_SLIDER_NAME.getValue());
        gameSpeedSlider.addChangeListener(e -> GAME_SPEED = gameSpeedSlider.getPreciseValue());

        SliderB volumeSlider = new SliderB(this, MIN_VOLUME.getValue(), MAX_VOLUME.getValue(), SOUND_SCALE, VOLUME_SLIDER_NAME.getValue());
        volumeSlider.addChangeListener(e -> {
            SOUND_SCALE = volumeSlider.getPreciseValue();
            setAllVolumes();
        });

        ButtonB back = new ButtonB(ButtonB.ButtonType.small_menu_button, "BACK", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                SettingsMenu.getINSTANCE().togglePanel();
                MainMenu.getINSTANCE().togglePanel();
            });
        }};
        constraints.gridwidth = 1;
        horizontalBulkAdd(List.of(gameSpeedSlider.labelButton, gameSpeedSlider));
        constraints.gridy++;
        constraints.gridx = 0;
        horizontalBulkAdd(List.of(volumeSlider.labelButton, volumeSlider));
        constraints.gridwidth = 2;
        add(back, false, true);
    }

    public static SettingsMenu getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SettingsMenu();
        return INSTANCE;
    }
}
