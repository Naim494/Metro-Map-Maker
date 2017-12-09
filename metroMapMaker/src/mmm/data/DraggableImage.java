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
import static mmm.data.Draggable.IMAGE;

/**
 *
 * @author naimyoussiftraore
 */
public class DraggableImage extends DraggableRectangle {

    Image image;
    String imagePath;
    File imageFile;

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
}
