package view.containers;

import view.charaterViews.GeoShapeView;
import view.menu.PanelB;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static controller.constants.ViewConstants.VERTEX_OFFSET;
import static controller.constants.ViewConstants.VERTEX_RADIUS;
import static view.containers.GlassFrame.getGlassFrame;

public class MotionPanelView extends PanelB {
    public volatile static MotionPanelView mainMotionPanelView;
    public volatile static ArrayList<MotionPanelView> allMotionPanelViewsList = new ArrayList<>();
    public volatile ArrayList<GeoShapeView> shapeViews = new ArrayList<>();
    public String viewId;

    public MotionPanelView(Dimension size, Point location) {
        super(size.width, size.height, null);
        if (mainMotionPanelView == null) mainMotionPanelView = this;
        setBackground(new Color(0, 0, 0, 0));
        setSize(size);
        setDoubleBuffered(true);
        setLocation(location);
        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        allMotionPanelViewsList.add(this);
        getGlassFrame().add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        for (GeoShapeView shapeView : new ArrayList<>(shapeViews)) {
            shapeView.rotatedIcon.paintIcon(this, g, 0, 0);
            for (Point point : shapeView.vertexLocations) {
                g.fillOval((int) (point.x - VERTEX_OFFSET.getValue()), (int) (point.y - VERTEX_OFFSET.getValue()),
                        (int) VERTEX_RADIUS.getValue(), (int) VERTEX_RADIUS.getValue());
            }
        }
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
