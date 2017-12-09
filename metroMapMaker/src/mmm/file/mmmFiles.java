/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.file;

import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jtps.jTPS;
import mmm.data.Draggable;
import static mmm.data.Draggable.IMAGE;
import static mmm.data.Draggable.LINE;
import static mmm.data.Draggable.STATION;
import static mmm.data.Draggable.TEXT;
import mmm.data.DraggableImage;
import mmm.data.DraggableLine;
import mmm.data.DraggableStation;
import mmm.data.DraggableText;
import mmm.data.mmmData;
import mmm.gui.mmmWorkspace;
import sun.misc.BASE64Decoder;

/**
 *
 * @author naimyoussiftraore
 */
public class mmmFiles implements AppFileComponent {

    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_START_X = "startX";
    static final String JSON_START_Y = "startY";
    static final String JSON_END_X = "endX";
    static final String JSON_END_Y = "endY";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    static final String JSON_IS_LINE_LABEL = "line_label";
    static final String JSON_IS_START_LABEL = "start_label";

    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";

    @Override
    public void saveData(AppDataComponent data, String name) throws IOException {
        // GET THE DATA
        mmmData dataManager = (mmmData) data;

        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = null;

        if (bgColor != null) {
            bgColorJson = makeJsonColorObject(bgColor);
        } else {
            bgColorJson = makeJsonColorObject(Color.WHITE);
        }

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Node> shapes = dataManager.getShapes();
        for (Node node : shapes) {
            Shape shape = (Shape) node;
            Draggable draggableShape = ((Draggable) shape);
            String type = draggableShape.getShapeType();
            double x = draggableShape.getX();
            double y = draggableShape.getY();
            double width = 0;
            double height = 0;
            if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(IMAGE) || draggableShape.getShapeType().equals(LINE)) {
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

            } else if (draggableShape.getShapeType().equals(STATION)) {
                JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                double outlineThickness = shape.getStrokeWidth();
                String lineOn = "";
                String stationName = "";
                String sLine = "";
                String nLine = "";

                lineOn = ((DraggableStation) draggableShape).getLineOn();
                stationName = ((DraggableStation) draggableShape).getName();
                sLine = ((DraggableStation) draggableShape).southLine;
                nLine = ((DraggableStation) draggableShape).northLine;

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add("LINE_ON", lineOn)
                        .add("STATION_NAME", stationName)
                        .add("SOUTH_LINE", sLine)
                        .add("NORTH_LINE", nLine).build();

            } else if (draggableShape.getShapeType().equals(LINE)) {
                JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                double outlineThickness = shape.getStrokeWidth();
                String lineName = "";
                int isExtension = 0;
                String startStation = "";
                String endStation = "";
                int extensionCounter = 0;
                String lastExt = "";
                String alias = "";
                
                extensionCounter = ((DraggableLine) draggableShape).extensionCounter;
                lastExt = ((DraggableLine) draggableShape).lastExtension;
                        

                lineName = ((DraggableLine) draggableShape).getName();
                if (((DraggableLine) draggableShape).isExtension()) {
                    isExtension = 1;
                }
                
                alias = ((DraggableLine) draggableShape).alias;

                startStation = ((DraggableLine) draggableShape).startStation;
                endStation = ((DraggableLine) draggableShape).endStation;

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add("LINE_NAME", lineName)
                        .add("IS_EXTENSION", isExtension)
                        .add("START_STA", startStation)
                        .add("END_STA", endStation)
                        .add("EXT_COUNT", extensionCounter)
                        .add("LAST_EXT", lastExt)
                        .add("ALIAS", alias).build();

            } else {

                String text = ((DraggableText) draggableShape).getText();
                double textSize = ((DraggableText) draggableShape).getFont().getSize();
                String fontFamily = ((DraggableText) draggableShape).getFont().getFamily();

                int isBold = 0;
                int isItalic = 0;
                int isLineLabel = 0;
                int isStartLabel = 0;
                int isStationLabel = 0;

                if (((DraggableText) draggableShape).isBold()) {
                    isBold = 1;
                }

                if (((DraggableText) draggableShape).isItalic()) {
                    isItalic = 1;
                }

                if (((DraggableText) draggableShape).isLineLabel()) {
                    isLineLabel = 1;
                }

                if (((DraggableText) draggableShape).isStartLabel()) {
                    isStartLabel = 1;
                }

                if (((DraggableText) draggableShape).isStationLabel) {
                    isStationLabel = 1;
                }

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add("bold", isBold)
                        .add("italic", isItalic)
                        .add("size", textSize)
                        .add("family", fontFamily)
                        .add("text", text)
                        .add(JSON_IS_LINE_LABEL, isLineLabel)
                        .add(JSON_IS_START_LABEL, isStartLabel)
                        .add("IS_STATION_LABEL", isStationLabel).build();
            }

            arrayBuilder.add(shapeJson);
        }
        JsonArray shapesArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_SHAPES, shapesArray)
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
//        OutputStream os = new FileOutputStream(filePath);
//        JsonWriter jsonFileWriter = Json.createWriter(os);
//        jsonFileWriter.writeObject(dataManagerJSO);
//        String prettyPrinted = sw.toString();
//        PrintWriter pw = new PrintWriter(filePath);
//        pw.write(prettyPrinted);
//        pw.close();
        
        OutputStream os = new FileOutputStream("export/" + name + "/" + name + " Metro.json");
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter("export/" + name + "/" + name + " Metro.json");
        pw.write(prettyPrinted);
        pw.close();

       
    }
    
     public void saveData(AppDataComponent data) throws IOException {
        // GET THE DATA
        mmmData dataManager = (mmmData) data;

        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = null;

        if (bgColor != null) {
            bgColorJson = makeJsonColorObject(bgColor);
        } else {
            bgColorJson = makeJsonColorObject(Color.WHITE);
        }

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ObservableList<Node> shapes = dataManager.getShapes();
        for (Node node : shapes) {
            Shape shape = (Shape) node;
            Draggable draggableShape = ((Draggable) shape);
            String type = draggableShape.getShapeType();
            double x = draggableShape.getX();
            double y = draggableShape.getY();
            double width = 0;
            double height = 0;
            if (draggableShape.getShapeType().equals(STATION) || draggableShape.getShapeType().equals(IMAGE) || draggableShape.getShapeType().equals(LINE)) {
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

            } else if (draggableShape.getShapeType().equals(STATION)) {
                JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                double outlineThickness = shape.getStrokeWidth();
                String lineOn = "";
                String stationName = "";
                String sLine = "";
                String nLine = "";
                

                lineOn = ((DraggableStation) draggableShape).getLineOn();
                stationName = ((DraggableStation) draggableShape).getName();
                sLine = ((DraggableStation) draggableShape).southLine;
                nLine = ((DraggableStation) draggableShape).northLine;

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add("LINE_ON", lineOn)
                        .add("STATION_NAME", stationName)
                        .add("SOUTH_LINE", sLine)
                        .add("NORTH_LINE", nLine).build();

            } else if (draggableShape.getShapeType().equals(LINE)) {
                JsonObject fillColorJson = makeJsonColorObject((Color) shape.getFill());
                JsonObject outlineColorJson = makeJsonColorObject((Color) shape.getStroke());
                double outlineThickness = shape.getStrokeWidth();
                String lineName = "";
                int isExtension = 0;
                String startStation = "";
                String endStation = "";
                int extensionCounter = 0;
                String lastExt = "";
                String alias = "";

                lineName = ((DraggableLine) draggableShape).getName();
                if (((DraggableLine) draggableShape).isExtension()) {
                    isExtension = 1;
                }

                startStation = ((DraggableLine) draggableShape).startStation;
                endStation = ((DraggableLine) draggableShape).endStation;
                
                alias = ((DraggableLine) draggableShape).alias;

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add(JSON_FILL_COLOR, fillColorJson)
                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
                        .add(JSON_OUTLINE_THICKNESS, outlineThickness)
                        .add("LINE_NAME", lineName)
                        .add("IS_EXTENSION", isExtension)
                        .add("START_STA", startStation)
                        .add("END_STA", endStation)
                        .add("EXT_COUNT", extensionCounter)
                        .add("LAST_EXT", lastExt)
                        .add("ALIAS", alias).build();

            } else {

                String text = ((DraggableText) draggableShape).getText();
                double textSize = ((DraggableText) draggableShape).getFont().getSize();
                String fontFamily = ((DraggableText) draggableShape).getFont().getFamily();

                int isBold = 0;
                int isItalic = 0;
                int isLineLabel = 0;
                int isStartLabel = 0;
                int isStationLabel = 0;

                if (((DraggableText) draggableShape).isBold()) {
                    isBold = 1;
                }

                if (((DraggableText) draggableShape).isItalic()) {
                    isItalic = 1;
                }

                if (((DraggableText) draggableShape).isLineLabel()) {
                    isLineLabel = 1;
                }

                if (((DraggableText) draggableShape).isStartLabel()) {
                    isStartLabel = 1;
                }

                if (((DraggableText) draggableShape).isStationLabel) {
                    isStationLabel = 1;
                }

                shapeJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_WIDTH, width)
                        .add(JSON_HEIGHT, height)
                        .add("bold", isBold)
                        .add("italic", isItalic)
                        .add("size", textSize)
                        .add("family", fontFamily)
                        .add("text", text)
                        .add(JSON_IS_LINE_LABEL, isLineLabel)
                        .add(JSON_IS_START_LABEL, isStartLabel)
                        .add("IS_STATION_LABEL", isStationLabel).build();
            }

            arrayBuilder.add(shapeJson);
        }
        JsonArray shapesArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_SHAPES, shapesArray)
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
//        OutputStream os = new FileOutputStream(filePath);
//        JsonWriter jsonFileWriter = Json.createWriter(os);
//        jsonFileWriter.writeObject(dataManagerJSO);
//        String prettyPrinted = sw.toString();
//        PrintWriter pw = new PrintWriter(filePath);
//        pw.write(prettyPrinted);
//        pw.close();
            System.out.println();
         System.out.println(dataManager.getMapName());
        OutputStream os = new FileOutputStream("export/" + dataManager.getMapName() + "/" + dataManager.getMapName() + " Metro.json");
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter("export/" + dataManager.getMapName() + "/" + dataManager.getMapName() + " Metro.json");
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
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
        mmmData dataManager = (mmmData) data;
        dataManager.resetData();

        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // LOAD THE BACKGROUND COLOR
        Color bgColor = loadColor(json, JSON_BG_COLOR);
        dataManager.setBackgroundColor(bgColor);

        // AND NOW LOAD ALL THE SHAPES
        ArrayList<DraggableLine> iterateLines = new ArrayList<DraggableLine>(); // ALL LINES, INCLUDING EXTENSIONS
        
        ArrayList<DraggableStation> iterateStations = new ArrayList<DraggableStation>();
        HashMap<String, DraggableText> lineLabelMap = new HashMap<String, DraggableText>();
        ArrayList<DraggableText> lineLabels = new ArrayList<DraggableText>();
        HashMap<String, DraggableText> stationLabels = new HashMap<String, DraggableText>();
        

        JsonArray jsonShapeArray = json.getJsonArray(JSON_SHAPES);
        for (int i = 0; i < jsonShapeArray.size(); i++) {
            JsonObject jsonShape = jsonShapeArray.getJsonObject(i);
            Shape shape = loadShape(jsonShape);
            String type = jsonShape.getString(JSON_TYPE);
            if (type.equals(LINE)) {
                DraggableLine line = (DraggableLine) shape;
                iterateLines.add(line);
                dataManager.allLines.put(line.alias, line);

                if (!line.isExtension) {
                    dataManager.lines.put(line.getName(), line);
                }

            } else if (type.equals(STATION)) {
                DraggableStation station = (DraggableStation) shape;
                iterateStations.add(station);
                dataManager.stations.add(station);
                dataManager.stationMap.put(station.getName(), station);
            } else if (type.equals(TEXT)) {
                DraggableText label = (DraggableText) shape;

                if (label.isLineLabel()) {
                    lineLabelMap.put(label.getText(), label);
                    lineLabels.add(label);
                } else if (label.isStationLabel) {
                    stationLabels.put(label.getText(), label);
                }

            }

            dataManager.addShape(shape);
        }

        // POPULATE THE STATIONS LIST OF ALL LINES
        for (DraggableStation s : dataManager.stations) {

            if (dataManager.lines.containsKey(s.getLineOn())) {
                dataManager.lines.get(s.getLineOn()).getStations().add(s);
            }
        }

        // BIND WHAT NEEDS BINDING
        for (DraggableStation s : iterateStations) {

            String label = s.getName();

            DoubleProperty xOffset = new SimpleDoubleProperty(15);
            DoubleProperty yOffset = new SimpleDoubleProperty(5);

            s.centerXProperty().bind(stationLabels.get(label).xProperty().subtract(xOffset));
            s.centerYProperty().bind(stationLabels.get(label).yProperty().add(yOffset));

            s.label = stationLabels.get(label);
        }

        for (DraggableLine l : iterateLines) {

            if (!(l.startStation.equals(""))) {

                if (dataManager.stationMap.containsKey(l.startStation)) {

                    l.startXProperty().bind(dataManager.stationMap.get(l.startStation).centerXProperty());
                    l.startYProperty().bind(dataManager.stationMap.get(l.startStation).centerYProperty());
                }

            } else {
                if (lineLabelMap.containsKey(l.getName())) {

//                    if (lineLabelMap.get(l.getName()).isStartLabel()) {
//                        l.startXProperty().bind(lineLabelMap.get(l.getName()).xProperty());
//                        l.startYProperty().bind(lineLabelMap.get(l.getName()).yProperty());
//                        
//                    }
                    for (DraggableText t : lineLabels) {
                        if (t.isStartLabel()) {
                            l.startXProperty().bind(t.xProperty());
                            l.startYProperty().bind(t.yProperty());

                            l.label1 = t;
                        }
                    }

                }
            }

            if (!(l.endStation.equals(""))) {

                if (dataManager.stationMap.containsKey(l.endStation)) {

                    l.endXProperty().bind(dataManager.stationMap.get(l.endStation).centerXProperty());
                    l.endYProperty().bind(dataManager.stationMap.get(l.endStation).centerYProperty());

                }

            } else {
                if (lineLabelMap.containsKey(l.getName())) {

                    for (DraggableText t : lineLabels) {
                        if (!(t.isStartLabel())) {
                            l.endXProperty().bind(t.xProperty());
                            l.endYProperty().bind(t.yProperty());

                            l.label2 = t;
                        }
                    }

                }

            }

        }
        
        

        mmmWorkspace.setStack(new jTPS());
    }

    private double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    private double getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.intValue();
    }

    private String getDataAsString(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonString text = (JsonString) value;

        return text.getString();
    }

    private Shape loadShape(JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String type = jsonShape.getString(JSON_TYPE);
        Shape shape;

        if (type.equals(STATION)) {
            shape = new DraggableStation();

        } else if (type.equals(IMAGE)) {
            shape = new DraggableImage();

        } else if (type.equals(LINE)) {
            shape = new DraggableLine();

        } else {
            shape = new DraggableText();
        }

        // THEN LOAD ITS FILL AND OUTLINE PROPERTIES
        if (type.equals(IMAGE)) {
            BufferedImage imageFill = loadImage(jsonShape.getString("image"));
            javafx.scene.image.Image fill = SwingFXUtils.toFXImage(imageFill, null);
            shape.setFill(new ImagePattern(fill));

        } else if ((type.equals(STATION))) {
            Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
            Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
            double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
            String name = (getDataAsString(jsonShape, "STATION_NAME"));
            String lineOn = (getDataAsString(jsonShape, "LINE_ON"));
            String sLine = (getDataAsString(jsonShape, "SOUTH_LINE"));
            String nLine = (getDataAsString(jsonShape, "NORTH_LINE"));

            shape.setFill(fillColor);
            shape.setStroke(outlineColor);
            shape.setStrokeWidth(outlineThickness);
            ((DraggableStation) shape).setName(name);
            ((DraggableStation) shape).setLineOn(lineOn);
            ((DraggableStation) shape).southLine = sLine;
            ((DraggableStation) shape).northLine = nLine;

        } else if ((type.equals(LINE))) {
            Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
            Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
            double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
            String lineName = (getDataAsString(jsonShape, "LINE_NAME"));
            int isExtension = (int) (getDataAsInt(jsonShape, "IS_EXTENSION"));
            String startStation = (getDataAsString(jsonShape, "START_STA"));
            String endStation = (getDataAsString(jsonShape, "END_STA"));
            String lastExt = (getDataAsString(jsonShape, "LAST_EXT"));
            int extCount = (int) (getDataAsInt(jsonShape, "EXT_COUNT"));
            String alias = (getDataAsString(jsonShape, "ALIAS"));

            shape.setFill(fillColor);
            shape.setStroke(outlineColor);
            shape.setStrokeWidth(outlineThickness);
            ((DraggableLine) shape).setName(lineName);
            if (isExtension == 1) {
                ((DraggableLine) shape).isExtension = true;
            }
            ((DraggableLine) shape).startStation = startStation;
            ((DraggableLine) shape).endStation = endStation;
            ((DraggableLine) shape).extensionCounter = extCount;
            ((DraggableLine) shape).lastExtension = lastExt;
            ((DraggableLine) shape).color = fillColor;
            ((DraggableLine) shape).setName(lineName);
            ((DraggableLine) shape).alias = alias;
            

        } else {

            String family = (getDataAsString(jsonShape, "family"));
            double size = getDataAsDouble(jsonShape, "size");
            int isBold = (int) getDataAsInt(jsonShape, "bold");
            int isItalic = (int) getDataAsInt(jsonShape, "italic");
            int isLineLabel = (int) (getDataAsInt(jsonShape, JSON_IS_LINE_LABEL));
            int isStartLabel = (int) (getDataAsInt(jsonShape, JSON_IS_START_LABEL));
            int isStationLabel = (int) (getDataAsInt(jsonShape, "IS_STATION_LABEL"));

            if (isLineLabel == 1) {
                ((DraggableText) shape).setIsLineLabel(true);
            }
            if (isStartLabel == 1) {
                ((DraggableText) shape).setIsStartLabel(true);
            }
            if (isStationLabel == 1) {
                ((DraggableText) shape).isStationLabel = true;
            }

            ((DraggableText) shape).setText(getDataAsString(jsonShape, "text"));

            if (isBold == 1 && isItalic == 0) {
                ((DraggableText) shape).setFont(Font.font(family, FontWeight.BOLD, size));
                ((DraggableText) shape).setBold(true);

            } else if (isBold == 1 && isItalic == 1) {
                ((DraggableText) shape).setFont(Font.font(family, FontWeight.BOLD, FontPosture.ITALIC, size));
                ((DraggableText) shape).setBold(true);
                ((DraggableText) shape).setItalic(true);

            } else if (isBold == 0 && isItalic == 1) {
                ((DraggableText) shape).setFont(Font.font(family, FontPosture.ITALIC, size));
                ((DraggableText) shape).setItalic(true);

            } else if (isBold == 0 && isItalic == 0) {
                ((DraggableText) shape).setFont(Font.font(family, size));
            }

        }

        // AND THEN ITS DRAGGABLE PROPERTIES
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        double width = getDataAsDouble(jsonShape, JSON_WIDTH);
        double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
        Draggable draggableShape = (Draggable) shape;
        if (type.equals(IMAGE) || type.equals(STATION) || type.equals(LINE)) {
            draggableShape.setLocationAndSize(x, y, width, height);
        } else {
            draggableShape.setLocationAndSize(x, y, 0, 0);
        }

        // ALL DONE, RETURN IT
        return shape;
    }

    private Color loadColor(JsonObject json, String colorToGet) {
        JsonObject jsonColor = json.getJsonObject(colorToGet);
        double red = getDataAsDouble(jsonColor, JSON_RED);
        double green = getDataAsDouble(jsonColor, JSON_GREEN);
        double blue = getDataAsDouble(jsonColor, JSON_BLUE);
        double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
        Color loadedColor = new Color(red, green, blue, alpha);
        return loadedColor;
    }

    private BufferedImage loadImage(String imageToGet) {
        //JsonObject jsonImage = json.getJsonObject(imageToGet);
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageToGet);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        System.out.println(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
