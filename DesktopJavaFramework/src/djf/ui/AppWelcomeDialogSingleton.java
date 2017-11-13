/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import djf.AppTemplate;
import djf.controller.AppFileController;
import static djf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;
import static djf.ui.AppMessageDialogSingleton.singleton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author naimyoussiftraore
 */
public class AppWelcomeDialogSingleton extends Stage{
    // HERE'S THE SINGLETON OBJECT
    static AppWelcomeDialogSingleton singleton = null;
    
    BorderPane pane;
    VBox recentWorkList;
    BorderPane newWorkPane;
    Scene welcomeScene;
    Text logoLabel;
    Text recentWorkLabel;
    Button newWork;
    AppFileController fileController;
    
    BorderPane bp1;
    BorderPane bp2;
    
    private AppWelcomeDialogSingleton() {
    }
    
     /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static AppWelcomeDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new AppWelcomeDialogSingleton();
	return singleton;
    }
    
     /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner, AppTemplate app) {
        // MAKE IT MODAL
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        fileController = new AppFileController(app);
       
        
        this.setTitle("Welcome To The Metro Map Maker");
        
        bp1 = new BorderPane();
        bp1.setPadding(new Insets(50, 50, 50, 50));
        bp2 = new BorderPane();
        bp2.setPadding(new Insets(50, 50, 50, 50));
        
        pane = new BorderPane();
        newWorkPane = new BorderPane();
        newWorkPane.setPrefWidth(400.00);
        newWorkPane.setPadding(new Insets(50, 50, 50, 50));
        newWorkPane.setStyle("-fx-background-color: #FFFFFF;");
        
        
        recentWorkList = new VBox();
        recentWorkLabel = new Text("Recent Work");
        recentWorkLabel.setFont(Font.font("plain", FontWeight.BOLD, 20));
        
        
        
        pane.setPrefHeight(500.00);
        pane.setPrefWidth(600.00);
        
        logoLabel = new Text("METRO MAP MAKER");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 50));
        
        
        recentWorkList = new VBox();
        recentWorkList.setPrefWidth(200.00);
        recentWorkList.setPadding(new Insets(20, 20, 20, 20));
        recentWorkList.setStyle("-fx-background-color: #FFFFCC;");
        
        newWork = new Button();
        newWork.setText("Create New Metro Map");
        
        bp1.setCenter(logoLabel);
        bp2.setCenter(newWork);
        
        
         newWork.setOnAction(e -> {
            fileController.handleNewRequest();
            AppWelcomeDialogSingleton.this.close();
        });
        
        recentWorkList.getChildren().add(recentWorkLabel);
        
        newWorkPane.setTop(bp1);
        newWorkPane.setBottom(bp2);
        
        
        pane.setLeft(recentWorkList);
        pane.setRight(newWorkPane);
        
        
        welcomeScene = new Scene(pane);
        this.setScene(welcomeScene);
        
        
        
        
    }
}
