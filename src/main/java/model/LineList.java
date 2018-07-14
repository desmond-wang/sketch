package model;

import javax.vecmath.Point2d;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LineList {
    /**
     * The observers that are watching this model for changes.
     */
    private List<Observer> observers;
    private List<Shape> lines;
    private float curXScale;
    private float curYScale;
    private Color curColor;
    private float strokeThickness;
    private int percentage;

    public LineList() {
        this.observers = new ArrayList<>();
        this.lines = new ArrayList<>();
        curColor = Color.BLACK;
        curXScale = 1.0f;
        curYScale = 1.0f;
        strokeThickness = 3.0f;
        percentage = 0;

    }


    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer : this.observers) {
            observer.update(this);
        }
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        notifyObservers();
    }

    public void addLines(Shape line) {

        int currentMax = getMax();
        if (percentage != currentMax) {
            int currentStroke = percentage / 1000;
            for (int i = currentMax / 1000; currentStroke+1 < i ; --i) {
                lines.remove(i - 1);
            }
            Shape oldLine = lines.get(currentStroke);
            int percentageOfRest = percentage % 1000;
            int len = oldLine.getNpoints();
            int npoints = len * percentageOfRest / 1000;
            oldLine.removePoints(npoints);
        }

        lines.add(line);
        percentage = getMax();
        notifyObservers();
    }

    public Color getCurColor() {
        return curColor;
    }

    public void setCurColor(Color curColor) {
        this.curColor = curColor;
        notifyObservers();
    }

    public float getStrokeThickness() {
        return strokeThickness;
    }

    public void setStrokeThickness(float strokeThickness) {
        if (strokeThickness < 0) {
            return;
        }
        this.strokeThickness = strokeThickness;
        notifyObservers();
    }

    public void setLines(int x, int y) {
        int size = lines.size();
        lines.get(size - 1).addPoint(x, y);
        notifyObservers();
    }

    public List<Shape> getLines() {
        return lines;
    }

    public float getCurXScale() {
        return curXScale;
    }

    public void setCurXScale(float curScale) {
        this.curXScale = curScale;
    }

    public float getCurYScale() {
        return curYScale;
    }

    public void setCurYScale(float curYScale) {
        this.curYScale = curYScale;
    }

    public void emptyLines() {
        this.lines = new ArrayList<>();
        notifyObservers();
    }

    public int getMax() {
        return lines.size() * 1000;
    }

    public void Export(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            String xscale = Float.toString(this.curXScale);
            String yscale = Float.toString(this.curYScale);
            String p = Integer.toString(this.percentage);
            writer.println(xscale);
            writer.println(yscale);
            writer.println(p);

            for (Shape line : lines) {
                String color = line.getColour().getRed() + " " + line.getColour().getGreen() + " "
                        + line.getColour().getBlue() + " " + line.getColour().getAlpha();
                String thickness = Float.toString(line.getStrokeThickness());

                writer.println(color);
                writer.println(thickness);
                StringBuilder Xpoints = new StringBuilder();
                StringBuilder Ypoints = new StringBuilder();
                for (int i = 0; i < line.getPoints().size(); i++) {
                    Xpoints.append(line.getPoints().get(i).x);
                    Xpoints.append(' ');
                    Ypoints.append(line.getPoints().get(i).y);
                    Ypoints.append(' ');
                }
                writer.println(Xpoints);
                writer.println(Ypoints);
            }
        }
    }

    public void Import(File rawfile) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(rawfile))) {
            emptyLines();
            String line;

            line = bufferedReader.readLine();
            this.setCurXScale(Float.parseFloat(line));
            line = bufferedReader.readLine();
            this.setCurYScale(Float.parseFloat(line));
            line = bufferedReader.readLine();
            this.setPercentage(Integer.parseInt(line));

            while ((line = bufferedReader.readLine()) != null) {
                String[] rgba = line.split(" ");
                Color color = new Color(Integer.parseInt(rgba[0]), Integer.parseInt(rgba[1]),
                        Integer.parseInt(rgba[2]), Integer.parseInt(rgba[3]));

                line = bufferedReader.readLine();
                float thickness = Float.parseFloat(line);

                Shape shape = new Shape(color, thickness);
                String[] xpoints = bufferedReader.readLine().split(" ");
                String[] ypoints = bufferedReader.readLine().split(" ");
                ArrayList<Point2d> points = new ArrayList<>();
                for (int i = 0; i < xpoints.length; ++i) {
                    points.add(new Point2d(Float.parseFloat(xpoints[i]), Float.parseFloat(ypoints[i])));
                }

                shape.setPoints(points);
                lines.add(shape);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + rawfile + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + rawfile + "'");
        }
        percentage = getMax();
        notifyObservers();
    }

    public void reset() {
        this.setCurColor(Color.BLACK);
        this.setCurXScale(1.0f);
        this.setCurYScale(1.0f);
        this.setStrokeThickness(3.0f);
        this.emptyLines();
        this.percentage = 0;
        notifyObservers();
    }
}
