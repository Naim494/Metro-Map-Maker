/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

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
public class AppRemoveNodeDialogSingleton extends Stage {
    
      
   static AppRemoveNodeDialogSingleton singleton;
    
    VBox pane;
    Scene messageScene;
    TextField textField = new TextField();
    
    Label messageLabel;
    Button ok;
    Button cancel;
    String name = "";
    
    Boolean isCancelled = false;
    Boolean isOkayed = false;
    
    public static final String OK = "Ok";
    public static final String CANCEL = "Cancel";
    
     private AppRemoveNodeDialogSingleton() {}
     
     public static AppRemoveNodeDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppRemoveNodeDialogSingleton();
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
        messageLabel = new Label("Remove Item?");        

        // ENGLISH, ITALIAN BUTTONS
        ok = new Button(OK);
        cancel = new Button(CANCEL);
        
        // MAKE THE EVENT HANDLER FOR THESE BUTTONS
        
          EventHandler<ActionEvent> whenOkayedHandler = (ActionEvent ae) -> {
            //Button sourceButton = (Button)ae.getSource();
            //AppRemoveNodeDialogSingleton.this.name = textField.getText();
           
           AppRemoveNodeDialogSingleton.this.close();
           isOkayed = true;
        };
          
           EventHandler<ActionEvent> whenCancelHandler = (ActionEvent ae) -> {
           // Button sourceButton = (Button)ae.getSource();
           
           AppRemoveNodeDialogSingleton.this.close();
           isCancelled = true;
        };
          
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        ok.setOnAction(whenOkayedHandler);
        cancel.setOnAction(whenCancelHandler);
        
        setTitle("Name");
        
         // NOW ORGANIZE OUR BUTTONS
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(ok);
        buttonBox.getChildren().add(cancel);
        
        //HBox specBox = new HBox();
        //specBox.getChildren().add(textField);
        
        
        // WE'LL PUT EVERYTHING HERE
        pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(messageLabel);
        //pane.getChildren().add(specBox);
        pane.getChildren().add(buttonBox);
        
         // MAKE IT LOOK NICE
        pane.setPadding(new Insets(10, 20, 20, 20));
        pane.setSpacing(10);
        
         // AND PUT IT IN THE WINDOW
        messageScene = new Scene(pane);
        this.setScene(messageScene);
        
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isCanceled() {
       return isCancelled; 
    }
    
    public boolean isOkayed() {
        return isOkayed;
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
