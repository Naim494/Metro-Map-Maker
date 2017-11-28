/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

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
   
    
    public DraggableStation(){
        
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
	setCenterX(newX);
	setCenterY(newY);
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
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadiusX(initWidth/2);
	setRadiusY(initHeight/2);
    }
    
    @Override
    public String getShapeType() {
	return STATION;
    }

    @Override
    public String getName() {
        return name;
    }
}
