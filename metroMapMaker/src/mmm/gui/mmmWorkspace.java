/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static mmm.css.mmmStyle.CLASS_BUTTON;
import static mmm.css.mmmStyle.CLASS_COLOR_CHOOSER_CONTROL;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR;
import static mmm.css.mmmStyle.CLASS_EDIT_TOOLBAR_ROW;
import static mmm.css.mmmStyle.CLASS_RENDER_CANVAS;
import mmm.data.Draggable;
import mmm.data.DraggableLine;
import mmm.data.mmmData;
import static mmm.mmmLanguageProperty.ADD_IMAGE_TOOLTIP;
import static mmm.mmmLanguageProperty.ADD_LABEL_TOOLTIP;
import static mmm.mmmLanguageProperty.ADD_LINE_TOOLTIP;
import static mmm.mmmLanguageProperty.ADD_STATION_TOOLTIP;
import static mmm.mmmLanguageProperty.ADD_STATION_TO_LINE_TOOLTIP;
import static mmm.mmmLanguageProperty.BOLD_ICON;
import static mmm.mmmLanguageProperty.BOLD_TOOLTIP;
import static mmm.mmmLanguageProperty.CHANGE_BACKGROUND_COLOR_TOOLTIP;
import static mmm.mmmLanguageProperty.CHANGE_LINE_THICKNESS_TOOLTIP;
import static mmm.mmmLanguageProperty.CHANGE_STATION_COLOR_TOOLTIP;
import static mmm.mmmLanguageProperty.CHANGE_STATION_RADIUS_TOOLTIP;
import static mmm.mmmLanguageProperty.CHANGE_TEXT_COLOR_TOOLTIP;
import static mmm.mmmLanguageProperty.COLLAPSE_ICON;
import static mmm.mmmLanguageProperty.COLLAPSE_TOOLTIP;
import static mmm.mmmLanguageProperty.DECOR_LABEL;
import static mmm.mmmLanguageProperty.EDIT_LINE_COLOR_TOOLTIP;
import static mmm.mmmLanguageProperty.EDIT_LINE_NAME_TOOLTIP;
import static mmm.mmmLanguageProperty.EDIT_STATION_NAME_TOOLTIP;
import static mmm.mmmLanguageProperty.END_STATION_TOOLTIP;
import static mmm.mmmLanguageProperty.EXPAND_ICON;
import static mmm.mmmLanguageProperty.EXPAND_TOOLTIP;
import static mmm.mmmLanguageProperty.FIND_ROUTE_TOOLTIP;
import static mmm.mmmLanguageProperty.FONT_FAMILY_TOOLTIP;
import static mmm.mmmLanguageProperty.FONT_LABEL;
import static mmm.mmmLanguageProperty.FONT_SIZE_TOOLTIP;
import static mmm.mmmLanguageProperty.ITALIC_ICON;
import static mmm.mmmLanguageProperty.ITALIC_TOOLTIP;
import static mmm.mmmLanguageProperty.LIST_ALL_STATIONS_TOOLTIP;
import static mmm.mmmLanguageProperty.LIST_STATIONS_ICON;
import static mmm.mmmLanguageProperty.METRO_LINES_LABEL;
import static mmm.mmmLanguageProperty.METRO_STATIONS_LABEL;
import static mmm.mmmLanguageProperty.MINUS_ICON;
import static mmm.mmmLanguageProperty.MOVE_STATION_LABEL_TOOLTIP;
import static mmm.mmmLanguageProperty.NAVIGATION_LABEL;
import static mmm.mmmLanguageProperty.PLUS_ICON;
import static mmm.mmmLanguageProperty.REMOVE_ELEMENT_TOOLTIP;
import static mmm.mmmLanguageProperty.REMOVE_LINE_TOOLTIP;
import static mmm.mmmLanguageProperty.REMOVE_STATION_FROM_LINE_TOOLTIP;
import static mmm.mmmLanguageProperty.REMOVE_STATION_TOOLTIP;
import static mmm.mmmLanguageProperty.ROTATE_ICON;
import static mmm.mmmLanguageProperty.ROTATE_STATION_LABEL_TOOLTIP;
import static mmm.mmmLanguageProperty.ROUTE_ICON;
import static mmm.mmmLanguageProperty.SET_BACKGROUND_IMAGE_TOOLTIP;
import static mmm.mmmLanguageProperty.SNAP_TO_GRID_TOOLTIP;
import static mmm.mmmLanguageProperty.START_STATION_TOOLTIP;
import static mmm.mmmLanguageProperty.ZOOM_IN_ICON;
import static mmm.mmmLanguageProperty.ZOOM_IN_TOOLTIP;
import static mmm.mmmLanguageProperty.ZOOM_OUT_ICON;
import static mmm.mmmLanguageProperty.ZOOM_OUT_TOOLTIP;
import properties_manager.PropertiesManager;

