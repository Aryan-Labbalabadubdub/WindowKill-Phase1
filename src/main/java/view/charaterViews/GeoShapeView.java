package view.charaterViews;

import view.Utils;
import view.containers.MotionPanelView;
import view.containers.RotatedIcon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.Utils.relativeLocation;
import static view.Utils.rotatedInfo;

public class GeoShapeView {
    public volatile static ArrayList<GeoShapeView> allShapeViewsList = new ArrayList<>();
    public String viewId;
    public RotatedIcon rotatedIcon;
    public ArrayList<Point> vertexLocations = new ArrayList<>();
    Dimension viewSize;
    Point relativeAnchorLocation;
    boolean isCircular;
    BufferedImage image;

    public GeoShapeView(BufferedImage image, Dimension viewSize, Point relativeAnchorLocation, MotionPanelView motionPanelView, boolean isCircular) {
        BufferedImage resized = Utils.toBufferedImage(image.getScaledInstance(viewSize.width, viewSize.height, Image.SCALE_SMOOTH));
        this.viewSize = viewSize;
        this.relativeAnchorLocation = relativeAnchorLocation;
        this.image = image;
        this.rotatedIcon = new RotatedIcon(Utils.bufferedImageClone(resized), new Point(relativeAnchorLocation), 0, isCircular);
        this.isCircular = isCircular;
        allShapeViewsList.add(this);
        motionPanelView.shapeViews.add(this);
    }

    public void shrinkView(float scale) {
//        Dimension newSize=new Dimension((int) (viewSize.width*scale), (int) (viewSize.height*scale));
//        if (newSize.width<=0 || newSize.height<=0) return;
//        this.relativeAnchorLocation=roundPoint(multiplyPoint(relativeAnchorLocation,scale));
//        this.viewSize=newSize;
//        BufferedImage resized= Utils.toBufferedImage(image.getScaledInstance(viewSize.width,viewSize.height,Image.SCALE_SMOOTH));
//        this.rotatedIcon =new RotatedIcon(Utils.bufferedImageClone(resized),new Point(relativeAnchorLocation),0,isCircular);
    }

    public void moveShapeView(Point newAnchorLocation) {
        rotatedIcon.corner = (Point) relativeLocation(newAnchorLocation, rotatedIcon.getRotationAnchor());
    }

    public void rotateShapeView(double angle) {
        rotatedIcon.rotate(angle);
        Dimension viewSizeSave = new Dimension(rotatedIcon.icon.getIconWidth(), rotatedIcon.icon.getIconHeight());
        Point[] rotatedInfo = rotatedInfo(viewSizeSave, rotatedIcon.getRotationAnchor(), rotatedIcon.degrees, rotatedIcon.isCircular);
        rotatedIcon.offset = new Point(rotatedIcon.corner.x - rotatedInfo[1].x, rotatedIcon.corner.y - rotatedInfo[1].y);
        rotatedIcon.width = rotatedInfo[0].x;
        rotatedIcon.height = rotatedInfo[0].y;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public RotatedIcon getRotatedIcon() {
        return rotatedIcon;
    }
}
