/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableStation extends Ellipse implements Draggable {

    double startCenterX;
    double startCenterY;
    String name;
    public DraggableText label = new DraggableText();
    private String lineOn = "";
    public String northLine = "";
    public String southLine = "";
    public DoubleProperty xOffset = new SimpleDoubleProperty(15);
    public DoubleProperty yOffset = new SimpleDoubleProperty(5);
    

    public DraggableStation() {

    }

    public DraggableStation(String name) {
        this.name = name;
        setCenterX(30.0);
        setCenterY(80.0);
        setRadiusX(10.0);
        setRadiusY(10.0);
        setOpacity(1.0);
        setStroke(Color.BLACK);
        setFill(Color.WHITE);
        startCenterX = 30.0;
        startCenterY = 80.0;
        label.setText(name);

        label.setX(30.0);
        label.setY(80.0);
        label.isStationLabel = true;
        
        DoubleProperty xOffset = new SimpleDoubleProperty(15);
        DoubleProperty yOffset = new SimpleDoubleProperty(5);

        this.centerXProperty().bind(label.xProperty().subtract(xOffset));
        this.centerYProperty().bind(label.yProperty().add(yOffset));

    }

    @Override
    public mmmState getStartingState() {
        return mmmState.STARTING_ELLIPSE;
    }

    @Override
    public void start(int x, int y) {
        startCenterX = x;
        startCenterY = y;
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - startCenterX;
        double diffY = y - startCenterY;
        double newX = getCenterX() + diffX;
        double newY = getCenterY() + diffY;
//        setCenterX(newX);
//        setCenterY(newY);
        startCenterX = x;
        startCenterY = y;
    }

    @Override
    public void size(int x, int y) {
        double width = x - startCenterX;
        double height = y - startCenterY;
        double centerX = startCenterX + (width / 2);
        double centerY = startCenterY + (height / 2);
        setCenterX(centerX);
        setCenterY(centerY);
        setRadiusX(width / 2);
        setRadiusY(height / 2);

    }

    @Override
    public double getX() {
        return getCenterX() - getRadiusX();
    }

    @Override
    public double getY() {
        return getCenterY() - getRadiusY();
    }

    @Override
    public double getWidth() {
        return getRadiusX() * 2;
    }

    @Override
    public double getHeight() {
        return getRadiusY() * 2;
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        setCenterX(initX + (initWidth / 2));
        setCenterY(initY + (initHeight / 2));
        setRadiusX(initWidth / 2);
        setRadiusY(initHeight / 2);
    }

    @Override
    public String getShapeType() {
        return STATION;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object clone() {

        DraggableStation clone = new DraggableStation(this.name);

//        clone.setCenterX(30.0);
//        clone.setCenterY(80.0);
        clone.setRadiusX(10.0);
        clone.setRadiusY(10.0);
        clone.setOpacity(1.0);
        clone.setStroke(Color.BLACK);
        clone.setFill(Color.WHITE);
        clone.startCenterX = 30.0;
        clone.startCenterY = 80.0;
        clone.label.setText(name);

        DoubleProperty xOffset = new SimpleDoubleProperty(15);
        DoubleProperty yOffset = new SimpleDoubleProperty(5);
        
        clone.centerXProperty().bind(clone.label.xProperty().subtract(xOffset));
        clone.centerYProperty().bind(clone.label.yProperty().add(yOffset));
        
        clone.label.isStationLabel = true;

        return clone;
    }
    
    public void processRotateLabel(int counter) {
        
        if(counter % 2 == 0 )
            label.setRotate(0);
        else
            label.setRotate(-90);
    }

    public void processMoveLabel(int corner) {

        DoubleProperty xOffset = null;
        DoubleProperty yOffset = null;

        if (corner == 0) {

            xOffset = new SimpleDoubleProperty(15);
            yOffset = new SimpleDoubleProperty(5);

            this.centerXProperty().bind(label.xProperty().subtract(xOffset));
            this.centerYProperty().bind(label.yProperty().add(yOffset));

        } else if (corner == 1) {

            xOffset = new SimpleDoubleProperty(35);
            yOffset = new SimpleDoubleProperty(5);

            this.centerXProperty().bind(label.xProperty().add(xOffset));
            this.centerYProperty().bind(label.yProperty().add(yOffset));

        } else if (corner == 2) {

            xOffset = new SimpleDoubleProperty(35);
            yOffset = new SimpleDoubleProperty(20);

            this.centerXProperty().bind(label.xProperty().add(xOffset));
            this.centerYProperty().bind(label.yProperty().subtract(yOffset));

        } else if (corner == 3) {

            xOffset = new SimpleDoubleProperty(15);
            yOffset = new SimpleDoubleProperty(20);

            this.centerXProperty().bind(label.xProperty().subtract(xOffset));
            this.centerYProperty().bind(label.yProperty().subtract(yOffset));

        }

    }
    
    public void setLineOn(String name) {
        this.lineOn = name;
    }
    
    public String getLineOn() {
        return lineOn;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}
