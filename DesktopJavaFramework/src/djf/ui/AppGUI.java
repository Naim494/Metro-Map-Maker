package djf.ui;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import djf.controller.AppFileController;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jtps.jTPS;

/**
 * This class provides the basic user interface for this application, including
 * all the file controls, but not including the workspace, which would be
 * customly provided for each app.
 *
 * @author Richard McKenna
 * @author Naim Youssif Traore
 * @version 1.0
 */
public class AppGUI {

    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    protected AppFileController fileController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI. NOTE THAT THE WORKSPACE WILL GO
    // IN THE CENTER REGION OF THE appPane
    protected BorderPane appPane;

    // THIS IS THE TOP PANE WHERE WE CAN PUT TOOLBAR
    protected BorderPane topToolbarPane;

    protected BorderPane leftSide;
    protected BorderPane rightSide;

    // THIS IS THE FILE TOOLBAR AND ITS CONTROLS
    protected FlowPane fileToolbar;

    // FILE TOOLBAR BUTTONS
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button saveAsButton;
    protected Button exitButton;
    protected Button exportButton;
    
    // THIS IS THE EDIT TOOLBAR AND ITS CONTROLS
    protected FlowPane editToolbar;

    // EDIT TOOLBAR BUTTONS
    protected Button cutButton;
    protected Button copyButton;
    protected Button pasteButton;

    // THIS IS THE UNDO/REDO TOOLBAR AND ITS CONTROLS
    protected FlowPane undoRedoToolbar;

    // UNDO/REDO TOOLBAR BUTTONS
    protected Button undoButton;
    protected Button redoButton;

    //THIS IS THE LANG TOOLBAR
    protected FlowPane settingsToolbar;

    //LANG TOOLBAR BUTTONS
    protected Button languageButton;
    protected Button aboutButton;

    // THIS DIALOG IS USED FOR GIVING FEEDBACK TO THE USER
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;

    // THIS TITLE WILL GO IN THE TITLE BAR
    protected String appTitle;
    
    protected String nameOfMap = "";

    /**
     * This constructor initializes the file toolbar for use.
     *
     * @param initPrimaryStage The window for this application.
     *
     * @param initAppTitle The title of this application, which will appear in
     * the window bar.
     *
     * @param app The app within this gui is used.
     */
    public AppGUI(Stage initPrimaryStage,
            String initAppTitle,
            AppTemplate app) {
        // SAVE THESE FOR LATER
        primaryStage = initPrimaryStage;
        appTitle = initAppTitle;

        // INIT THE TOOLBAR
        initTopToolbar(app);

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();

        // INIT THE STYLESHEET AND THE STYLE FOR THE FILE TOOLBAR
        initStylesheet(app);
        initFileToolbarStyle();
    }

    /**
     * Accessor method for getting the file toolbar controller.
     */
    public AppFileController getFileController() {
        return fileController;
    }

    /**
     * Accessor method for getting the application pane, within which all user
     * interface controls are ultimately placed.
     *
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() {
        return appPane;
    }

    /**
     * Accessor method for getting the toolbar pane in the top, within which
     * other toolbars are placed.
     *
     * @return This application GUI's app pane.
     */
    public BorderPane getTopToolbarPane() {
        return topToolbarPane;
    }

    /**
     * Accessor method for getting the file toolbar pane, within which all file
     * controls are ultimately placed.
     *
     * @return This application GUI's app pane.
     */
    public FlowPane getFileToolbar() {
        return fileToolbar;
    }

    /**
     * Accessor method for getting this application's primary stage's, scene.
     *
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() {
        return primaryScene;
    }

    /**
     * Accessor method for getting this application's window, which is the
     * primary stage within which the full GUI will be placed.
     *
     * @return This application's primary stage (i.e. window).
     */
    public Stage getWindow() {
        return primaryStage;
    }

