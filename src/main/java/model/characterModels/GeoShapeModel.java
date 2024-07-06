package model.characterModels;

import model.collision.Collidable;
import model.entityModel.Entity;
import model.movement.Movable;
import model.movement.Movement;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static controller.UserInterfaceController.moveGeoShape;
import static controller.UserInterfaceController.rotateGeoShape;
import static controller.constants.DefaultMethods.cosTable;
import static controller.constants.DefaultMethods.sinTable;
import static model.Utils.*;

public class GeoShapeModel extends Entity implements Collidable, Movable {
    public volatile static ArrayList<GeoShapeModel> allShapeModelsList = new ArrayList<>();
    public boolean isCircular;
    public String modelId;
    public Point2D anchorSave;
    public ArrayList<Point2D> vertices;
    public ArrayList<Point2D> verticesSave;
    public Movement movement;
    public String motionPanelId;
    double totalRotation = 0;
    Geometry geometry;

    public GeoShapeModel(Point2D anchor, ArrayList<Point2D> vertices, int health) {
        setVerticesSave(vertices);
        this.fullHealth = health;
        this.health = health;
        this.modelId = UUID.randomUUID().toString();
        vulnerable = true;
        movement = new Movement(modelId, anchor);
        allShapeModelsList.add(this);
        Collidable.collidables.add(this);
    }

    public void placeVertices(int n) {
        if (isCircular) {
            ArrayList<Point2D> newVertices = new ArrayList<>();
            float step = 360f / n;
            for (int i = 0; i < n; i++) {
                float angleModified = step * i;
                angleModified = (float) (angleModified - Math.floor(angleModified / 360) * 360);
                Point2D vertex = new Point2D.Float((float) (getRadius() * cosTable[(int) angleModified]), (float) (getRadius() * sinTable[(int) angleModified]));
                vertex = addUpPoints(vertex, anchorSave);
                newVertices.add(vertex);
            }
            setVerticesSave(newVertices);
        }
    }

    public void addVertex() {
        if (isCircular) placeVertices(verticesSave.size() + 1);
    }

    public void setVerticesSave(ArrayList<Point2D> verticesSave) {
        this.vertices = deepCloneList(verticesSave);
        this.verticesSave = deepCloneList(verticesSave);
        createGeometry();
    }

    @Override
    public void moveShapeModel(Point2D newAnchor) {
        for (int i = 0; i < verticesSave.size(); i++) vertices.set(i, addUpPoints(verticesSave.get(i), relativeLocation(newAnchor, anchorSave)));
        moveGeoShape(modelId, movement.getAnchor());
    }

    @Override
    public void rotateShapeModel(double currentRotation) {
        totalRotation -= currentRotation;
        for (int i = 0; i < vertices.size(); i++)
            vertices.set(i, addUpPoints(relativeLocation(getMovement().getAnchor(), anchorSave),
                    rotateAbout(verticesSave.get(i), anchorSave, totalRotation)));
        rotateGeoShape(modelId, currentRotation);
    }

    @Override
    public void createGeometry() {
        if (verticesSave.size() != 0) {
            Coordinate[] coordinates = new Coordinate[verticesSave.size() + 1];
            for (int i = 0; i < vertices.size(); i++) coordinates[i] = toCoordinate(vertices.get(i));
            coordinates[vertices.size()] = toCoordinate(vertices.get(0));
            geometry = new GeometryFactory().createLineString(coordinates);
        } else geometry = new GeometryFactory().createLineString(new Coordinate[0]);
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public boolean isCircular() {
        return isCircular;
    }

    @Override
    public float getRadius() {
        return (float) anchorSave.getX();
    }

    @Override
    public Point2D getAnchor() {
        return movement.getAnchor();
    }

    @Override
    public Point2D getLastAnchor() {
        return movement.getLastAnchor();
    }

    @Override
    public float getSpeed() {
        return movement.speed;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return collidable instanceof GeoShapeModel;
    }

    @Override
    public long getPositionUpdateTimeDiffCapture() {
        return this.movement.positionUpdateTimeDiffCapture;
    }

    @Override
    public void setPositionUpdateTimeDiffCapture(long time) {
        this.movement.positionUpdateTimeDiffCapture = time;
    }

    @Override
    public long getLastPositionUpdateTime() {
        return this.movement.lastAnchorUpdateTime;
    }

    @Override
    public void setLastPositionUpdateTime(long time) {
        this.movement.lastAnchorUpdateTime = time;
    }

    public Point2D getCenter() {
        Point2D sumPoint = new Point2D.Float(0, 0);
        for (Point2D vertex : vertices) sumPoint = addUpPoints(sumPoint, vertex);
        return new Point2D.Float((float) sumPoint.getX() / vertices.size(), (float) sumPoint.getY() / vertices.size());
    }

    public Movement getMovement() {
        return movement;
    }

    public String getModelId() {
        return modelId;
    }

    @Override
    protected String getMotionPanelId() {
        return motionPanelId;
    }
}
