package otp.group6.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.controller.Controller;

/**
 * 
 * @author Joonas Soininen
 *
 */
public class LoadSelectionController {
	Controller controller;
	MainController mc;
	
	@FXML
	AnchorPane mainContainer;
	
	@FXML
	private Button closeButton;
	
	private List<String> lines = new ArrayList<String>();
	
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
	
	public void loadFromCloud() {
		mc.openMixerSettings();
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Method to load mixer settings from local storage
	 */
	public void loadFromLocal() {
		//mc.loadLocalMixerSettings();
	    try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
	        Scanner myReader = new Scanner(file);
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          
	          lines.add(data);
	        }
	        myReader.close();
			mc.setSliderValues(
					Double.parseDouble(lines.get(0)),
					Double.parseDouble(lines.get(1)), 
					Double.parseDouble(lines.get(2)), 
					Double.parseDouble(lines.get(3)),
					Double.parseDouble(lines.get(4)),
					Double.parseDouble(lines.get(5)),
					Double.parseDouble(lines.get(6)),
					Float.parseFloat(lines.get(7)));
	      } catch (FileNotFoundException e) {
	    	  
	        e.printStackTrace();
	      }
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
}
