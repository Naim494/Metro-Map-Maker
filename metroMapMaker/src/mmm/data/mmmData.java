/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static mmm.data.mmmState.SELECTING_SHAPE;
import static mmm.data.mmmState.SIZING_SHAPE;
import mmm.gui.mmmWorkspace;

/**
 *
 * @author naimyoussiftraore
 */
public class mmmData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES

    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> shapes;
    
    public String mapName = "";

    // THESE ARE THE UNIQUE LINES, NO EXTENSIONS
    public HashMap<String, DraggableLine> lines = new HashMap<String, DraggableLine>();
    
    // THESE ARE ALL THE LINES, INCLUDING EXTENSIONS
    public HashMap<String, DraggableLine> allLines = new HashMap<String, DraggableLine>();

    // THESE ARE THE STATIONS
    public ArrayList<DraggableStation> stations = new ArrayList<DraggableStation>();
    public HashMap<String, DraggableStation> stationMap = new HashMap<String, DraggableStation>();

    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    Image backgroundImage;

    // AND NOW THE EDITING DATA
    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Shape newShape;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Shape selectedShape;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    // CURRENT STATE OF THE APP
    mmmState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 1;

    public mmmData(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
        app = initApp;

        // NO SHAPE STARTS OUT AS SELECTED
        newShape = null;
        selectedShape = null;
        
        //mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        

        // INIT THE COLORS
        currentFillColor = Color.web(WHITE_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);
        currentBorderWidth = 1;

        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(15);
        highlightedEffect = dropShadowEffect;

    }

    public ObservableList<Node> getShapes() {
        return shapes;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
        return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
        return currentBorderWidth;
    }

    public void setShapes(ObservableList<Node> initShapes) {
        shapes = initShapes;
    }

    public void setBackgroundColor(Color initBackgroundColor) {
        backgroundColor = initBackgroundColor;
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
    }
    
    public void setBackgroundImage(Image image) {
         backgroundImage = image;
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        BackgroundImage fill = new BackgroundImage(backgroundImage,  BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,  BackgroundPosition.CENTER,  BackgroundSize.DEFAULT);
        Background background = new Background(fill);
        canvas.setBackground(background);
    }

    public void setCurrentFillColor(Color initColor) {
        currentFillColor = initColor;
        if (selectedShape != null) {
            selectedShape.setFill(currentFillColor);
        }
    }

    public void setCurrentOutlineColor(Color initColor) {
        currentOutlineColor = initColor;
        if (selectedShape != null) {
            selectedShape.setStroke(initColor);
        }
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
        currentBorderWidth = initBorderWidth;
        if (selectedShape != null) {
            selectedShape.setStrokeWidth(initBorderWidth);
        }
    }

    public void removeSelectedShape() {
        if (selectedShape != null) {
            shapes.remove(selectedShape);
            selectedShape = null;
        }
    }

    public void moveSelectedShapeToBack() {
        if (selectedShape != null) {
            shapes.remove(selectedShape);
            if (shapes.isEmpty()) {
                shapes.add(selectedShape);
            } else {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(selectedShape);
                for (Node node : shapes) {
                    temp.add(node);
                }
                shapes.clear();
                for (Node node : temp) {
                    shapes.add(node);
                }
            }
        }
    }

    public void moveSelectedShapeToFront() {
        if (selectedShape != null) {
            shapes.remove(selectedShape);
            shapes.add(selectedShape);
        }
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        setState(SELECTING_SHAPE);
        newShape = null;
        selectedShape = null;

        // INIT THE COLORS
        currentFillColor = Color.web(WHITE_HEX);
        currentOutlineColor = Color.web(BLACK_HEX);

        shapes.clear();
        ((mmmWorkspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    public void selectSizedShape() {
        if (selectedShape != null) {
            unhighlightShape(selectedShape);
        }
        selectedShape = newShape;
        highlightShape(selectedShape);
        newShape = null;
        if (state == SIZING_SHAPE) {
            state = ((Draggable) selectedShape).getStartingState();
        }
    }

    public void unhighlightShape(Shape shape) {
        selectedShape.setEffect(null);
    }

    public void highlightShape(Shape shape) {
        shape.setEffect(highlightedEffect);
    }

//    public void startNewRectangle(int x, int y) {
//	DraggableRectangle newRectangle = new DraggableRectangle();
//	newRectangle.start(x, y);
//	newShape = newRectangle;
//	initNewShape();
//    }
//    public void startNewEllipse(int x, int y) {
//	DraggableEllipse newEllipse = new DraggableEllipse();
//	newEllipse.start(x, y);
//	newShape = newEllipse;
//	initNewShape();
//    }
    public void initNewShape() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedShape != null) {
            unhighlightShape(selectedShape);
            selectedShape = null;
        }

        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	newShape.setFill(workspace.getFillColorPicker().getValue());
//	newShape.setStroke(workspace.getOutlineColorPicker().getValue());
//	newShape.setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
        // ADD THE SHAPE TO THE CANVAS
        shapes.add(newShape);

        // GO INTO SHAPE SIZING MODE
        state = mmmState.SIZING_SHAPE;
    }

    public Shape getNewShape() {
        return newShape;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape initSelectedShape) {
        selectedShape = initSelectedShape;
    }

    public Shape selectTopShape(int x, int y) {
        Shape shape = getTopShape(x, y);

        if (shape != null) {
            ((Draggable) shape).start(x, y);
        }
        if (shape == selectedShape) {
            return shape;
        }

        if (selectedShape != null) {
            unhighlightShape(selectedShape);
        }
        if (shape != null) {
            highlightShape(shape);
            mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
            workspace.loadSelectedShapeSettings(shape);
        }
        selectedShape = shape;

        return shape;
    }

    public void selectTopShape(String name) {
        DraggableLine line = null;
        Shape shape = null;

        for (int i = shapes.size() - 1; i >= 0; i--) {

            try {
                line = (DraggableLine) (Shape) shapes.get(i);

                if (line.getName().equals(name)) {
                    shape = (Shape) shapes.get(i);
                    highlightShape(shape);
                    mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
                    workspace.loadSelectedShapeSettings(shape);

                    if (selectedShape != null) {
                        unhighlightShape(selectedShape);
                    }
                    
                    selectedShape = shape;
                }

            } catch (ClassCastException e) {

            }
        }
    }

    public Shape getTopShape(int x, int y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = (Shape) shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    public void addShape(Shape shapeToAdd) {
        shapes.add(shapeToAdd);
    }

    public void removeShape(Shape shapeToRemove) {
        shapes.remove(shapeToRemove);
    }

    public mmmState getState() {
        return state;
    }

    public void setState(mmmState initState) {
        state = initState;
    }

    public boolean isInState(mmmState testState) {
        return state == testState;
    }
    
    public String getMapName() {
        
        mmmWorkspace w = (mmmWorkspace)app.getWorkspaceComponent();
        return w.getMapName();
    }

}
