/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;

/**
 *
 * @author naimyoussiftraore
 */
public class ChangeLineThicknessTransaction implements jTPS_Transaction {
    
     AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    int newThickness = 0;
    int oldThickness = 0;
    Shape selectedShape;
    
    public ChangeLineThicknessTransaction(AppTemplate app) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.newThickness =  (int) workspace.getLineThicknessSlider().getValue();
        this.selectedShape = dataManager.getSelectedShape();
        if(dataManager.getSelectedShape() != null)
           this.oldThickness = (int) dataManager.getSelectedShape().getStrokeWidth();
    }
    

    @Override
    public void doTransaction() {
        //dataManager.setCurrentOutlineThickness(newThickness);
        if(selectedShape != null) {
            selectedShape.setStrokeWidth(newThickness);
            app.getGUI().updateToolbarControls(false);
        }
    }

    @Override
    public void undoTransaction() {
        //dataManager.setCurrentOutlineThickness(oldThickness);
        if(selectedShape != null) {
            selectedShape.setStrokeWidth(oldThickness);
            app.getGUI().updateToolbarControls(true);
         }
    
    }
    
    
}
