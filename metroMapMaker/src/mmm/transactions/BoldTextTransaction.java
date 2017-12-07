/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.transactions;

import djf.AppTemplate;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import static mmm.gui.MapEditController.isText;
import mmm.gui.mmmWorkspace;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class BoldTextTransaction implements jTPS_Transaction {

    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Shape selectedShape;

    public BoldTextTransaction(AppTemplate app) {
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.selectedShape = dataManager.getSelectedShape();

    }

    @Override
    public void doTransaction() {
        if (selectedShape != null && isText(selectedShape)) {
            DraggableText draggableText = (DraggableText) selectedShape;

            if (draggableText.isBold() && !(draggableText.isItalic())) {
                draggableText.setFont(Font.font(draggableText.getFont().getFamily(), draggableText.getFont().getSize()));
                draggableText.setBold(false);
            } else if (draggableText.isBold() && draggableText.isItalic()) {
                draggableText.setFont(Font.font(draggableText.getFont().getFamily(), FontPosture.ITALIC, draggableText.getFont().getSize()));
                draggableText.setBold(false);

            } else if (!(draggableText.isBold()) && draggableText.isItalic()) {
                draggableText.setFont(Font.font(draggableText.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, draggableText.getFont().getSize()));
                draggableText.setBold(true);
            } else {
                draggableText.setFont(Font.font(draggableText.getFont().getFamily(), FontWeight.BOLD, draggableText.getFont().getSize()));
                draggableText.setBold(true);
            }

            // ENABLE/DISABLE THE PROPER BUTTONS
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }
    }

    @Override
    public void undoTransaction() {
        doTransaction();

        // ENABLE/DISABLE THE PROPER BUTTONS
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(true);
    }

}
