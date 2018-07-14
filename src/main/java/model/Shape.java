package model;/*
 *  model.Shape: See ShapeDemo for an example how to use this class.
 *
 */

import javax.vecmath.Point2d;
import java.awt.*;
import java.util.ArrayList;

// simple shape model class
public class Shape {

    // shape is polyline or polygon
    Boolean isClosed = false;
    // some optimization to cache points for drawing
    Boolean pointsChanged = false; // dirty bit
    int[] xpoints, ypoints;

    // shape's transform
    int npoints = 0;
    // shape points
    private ArrayList<Point2d> points;
    // drawing attributes
    private Color colour;
    private float strokeThickness;

    public Shape(Color colour, float strokeThickness) {
        this.colour = colour;
        this.strokeThickness = strokeThickness;
        points = new ArrayList<Point2d>();
    }

    public void clearPoints() {
        points = new ArrayList<Point2d>();
        pointsChanged = true;
    }

    // add a point to end of shape
    public void addPoint(Point2d p) {
        if (points == null) clearPoints();
        points.add(p);
        pointsChanged = true;
    }

    // add a point to end of shape
    public void addPoint(double x, double y) {
        if (points == null) clearPoints();
        addPoint(new Point2d(x, y));
    }

//    // if polygon is filled or not  [fill color]
//    Boolean isFilled = false;
//
//    public Boolean getIsFilled() {
//        return isFilled;
//    }
//
//    public void setIsFilled(Boolean isFilled) {
//        this.isFilled = isFilled;
//    }

    public int npoints() {
        return points.size();
    }

//	public void setColour(Color colour) {
//		this.colour = colour;
//	}

    public Boolean getIsClosed() {
        return isClosed;
    }

//	public void setStrokeThickness(float strokeThickness) {
//		this.strokeThickness = strokeThickness;
//	}


    // shape's transform

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public void removePoints(int npointsRemain) {
        for (int i = points.size(); npointsRemain < i; i--) {
            points.remove(i - 1);
        }
        cachePointsArray();
    }

    public Color getColour() {
        return colour;
    }

    public float getStrokeThickness() {
        return strokeThickness;
    }

    public void cachePointsArray() {
        xpoints = new int[points.size()];
        ypoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xpoints[i] = (int) points.get(i).x;
            ypoints[i] = (int) points.get(i).y;
        }
        npoints = points.size();
        pointsChanged = false;
    }


    public int getNpoints() {
        return npoints;
    }

    public int[] getXpoints() {
        return xpoints;
    }

    public int[] getYpoints() {
        return ypoints;
    }

    public Boolean getPointsChanged() {
        return pointsChanged;
    }

    public ArrayList<Point2d> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point2d> points) {
        this.points = points;
        cachePointsArray();
    }

    // let shape handle its own hit testing
    // (x,y) is the point to test against
    // (x,y) needs to be in same coordinate frame as shape, you could add
    // a panel-to-shape transform as an extra parameter to this function
    // (note this isn't good separation of shape Controller from shape Model)    
    public boolean hittest(double x, double y) {
        if (points != null) {

            // TODO Implement

        }

        return false;
    }
}
