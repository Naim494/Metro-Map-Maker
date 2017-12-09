/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import djf.AppTemplate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import mmm.data.Draggable;
import static mmm.data.Draggable.IMAGE;
import static mmm.data.Draggable.LINE;
import static mmm.data.Draggable.STATION;
import mmm.data.DraggableImage;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import mmm.transactions.AddImageTransaction;
import mmm.transactions.AddLineTransaction;
import mmm.transactions.AddStationTransaction;
import mmm.transactions.AddTextTransaction;
import mmm.transactions.BoldTextTransaction;
import mmm.transactions.ChangeBackgroundColorTransaction;
import mmm.transactions.ChangeLineThicknessTransaction;
import mmm.transactions.ChangeTextColorTransaction;
import mmm.transactions.ItalicizeTextTransaction;
import mmm.transactions.RemoveSelectedShapeTransaction;
import mmm.transactions.SelectFontFamilyTransaction;
import mmm.transactions.SelectFontSizeTransaction;

/**
 *
 * @author naimyoussiftraore
 */
public class MapEditController {

    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_STATIONS = "stations";
    static final String JSON_LINES = "lines";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_NAME = "name";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";

    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";

    AppTemplate app;
    mmmData dataManager;
    ImageView image;

    Shape cutCopiedShape;
    mmmWorkspace workspace;

