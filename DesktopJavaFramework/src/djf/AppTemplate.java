package djf;

import djf.ui.*;
import djf.components.*;
import javafx.application.Application;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.*;
import java.io.File;
import properties_manager.InvalidXMLFileFormatException;

/**
 * This is the framework's JavaFX application. It provides the start method that
 * begins the program initialization, which delegates component initialization
 * to the application-specific child class' hook function.
 *
 * @author Richard McKenna
 * @author Naim Youssif Traore
 * @version 1.0
 */
public abstract class AppTemplate extends Application {

    // THIS IS THE APP'S FULL JavaFX GUI. NOTE THAT ALL APPS WOULD
    // SHARE A COMMON UI EXCEPT FOR THE CUSTOM WORKSPACE
    protected AppGUI gui;

    // THIS CLASS USES A COMPONENT ARCHITECTURE DESIGN PATTERN, MEANING IT
    // HAS OBJECTS THAT CAN BE SWAPPED OUT FOR OTHER COMPONENTS
    // THIS APP HAS 4 COMPONENTS
    // THE COMPONENT FOR MANAGING CUSTOM APP DATA
    protected AppDataComponent dataComponent;

    // THE COMPONENT FOR MANAGING CUSTOM FILE I/O
    protected AppFileComponent fileComponent;

    // THE COMPONENT FOR THE GUI WORKSPACE
    protected AppWorkspaceComponent workspaceComponent;

    protected File selectedEN;
    protected File selectedITA;

    public static AppSelectLanguageDialogSingleton selectLanguageDialog;
    public static AppAboutMessageDialogSingleton aboutDialog;
    public static AppTextInputDialogSingleton textInputDialog;
    public static AppAddLineDialogSingleton addLineDialog;
    public static AppAddStationDialogSingleton addStationDialog;
    public static AppRemoveNodeDialogSingleton removeItemDialog;
    public static AppListStationsDialogSingleton listStationsDialog;
    public static AppWelcomeDialogSingleton welcomeDialog;

    // THIS METHOD MUST BE OVERRIDDEN WHERE THE CUSTOM BUILDER OBJECT
    // WILL PROVIDE THE CUSTOM APP COMPONENTS
    /**
     * This function must be overridden, it should initialize all of the
     * components used by the app in the proper order according to the
     * particular app's dependencies.
     */
    public abstract void buildAppComponentsHook();

    // COMPONENT ACCESSOR METHODS
    /**
     * Accessor for the data component.
     */
    public AppDataComponent getDataComponent() {
        return dataComponent;
    }

    /**
     * Accessor for the file component.
     */
    public AppFileComponent getFileComponent() {
        return fileComponent;
    }

    /**
     * Accessor for the workspace component.
     */
    public AppWorkspaceComponent getWorkspaceComponent() {
        return workspaceComponent;
    }

    /**
     * Accessor for the gui. Note that the GUI would contain the workspace.
     */
    public AppGUI getGUI() {
        return gui;
    }

    /**
     * This is where our Application begins its initialization, it will load the
     * custom app properties, build the components, and fully initialize
     * everything to get the app rolling.
     *
     * @param primaryStage This application's window.
     */
    @Override
    public void start(Stage primaryStage) {
        // LET'S START BY INITIALIZING OUR DIALOGS
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        messageDialog.init(primaryStage);
        
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.init(primaryStage);
        
        aboutDialog =  AppAboutMessageDialogSingleton.getSingleton();
        aboutDialog.init(primaryStage);
        
        textInputDialog = AppTextInputDialogSingleton.getSingleton();
        textInputDialog.init(primaryStage);
        
        addLineDialog = AppAddLineDialogSingleton.getSingleton();
        addLineDialog.init(primaryStage);
        
        addStationDialog = AppAddStationDialogSingleton.getSingleton();
        addStationDialog.init(primaryStage);
        
        removeItemDialog = AppRemoveNodeDialogSingleton.getSingleton();
        removeItemDialog.init(primaryStage);
        
        listStationsDialog = AppListStationsDialogSingleton.getSingleton();
        listStationsDialog.init(primaryStage);
        
        welcomeDialog = AppWelcomeDialogSingleton.getSingleton();
        welcomeDialog.init(primaryStage, this);
        
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        selectLanguageDialog = AppSelectLanguageDialogSingleton.getSingleton();
        selectLanguageDialog.init(primaryStage);

        String selection = "";
        selectedEN = new File("EN");
        selectedITA = new File("ITA");

        if (!(selectedEN.exists()) && !(selectedITA.exists())) {
            // ASK THE USER TO SELECT A LANGUAGE FIRST
            selectLanguageDialog.showAndWait();

            // AND NOW GET THE USER'S SELECTION
            selection = selectLanguageDialog.getSelection();
            

        }
        
        

        try {
            // LOAD APP PROPERTIES, BOTH THE BASIC UI STUFF FOR THE FRAMEWORK
            // AND THE CUSTOM UI STUFF FOR THE WORKSPACE
            boolean success = false;
            if (selection.equals("English") || selectedEN.exists()) {
                selectedEN.createNewFile();
                success = loadProperties(APP_PROPERTIES_FILE_NAME);
                
            } else if (selection.equals("Italian") || selectedITA.exists()) {
                selectedITA.createNewFile();
                success = loadProperties(APP_PROPERTIES_FILE_NAME_ITA);
            }

            if (success) {
                // GET THE TITLE FROM THE XML FILE
                String appTitle = props.getProperty(APP_TITLE);

                // BUILD THE BASIC APP GUI OBJECT FIRST
                gui = new AppGUI(primaryStage, appTitle, this);

                // THIS BUILDS ALL OF THE COMPONENTS, NOTE THAT
                // IT WOULD BE DEFINED IN AN APPLICATION-SPECIFIC
                // CHILD CLASS
                buildAppComponentsHook();
                
                welcomeDialog.showAndWait();
                
                gui.setNameOfMap(welcomeDialog.getName());

                // NOW OPEN UP THE WINDOW
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
        }
    }

    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @param propertiesFileName The XML file containing properties to be loaded
     * in order to initialize the UI.
     *
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties(String propertiesFileName) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
        } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
            return false;
        }
    }
}
