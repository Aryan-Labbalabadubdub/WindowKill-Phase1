package view;

import view.charaterViews.GeoShapeView;
import view.containers.MotionPanelView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static controller.constants.DimensionConstants.BULLET_DIMENSION;
import static controller.constants.FilePaths.BULLET_IMAGEPATH;


public class BulletView extends GeoShapeView {
    public static BufferedImage bimage;

    static {
        try {
            bimage = ImageIO.read(new File(BULLET_IMAGEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BulletView(Point relativeAnchorLocation, MotionPanelView motionPanelView) {
        super(bimage, BULLET_DIMENSION.getValue(), relativeAnchorLocation, motionPanelView, true);
    }

}
