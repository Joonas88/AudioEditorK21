package otp.group6.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import otp.group6.controller.*;
/**
 * Main controller for the view
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController {
	Controller controller;
	public MainController() {
		controller = new Controller();
	}
	
	@FXML
	Button btn;
	
	@FXML
	Text text;
	
	@FXML
	public void doStuff() {
		System.out.print("testi");
		text.setText("testi");
	}
}
