/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author naimyoussiftraore
 */
public class AppListStationsDialogSingleton extends Stage {
    
    // HERE'S THE SINGLETON
    static AppListStationsDialogSingleton singleton;
    
    // GUI CONTROLS FOR OUR DIALOG
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    Button closeButton;
    
    // CONSTANT CHOICES

    public static final String CLOSE = "CLOSE";
    
    
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private AppListStationsDialogSingleton() {}
    
    /**
     * The static accessor method for this singleton.
     * 
     * @return The singleton object for this type.
     */
    public static AppListStationsDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppListStationsDialogSingleton();
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
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label();        

        // YES, NO, AND CANCEL BUTTONS
        closeButton = new Button(CLOSE);
	
	// MAKE THE EVENT HANDLER FOR THESE BUTTONS
        EventHandler<ActionEvent> closeHandler = (ActionEvent ae) -> {
            
            
            AppListStationsDialogSingleton.this.close();
        };
        
	// AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        closeButton.setOnAction(closeHandler);
      

        // NOW ORGANIZE OUR BUTTONS
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(closeButton);
        
        
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
     * This method loads a custom message into the label
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window bar.
     * 
     * @param message Message to appear inside the dialog.
     */
    public void show(String title, String message) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle(title);
	
	// SET THE MESSAGE TO DISPLAY TO THE USER
        messageLabel.setText(message);
	
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
