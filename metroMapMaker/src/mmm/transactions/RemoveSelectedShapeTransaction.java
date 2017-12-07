/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class RemoveSelectedShapeTransaction implements jTPS_Transaction {
    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Shape selectedShape;
    ObservableList<Node> shapes;
    
    public RemoveSelectedShapeTransaction(AppTemplate app) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.selectedShape = dataManager.getSelectedShape();
        this.shapes = dataManager.getShapes();
    }

    @Override
    public void doTransaction() {
        if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    //selectedShape = null;
            
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
	}
    }

    @Override
    public void undoTransaction() {
       if (selectedShape != null) {
	    shapes.add(selectedShape);
            
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(true);
	}
    }
    
}
