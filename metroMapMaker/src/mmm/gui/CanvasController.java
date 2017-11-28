/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import mmm.data.Draggable;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import mmm.data.mmmState;
import static mmm.data.mmmState.DRAGGING_NOTHING;
import static mmm.data.mmmState.DRAGGING_SHAPE;
import static mmm.data.mmmState.SELECTING_SHAPE;
import static mmm.data.mmmState.SIZING_SHAPE;

/**
 *
 * @author naimyoussiftraore
 */
public class CanvasController {

    AppTemplate app;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();

        if (dataManager.isInState(SELECTING_SHAPE)) {
            // SELECT THE TOP SHAPE
            Shape shape = dataManager.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            app.getGUI().getCopyButton().setDisable(false);
            app.getGUI().getCutButton().setDisable(false);

            if (workspace.addingStation) {
                if (workspace.line != null && dataManager.getSelectedShape() != null) {
                    if (((Draggable) shape).getShapeType().equals(Draggable.STATION)) {
                        workspace.line.getStations().add((DraggableStation) dataManager.getSelectedShape());

                    }
                }
                workspace.addingStation = false;

            }

            // AND START DRAGGING IT
            if (shape != null) {
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(mmmState.DRAGGING_SHAPE);
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        } else if (dataManager.isInState(mmmState.STARTING_RECTANGLE)) {
            //dataManager.startNewRectangle(x, y);

        } else if (dataManager.isInState(mmmState.STARTING_ELLIPSE)) {
            // dataManager.startNewEllipse(x, y);

        }
        //mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            Draggable newDraggableShape = (Draggable) dataManager.getNewShape();
            newDraggableShape.size(x, y);
        } else if (dataManager.isInState(DRAGGING_SHAPE)) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call
     * canvas, but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            dataManager.selectSizedShape();
            //DrawRectangleTransaction transaction = new DrawRectangleTransaction(app);
            //olWorkspace.jTPS.addTransaction(transaction);
            //app.getGUI().getUndoButton().setDisable(false);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mmmState.DRAGGING_SHAPE)) {
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mmmState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }

    public void processCanvasMouseDoubleClicked(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();

        Shape shape = dataManager.selectTopShape(x, y);

        if (shape != null) {
            try {
                DraggableText draggableText = (DraggableText) shape;

                app.textInputDialog.showAndWait();

                String text = app.textInputDialog.getText();

                draggableText.setText(text);

            } catch (ClassCastException e) {

            }
        }

    }

}
