/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import static djf.ui.AppSelectLanguageDialogSingleton.ENGLISH;
import static djf.ui.AppSelectLanguageDialogSingleton.ITALIAN;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author naimyoussiftraore
 */
public class AppTextInputDialogSingleton extends Stage{
    
    static AppTextInputDialogSingleton singleton;
    
    VBox pane;
    Scene messageScene;
    TextField textField = new TextField();
    Label messageLabel;
    Button done;
    Button cancel;
    String text = "";
    Boolean isCancelled = false;
    
    public static final String DONE = "Done";
    public static final String CANCEL = "Cancel";
    
     private AppTextInputDialogSingleton() {}
     
     public static AppTextInputDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppTextInputDialogSingleton();
	return singleton;
    }
    
      public void init(Stage primaryStage) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        //
        //setTitle("Enter Text");
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        messageLabel = new Label();        

        // ENGLISH, ITALIAN BUTTONS
        done = new Button(DONE);
        cancel = new Button(CANCEL);
        
        // MAKE THE EVENT HANDLER FOR THESE BUTTONS
        
          EventHandler<ActionEvent> whenDoneHandler = (ActionEvent ae) -> {
            //Button sourceButton = (Button)ae.getSource();
            AppTextInputDialogSingleton.this.text = textField.getText();
           AppTextInputDialogSingleton.this.close();
        };
          
           EventHandler<ActionEvent> whenCancelHandler = (ActionEvent ae) -> {
           // Button sourceButton = (Button)ae.getSource();
           
           AppTextInputDialogSingleton.this.close();
           isCancelled = true;
        };
          
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        done.setOnAction(whenDoneHandler);
        cancel.setOnAction(whenCancelHandler);
        
         // NOW ORGANIZE OUR BUTTONS
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(done);
        buttonBox.getChildren().add(cancel);
        
        // WE'LL PUT EVERYTHING HERE
        pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(messageLabel);
        pane.getChildren().add(textField);
        pane.getChildren().add(buttonBox);
        
         // MAKE IT LOOK NICE
        pane.setPadding(new Insets(10, 20, 20, 20));
        pane.setSpacing(10);
        
         // AND PUT IT IN THE WINDOW
        messageScene = new Scene(pane);
        this.setScene(messageScene);
        
    }
    
    public String getText() {
        return text;
    }
    
    public boolean isCanceled() {
       return isCancelled; 
    }
    
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
