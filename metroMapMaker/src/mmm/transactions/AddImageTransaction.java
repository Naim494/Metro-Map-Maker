/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import mmm.data.DraggableImage;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class AddImageTransaction implements jTPS_Transaction {

    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Pane canvas;
    DraggableImage imageNode;

    public AddImageTransaction(AppTemplate app, String imagePath, File imageFile) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.canvas = workspace.getCanvas();
        imageNode = new DraggableImage(new Image(imagePath), imageFile);

    }

    @Override
    public void doTransaction() {
        canvas.getChildren().add(imageNode);

        // ENABLE/DISABLE THE PROPER BUTTONS
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }

    @Override
    public void undoTransaction() {
         dataManager.getShapes().remove(imageNode);
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(true);
    }

}
