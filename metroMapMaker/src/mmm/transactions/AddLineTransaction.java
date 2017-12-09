/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import mmm.data.DraggableLine;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;

/**
 *
 * @author naimyoussiftraore
 */
public class AddLineTransaction implements jTPS_Transaction{

    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Pane canvas;
    DraggableLine line;

    public AddLineTransaction(AppTemplate app, String name, Color color) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.canvas = workspace.getCanvas();
        line = new DraggableLine(name, color);
    }

    @Override
    public void doTransaction() {
        workspace.lines.put(line.getName(), line);
        dataManager.lines.put(line.getName(), line);
        dataManager.allLines.put(line.alias, line);
        workspace.getSelectLineName().getItems().add(line.getName());
        canvas.getChildren().add(line);
        canvas.getChildren().add(line.label1);
        canvas.getChildren().add(line.label2);
        // ENABLE/DISABLE THE PROPER BUTTONS
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }

    @Override
    public void undoTransaction() {
        dataManager.getShapes().remove(line);
        dataManager.getShapes().remove(line.label1);
        dataManager.getShapes().remove(line.label2);
        workspace.getSelectLineName().getItems().remove(line.getName());
        workspace.lines.remove(line.getName());
        dataManager.lines.remove(line.getName());
        dataManager.allLines.remove(line.alias);
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(true);
    }

}
