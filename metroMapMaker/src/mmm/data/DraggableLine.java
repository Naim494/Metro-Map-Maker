/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableLine extends Polyline implements Draggable {

    double startX;
    double startY;
    String name;
    ArrayList<DraggableStation> stations;
    

    public DraggableLine(String name, Color color) {
        this.name = name;
        setStroke(color);
        setFill(color);
        getPoints().addAll(new Double[]{
            20.0, 10.0,
            20.0, 50.0});
        
        setStrokeWidth(5.0);
        
        startX = 0.0;
        startY = 0.0;
        
        
       
    }

    @Override
    public mmmState getStartingState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(int x, int y) {
        startX = x;
	startY = y;
	//setX(x);
	//setY(y);
        //layoutXProperty().set(x);
        //layoutYProperty().set(y);
        
       
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - getLayoutX() ;
	double diffY = y - getLayoutY() ;
	double newX = getLayoutX() + diffX;
	double newY = getLayoutY() + diffY;
	//xProperty().set(newX);
	//yProperty().set(newY);
        layoutXProperty().set(newX);
        layoutYProperty().set(newY);
        startX = x;
	startY = y;
        
        ObservableList<Double> points = this.getPoints();
        
        int c = 1;
        double y1 = 0;
        
        for(double p : points) {
            
            if(c%2 == 0) {
                if(c == 2){
                    p = newY;
                 
                }
                else 
                    p = y1 + 40.0;
            }
            else{
                p = newX;
            }
            
            c = c + 1;
              
        }
               
//        this.getPoints().clear();
//        
//        for(double z : points) {
//            
//            this.getPoints().add(z);
//        }
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getX() {
        return getLayoutX();
    }

    @Override
    public double getY() {
       return getLayoutX();
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
       layoutXProperty().set(initX);
        layoutYProperty().set(initY);
	//widthProperty().set(initWidth);
	//heightProperty().set(initHeight);emplates.
    }

    @Override
    public String getShapeType() {
        return LINE;
        
    }
    
    public ArrayList<DraggableStation> getStations() {
        return stations;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setColor(Color color) {
        setStroke(color);
        setFill(color);
    } 

    @Override
    public String getName() {
        return name;
    }

}