    public MapEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (mmmData) app.getDataComponent();
        workspace = (mmmWorkspace) app.getWorkspaceComponent();
    }

    public void processEditLine() {
        app.addLineDialog.showAndWait();

        if (!app.addLineDialog.isCanceled()) {

            DraggableLine line = (DraggableLine) dataManager.getSelectedShape();

            if (line != null) {
                line.setName(app.addLineDialog.getName());
                line.setColor(app.addLineDialog.getColor());
            }

        }

    }

    public void processAddLine() {
        app.addLineDialog.showAndWait();

        if (!app.addLineDialog.isCanceled()) {

            String name = app.addLineDialog.getName();
            Color color = app.addLineDialog.getColor();

            AddLineTransaction transaction = new AddLineTransaction(app, name, color);
            mmmWorkspace.jTPS.addTransaction(transaction);
            app.getGUI().getUndoButton().setDisable(false);

//            DraggableLine line = new DraggableLine(name, color);
//            mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
//            mmmData data = (mmmData) app.getDataComponent();
//            Pane canvas = workspace.getCanvas();
//
//            workspace.lines.put(name, line);
//            data.lines.put(name, line);
//            canvas.getChildren().add(line);
//            canvas.getChildren().add(line.label1);
//            canvas.getChildren().add(line.label2);
            //Toolbar controls need to be updated
        }
        
        app.addLineDialog.isCancelled = false;

    }

    public void processRemoveLine() {
        app.removeItemDialog.showAndWait();
        Shape selectedShape = dataManager.getSelectedShape();
        Draggable draggableShape = (Draggable) selectedShape;
        ObservableList<Node> shapes = dataManager.getShapes();
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();

        if (app.removeItemDialog.isOkayed()) {

            if (selectedShape != null && draggableShape.getShapeType().equals(Draggable.LINE)) {
                shapes.remove(selectedShape);
                
                DraggableLine line = (DraggableLine) selectedShape;
                shapes.remove(line.label1);
                shapes.remove(line.label2);

                workspace.lines.remove(line.getName());
                //selectedShape = null;

                //workspace.reloadWorkspace(dataManager);
                //app.getGUI().updateToolbarControls(false);
            }

        }

    }

    /**
     * This method processes a user request to select the outline thickness for
     * shape drawing.
     */
    public void processSelectLineThickness() {
        /**
         * golWorkspace workspace = (golWorkspace) app.getWorkspaceComponent();
         * int outlineThickness = (int)
         * workspace.getOutlineThicknessSlider().getValue();
         * dataManager.setCurrentOutlineThickness(outlineThickness);
         * app.getGUI().updateToolbarControls(false);*
         */
        ChangeLineThicknessTransaction transaction = new ChangeLineThicknessTransaction(app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);
    }

    public void processMoveLineEnd() {

    }

    public void processAddStationToLine(DraggableLine line) {

        line.getStations().add((DraggableStation) dataManager.getSelectedShape());

    }

    public void processListAllStations(DraggableLine line) {

        String list = "";

        for (DraggableStation s : line.getStations()) {

            list += s.getName() + "\n";
        }

        app.listStationsDialog.show("All Stations on This Line:", list);

    }

    public void processRemoveStationFromLine() {

    }

    public void processAddNewStation() {
        app.addStationDialog.showAndWait();

        if (!app.addStationDialog.isCanceled()) {

            String name = app.addStationDialog.getName();

            AddStationTransaction transaction = new AddStationTransaction(app, name);
            mmmWorkspace.jTPS.addTransaction(transaction);
            app.getGUI().getUndoButton().setDisable(false);

//            DraggableStation station = new DraggableStation(name);
//            mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
//            Pane canvas = workspace.getCanvas();
//
//            canvas.getChildren().add(station);
            //canvas.getChildren().add(station.label);
            //Toolbar controls need to be updated
        }
        app.addStationDialog.isCancelled = false;
        
    }

    public void processRemoveStation() {

        app.removeItemDialog.showAndWait();
        Shape selectedShape = dataManager.getSelectedShape();
        Draggable draggableShape = (Draggable) selectedShape;
        ObservableList<Node> shapes = dataManager.getShapes();
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();

        if (app.removeItemDialog.isOkayed()) {

            if (selectedShape != null && draggableShape.getShapeType().equals(Draggable.STATION)) {
                shapes.remove(selectedShape);
                DraggableStation s = (DraggableStation) selectedShape;
                shapes.remove(s.label);
                //selectedShape = null;

                //workspace.reloadWorkspace(dataManager);
                //app.getGUI().updateToolbarControls(false);
            }

        }

    }

    public void processMoveStation() {

    }

    /**
     * This method processes a user request to take a snapshot of the current
     * scene.
     */
    public void processSnapshot() {

        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        String mapName = workspace.mapName;
        Pane canvas = workspace.getCanvas();
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        File file = new File("export/" + mapName + "/" + mapName + " Metro.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void processExportJSON() throws UnsupportedEncodingException, IOException {

        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        String mapName = workspace.mapName;

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder1 = Json.createArrayBuilder();
        ObservableList<Node> shapes = dataManager.getShapes();
        for (Node node : shapes) {

            Shape shape = (Shape) node;

            Draggable draggableShape = ((Draggable) shape);
            String type = draggableShape.getShapeType();

            if (type.equals("LINE")) {

                String name = draggableShape.getName();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                double width = 0;
                double height = 0;
                if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(IMAGE)) {
                    width = draggableShape.getWidth();
                    height = draggableShape.getHeight();
                }

                JsonObject shapeJson = null;

                if (draggableShape.getShapeType().equals(IMAGE)) {
                    File file = ((DraggableImage) shape).getImageFile();
                    Path path = Paths.get(file.toURI());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream(
                            (int) (Files.size(path) * 4 / 3 + 4));

                    try (OutputStream base64Stream = Base64.getEncoder().wrap(bytes)) {
                        Files.copy(path, base64Stream);
                    }

                    String base64 = bytes.toString("US-ASCII");

                    shapeJson = Json.createObjectBuilder()
                            .add("image", base64)
                            .add(JSON_TYPE, type)
                            .add(JSON_X, x)
                            .add(JSON_Y, y)
                            .add(JSON_WIDTH, width)
                            .add(JSON_HEIGHT, height).build();

                } else if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(LINE)) {
                    JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                    JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                    double outlineThickness = shape.getStrokeWidth();

                    shapeJson = Json.createObjectBuilder()
                            .add(JSON_NAME, name)
                            .add(JSON_FILL_COLOR, fillColorJson)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
//                        .add(JSON_WIDTH, width)
//                        .add(JSON_HEIGHT, height)
//                        .add(JSON_FILL_COLOR, fillColorJson)
//                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
//                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
                } else {

                    String text = ((DraggableText) draggableShape).getText();
                    double textSize = ((DraggableText) draggableShape).getFont().getSize();
                    String fontFamily = ((DraggableText) draggableShape).getFont().getFamily();

                    int isBold = 0;
                    int isItalic = 0;

                    if (((DraggableText) draggableShape).isBold()) {
                        isBold = 1;
                    }

                    if (((DraggableText) draggableShape).isItalic()) {
                        isItalic = 1;
                    }

                    shapeJson = Json.createObjectBuilder()
                            .add(JSON_NAME, name)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
//                        .add(JSON_WIDTH, width)
//                        .add(JSON_HEIGHT, height)
//                        .add("bold", isBold)
//                        .add("italic", isItalic)
//                        .add("size", textSize)
//                        .add("family", fontFamily)
//                        .add("text", text).build();
                }

                arrayBuilder1.add(shapeJson);

            }

        }

        JsonArray linesArray = arrayBuilder1.build();

        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
        //ObservableList<Node> shapes = dataManager.getShapes();
        for (Node node : shapes) {

            Shape shape = (Shape) node;

            Draggable draggableShape = ((Draggable) shape);
            String type = draggableShape.getShapeType();

            if (type.equals("STATION")) {

                String name = draggableShape.getName();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                double width = 0;
                double height = 0;
                if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(IMAGE)) {
                    width = draggableShape.getWidth();
                    height = draggableShape.getHeight();
                }

                JsonObject shapeJson = null;

                if (draggableShape.getShapeType().equals(IMAGE)) {
                    File file = ((DraggableImage) shape).getImageFile();
                    Path path = Paths.get(file.toURI());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream(
                            (int) (Files.size(path) * 4 / 3 + 4));

                    try (OutputStream base64Stream = Base64.getEncoder().wrap(bytes)) {
                        Files.copy(path, base64Stream);
                    }

                    String base64 = bytes.toString("US-ASCII");

                    shapeJson = Json.createObjectBuilder()
                            .add("image", base64)
                            .add(JSON_TYPE, type)
                            .add(JSON_X, x)
                            .add(JSON_Y, y)
                            .add(JSON_WIDTH, width)
                            .add(JSON_HEIGHT, height).build();

                } else if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(LINE)) {
                    JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                    JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                    double outlineThickness = shape.getStrokeWidth();

                    shapeJson = Json.createObjectBuilder()
                            .add(JSON_NAME, name)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
//                        .add(JSON_WIDTH, width)
//                        .add(JSON_HEIGHT, height)
//                        .add(JSON_FILL_COLOR, fillColorJson)
//                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
//                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
                } else {

                    String text = ((DraggableText) draggableShape).getText();
                    double textSize = ((DraggableText) draggableShape).getFont().getSize();
                    String fontFamily = ((DraggableText) draggableShape).getFont().getFamily();

                    int isBold = 0;
                    int isItalic = 0;

                    if (((DraggableText) draggableShape).isBold()) {
                        isBold = 1;
                    }

                    if (((DraggableText) draggableShape).isItalic()) {
                        isItalic = 1;
                    }

                    shapeJson = Json.createObjectBuilder()
                            .add(JSON_NAME, name)
                            .add(JSON_X, x)
                            .add(JSON_Y, y).build();
//                        .add(JSON_WIDTH, width)
//                        .add(JSON_HEIGHT, height)
//                        .add("bold", isBold)
//                        .add("italic", isItalic)
//                        .add("size", textSize)
//                        .add("family", fontFamily)
//                        .add("text", text).build();
                }

                arrayBuilder2.add(shapeJson);

            }

        }

        JsonArray stationsArray = arrayBuilder2.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                //.add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_NAME, mapName)
                .add(JSON_LINES, linesArray)
                .add(JSON_STATIONS, stationsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream("export/" + dataManager.getMapName() + "/" + dataManager.getMapName() + "_" +" Metro.json");
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter("export/" + dataManager.getMapName() + "/" + dataManager.getMapName() + "_" + " Metro.json");
        pw.write(prettyPrinted);
        pw.close();
    }

    private JsonObject makeJsonColorObject(Color color) {
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_RED, color.getRed())
                .add(JSON_GREEN, color.getGreen())
                .add(JSON_BLUE, color.getBlue())
                .add(JSON_ALPHA, color.getOpacity()).build();
        return colorJson;
    }

    /**
     * This method handles a user request to remove the selected shape.
     */
    public void processRemoveSelectedShape() {

        RemoveSelectedShapeTransaction transaction = new RemoveSelectedShapeTransaction(app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    /**
     * This method processes a user request to select the background color.
     */
    public void processSelectBackgroundColor(Pane canvas, Color color, mmmData data) {

        ChangeBackgroundColorTransaction transaction = new ChangeBackgroundColorTransaction(canvas, color, data);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }
    
    public void processSelectTextColor(DraggableText text, Color color, mmmData data) {

        ChangeTextColorTransaction transaction = new ChangeTextColorTransaction(text, color, data);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }
    
    
    
    

    public void processAddNewImage() {

        image = new ImageView();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(app.getGUI().getWindow());

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();

            AddImageTransaction transaction = new AddImageTransaction(app, imagePath, selectedFile);
            mmmWorkspace.jTPS.addTransaction(transaction);
            app.getGUI().getUndoButton().setDisable(false);

        }

    }

    public void processAddText() {

        app.textInputDialog.showAndWait();

        String text = app.textInputDialog.getText();

        AddTextTransaction transaction = new AddTextTransaction(app, text);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    public void processChangeFontFamily(String font) {
        SelectFontFamilyTransaction transaction = new SelectFontFamilyTransaction(font, app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    public void processChangeFontSize(String fontSize) {
        SelectFontSizeTransaction transaction = new SelectFontSizeTransaction(fontSize, app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    public void processBoldText() {

        BoldTextTransaction transaction = new BoldTextTransaction(app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    public void processItalicizeText() {

        ItalicizeTextTransaction transaction = new ItalicizeTextTransaction(app);
        mmmWorkspace.jTPS.addTransaction(transaction);
        app.getGUI().getUndoButton().setDisable(false);

    }

    public static boolean isText(Shape shape) {
        try {
            DraggableText draggableText = (DraggableText) shape;

            return true;

        } catch (ClassCastException e) {
            return false;
        }
    }

    private boolean isImage(Shape shape) {
        try {
            DraggableImage draggableImage = (DraggableImage) shape;

            return true;

        } catch (ClassCastException e) {
            return false;
        }
    }

}
