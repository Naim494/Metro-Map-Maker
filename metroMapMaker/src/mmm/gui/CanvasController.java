/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import javafx.collections.ObservableList;
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
    DraggableLine lastExtension = null;

    public CanvasController(AppTemplate initApp) {
        app = initApp;
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y) throws CloneNotSupportedException {
        //System.out.println("Entered canvas mouse pressed");
        mmmData dataManager = (mmmData) app.getDataComponent();
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        MapEditController mapEditController = new MapEditController(app);

        if (dataManager.isInState(SELECTING_SHAPE)) {
            //System.out.println("Entered selecting shape clause");
            // SELECT THE TOP SHAPE
            Shape shape = dataManager.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();

            app.getGUI().getCopyButton().setDisable(false);
            app.getGUI().getCutButton().setDisable(false);

            if (shape == null) {
                workspace.line = null;
                workspace.addingStation = false;
                workspace.removingStation = false;

            } else if (workspace.addingStation) {

                if (workspace.line != null && dataManager.getSelectedShape() != null) {

                    if (((Draggable) shape).getShapeType().equals(Draggable.STATION)) {

                        if (!(workspace.lines.get(workspace.line.getName()).getStations().contains((DraggableStation) dataManager.getSelectedShape()))) {

                            //GET SELECTED LINE AND STATION
                            DraggableStation s = (DraggableStation) dataManager.getSelectedShape();
                            DraggableLine l = workspace.line;

                            String stationLineOn = s.getLineOn();

                            DraggableLine nLine = null;
                            DraggableLine sLine = null;
                            DraggableLine lastExtension = dataManager.allLines.get(workspace.lines.get(l.getName()).lastExtension);

                            if (!(stationLineOn.equals(l.getName()))) {
                                nLine = dataManager.allLines.get(nLine);
                                sLine = dataManager.allLines.get(sLine);

                            }

                            double xptn = 0;
                            double yptn = 0;

//                            if (l.getExtension() == null) {
//                                //GET THE POINT TO PLACE THE STATION
//                                xptn = l.getStartX();
//                                yptn = l.getStartY();
//                            } else {
//                                xptn = l.getExtension().getStartX();
//                                yptn = l.getExtension().getStartY();
//                            }
//                            
//                            if (lastExtension == null) {
//                                //GET THE POINT TO PLACE THE STATION
//                                xptn = l.getStartX();
//                                yptn = l.getStartY();
//                            } else {
//                                xptn = lastExtension.getStartX();
//                                yptn = lastExtension.getStartY();
//                            }
                            if (lastExtension == null) {
                                //GET THE POINT TO PLACE THE STATION
                                xptn = l.getStartX();
                                yptn = l.getStartY();
                            } else {
                                xptn = lastExtension.getStartX();
                                yptn = lastExtension.getStartY();
                            }

                            //MAKE A COPY OF THE STATION
                            DraggableStation station = (DraggableStation) s.clone();
//                            station.setCenterX(xptn);
//                            station.setCenterY(yptn);
                            station.label.setX(xptn);
                            station.label.setY(yptn);
                            station.setLineOn(l.getName());

                            //MAKE AN EXTENSION FOR AFTER THE STATION IS ADDED
                            DraggableLine extension = new DraggableLine(station, l.getName());
                            extension.setName(l.getName());
                            extension.setColor(l.color);
                            extension.label1.setText(l.getName());
                            extension.label1.setX(xptn + 20);
                            extension.label1.setY(yptn + 20);
                            dataManager.allLines.put(extension.alias, extension);

                            //BIND THE ORIGINAL LINE'S START POINT TO THE STATION
                            if (lastExtension == null) {
                                l.startXProperty().bind(station.centerXProperty());
                                l.startYProperty().bind(station.centerYProperty());
                                l.startStation = station.getName();
                                station.southLine = l.alias;
//                             l.startXProperty().bind(station.label.xProperty());
//                             l.startYProperty().bind(station.label.yProperty());

                                if (nLine != null && sLine != null) {
                                    sLine.startXProperty().bind(station.centerXProperty());
                                    sLine.startYProperty().bind(station.centerYProperty());
                                    nLine.endXProperty().bind(station.centerXProperty());
                                    nLine.endYProperty().bind(station.centerYProperty());
                                }

                            } else {
                                lastExtension.startXProperty().bind(station.centerXProperty());
                                lastExtension.startYProperty().bind(station.centerYProperty());
                                lastExtension.startStation = station.getName();
                                station.southLine = lastExtension.alias;
//                            l.getExtension().startXProperty().bind(station.label.xProperty());
//                            l.getExtension().startYProperty().bind(station.label.yProperty());
                                if (nLine != null && sLine != null) {
                                    sLine.startXProperty().bind(station.centerXProperty());
                                    sLine.startYProperty().bind(station.centerYProperty());
                                    nLine.endXProperty().bind(station.centerXProperty());
                                    nLine.endYProperty().bind(station.centerYProperty());
                                }
                            }

                            //RENDER
                            if (lastExtension == null) {
                                canvas.getChildren().remove(l.label1);

                            } else {
                                canvas.getChildren().remove(lastExtension.label1);
                            }

                            canvas.getChildren().add(extension);
                            canvas.getChildren().add(extension.label1);
                            canvas.getChildren().add(station);
                            canvas.getChildren().add(station.label);  //ADDED

                            //ADD THE STATION TO THE LINE PROGRAMMATICALLY
                            workspace.lines.get(l.getName()).getStations().add(station);

                            mapEditController.processRemoveStation();

//                            extension.setExtension(extension);
//                            workspace.lines.get(l.getName()).setExtension(extension);
//                            lastExtension = extension;
                            try {
                                workspace.lines.get(l.getName()).lastExtension = extension.alias;
                                dataManager.allLines.get(workspace.lines.get(l.getName()).alias).lastExtension = extension.alias;
                                
                                //dataManager.allLines.get(workspace.lines.get(tempLine.getName()).alias).lastExtension = tempLine.alias;

                            } catch (NullPointerException e) {

                            }

                        }

                    }
//                    workspace.line = null;
//                    workspace.addingStation = false;
                }

            } else if (workspace.removingStation) {
                System.out.println("Entered removing station clause");
                if (workspace.line != null && dataManager.getSelectedShape() != null) {
                    if (((Draggable) shape).getShapeType().equals(Draggable.STATION)) {

                        ObservableList shapes = dataManager.getShapes();
                        DraggableStation s = (DraggableStation) dataManager.getSelectedShape();
                        DraggableLine tempLine = dataManager.allLines.get(s.southLine);
                        DraggableStation tempStation = null;
                        DraggableText tempLabel = null;

                        if (dataManager.allLines == null) {
                            System.out.println("allLines is null");
                        }
                        
                        if (s.northLine == null) {
                            System.out.println("STATION N LINE  is null");
                        }
                        
                        System.out.println(s.northLine);

//                        System.out.println("station north line:" + s.northLine);
//                        System.out.println("" + dataManager.allLines.get(s.northLine).alias);
                        if (!(s.northLine).equals("")) {

                            if (!(dataManager.allLines.get(s.northLine).startStation.equals(""))) {
                                tempStation = dataManager.stationMap.get(dataManager.allLines.get(s.northLine).startStation);

                                dataManager.getShapes().remove(s);
                                dataManager.getShapes().remove(s.label);
                                dataManager.getShapes().remove(dataManager.allLines.get(s.northLine));

                                tempLine.startXProperty().bind(tempStation.centerXProperty());
                                tempLine.startYProperty().bind(tempStation.centerYProperty());

                                tempLine.startStation = tempStation.getName();
                                tempStation.southLine = tempLine.getAlias();

                            } else {

                                for (int i = shapes.size() - 1; i >= 0; i--) {

                                    try {

                                        if (((DraggableText) shapes.get(i)).getText().equals(tempLine.getName()) && ((DraggableText) shapes.get(i)).isStartLabel()) {
                                            tempLabel = (DraggableText) shapes.get(i);
                                        }

                                    } catch (ClassCastException e) {

                                    }
                                }
                                System.out.println();
                                System.out.println(tempLabel.getText());
                                System.out.println(tempLine.alias);
                                System.out.println();
                                
                                

//                                tempLine.startXProperty().unbind();
//                                tempLine.startYProperty().unbind();
                                dataManager.getShapes().remove(s);
                                dataManager.getShapes().remove(s.label);
                                dataManager.getShapes().remove(dataManager.allLines.get(s.northLine));
                                
                                dataManager.getShapes().remove(dataManager.allLines.get(s.northLine).label1);
                                
                                
//                                if(tempLabel.getText().equals(tempLine.getName())){
//                                    
//                                    tempLine.startXProperty().bind(tempLabel.xProperty());
//                                    tempLine.startYProperty().bind(tempLabel.yProperty());
//                                    System.out.println("DID THE BINDING");
//                                }

                                canvas.getChildren().add(tempLine.label1);
                                
                                dataManager.allLines.get(s.southLine).startXProperty().bind(dataManager.allLines.get(s.southLine).label1.xProperty());
                                dataManager.allLines.get(s.southLine).startYProperty().bind(dataManager.allLines.get(s.southLine).label1.yProperty());
                                

//                            System.out.println("Templabel: " +tempLabel.getX() + " " + tempLabel.getY());
//                            System.out.println();
//                            System.out.println("Templine: " +tempLine.getX() + " " + tempLine.getY());
                                                                

//                            System.out.println("Templabel: " +tempLabel.getX() + " " + tempLabel.getY());
//                            System.out.println();
//                            System.out.println("Templine: " + tempLine.getX() + " " + tempLine.getY());
                                tempLine.startStation = "";
                                //lastExtension = tempLine;
                                dataManager.allLines.get(workspace.lines.get(tempLine.getName()).alias).lastExtension = tempLine.alias;

                            }

                        }

//                        dataManager.getShapes().remove(s);
//                        dataManager.getShapes().remove(dataManager.allLines.get(s.northLine));
//
//                        tempLine.startXProperty().bind(tempStation.centerXProperty());
//                        tempLine.startYProperty().bind(tempStation.centerYProperty());
//                        
//                        tempLine.startStation = tempStation.getName();
//                        tempStation.southLine = tempLine.getAlias();
                    }

                }

//                workspace.line = null;
//                workspace.removingStation = false;
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
        else {
             workspace.line = null;
                workspace.addingStation = false;
                workspace.removingStation = false;
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
