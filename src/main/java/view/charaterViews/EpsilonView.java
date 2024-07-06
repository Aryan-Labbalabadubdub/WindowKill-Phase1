package view.charaterViews;

import view.containers.MotionPanelView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.DimensionConstants.EPSILON_DIMENSION;
import static controller.constants.FilePaths.EPSILON_IMAGEPATH;

public class EpsilonView extends GeoShapeView {
    public static BufferedImage bimage;
    public static EpsilonView INSTANCE;

    static {
        try {
            bimage = ImageIO.read(new File(EPSILON_IMAGEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EpsilonView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(bimage, EPSILON_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, true);
        INSTANCE = this;
    }
}
