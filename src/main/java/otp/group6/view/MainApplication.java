package otp.group6.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import otp.group6.*;
/**
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainApplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("AudioEditor");
		primaryStage.show();
	}
	//TODO Täytä metodit, tyhjennä "start"
	public void intializeRootLayout() {
		
	}
	public void intitalizeMainLayout() {
		
	}
}
