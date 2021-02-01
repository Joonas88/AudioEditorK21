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
	 * Adds a new button to the soundboard.
	 * TODO lisää toiminto jolla pitää kirjaa nappien indexeistä.
	 * TODO Sijoittelu toiminnallisuus
	 * TODO Siisti koodia
	 */
	@FXML
	public void addButton() {
		if(button_count + 1 <= controller.getSampleAmount()) {
			Button button = new Button();
			int i = button_count;
			button.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					controller.playSound(i);
				}
			});
			button.setText(Integer.toString(button_count + 1));
			button.layoutXProperty().set(100 * (button_count + 1));
			button.layoutYProperty().set(150);
			try {
				ap.getChildren().add(button);
				button_count++;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
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
