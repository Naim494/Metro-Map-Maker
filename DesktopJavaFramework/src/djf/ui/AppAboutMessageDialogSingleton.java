/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import static djf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static djf.ui.AppMessageDialogSingleton.singleton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author naimyoussiftraore
 */
public class AppAboutMessageDialogSingleton extends Stage {

    // HERE'S THE SINGLETON OBJECT
    static AppAboutMessageDialogSingleton singleton = null;

    // HERE ARE THE DIALOG COMPONENTS
    VBox messagePane;
    Scene messageScene;
    Text message;
    Image logo;
    ImageView image;

    /**
     * Initializes this dialog so that it can be used repeatedly for all kinds
     * of messages. Note this is a singleton design pattern so the constructor
     * is private.
     *
     * @param owner The owner stage of this modal dialoge.
     *
     * @param closeButtonText Text to appear on the close button.
     */
    private AppAboutMessageDialogSingleton() {
    }

    /**
     * A static accessor method for getting the singleton object.
     *
     * @return The one singleton dialog of this object type.
     */
    public static AppAboutMessageDialogSingleton getSingleton() {
        if (singleton == null) {
            singleton = new AppAboutMessageDialogSingleton();
        }
        return singleton;
    }

    /**
     * This function fully initializes the singleton dialog for use.
     *
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        // MAKE IT MODAL
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        this.setWidth(250);
        this.setHeight(250);
        
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + "Logo.png";
        Image logoImage = new Image(imagePath);

        image = new ImageView(logoImage);
        image.setFitWidth(70);
        image.setFitHeight(70);

        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        message = new Text("METRO MAP MAKER\nVersion 1.0\nRichard McKenna & Naim Youssif\n2017");
        message.setTextAlignment(TextAlignment.CENTER);

        // WE'LL PUT EVERYTHING HERE
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(image);
        messagePane.getChildren().add(message);

        // MAKE IT LOOK NICE
        messagePane.setPadding(new Insets(80, 60, 80, 60));
        messagePane.setSpacing(20);

        // AND PUT IT IN THE WINDOW
        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
    }

}
