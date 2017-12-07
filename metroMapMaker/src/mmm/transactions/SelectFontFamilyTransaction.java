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
import jtps.jTPS_Transaction;

/**
 *
 * @author naimyoussiftraore
 */
public class SelectFontFamilyTransaction implements jTPS_Transaction {

    AppTemplate app;
    mmmData dataManager;
    mmmWorkspace workspace;
    Shape selectedShape;
    String newFont;
    String oldFont;

    public SelectFontFamilyTransaction(String newFont, AppTemplate app) {
        this.newFont = newFont;
        this.app = app;
        this.dataManager = (mmmData) app.getDataComponent();
        this.workspace = (mmmWorkspace) app.getWorkspaceComponent();
        this.selectedShape = dataManager.getSelectedShape();

        if (selectedShape != null && isText(selectedShape)) {
            oldFont = ((DraggableText) selectedShape).getFont().getFamily();
        }
    }

    @Override
    public void doTransaction() {
        if (selectedShape != null && isText(selectedShape)) {
            DraggableText draggableText = (DraggableText) selectedShape;
            draggableText.setFont(Font.font(newFont, draggableText.getFont().getSize()));

            // ENABLE/DISABLE THE PROPER BUTTONS
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }

    }

    @Override
    public void undoTransaction() {
        if (selectedShape != null && isText(selectedShape)) {
            DraggableText draggableText = (DraggableText) selectedShape;
            draggableText.setFont(Font.font(oldFont, draggableText.getFont().getSize()));

            // ENABLE/DISABLE THE PROPER BUTTONS
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(true);
        }
    }

}
