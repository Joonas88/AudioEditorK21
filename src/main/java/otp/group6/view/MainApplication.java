package otp.group6.view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import otp.group6.*;

/**
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainApplication extends Application {

	
	private Stage primaryStage;
	private AnchorPane rootLayout;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AudioEditor");
		
		initializeRootLayout();
		
		
		/*
		//ALKUP
		Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.show();
		//
		 * */
		 
		
		
		
	}
<<<<<<< HEAD

	// TODO Täytä metodit, tyhjennä "start"
	public void intializeRootLayout() {

	}

	public void intitalizeMainLayout() {

=======
	/**
     * Initializes the root layout.
     */
	//TODO Täytä metodit, tyhjennä "start"
	public void initializeRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			MainController mainController = loader.getController();
			System.out.println("LOOOOOL");
			mainController.alustaMikseri();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void initializeMainLayout() {
		
>>>>>>> 6106280e4a73ef9aa87d06b0bed487d2817b544d
	}
}
