/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import mmm.data.DraggableText;
import mmm.data.mmmData;

/**
 *
 * @author naimyoussiftraore
 */
public class ChangeTextColorTransaction implements jTPS_Transaction {

    mmmData dataManager;
//    mmmWorkspace workspace = null;
    Color newColor;
    Color oldColor;
    Background bg;
    Pane canvas;
    DraggableText text;

    public ChangeTextColorTransaction(DraggableText text, Color color, mmmData data) {
        //this.app = app;
        this.dataManager = data;
        //this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.newColor = color;
        
        this.text = text;

        //(Color)(workspace.getCanvas().getBackground().getFills().get(0).getFill()) != null
        oldColor = (Color) text.getFill();
//            if ((Color) (canvas.getBackground().getFills().get(0).getFill()) != null) {
//                this.oldColor = ((Color) (canvas.getBackground().getFills().get(0).getFill()));
//            } else {
//                this.oldColor = Color.WHITE;
//            }
    }

    @Override
    public void doTransaction() {
        if (newColor != null) {
            text.setFill(newColor);
            //app.getGUI().updateToolbarControls(false);
        }
    }

    @Override
    public void undoTransaction() {
        if (oldColor != null) {
            text.setFill(oldColor);
            //app.getGUI().updateToolbarControls(true);
        }
    }

}
