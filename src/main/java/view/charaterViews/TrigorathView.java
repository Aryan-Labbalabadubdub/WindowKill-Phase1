package view.charaterViews;

import view.containers.MotionPanelView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.DimensionConstants.TRIGORATH_DIMENSION;
import static controller.constants.FilePaths.TRIGORATH_IMAGEPATH;

public class TrigorathView extends GeoShapeView {

    public static BufferedImage bimage;

    static {
        try {
            bimage = ImageIO.read(new File(TRIGORATH_IMAGEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TrigorathView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(bimage, TRIGORATH_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, false);
    }
}