/**
 *
 * @author naimyoussiftraore
 */
public class mmmWorkspace extends AppWorkspaceComponent {

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

     ScrollPane scrollPane = new ScrollPane();
     
    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;

    // FIRST ROW
    VBox row1Box;
    HBox h1;
    HBox h2;
    HBox h3;
    Label metroLinesLabel;
    ComboBox<String> changeLineName;
    ColorPicker changeLineColor;
    Button editLineButton;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLineButton;
    Button removeStationFromLineButton;
    Button listAllStationsInLineButton;
    Slider lineThicknessSlider;

    // SECOND ROW
    VBox row2Box;
    HBox h11;
    HBox h22;
    HBox h33;
    Label metroStationsLabel;
    ComboBox<String> changeStationName;
    ColorPicker changeStationFillColor;
    Button addStationButton;
    Button removeStationButton;
    Button snapToGridButton;
    Button moveStationLabelButton;
    Button rotateStationLabelButton;
    Slider changeStationCircleRadius;

    // THIRD ROW
    HBox row3Box;
    VBox v1;
    VBox v2;
    ComboBox<String> startStation;
    ComboBox<String> endStation;
    Button findRoute;

    // FOURTH ROW
    VBox row4Box;
    HBox h4;
    HBox h44;
    Label decorLabel;
    ColorPicker backGroundColorPicker;
    Button backGroundImage;
    Button addImage;
    Button addLabel;
    Button removeElement;

    // FIFTH ROW
    VBox row5Box;
    HBox h5;
    HBox h55;
    Label fontLabel;
    ColorPicker textColorPicker;
    ComboBox<String> fontFamily;
    ComboBox<String> fontSize;
    Button boldToggleButton;
    Button italicsToggleButton;

    List<String> families;
    ObservableList<String> fontFamilyList;
    ObservableList<String> fontSizeList;
    List<String> sizes = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");

    // SIXTH ROW
    VBox row6Box;
    HBox h6;
    HBox h66;
    Label navigationLabel;
    CheckBox showGrid;
    Button zoomIn;
    Button zoomOut;
    Button collapse;
    Button expand;

    Pane canvas;
    
     // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    MapEditController mapEditController;

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
     // FOR DISPLAYING DEBUG STUFF
    Text debugText;
    
    //BOOLEAN FLAG FOR ADDING STATION TO A LINE
    boolean addingStation = false;
    
    DraggableLine line = null;
    
