/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm;

import java.util.Locale;
//import gol.data.golData;
//import gol.file.golFiles;
import mmm.gui.mmmWorkspace;
import djf.AppTemplate;
import static javafx.application.Application.launch;
import mmm.data.mmmData;


/**
 *
 * @author naimyoussiftraore
 */
public class metroMapMakerApp extends AppTemplate {
    
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL THREE COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, AND THE DATA COMPONENT NEEDS THE
        // FILE COMPONENT SO WE MUST BE CAREFUL OF THE ORDER
        //fileComponent = new golFiles();
        //dataComponent = new golData(this);
        workspaceComponent = new mmmWorkspace(this);
        dataComponent = new mmmData(this);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
	launch(args);
    }
    
}
