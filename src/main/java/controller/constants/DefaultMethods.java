package controller.constants;

import javax.sound.sampled.Clip;

import static controller.AudioHandler.clips;

public class DefaultMethods {
    //SIN,COS PRE-PROCESSING//
    public static double[] sinTable = new double[360];
    public static double[] cosTable = new double[360];
    public static double[] radianTable = new double[360];

    static {
        for (int i = 0; i < 360; i++) {
            double radian = Math.toRadians(i);
            DefaultMethods.sinTable[i] = Math.sin(radian);
            DefaultMethods.cosTable[i] = Math.cos(radian);
            DefaultMethods.radianTable[i] = radian;
        }
    }

    public static float getVolumeDB(Clip clip) {
        return switch (clips.get(clip)) {
            case MENU_THEME -> 0.1f;
            case GAME_THEME -> 0.082f;
            case SHOOT -> 0.05f;
            case COUNTDOWN -> 0.235f;
            default -> 0.172f;
        };
    }

    public static String PURCHASE_MESSAGE(int cost) {
        return "Do you want to purchase this skill for " + cost + " XP?";
    }

    public static String SUCCESSFUL_PURCHASE_MESSAGE(String name) {
        return "You learned " + name + ".";
    }

    public static String ACTIVATE_MESSAGE(String name) {
        return "Do you want to choose \"" + name + "\" as your active skill?";
    }
}