    String mapName = "";

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public mmmWorkspace(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER
        gui = app.getGUI();

        // LAYOUT THE APP
        initLayout();

        // HOOK UP THE CONTROLLERS
        initControllers();

        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
        
        //mapName = gui.getNameOfMap();
    }
    
//    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
//    // MAY NEED TO UPDATE OR ACCESS DATA FROM
//    public ColorPicker getFillColorPicker() {
//        return fillColorPicker;
//    }
//
//    public ColorPicker getOutlineColorPicker() {
//        return outlineColorPicker;
//    }
//
//    public ColorPicker getBackgroundColorPicker() {
//        return backgroundColorPicker;
//    }
//
//    public Slider getOutlineThicknessSlider() {
//        return outlineThicknessSlider;
//    }
    
    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
        debugText.setText(text);
    }
    
    public Pane getCanvas() {
        return canvas;
    }

    private void initLayout() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
        editToolbar = new VBox();

        //ROW 1
        row1Box = new VBox();
        h1 = new HBox();
        h1.setSpacing(10);
        h2 = new HBox();
        h2.setSpacing(10);
        h3 = new HBox();
        h3.setSpacing(10);

        metroLinesLabel = new Label(props.getProperty(METRO_LINES_LABEL.toString()));
        changeLineColor = new ColorPicker(Color.valueOf("#EEEE00"));
        changeLineColor.setTooltip(new Tooltip(props.getProperty(EDIT_LINE_COLOR_TOOLTIP.toString())));
        changeLineName = new ComboBox<>();
        changeLineName.setTooltip(new Tooltip(props.getProperty(EDIT_LINE_NAME_TOOLTIP.toString())));
        changeLineName.setPromptText(props.getProperty(EDIT_LINE_NAME_TOOLTIP.toString()));
        changeLineName.setEditable(true);
        editLineButton = new Button();
        editLineButton.setText("Edit\nLine");
        editLineButton.setDisable(false);
        

        h1.getChildren().add(metroLinesLabel);
        h1.getChildren().add(changeLineName);
        h1.getChildren().add(changeLineColor);
        h1.getChildren().add(editLineButton);

        addLineButton = gui.initChildButton(h2, PLUS_ICON.toString(), ADD_LINE_TOOLTIP.toString(), false);
        removeLineButton = gui.initChildButton(h2, MINUS_ICON.toString(), REMOVE_LINE_TOOLTIP.toString(), false);
        addStationToLineButton = gui.initChildButton(h2, ADD_STATION_TO_LINE_TOOLTIP.toString(), true);
        addStationToLineButton.setText("Add\nStation");
        removeStationFromLineButton = gui.initChildButton(h2, REMOVE_STATION_FROM_LINE_TOOLTIP.toString(), true);
        removeStationFromLineButton.setText("Remove\nStation");
        listAllStationsInLineButton = gui.initChildButton(h2, LIST_STATIONS_ICON.toString(), LIST_ALL_STATIONS_TOOLTIP.toString(), true);

        lineThicknessSlider = new Slider(0, 10, 1);
        lineThicknessSlider.setTooltip(new Tooltip(props.getProperty(CHANGE_LINE_THICKNESS_TOOLTIP.toString())));
        h3.getChildren().add(lineThicknessSlider);

        row1Box.getChildren().addAll(h1, h2, h3);

        //ROW 2
        row2Box = new VBox();
        h11 = new HBox();
        h11.setSpacing(10);
        h22 = new HBox();
        h22.setSpacing(10);
        h33 = new HBox();
        h33.setSpacing(10);

        metroStationsLabel = new Label(props.getProperty(METRO_STATIONS_LABEL.toString()));
        changeStationFillColor = new ColorPicker(Color.valueOf("#D3D3D3"));
        changeStationFillColor.setTooltip(new Tooltip(props.getProperty(CHANGE_STATION_COLOR_TOOLTIP.toString())));
        changeStationName = new ComboBox<>();
        changeStationName.setTooltip(new Tooltip(props.getProperty(EDIT_STATION_NAME_TOOLTIP.toString())));
        changeStationName.setPromptText(props.getProperty(EDIT_STATION_NAME_TOOLTIP.toString()));
        changeStationName.setEditable(true);

        h11.getChildren().add(metroStationsLabel);
        h11.getChildren().add(changeStationName);
        h11.getChildren().add(changeStationFillColor);

        addStationButton = gui.initChildButton(h22, PLUS_ICON.toString(), ADD_STATION_TOOLTIP.toString(), false);
        removeStationButton = gui.initChildButton(h22, MINUS_ICON.toString(), REMOVE_STATION_TOOLTIP.toString(), false);
        snapToGridButton = gui.initChildButton(h22, SNAP_TO_GRID_TOOLTIP.toString(), true);
        snapToGridButton.setText("Snap");
        moveStationLabelButton = gui.initChildButton(h22, MOVE_STATION_LABEL_TOOLTIP.toString(), true);
        moveStationLabelButton.setText("Move\nLabel");
        rotateStationLabelButton = gui.initChildButton(h22, ROTATE_ICON.toString(), ROTATE_STATION_LABEL_TOOLTIP.toString(), true);

        changeStationCircleRadius = new Slider(0, 10, 1);
        changeStationCircleRadius.setTooltip(new Tooltip(props.getProperty(CHANGE_STATION_RADIUS_TOOLTIP.toString())));
        h33.getChildren().add(changeStationCircleRadius);

        row2Box.getChildren().addAll(h11, h22, h33);

        //ROW 3
        row3Box = new HBox();
        v1 = new VBox();
        v1.setSpacing(10);
        v2 = new VBox();
        v2.setSpacing(10);

        startStation = new ComboBox<>();
        startStation.setTooltip(new Tooltip(props.getProperty(START_STATION_TOOLTIP.toString())));
        startStation.setPromptText(props.getProperty(START_STATION_TOOLTIP.toString()));
        startStation.setEditable(true);

        endStation = new ComboBox<>();
        endStation.setTooltip(new Tooltip(props.getProperty(END_STATION_TOOLTIP.toString())));
        endStation.setPromptText(props.getProperty(END_STATION_TOOLTIP.toString()));
        endStation.setEditable(true);

        v1.getChildren().addAll(startStation, endStation);

        findRoute = gui.initChildButton(v2, ROUTE_ICON.toString(), FIND_ROUTE_TOOLTIP.toString(), true);

        row3Box.getChildren().addAll(v1, v2);

        //ROW 4
        row4Box = new VBox();
        h4 = new HBox();
        h4.setSpacing(10);
        h44 = new HBox();
        h44.setSpacing(10);

        decorLabel = new Label(props.getProperty(DECOR_LABEL.toString()));
        backGroundColorPicker = new ColorPicker(Color.valueOf("#ADD8E6"));
        backGroundColorPicker.setTooltip(new Tooltip(props.getProperty(CHANGE_BACKGROUND_COLOR_TOOLTIP.toString())));

        h4.getChildren().addAll(decorLabel, backGroundColorPicker);

        backGroundImage = gui.initChildButton(h44, SET_BACKGROUND_IMAGE_TOOLTIP.toString(), true);
        backGroundImage.setText("Set Image \nBackground");
        addImage = gui.initChildButton(h44, ADD_IMAGE_TOOLTIP.toString(), true);
        addImage.setText("Add \nImage");
        addLabel = gui.initChildButton(h44, ADD_LABEL_TOOLTIP.toString(), true);
        addLabel.setText("Add \nLabel");
        removeElement = gui.initChildButton(h44, REMOVE_ELEMENT_TOOLTIP.toString(), true);
        removeElement.setText("Remove \nElement");

        row4Box.getChildren().addAll(h4, h44);

          //ROW 5
        row5Box = new VBox();
        h5 = new HBox();
        h5.setSpacing(10);
        h55 = new HBox();
        h55.setSpacing(10);
        
        fontLabel = new Label(props.getProperty(FONT_LABEL.toString()));
        textColorPicker = new ColorPicker(Color.valueOf("#000000"));
        textColorPicker.setTooltip(new Tooltip(props.getProperty(CHANGE_TEXT_COLOR_TOOLTIP.toString())));
        
        h5.getChildren().addAll(fontLabel, textColorPicker);
        
        fontFamily = new ComboBox<>();
        fontFamily.setTooltip(new Tooltip(props.getProperty(FONT_FAMILY_TOOLTIP.toString())));
        fontFamily.setPromptText(props.getProperty(FONT_FAMILY_TOOLTIP.toString()));
        families = Font.getFamilies();
        fontFamilyList = FXCollections.observableList(families);
        fontFamily.setItems(fontFamilyList);

        fontSize = new ComboBox<>();
        fontSize.setTooltip(new Tooltip(props.getProperty(FONT_SIZE_TOOLTIP.toString())));
        fontSize.setPromptText(props.getProperty(FONT_SIZE_TOOLTIP.toString()));
        fontSizeList = FXCollections.observableList(sizes);
        fontSize.setItems(fontSizeList);

        boldToggleButton = new Button();
        italicsToggleButton = new Button();
        
        boldToggleButton = gui.initChildButton(h55, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), true);
        italicsToggleButton = gui.initChildButton(h55, ITALIC_ICON.toString(), ITALIC_TOOLTIP.toString(), true);
        
        h55.getChildren().addAll(fontSize, fontFamily);
        
        row5Box.getChildren().addAll(h5, h55);
        
        //ROW 6
        row6Box = new VBox();
        h6 = new HBox();
        h6.setSpacing(10);
        h66 = new HBox();
        h66.setSpacing(10);
        
        navigationLabel = new Label(props.getProperty(NAVIGATION_LABEL.toString()));
        showGrid = new CheckBox("Show Grid");
        
        h6.getChildren().addAll(navigationLabel, showGrid);
        
        
        zoomIn = gui.initChildButton(h66, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), true);
        zoomOut = gui.initChildButton(h66, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), true);
        collapse = gui.initChildButton(h66, COLLAPSE_ICON.toString(), COLLAPSE_TOOLTIP.toString(), true);
        expand = gui.initChildButton(h66, EXPAND_ICON.toString(), EXPAND_TOOLTIP.toString(), true);
        
        row6Box.getChildren().addAll(h6, h66);
        
        // NOW ORGANIZE THE EDIT TOOLBAR
        editToolbar.getChildren().add(row1Box);
        editToolbar.getChildren().add(row2Box);
        editToolbar.getChildren().add(row3Box);
        editToolbar.getChildren().add(row4Box);
        editToolbar.getChildren().add(row5Box);
        editToolbar.getChildren().add(row6Box);

        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        canvas = new Pane();
        debugText = new Text();
        canvas.getChildren().add(debugText);
        debugText.setX(100);
        debugText.setY(100);
        
        scrollPane.setContent(editToolbar);
        
        // AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
        mmmData data = (mmmData) app.getDataComponent();
        data.setShapes(canvas.getChildren());

        // AND NOW SETUP THE WORKSPACE
        workspace = new BorderPane();
        ((BorderPane) workspace).setLeft(scrollPane);
        ((BorderPane) workspace).setCenter(canvas);

    }

    private void initControllers() {
        
         // MAKE THE EDIT CONTROLLER
        mapEditController = new MapEditController(app);
        
         // NOW CONNECT THE BUTTONS TO THEIR HANDLERS
         
         addLineButton.setOnAction(e -> {
            mapEditController.processAddLine();
        });
         
         addStationButton.setOnAction(e -> {
            mapEditController.processAddNewStation();
        });
         
         removeLineButton.setOnAction(e -> {
            mapEditController.processRemoveLine();
        });
         
         removeStationButton.setOnAction(e -> {
             
            mapEditController.processRemoveStation();
        });
         
         editLineButton.setOnAction(e -> {
             
            mapEditController.processEditLine();
        });
         
        
          
         addStationToLineButton.setOnAction(e -> {
             
           
            if(line == null && mapEditController.dataManager.getSelectedShape() != null){
                addingStation = true;
                line = (DraggableLine)mapEditController.dataManager.getSelectedShape();
            }
                 
           
             
         
        });
         
         
         
         
         
         
       // MAKE THE CANVAS CONTROLLER	
        canvasController = new CanvasController(app);

        canvas.setOnMousePressed(e -> {
            
              canvasController.processCanvasMousePress((int) e.getX(), (int) e.getY());
              
              //addingStation = false;

        });

        canvas.setOnMouseReleased(e -> {

            canvasController.processCanvasMouseRelease((int) e.getX(), (int) e.getY());
        });

        canvas.setOnMouseDragged(e -> {

            canvasController.processCanvasMouseDragged((int) e.getX(), (int) e.getY());
        });

        canvas.setOnMouseClicked(e -> {

            if (e.getClickCount() == 2) {
                canvasController.processCanvasMouseDoubleClicked((int) e.getX(), (int) e.getY());

            }
        });

        
        gui.getExportButton().setOnAction(e -> {
            mapName = gui.getNameOfMap(); 
            mapEditController.processSnapshot();
            try {
                mapEditController.processExportJSON();
            } catch (IOException ex) {
                Logger.getLogger(mmmWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
         
    }
    
     // HELPER METHOD
    public void loadSelectedShapeSettings(Node node) {

        try {
            Shape shape = (Shape) node;

            if (shape != null) {
                Color fillColor = (Color) shape.getFill();
                Color strokeColor = (Color) shape.getStroke();
                double lineThickness = shape.getStrokeWidth();
//                fillColorPicker.setValue(fillColor);
//                outlineColorPicker.setValue(strokeColor);
//                outlineThicknessSlider.setValue(lineThickness);
            }

        } catch (ClassCastException e) {

        }

    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
        // NOTE THAT EACH CLASS SHOULD CORRESPOND TO
        // A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
        // CSS FILE
        canvas.getStyleClass().add(CLASS_RENDER_CANVAS);

        // COLOR PICKER STYLE
        changeLineColor.getStyleClass().add(CLASS_BUTTON);
        changeStationFillColor.getStyleClass().add(CLASS_BUTTON);
//        outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
//        backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);

        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
        row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);

        metroStationsLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        metroLinesLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        decorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        fontLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        navigationLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);

//        
//        fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//        
//        outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//        
//        outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        //row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    public void refreshUIControls() {

    }

    @Override
    public void resetWorkspace() {

    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {

    }

    public Button initButton(GridPane toolbar, String icon, String tooltip, boolean disabled, int y, int x) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);

        // PUT THE BUTTON IN THE TOOLBAR
        toolbar.add(button, y, x);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    public boolean addingStation(){
        
        return addingStation;
    }

}
