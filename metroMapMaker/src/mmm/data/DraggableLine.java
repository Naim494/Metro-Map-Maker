/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableLine extends Line implements Draggable {

    double startX;
    double startY;
    public DraggableText label1 = new DraggableText();
    public DraggableText label2 = new DraggableText();
    String name;
    public Color color;
    ArrayList<DraggableStation> stations = new ArrayList<DraggableStation>();
    private DraggableLine extension = null;
    boolean isExtension = false;
    
     public DraggableLine() {
         
     }

    public DraggableLine(DraggableStation s) {

        setStrokeWidth(5.0);
        startXProperty().bind(label1.xProperty());
        startYProperty().bind(label1.yProperty());
        endXProperty().bind(s.centerXProperty());
        endYProperty().bind(s.centerYProperty());
        isExtension = true;
    }

    public DraggableLine(String name, Color color) {
        setStrokeWidth(5.0);
        this.name = name;
        this.label1.setText(name);
        this.label2.setText(name);
        this.color = color;
        setStroke(color);
        setFill(color);
        System.out.print(name);

        this.label1.setX(20.0);
        this.label1.setY(10.0);
        this.label2.setX(20.0);
        this.label2.setY(50.0);

//        setStartX(20.0);
//        setStartY(10.0);
//        setEndX(20.0);
//        setEndY(50.0);

        startX = 0.0;
        startY = 0.0;

        startXProperty().bind(label1.xProperty());
        startYProperty().bind(label1.yProperty());
        endXProperty().bind(label2.xProperty());
        endYProperty().bind(label2.yProperty());


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
//        double diffX = x - getLayoutX();
//        double diffY = y - getLayoutY();
//        double newX = getLayoutX() + diffX;
//        double newY = getLayoutY() + diffY;
//        startXProperty().set(newX);
//
//        startYProperty().set(newY);
//        
//        endXProperty().set(newX);
//        endYProperty().set(newY + 40);
//        
//        System.out.print(newX + " " + newY);
//        
//        
//        //layoutXProperty().set(newX);
//        //layoutYProperty().set(newY);
//        startX = x;
//        startY = y;

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
        startXProperty().set(initX);
        startYProperty().set(initY);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
        setStroke(color);
        setFill(color);
    }

    @Override
    public String getName() {
        return name;
    }
    
    public DraggableLine getExtension(){
        return extension;
    
    }

    public void setExtension(DraggableLine e){
        this.extension = e;
    }
}
