package org.jacp.demo.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacp.javafx.rcp.util.FXUtil;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;

public class FXMLAdoptionForm extends Application{
	public static void main(String[] args) {
		launch(args);
		}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//stage.setTitle("FXML GridPane Demo");
		//Parent root = FXMLLoader.load(getClass().getResource("AdoptionForm.fxml"));
/*		AdoptionFormController controller = new AdoptionFormController();
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AdoptionForm.fxml"));
		FXUtil.setPrivateMemberValue(FXMLLoader.class, fxmlLoader, "controller", controller);
		fxmlLoader.setClassLoader(controller.getClass().getClassLoader());
//		fxmlLoader.setBuilderFactory(new BuilderFactory() {
//			
//			@Override
//			public Builder<?> getBuilder(Class<?> arg0) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
 
            @Override
            public Object call(Class<?> paramClass) {
                // Never reached <<<<<<<<<<<<<<<<<<<<<<< 
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Using provided controller");
               FXMLLoader tmp = fxmlLoader;
                return null;
            }
        });
      
        Parent root = (Parent) fxmlLoader.load();*/
		 MenuBar bar = new javafx.scene.control.MenuBar();
	        bar.setUseSystemMenuBar(true);
	        final Menu menu1 = new Menu("File");
	        final Menu menu2 = new Menu("Options");
	        final Menu menu3 = new Menu("Help");
	        bar.getMenus().addAll(menu1, menu2, menu3);
		
		 primaryStage.setTitle("Hello World!");
	        Button btn = new Button();
	        btn.setText("Say 'Hello World'");
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Hello World!");
	            }
	        });
	        
	        StackPane root = new StackPane();
	        root.getChildren().addAll(btn,bar);
	        primaryStage.setScene(new Scene(root, 300, 250));
	        primaryStage.show();

	}

}