    /**
     * This method is used to activate/deactivate toolbar buttons when they can
     * and cannot be used so as to provide foolproof design.
     *
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);

        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
        newButton.setDisable(false);
        loadButton.setDisable(false);
        exitButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /**
     * *************************************************************************
     */
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /**
     * *************************************************************************
     */
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initTopToolbar(AppTemplate app) {
        fileToolbar = new FlowPane();
        fileToolbar.setMaxWidth(350);

        settingsToolbar = new FlowPane();
        settingsToolbar.setAlignment(Pos.TOP_RIGHT);
        settingsToolbar.setMaxWidth(355);

        editToolbar = new FlowPane();
        editToolbar.setMaxWidth(355);

        undoRedoToolbar = new FlowPane();
        undoRedoToolbar.setMaxWidth(350);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbar, NEW_ICON.toString(), NEW_TOOLTIP.toString(), false);
        loadButton = initChildButton(fileToolbar, LOAD_ICON.toString(), LOAD_TOOLTIP.toString(), false);
        saveButton = initChildButton(fileToolbar, SAVE_ICON.toString(), SAVE_TOOLTIP.toString(), true);
        saveAsButton = initChildButton(fileToolbar, SAVE_AS_ICON.toString(), SAVE_TOOLTIP.toString(), false);
        exitButton = initChildButton(fileToolbar, EXIT_ICON.toString(), EXIT_TOOLTIP.toString(), false);
        exportButton = new Button();
        exportButton.setText("Export");
        exportButton.setDisable(false);
        fileToolbar.getChildren().add(exportButton);

