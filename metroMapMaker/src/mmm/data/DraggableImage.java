/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableImage extends Rectangle implements Draggable {

    Image image;
    String imagePath;
    File imageFile;
    String name = "";

    public DraggableImage() {
        
    }
        
    public DraggableImage(Image image, File imageFile) {
        this.image = image;
        this.imageFile = imageFile;
        setWidth(image.getWidth());
        setHeight(image.getHeight());
        setFill(new ImagePattern(image));

    }
    
    public Image getImage(){
        return image;
    }
    
    public String getPath(){
        return imagePath;
    }
    
    public File getImageFile(){
        return imageFile;
    }

    @Override
    public String getShapeType() {
	return IMAGE;
    }

    @Override
    public mmmState getStartingState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drag(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void size(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }
}
