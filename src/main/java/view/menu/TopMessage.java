package view.menu;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.playCountdownEffect;
import static controller.constants.DimensionConstants.SCREEN_SIZE;
import static controller.constants.UIConstants.*;
import static view.Utils.changeColorOpacity;
import static view.containers.GlassFrame.getGlassFrame;

public class TopMessage extends JLabel implements TopElement {
    private final JFrame frame;
    private final Color foregroundColor = BLOOD_RED.darker().darker();
    private float opacity = 1f;

    public TopMessage(MessageType type) {
        this(type, getGlassFrame());
    }

    public TopMessage(MessageType type, JFrame frame) {
        super();
        this.frame = frame;
        setBackground(new Color(0, 0, 0, opacity));
        setFont(MANTINIA_FONT.deriveFont(MESSAGE_FONT_SIZE.getValue()));
        setForeground(foregroundColor);
        setText(type.toString());
        setSize(SCREEN_SIZE.getValue().width, (int) MESSAGE_HEIGHT.getValue());
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        frame.setEnabled(false);
        frame.getContentPane().add(this);

        float length = 2 + playCountdownEffect(Arrays.stream(MessageType.values()).toList().indexOf(type));
        long exactLength = (long) (TimeUnit.SECONDS.toNanos(1) * length);
        long startTime = System.nanoTime();
        Timer fadeTimer = new Timer((int) MESSAGE_FADE_INTERVAL.getValue(), null);
        fadeTimer.addActionListener(e -> {
            long elapsedTime = System.nanoTime() - startTime;
            pinOnTop();
            opacity = fadeCurve((float) elapsedTime / exactLength);
            setSize(SCREEN_SIZE.getValue().width, (int) MESSAGE_HEIGHT.getValue());
            setLocation((SCREEN_SIZE.getValue().width - getWidth()) / 2, (SCREEN_SIZE.getValue().height - getHeight()) / 2);
            setBackground(new Color(0, 0, 0, opacity));
            setForeground(changeColorOpacity(foregroundColor, opacity));
            if (elapsedTime > exactLength) {
                frame.getContentPane().remove(this);
                this.setVisible(false);
                frame.setEnabled(true);
                fadeTimer.stop();
            }
        });
        fadeTimer.start();
    }

    /**
     * A map [0,1]->[0,1] to demonstrate the fade process
     *
     * @return momentary opacity
     */
    public float fadeCurve(float x) {
        if (x < 0 || x > 1) return 0;
        return (float) (0.3f * Math.sqrt(1 - x * x) + 0.7f * (1 - Math.pow(x, 5)));
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }

    public enum MessageType {
        STRING_ZERO("NIHIL"),
        STRING_ONE("UNUS"),
        STRING_TWO("DUO"),
        STRING_THREE("TRES"),
        STRING_FOUR("QUATTUOR"),
        STRING_FIVE("QUINQUE"),
        STRING_SIX("SEX");
        private final String text;

        MessageType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
