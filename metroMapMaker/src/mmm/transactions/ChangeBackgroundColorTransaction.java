/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class ChangeBackgroundColorTransaction implements jTPS_Transaction {
//    AppTemplate app;

    mmmData dataManager;
//    mmmWorkspace workspace = null;
    Color newColor;
    Color oldColor;
    Background bg;
    Pane canvas;

    public ChangeBackgroundColorTransaction(Pane canvas, Color color, mmmData data) {
        //this.app = app;
        this.dataManager = data;
        //this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.newColor = color;

        //(Color)(workspace.getCanvas().getBackground().getFills().get(0).getFill()) != null
        if (canvas == null) {
            System.out.println("Null");
        } else {

            try {
                this.oldColor = ((Color) (canvas.getBackground().getFills().get(0).getFill()));
                
            } catch (NullPointerException e) {
                
                this.oldColor = Color.WHITE;
            }
//            if ((Color) (canvas.getBackground().getFills().get(0).getFill()) != null) {
//                this.oldColor = ((Color) (canvas.getBackground().getFills().get(0).getFill()));
//            } else {
//                this.oldColor = Color.WHITE;
//            }
        }

    }

    @Override
    public void doTransaction() {
        if (newColor != null) {
            dataManager.setBackgroundColor(newColor);
            //app.getGUI().updateToolbarControls(false);
        }

    }

    @Override
    public void undoTransaction() {
        if (oldColor != null) {
            dataManager.setBackgroundColor(oldColor);
            //app.getGUI().updateToolbarControls(true);
        }
    }

}
