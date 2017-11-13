/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;


/**
 *
 * @author naimyoussiftraore
 */
public class AppSelectLanguageDialogSingleton extends Stage{
     // HERE'S THE SINGLETON
    static AppSelectLanguageDialogSingleton singleton;
    
     // GUI CONTROLS FOR OUR DIALOG
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button englishButton;
    Button italianButton;
    String selection;
    
    // CONSTANT CHOICES

    public static final String ENGLISH = "English";
    public static final String ITALIAN = "Italian";
    
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     */
    private AppSelectLanguageDialogSingleton() {}
    
     /**
     * The static accessor method for this singleton.
     * 
     * @return The singleton object for this type.
     */
    public static AppSelectLanguageDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppSelectLanguageDialogSingleton();
	return singleton;
    }
    
     /**
     * This method initializes the singleton for use.
     * 
     * @param primaryStage The window above which this
     * dialog will be centered.
     */
    public void init(Stage primaryStage) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        //
        setTitle("Select Language");
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label("Please select a language:");        

        // ENGLISH, ITALIAN BUTTONS
        englishButton = new Button(ENGLISH);
        italianButton = new Button(ITALIAN);
        
        // MAKE THE EVENT HANDLER FOR THESE BUTTONS
        
          EventHandler<ActionEvent> englishItalianHandler = (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            AppSelectLanguageDialogSingleton.this.selection = sourceButton.getText();
           AppSelectLanguageDialogSingleton.this.close();
        };
          
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        englishButton.setOnAction(englishItalianHandler);
        italianButton.setOnAction(englishItalianHandler);
        
         // NOW ORGANIZE OUR BUTTONS
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(englishButton);
        buttonBox.getChildren().add(italianButton);
        
        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(buttonBox);
        
         // MAKE IT LOOK NICE
        messagePane.setPadding(new Insets(10, 20, 20, 20));
        messagePane.setSpacing(10);
        
         // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
        
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either ENGLISH or ITALIAN, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }
    
     
}
