package otp.group6.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import otp.group6.controller.*;
/**
 * Main controller for the view
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController {
	Controller controller;
	
	//Dev test code. REMOVE FROM FINAL
	int button_count;
	
	public MainController() {
		//Dev test code. REMOVE FROM FINAL
		button_count = 0;
		
		controller = new Controller();
	}
	
	@FXML
	Button btn;
	
	@FXML
	Text text;
	
	@FXML
	AnchorPane ap;
	
	@FXML
	TextField tf;
	
	/**
	 * TODO REGEX tarkistus tiedostonimille
	 */
	@FXML
	public void recordAudio() {
		controller.recordAudio(tf.getText());
	}
	
	@FXML
	public void stopRecord() {
		controller.stopRecord();
	}
}
