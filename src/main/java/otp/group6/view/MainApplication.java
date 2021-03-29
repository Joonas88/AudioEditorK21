package otp.group6.view;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import otp.group6.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import otp.group6.*;

/**
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainApplication extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private MainController mainController;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AudioEditor");

		initializeRootLayout();
		// Sulkee ohjelman, kun käyttäjä sulkee ikkunan
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				mainController.saveSamples();
				Platform.exit();
				System.exit(0);
			}
		});
	}

	/**
	 * Initializes the root layout.
	 */
	// TODO Täytä metodit, tyhjennä "start"
	public void initializeRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("/MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

			mainController = loader.getController();
			mainController.initializeMixer();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
