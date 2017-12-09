/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;

/**
 *
 * @author naimyoussiftraore
 */
public class AddStationTransaction implements jTPS_Transaction {
    
     AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Pane canvas;
    DraggableStation station;

    public AddStationTransaction(AppTemplate app, String name) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.canvas = workspace.getCanvas();
        station = new DraggableStation(name);
    }

    @Override
    public void doTransaction() {
        //workspace.lines.put(line.getName(), line);
       // dataManager.lines.put(line.getName(), line);
        canvas.getChildren().add(station);
        canvas.getChildren().add(station.label);
        dataManager.stationMap.put(station.getName(), station);
//        canvas.getChildren().add(line.label1);
//        canvas.getChildren().add(line.label2);
        // ENABLE/DISABLE THE PROPER BUTTONS
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }

    @Override
    public void undoTransaction() {
        dataManager.getShapes().remove(station.label);
        dataManager.getShapes().remove(station);
        dataManager.stationMap.remove(station.getName());
//        dataManager.getShapes().remove(line.label1);
//        dataManager.getShapes().remove(line.label2);
//        workspace.lines.remove(line.getName());
//        dataManager.lines.remove(line.getName());
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(true);
    }

    
}
