package view.charaterViews;

import view.containers.MotionPanelView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.DimensionConstants.SQUARANTINE_DIMENSION;
import static controller.constants.FilePaths.SQUARANTINE_IMAGEPATH;

public class SquarantineView extends GeoShapeView {
    public static BufferedImage bimage;

    static {
        try {
            bimage = ImageIO.read(new File(SQUARANTINE_IMAGEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SquarantineView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(bimage, SQUARANTINE_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, false);
    }
}
