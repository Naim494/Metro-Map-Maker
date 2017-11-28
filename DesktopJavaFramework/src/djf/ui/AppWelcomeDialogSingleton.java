/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import djf.AppTemplate;
import djf.controller.AppFileController;
import static djf.settings.AppPropertyType.NEW_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.NEW_ERROR_TITLE;
import static djf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;
import static djf.ui.AppMessageDialogSingleton.singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author naimyoussiftraore
 */
public class AppWelcomeDialogSingleton extends Stage {

    // HERE'S THE SINGLETON OBJECT
    static AppWelcomeDialogSingleton singleton = null;

    BorderPane pane;
    VBox recentWorkList;
    BorderPane newWorkPane;
    Scene welcomeScene;
    Text logoLabel;
    ImageView logo;
    Image image;
    Text recentWorkLabel;
    Button newWork;
    AppFileController fileController;

    BorderPane bp1;
    BorderPane bp2;
    String name = "";

    private AppWelcomeDialogSingleton() {
    }

    /**
     * A static accessor method for getting the singleton object.
     *
     * @return The one singleton dialog of this object type.
     */
    public static AppWelcomeDialogSingleton getSingleton() {
        if (singleton == null) {
            singleton = new AppWelcomeDialogSingleton();
        }
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

        File img = new File("/Users/naimyoussiftraore/NetBeansProjects/METRO MAP MAKER/mmmLogo.png");
        image = new Image(img.toURI().toString());
        logo = new ImageView();
        logo.setImage(image);

        pane = new BorderPane();
        newWorkPane = new BorderPane();
        newWorkPane.setPrefWidth(400.00);
        newWorkPane.setPadding(new Insets(50, 50, 50, 50));
        newWorkPane.setStyle("-fx-background-color: #FFFFFF;");

        recentWorkList = new VBox();
        recentWorkList.setSpacing(20);
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

        bp1.setCenter(logo);
        bp2.setCenter(newWork);
        
        File recentList = new File("recentList.txt");
        

        newWork.setOnAction(e -> {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            AppTextInputDialogSingleton textDialog = AppTextInputDialogSingleton.getSingleton();
            textDialog.isCancelled = false;

            //String name = "";
            boolean done = false;
            

            try {
                done = false;

                textDialog.show("Name", "Enter a name for the map:");
                File file;

                while (!(done) && !(textDialog.isCanceled())) {

                    name = textDialog.getText();

                    file = new File("export/" + name + "/" + name);

                    if (!(file.exists())) {

                        file.getParentFile().mkdir();
                        file.createNewFile();

                        done = true;

                       
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
            AppWelcomeDialogSingleton.this.close();
        });
        

        recentWorkList.getChildren().add(recentWorkLabel);
        
        
        ArrayList<String> maps = getRecentMaps(recentList);
        
        if(maps != null) {
            for(String map: maps) {
                Button button = new Button();
                button.setText(map);
                recentWorkList.getChildren().add(button);
            }
        }

        newWorkPane.setTop(bp1);
        newWorkPane.setBottom(bp2);

        pane.setLeft(recentWorkList);
        pane.setRight(newWorkPane);

        welcomeScene = new Scene(pane);
        this.setScene(welcomeScene);

    }
    
    public ArrayList<String> getRecentMaps(File list) {
        
        ArrayList<String> maps = new ArrayList<String>();
        
        try (Scanner in = new Scanner(list)) {

            while (in.hasNextLine()) {

                maps.add(in.nextLine());   

            }
        }catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND");

        } 

        return maps;
    
    }
    
    public String getName() {
        return name;
    }
}
