package ui;

import model.LineList;
import model.Observer;
import model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

// import java.awt.*;

public class DrawImage extends JComponent implements Observer {
    private Shape line;
    private LineList lineList;
    private static final float initScale = 100;

    DrawImage(LineList lines) {
        lineList = lines;
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                line = new Shape(lines.getCurColor(), lines.getStrokeThickness());
                // change shape type
//                shape.setIsClosed(true);
//                shape.setIsFilled(true);
                lineList.addLines(line);
                lineList.setLines((int)(e.getX()/lines.getCurXScale()), (int)(e.getY()/lines.getCurYScale()));
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                lineList.setLines((int)(e.getX()/lines.getCurXScale()), (int)(e.getY()/lines.getCurYScale()));
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //System.out.println(getWidth()+ " " + getHeight());
                lines.setCurXScale(getWidth() / 1000f);
                lines.setCurYScale(getHeight() / 1000f);

            }
        });

        lineList.addObserver(this);

    }


    private void paintLine(Graphics2D g2, Shape line, int[] xpoints, int[] ypoints, int npoints){
        g2.setColor(line.getColour());
        g2.setStroke(new BasicStroke(line.getStrokeThickness()));

        if (line.getIsClosed())
            g2.drawPolygon(xpoints, ypoints, npoints);
        else
            g2.drawPolyline(xpoints, ypoints, npoints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);
//
        // multiply in this shape's transform
        // (uniform scale)

        float xscale = lineList.getCurXScale();
        float yscale = lineList.getCurYScale();
        g2.scale(xscale, yscale);


        int curPercentage = lineList.getPercentage();
        int numOfFullStroke = curPercentage / 1000;
        int percentageOfRest = curPercentage % 1000;

        for (int i = 0; i < numOfFullStroke; i++) {
            if (lineList.getLines().size() == 0){
                return;
            }
            Shape line = lineList.getLines().get(i);

            if (line.getPointsChanged()) line.cachePointsArray();
            paintLine(g2,line,line.getXpoints(), line.getYpoints(), line.getNpoints());
//        }
        }
        if (percentageOfRest != 0) {
            Shape line = lineList.getLines().get(numOfFullStroke);
            int len = line.getNpoints();
            int npoints = len * percentageOfRest / 1000;
            int[] xpoints = new int[npoints];
            int[] ypoints = new int[npoints];
            for (int i = 0; i < npoints; i++) {
                xpoints[i] = (int) line.getPoints().get(i).x;
                ypoints[i] = (int) line.getPoints().get(i).y;
            }
            paintLine(g2,line,xpoints, ypoints, npoints);
        }


        // reset the transform to what it was before we drew the shape
//        g2.setTransform(M);
    }

    @Override
    public void update(Object observable) {
        repaint();
    }
}

