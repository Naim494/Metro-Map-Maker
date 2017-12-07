/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class AddTextTransaction implements jTPS_Transaction {
    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Pane canvas;
    DraggableText textNode;
    
    public AddTextTransaction(AppTemplate app, String text) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.canvas = workspace.getCanvas();
        textNode = new DraggableText(text);
        
        
    }

    @Override
    public void doTransaction() {
        canvas.getChildren().add(textNode);

        // ENABLE/DISABLE THE PROPER BUTTONS
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
        
    }

    @Override
    public void undoTransaction() {
        dataManager.getShapes().remove(textNode);
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(true);
    }
    
}