        languageButton = initChildButton(settingsToolbar, LANGUAGE_ICON.toString(), LANGUAGE_TOOLTIP.toString(), false);
        aboutButton = initChildButton(settingsToolbar, ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), false);

        cutButton = initChildButton(editToolbar, CUT_ICON.toString(), CUT_TOOLTIP.toString(), true);
        copyButton = initChildButton(editToolbar, COPY_ICON.toString(), COPY_TOOLTIP.toString(), true);
        pasteButton = initChildButton(editToolbar, PASTE_ICON.toString(), PASTE_TOOLTIP.toString(), true);

        undoButton = initChildButton(undoRedoToolbar, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);
        redoButton = initChildButton(undoRedoToolbar, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);

        // AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);

        newButton.setOnAction(e -> {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            AppTextInputDialogSingleton textDialog = AppTextInputDialogSingleton.getSingleton();
            textDialog.isCancelled = false;

            String name = "";
            boolean done = false;

            try {
                done = false;

                textDialog.show("Name", "Enter a name for the map:");
                File file;

                while (!(done) && !(textDialog.isCanceled())) {

                    name = textDialog.getText();
                    nameOfMap = textDialog.getText();

                    file = new File("export/" + name + "/" + name);

                    if (!(file.exists())) {

                        file.getParentFile().mkdir();
                        file.createNewFile();

                        done = true;
                        
                        File recentList = new File("recentList.txt");
                        if (!(recentList.exists())) {
                            recentList.createNewFile();
                        }
                        Files.write(Paths.get("recentList.txt"), (name + "\n").getBytes(), StandardOpenOption.APPEND);

                    } else {
                        textDialog.show("Name", "Name already in use, please enter a new name:");

                    }
                }
            } catch (IOException ioe) {
                // SOMETHING WENT WRONG, PROVIDE FEEDBACK
                dialog.show(props.getProperty(NEW_ERROR_TITLE), props.getProperty(NEW_ERROR_MESSAGE));
            }

            if (done) {
                fileController.handleNewRequest();
            }
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
            this.setNameOfMap(fileController.name);

        });
        saveButton.setOnAction(e -> {
            try {
                fileController.saveWork();
            } catch (IOException ex) {
                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        saveAsButton.setOnAction(e -> {
            
            try {
                fileController.handleSaveAsRequest();
            } catch (IOException ex) {
                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });

        aboutButton.setOnAction(e -> {
            AppTemplate.aboutDialog.showAndWait();
        });

        // NOW PUT THE FILE TOOLBAR IN THE TOP TOOLBAR, WHICH COULD
        // ALSO STORE OTHER TOOLBARS
        topToolbarPane = new BorderPane();
        leftSide = new BorderPane();
        rightSide = new BorderPane();

        leftSide.setLeft(fileToolbar);
        leftSide.setRight(editToolbar);

        rightSide.setLeft(undoRedoToolbar);
        rightSide.setRight(settingsToolbar);

        topToolbarPane.setLeft(leftSide);
        topToolbarPane.setRight(rightSide);

        /**
         * topToolbarPane.setLeft(fileToolbar);
         * topToolbarPane.setCenter(editToolbar);
         * topToolbarPane.setCenter(undoRedoToolbar);
         * topToolbarPane.setRight(settingsToolbar);*
         */
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);
        //primaryStage.getIcons().add(e)

        // START FULL-SCREEN OR NOT, ACCORDING TO PREFERENCES
        primaryStage.setMaximized("true".equals(props.getProperty(START_MAXIMIZED)));

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(topToolbarPane);
        primaryScene = new Scene(appPane);

        // SET THE APP PANE PREFERRED SIZE ACCORDING TO THE PREFERENCES
        double prefWidth = Double.parseDouble(props.getProperty(PREF_WIDTH));
        double prefHeight = Double.parseDouble(props.getProperty(PREF_HEIGHT));
        appPane.setPrefWidth(prefWidth);
        appPane.setPrefHeight(prefHeight);

        // SET THE APP ICON
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW
        primaryStage.setScene(primaryScene);
    }

    /**
     * This is a public helper method for initializing a simple button with an
     * icon and tooltip and placing it into a toolbar.
     *
     * @param toolbar Toolbar pane into which to place this button.
     *
     * @param icon Icon image file name for the button.
     *
     * @param tooltip Tooltip to appear when the user mouses over the button.
     *
     * @param disabled true if the button is to start off disabled, false
     * otherwise.
     *
     * @return A constructed, fully initialized button placed into its
     * appropriate pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
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
        toolbar.getChildren().add(button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }

    public Button initChildButton(Pane toolbar, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);

        // PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }

    /**
     * Note that this is the default style class for the top file toolbar and
     * that style characteristics for this type of component should be put
     * inside app_properties.xml.
     */
    public static final String CLASS_BORDERED_PANE = "bordered_pane";

    /**
     * Note that this is the default style class for the file buttons in the top
     * file toolbar and that style characteristics for this type of component
     * should be put inside app_properties.xml.
     */
    public static final String CLASS_FILE_BUTTON = "file_button";

    /**
     * This function sets up the stylesheet to be used for specifying all style
     * for this application. Note that it does not attach CSS style classes to
     * controls, that must be done separately.
     */
    private void initStylesheet(AppTemplate app) {
        // SELECT THE STYLESHEET
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String stylesheet = props.getProperty(APP_PATH_CSS);
        stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
        URL stylesheetURL = appClass.getResource(stylesheet);
        String stylesheetPath = stylesheetURL.toExternalForm();
        primaryScene.getStylesheets().add(stylesheetPath);
    }

    /**
     * This function specifies the CSS style classes for the controls managed by
     * this framework.
     */
    private void initFileToolbarStyle() {
        topToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        fileToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        settingsToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        editToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        undoRedoToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        newButton.getStyleClass().add(CLASS_FILE_BUTTON);
        loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
        exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
        languageButton.getStyleClass().add(CLASS_FILE_BUTTON);
        aboutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        cutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        copyButton.getStyleClass().add(CLASS_FILE_BUTTON);
        pasteButton.getStyleClass().add(CLASS_FILE_BUTTON);
        undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }

    public void refreshTopToolbar() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        newButton.setTooltip(new Tooltip(props.getProperty(NEW_TOOLTIP.toString())));
        loadButton.setTooltip(new Tooltip(props.getProperty(LOAD_TOOLTIP.toString())));
        saveButton.setTooltip(new Tooltip(props.getProperty(SAVE_TOOLTIP.toString())));
        exitButton.setTooltip(new Tooltip(props.getProperty(EXIT_TOOLTIP.toString())));
        languageButton.setTooltip(new Tooltip(props.getProperty(LANGUAGE_TOOLTIP.toString())));
        aboutButton.setTooltip(new Tooltip(props.getProperty(ABOUT_TOOLTIP.toString())));
        cutButton.setTooltip(new Tooltip(props.getProperty(CUT_TOOLTIP.toString())));
        copyButton.setTooltip(new Tooltip(props.getProperty(COPY_TOOLTIP.toString())));
        pasteButton.setTooltip(new Tooltip(props.getProperty(PASTE_TOOLTIP.toString())));
        undoButton.setTooltip(new Tooltip(props.getProperty(UNDO_TOOLTIP.toString())));
        redoButton.setTooltip(new Tooltip(props.getProperty(REDO_TOOLTIP.toString())));

    }

    public Button getLanguageButton() {
        return languageButton;
    }

    public Button getCutButton() {
        return cutButton;
    }

    public Button getCopyButton() {
        return copyButton;
    }

    public Button getPasteButton() {
        return pasteButton;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public Button getRedoButton() {
        return redoButton;
    }
    
    public Button getExportButton() {
        return exportButton;
    }
    
    public String getNameOfMap() {
        return nameOfMap;
    }
    
    public void setNameOfMap(String name) {
        this.nameOfMap = name;
    }
}
