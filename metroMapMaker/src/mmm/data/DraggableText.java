/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import javafx.scene.text.Text;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableText extends Text implements Draggable {
    double startX;
    double startY;
    
    boolean isItalic;
    boolean isBold;
    
    boolean isLineLabel;
    boolean isStartLabel;
    public boolean isStationLabel;
    
    String name = "";
    
    public DraggableText() {
        
    }
    
    public DraggableText(String text) {
        setX(20.0);
	setY(10.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
        
        setText(text);
        
    }
    
    public void setItalic(boolean isItalic){
        this.isItalic = isItalic;
    }
    
    public void setBold(boolean isBold) {
        this.isBold =  isBold;
    }
    
    public void setIsLineLabel(boolean b) {
        this.isLineLabel =  b;
    }
    
    public void setIsStartLabel(boolean b) {
        this.isStartLabel =  b;
    }
    
    public boolean isItalic() {
        return isItalic;
    }
    
    public boolean isBold() {
        return isBold;
    }
    
    public boolean isLineLabel() {
        return isLineLabel;
    }
    
    public boolean isStartLabel() {
        return isStartLabel;
    }

    @Override
    public mmmState getStartingState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(int x, int y) {
        startX = x;
	startY = y;
	setX(x);
	setY(y);
    }

    @Override
    public void drag(int x, int y) {
        double diffX = x - getX() ;
	double diffY = y - getY() ;
	double newX = getX() + diffX;
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        xProperty().set(initX);
	yProperty().set(initY);
    }

    @Override
    public String getShapeType() {
       return TEXT;
    }

    @Override
    public String getName() {
       return name;
    }

    
}
