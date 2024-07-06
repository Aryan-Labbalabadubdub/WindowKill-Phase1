package controller.constants;

public enum UIMessageConstants {
    GAME_SPEED_SLIDER_NAME, VOLUME_SLIDER_NAME, EXIT_MESSAGE, EXIT_TITLE, PURCHASE_TITLE,
    SUCCESSFUL_PURCHASE_TITLE, ACTIVATE_TITLE, TUTORIAL_MESSAGE, TUTORIAL_TITLE, EXIT_GAME_MESSAGE,
    EXIT_GAME_TITLE;

    public String getValue() {
        return switch (this) {

            case GAME_SPEED_SLIDER_NAME -> "Game Speed";
            case VOLUME_SLIDER_NAME -> "Master Volume";
            case EXIT_MESSAGE -> "Are you sure to exit the game?";
            case EXIT_TITLE -> "Confirm Exit";
            case PURCHASE_TITLE -> "Confirm Purchase";
            case SUCCESSFUL_PURCHASE_TITLE -> "Skill Acquired";
            case ACTIVATE_TITLE -> "Confirm Activation";
            case TUTORIAL_MESSAGE -> """
                    Use WASD to move your character\s
                    Use LMB to shoot at your enemies\s
                    Each enemy has a certain amount of HP\s
                    Entities fade in color as they lose HP\s
                    Nearby collisions emit impact waves. Use them in your favor\s
                    Enemies become more and more as time passes. Be fast to survive.\s
                    Use SHIFT to use your selected skill. Good Luck!
                    """;
            case TUTORIAL_TITLE -> "Game Tutorial";
            case EXIT_GAME_MESSAGE -> "Are you sure to exit the game?\nAll progress will be lost.";
            case EXIT_GAME_TITLE -> "Exit Game";
        };
    }
}
