/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;

import djf.AppTemplate;
import djf.components.AppDataComponent;

/**
 *
 * @author naimyoussiftraore
 */
public class mmmData implements AppDataComponent{
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    public mmmData(AppTemplate initApp) {
	// KEEP THE APP FOR LATER
	app = initApp;

	
    }

    @Override
    public void resetData() {
        
    }
    
}
