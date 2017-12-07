/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
    public void processCanvasMousePress(int x, int y) throws CloneNotSupportedException {
        mmmData dataManager = (mmmData) app.getDataComponent();
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        MapEditController mapEditController = new MapEditController(app);

        if (dataManager.isInState(SELECTING_SHAPE)) {
            // SELECT THE TOP SHAPE
            Shape shape = dataManager.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            app.getGUI().getCopyButton().setDisable(false);
            app.getGUI().getCutButton().setDisable(false);

            if (shape == null) {
                workspace.line = null;
                workspace.addingStation = false;
            } else if (workspace.addingStation) {
                if (workspace.line != null && dataManager.getSelectedShape() != null) {
                    if (((Draggable) shape).getShapeType().equals(Draggable.STATION)) {
                        // workspace.line.getStations().add((DraggableStation) dataManager.getSelectedShape());

                        //GET SELECTED LINE AND STATION
                        DraggableStation s = (DraggableStation) dataManager.getSelectedShape();
                        DraggableLine l = workspace.line;
                        
                       
                        double xptn = 0;
                        double yptn = 0;

                        if (l.getExtension() == null) {
                            //GET THE POINT TO PLACE THE STATION
                             xptn = l.getStartX();
                             yptn = l.getStartY();
                        }else{
                            xptn = l.getExtension().getStartX();
                            yptn = l.getExtension().getStartY();
                        }
                        
                        

                        //MAKE A COPY OF THE STATION
                        DraggableStation station = (DraggableStation) s.clone();
                        station.setCenterX(xptn);
                        station.setCenterY(yptn);

                        //MAKE AN EXTENSION FOR AFTER THE STATION IS ADDED
                        DraggableLine extension = new DraggableLine(station);
                        extension.setName(l.getName());
                        extension.setColor(l.color);
                        extension.label1.setText(l.getName());
                        extension.label1.setX(xptn + 20);
                        extension.label1.setY(yptn + 20);
                        
                        //BIND THE ORIGINAL LINE'S START POINT TO THE STATION
                        if (l.getExtension() == null) {
                             l.startXProperty().bind(station.centerXProperty());
                             l.startYProperty().bind(station.centerYProperty());
                        }else{
                            l.getExtension().startXProperty().bind(station.centerXProperty());
                            l.getExtension().startYProperty().bind(station.centerYProperty());
                        }
                          

                        //RENDER
                        if (l.getExtension() == null) {
                            canvas.getChildren().remove(l.label1);
                        }else{
                            canvas.getChildren().remove(l.getExtension().label1);
                        }
                            
                        canvas.getChildren().add(extension);
                        canvas.getChildren().add(extension.label1);
                        canvas.getChildren().add(station);

                        //ADD THE STATION TO THE LINE PROGRAMMATICALLY
                        workspace.lines.get(l.getName()).getStations().add(station);

                        mapEditController.processRemoveStation();
                        
                         extension.setExtension(extension);
                        workspace.lines.get(l.getName()).setExtension(extension);
                        

                    }

                }
                workspace.line = null;
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
