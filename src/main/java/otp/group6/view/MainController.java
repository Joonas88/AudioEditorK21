package otp.group6.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.sun.glass.ui.MenuItem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.AudioEditor.Car;
import otp.group6.AudioEditor.Soundboard.Sample;
import otp.group6.controller.*;
import otp.group6.prototypes.testFileOpener;
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
	/**
	 * FXML element variables
	 */
	@FXML
	AnchorPane mainContainer,soundboardContainer;
	@FXML
	Button newSoundButton;
	@FXML
	GridPane buttonGrid;
	
	/**
	 * TODO REGEX tarkistus tiedostonimille
	 */
	@FXML
	public void recordAudio() {
		//controller.recordAudio(tf.getText());
	}
	
	@FXML
	public void stopRecord() {
		controller.stopRecord();
	}
	@FXML
	public void openFile() {
		try {
		File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
		System.out.println(file);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void openSample() {
		try {
		File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
		controller.addSample(file.getAbsolutePath());
		addButton(controller.getSampleArrayLength() - 1);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Button addButton(int index) {
		AnchorPane ap = (AnchorPane) newSoundButton.getParent();
		Button temp = (Button) ap.getChildren().remove(0);
		Button button = new Button();
		button.layoutXProperty().set(65);
		button.layoutYProperty().set(45);
		button.setText("Play");
		button.setOnAction(new EventHandler() {
			@Override
			public void handle(Event event) {
				controller.playSound(index);
			}
		});
		ap.getChildren().add(button);
		setButtonDescription(ap);
		
		if(buttonGrid.getChildren().indexOf(ap) < buttonGrid.getChildren().size() - 1) {
		ap = (AnchorPane) buttonGrid.getChildren().get(buttonGrid.getChildren().indexOf(ap) + 1);
		ap.getChildren().add(temp);
		return temp;
		} else {
			return null;
		}
	}
	public void setButtonDescription(AnchorPane ap) {
		Text text = new Text();
		text.setText("Insert name");
		text.layoutXProperty().set(65);
		text.layoutYProperty().set(30);
		text.setTextAlignment(TextAlignment.CENTER);
		ap.getChildren().add(text);
	}
	
	/**
	 * Joonaksen tekemiä lisäyksiä
	 */
	
	@FXML
	private Slider slider1;
	@FXML
	private Slider slider2;
	@FXML
	private Slider slider3;
	@FXML
	private Slider slider4;
	@FXML
	private Slider slider5;
	@FXML
	private Slider slider6;
	
	/**
	 * Method opens a new scene Login and Register form
	 */
	public void openLoginRegister() {
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\RegisterLoginView.fxml"));
		    Parent root1 = (Parent) fxmlLoader.load();
		    Stage stage = new Stage();
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.initStyle(StageStyle.UNDECORATED);
		    stage.setTitle("Login or Register");
		    stage.setScene(new Scene(root1));  
		    stage.show();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method opens a new scene, the mixer settings from the database
	 */
	public void openMixerSettings() {
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\MixerSettingsView.fxml"));
		    Parent root1 = (Parent) fxmlLoader.load();
		    Stage stage = new Stage();
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.initStyle(StageStyle.UNDECORATED);
		    stage.setTitle("Mixer Settings Loader");
		    stage.setScene(new Scene(root1));  
		    stage.show();	   

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open a new scene where the mixer settings can be saved to the database
	 */
	public void openMixerSave() {
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\SaveMixerSettings.fxml"));
		    Parent root1 = (Parent) fxmlLoader.load();
		    
		    SaveMixerSettingsController smsc = fxmlLoader.getController();
		    smsc.getSettings(slider1.getValue(),slider2.getValue(),slider3.getValue(),slider4.getValue(),slider5.getValue(),slider6.getValue());
		    
		    Stage stage = new Stage();
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.initStyle(StageStyle.UNDECORATED);
		    stage.setTitle("Save Mixer Settings");
		    stage.setScene(new Scene(root1));  
		    stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Does a query to the database class to check for a logged in user
	 */
	public void checkForloggedin() {
		if (!(controller.loggedIn()==" ")) {
			System.out.println(controller.loggedIn()); //Poistettava
			//TODO metodi avata suoraan oikea valintaikkuna!! Ehkä tämä on valmis :thinking:
			openMixerSave();
		} else {
			openLoginRegister();
		}
	}
	
	@FXML
	private Button closeButton;
	
	/**
	 * Method to close open scenes
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	//TODO tarvitaan arvojen asettamiselle tapa!
	/**
	 * Method to set the slider values from the database stored settings
	 * @param set1
	 * @param set2
	 * @param set3
	 * @param set4
	 * @param set5
	 * @param set6
	 */
	public void setSliderValues(double set1, double set2, double set3, double set4, double set5, double set6) {
	
	}
	
}
