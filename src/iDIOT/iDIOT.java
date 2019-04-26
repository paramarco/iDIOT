package iDIOT;

import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;


/**
 * @author mvereda
 *
 */

public class iDIOT {
	

    static
    {
        System.setProperty("java.net.useSystemProxies", "true");
        if (Configuration.isMacOS())
        {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "World Wind Application");
            System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
            System.setProperty("apple.awt.brushMetalLook", "true");
        }
        else if (Configuration.isWindowsOS())
        {
            System.setProperty("sun.awt.noerasebackground", "true"); // prevents flashing during window resizing
        }
    }



    public static AppFrame start(String appName, Class<AppFrame> appFrameClass)
    {
        if (Configuration.isMacOS() && appName != null)
        {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
        }

        try
        {
            final AppFrame frame = (AppFrame) appFrameClass.newInstance();
            frame.setTitle(appName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    frame.setVisible(true);
                }
            });

            return frame;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    protected static void loadInitialWWConfig() {
        /*double VIEW_LATITUDE = 48.90;
        double VIEW_LONGITUDE = 11.15;
        double VIEW_ALTITUDE = 25e4;
        double VIEW_PITCH  = 70;
        */
    	
    	double VIEW_LATITUDE = 48.90;
        double VIEW_LONGITUDE = 11.15;
        double VIEW_ALTITUDE = 70e4;
        double VIEW_PITCH  = 0;
        
        Configuration.setValue(AVKey.INITIAL_LATITUDE, VIEW_LATITUDE);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, VIEW_LONGITUDE);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, VIEW_ALTITUDE );
        Configuration.setValue(AVKey.INITIAL_PITCH, VIEW_PITCH);
    }
    
    static Logger logger;
	
    private static void openFileDialog ( App myApp ) {
    	
    	JFileChooser fc;
    	fc = new JFileChooser();        
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);     
              
    	int returnVal = fc.showOpenDialog(myApp.myFrame.controlPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (file != null) {
				myApp.targetPath = file.getAbsolutePath();
				myApp.start();
	       }
		}  
    }
    
    private static void loadLogger() {
    	System.setProperty("log4j.configurationFile", "./lib/log4j2.properties");
    	logger = LogManager.getLogger(iDIOT.class);
    	logger.info("launching application  ...");
    }

    public static void main(String[] args)
    {    	
    	// Logger's settings
    	loadLogger();
		// set-up the viewer's perspective
    	loadInitialWWConfig();
    		
        // Call the static start method like this from the main method of your derived class.
    	AppFrame myFrame = start("iDIOT - iTEC Diagnostic internal odd Tool", AppFrame.class);  
    	
    	// start-up the Application objects
    	App myApp = new App(myFrame);
    	
    	if (args.length > 0) {
    		myApp.targetPath = args[0];
    		myApp.start();
    		
    	}else {
    		openFileDialog ( myApp );
    	}

    }
}