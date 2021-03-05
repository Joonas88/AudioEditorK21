package otp.group6.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * 
 * @author Joonas Soininen
 *
 */
public class SaveSelectionController {
	Controller controller;
	MainController mc;	
	
	@FXML
	private Button closeButton;
	/**
	 * Method to get mainController
	 * @param mainController
	 */
	public void setMainController (MainController mainController) {
		this.mc=mainController;
		this.controller=mc.getController();
	}
	
	/**
	 * Method to close opened scenes
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Does a query to the database class to check for a logged in user
	 */
	public void checkForloggedin() {
		controller.intializeDatabaseConnection();
		if (!(controller.loggedIn()==" ")) {
			mc.openMixerSave();
		    Stage stage = (Stage) closeButton.getScene().getWindow();
		    stage.close();
		} else {
			mc.openLoginRegister();
		    Stage stage = (Stage) closeButton.getScene().getWindow();
		    stage.close();
		}
	}
	
	/**
	 * Method for storing settings on users computer
	 */
	public void saveLocal() {
		mc.soundManipulatorSaveMixerSettings();
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	
}
