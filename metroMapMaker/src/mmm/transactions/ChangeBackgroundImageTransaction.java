/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;

/**
 *
 * @author naimyoussiftraore
 */
public class ChangeBackgroundImageTransaction implements jTPS_Transaction {
    
    mmmData dataManager;
//    mmmWorkspace workspace = null;
    Image newImage;
    Image oldImage;
    Background bg;
    Pane canvas;
    
    public ChangeBackgroundImageTransaction(Pane canvas, Image image, mmmData data) {
        //this.app = app;
        this.dataManager = data;
        //this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.newImage = image;

        //(Color)(workspace.getCanvas().getBackground().getFills().get(0).getFill()) != null
//        if (canvas == null) {
//            System.out.println("Null");
//        } else {
//
//            try {
//                this.oldImage = ((Image) (canvas.getBackground().getFills().g));
//                
//            } catch (NullPointerException e) {
//                
//                this.oldColor = Color.WHITE;
//            }
////            if ((Color) (canvas.getBackground().getFills().get(0).getFill()) != null) {
////                this.oldColor = ((Color) (canvas.getBackground().getFills().get(0).getFill()));
////            } else {
////                this.oldColor = Color.WHITE;
////            }
//        }

    }
    
    
     @Override
    public void doTransaction() {
        if (newImage != null) {
            dataManager.setBackgroundImage(newImage);
            //app.getGUI().updateToolbarControls(false);
        }

    }

    @Override
    public void undoTransaction() {
        if (oldImage != null) {
            dataManager.setBackgroundImage(oldImage);
            //app.getGUI().updateToolbarControls(true);
        }
    }
    
}
