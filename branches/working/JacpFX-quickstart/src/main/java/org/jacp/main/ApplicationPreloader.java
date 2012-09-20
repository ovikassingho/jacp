package org.jacp.main;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**
 * The application Preloader. It is just a simple splash screen on application init. The preloader shows up only when execute the package.
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 *
 */
public class ApplicationPreloader extends Preloader {
    private ProgressBar bar;
    private Stage stage; 
   
    
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());        
        stage.show();
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }
 
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }    
    
    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        final BorderPane p = new BorderPane();
        p.setCenter(bar);
        return new Scene(p, 640, 480);        
    }
}