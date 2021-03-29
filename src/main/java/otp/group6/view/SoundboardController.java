package otp.group6.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.controller.Controller;

public class SoundboardController implements Initializable {
	private Controller controller;
	
	/**
	 * If main controller is given, creates a new controller inside object
	 * otherwise requires controller
	 * @param mainController
	 */
	public SoundboardController() {
		controller = new Controller();
	}
	
	public SoundboardController(Controller controller) {
		this.controller = controller;
	}
	public static Button lastButton,currentButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		controller.readSampleData();
		checkSavedSamples();
		soundboardInit();
		applyBackgroundColor();
	}
	
	
	/**
	 * FXML element variables
	 */
	@FXML
	AnchorPane mainContainer, soundboardContainer;
	@FXML
	Button newSoundButton;
	@FXML
	GridPane buttonGrid;
	@FXML
	Button clearButton;
	
	
	/**
	 * Used to edit existing sample in the sample array Opens File explorer and
	 * edits sample with given index to contain selected wav file Checks file
	 * validity with REGEX
	 * 
	 * @param index
	 */
	public void openFile(int index) {
		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());

			// File must be not null to add button
			if (file != null) {
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					controller.editSample(file.getAbsolutePath(), index);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens file explorer and adds selected wav file to sample array in soundboard
	 * Then adds a button to the soundboard Checks file validity with REGEX
	 */
	@FXML
	public void openSample() {
		// REGEX pattern for ".wav"
		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());

			// File must be not null to add button
			if (file != null) {
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					controller.addSample(file.getAbsolutePath());
					addButton(controller.getSampleArrayLength() - 1);
					applyBackgroundColor();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a button to soundboard
	 * 
	 * @param index - the index of current gridpane child
	 */
	public void addButton(int index) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApplication.class.getResource("/SoundBoardButton.fxml"));

		AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(index);
		Node soundButtonRoot;

		try {
			soundButtonRoot = (Node) loader.load();

			Button temp = (Button) gridRoot.getChildren().remove(0);

			if (index < buttonGrid.getChildren().size() - 1) {
				AnchorPane ap = (AnchorPane) buttonGrid.getChildren().get(index + 1);
				ap.getChildren().add(temp);
			}

			gridRoot.getChildren().add(soundButtonRoot);
			configureSoundButton((AnchorPane) soundButtonRoot, index);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Configures the button with given index
	 * 
	 * @param ap    AnchorPane -- root of the button element
	 * @param index -- index of the gridPane child
	 */
	public void configureSoundButton(AnchorPane ap, int index) {
		Button play = (Button) ap.getChildren().get(0);
		Text description = (Text) ap.getChildren().get(1);
		MenuButton mp = (MenuButton) ap.getChildren().get(2);

		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButtonFunctionality(play, index);
			}
		});
		description.setText(controller.getSampleName(index));
		description.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				renameButton(description, ap, index);
			}

		});
		MenuItem renameButton = (MenuItem) mp.getItems().get(0);
		renameButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				renameButton(description, ap, index);
			}
		});

		MenuItem editButton = (MenuItem) mp.getItems().get(1);
		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFile(index);
			}

		});

		MenuItem deleteButton = mp.getItems().get(2);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.removeSample(index);
				refreshButtons();
				removeLast();
			}

		});
	}
	/**
	 * Kooi atm al dente eli spagettia - TODO REWORK
	 * @param button
	 * @param index
	 */
	public void playButtonFunctionality(Button button, int index) {
		if (!controller.isPlaying() || lastButton != button) {
			currentButton = button;
			resetAllButtonNames();
			controller.playSound(index);
			button.setText("Stop");
		} else {
			resetAllButtonNames();
			controller.stopSound();
		}
		lastButton = button;
	}
	/**
	 * Adds a listener to reset all button names;
	 */
	public void soundboardInit() {
		controller.setListener(new LineListener() {

			@Override
			public void update(LineEvent event) {
				if(event.getType() == Type.STOP) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							resetAllButtonNames();
							if(controller.isPlaying()) {
								currentButton.setText("Stop");
							}
						}
						
					});
				}
			}
			
		});
	}
	/**
	 * ATM raviolia eli TODO REWORK
	 * jos se toimii nii se toimii - älä valita
	 */
	public void resetAllButtonNames() {
		ObservableList<Node> tempList =(ObservableList<Node>) buttonGrid.getChildren();
		ArrayList<AnchorPane> apList = new ArrayList<AnchorPane>();
		int index = controller.getSampleArrayLength();
		tempList.forEach(root -> {
			apList.add((AnchorPane) root);
		});
		for(int i = 0; i < index ;i++) {
			AnchorPane apRoot = apList.get(i);
			AnchorPane btnRoot = (AnchorPane) apRoot.getChildren().get(0);
			Button btnTemp = (Button) btnRoot.getChildren().get(0);
			btnTemp.setText("Play");
		}
	}
	/**
	 * Method for renaming soundboard buttons
	 * 
	 * @author Kevin Akkoyun
	 * @param text  -- text element of the button
	 * @param ap    -- parent of the button
	 * @param index -- index of the GridPane child
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void renameButton(Text text, AnchorPane ap, int index) {
		TextField tf = new TextField();
		tf.forward();
		ChangeListener cl = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {

				if (!tf.isFocused()) {
					String temp = tf.getText();
					if (temp.length() > 20) {
						temp = temp.substring(0, 20);
					}
					if (checkEmpty(temp)) {
						temp = text.getText();
					}
					text.setText(temp);
					controller.setSampleName(index, temp);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
		};
		tf.focusedProperty().addListener(cl);
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					String temp = tf.getText();
					if (temp.length() > 20) {
						temp = temp.substring(0, 20);
					}
					if (checkEmpty(temp)) {
						temp = text.getText();
					}
					text.setText(temp);
					controller.setSampleName(index, temp);
					tf.focusedProperty().removeListener(cl);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}

		});
		ap.getChildren().add(tf);
		tf.layoutXProperty().set(text.getLayoutX());
		tf.layoutYProperty().set(text.getLayoutY() - 20);
		tf.requestFocus();
	}

	/**
	 * Checks if string contains only whitespaces
	 * 
	 * @author Kevin Akkoyun
	 * @param input -- String to be checked
	 * @return returns true if string contains only whitespaces, otherwise returns
	 *         false
	 */
	public boolean checkEmpty(String input) {
		input = input.trim();
		return input.isEmpty();
	}

	/**
	 * Refreshes soundboard buttons and reassigns their names
	 * 
	 * @author Kevin Akkoyun
	 */
	public void refreshButtons() {
		applyBackgroundColor();
		int length = controller.getSampleArrayLength();
		for (int i = 0; i < length; i++) {
			AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(i);
			AnchorPane root = (AnchorPane) gridRoot.getChildren().get(0);
			Text description = (Text) root.getChildren().get(1);
			description.setText(controller.getSampleName(i));
		}
	}

	/**
	 * Removes last soundboard button
	 * 
	 * @author Kevin Akkoyun
	 */
	public void removeLast() {
		int length = controller.getSampleArrayLength();
		AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(length);
		if (length + 1 < buttonGrid.getChildren().size()) {
			AnchorPane newSoundRoot = (AnchorPane) buttonGrid.getChildren().get(length + 1);
			Button temp = (Button) newSoundRoot.getChildren().remove(0);

			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(temp);
		} else {
			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(newSoundButton);
		}
	}
	
	@FXML
	public void removeAllCheck() {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setContentText("Are you sure you want to clear everything?");

		ButtonType type = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType ntype = new ButtonType("No", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().add(type);
		alert.getButtonTypes().add(ntype);

		alert.setTitle("WARNING");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().getButtonData() == ButtonData.OK_DONE) {
			removeAll();
		}
	}

	public void removeAll() {
		try {
			ArrayList<AnchorPane> temp = new ArrayList<AnchorPane>();
			ObservableList<Node> gridList = buttonGrid.getChildren();
			gridList.forEach(root -> {
				temp.add((AnchorPane) root);
			});
			temp.forEach(root -> {
				if (!root.getChildren().isEmpty()) {
					root.getChildren().remove(0);
				}
			});
			AnchorPane firstGrid = (AnchorPane) buttonGrid.getChildren().get(0);
			firstGrid.getChildren().add(newSoundButton);
			controller.clearSampleData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**			
	 * @author Kevin Akkoyun
	 */
	public void checkSavedSamples() {
		int length = controller.getSampleArrayLength();
		if (length > 0) {
			for (int index = 0; index < length; index++) {
				addButton(index);
			}
		}
	}

	public void saveSamples() {
		controller.saveSampleData();
	}
	
	public void applyBackgroundColor(){
		int length = controller.getSampleArrayLength();
		for (int i = 0; i < length; i++) {
			AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(i);
			AnchorPane root = (AnchorPane) gridRoot.getChildren().get(0);
			if(i % 2 != 0) {
				root.setStyle("-fx-background-color: lightgray;");
			}
			else {
				root.setStyle("-fx-background-color: lightblue;");
			}
		}
	}

}
