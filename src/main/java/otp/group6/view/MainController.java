package otp.group6.view;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;
import java.text.DecimalFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.AudioEditor.Soundboard.Sample;
import otp.group6.controller.*;
import otp.group6.prototypes.testFileOpener;

/**
 * Main controller for the view
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController {
	Controller controller;

	// Dev test code. REMOVE FROM FINAL
	int button_count;

	public MainController() {
		// Dev test code. REMOVE FROM FINAL
		button_count = 0;

		controller = new Controller();
		
		
		///ROOSAN TESTI
		
		//alustaMikseri();
		///
		
		
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

	
	
	//ROOSAN TESTIT////////////////////////////////////////////////////
	//MIXER
	@FXML
	private Slider sliderPitch;
	@FXML
	private Slider sliderEchoLength;
	@FXML
	private Slider sliderDecay;
	@FXML
	private Slider sliderGain;
	@FXML
	private Slider sliderFlangerLength;
	@FXML
	private Slider sliderWetness;
	
	@FXML
	private Text textCurrentPitch;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); //kaikki luvut kahden desimaalin tarkkuuteen
	
	public void alustaMikseri() {
		//Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("pitch " + newValue);
				controller.soundManipulatorAdjustParameters(newValue.floatValue(), -1, -1, -1);
				
				textCurrentPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//Echo length slider
		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("echo length " + newValue);
				controller.soundManipulatorAdjustParameters(-1, newValue.doubleValue(), -1, -1);
			}
		});
		
		// Echo decay slider
		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				System.out.println("decay " + newValue);
				controller.soundManipulatorAdjustParameters(-1, -1, newValue.doubleValue(), -1);
			}
		});
		
		//Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				controller.soundManipulatorAdjustParameters(-1, -1, -1, newValue.doubleValue());
				
			}
		});
	}

	
	@FXML
	public void toistaAani() {
		controller.soundManipulatorPlayAudio();
	}
	
	@FXML
	public void saveMixedFile() {
		controller.soundManipulatorSaveFile();
	}
	
	
		///////////////////////////////////////////////////////////

	/**
	 * TODO REGEX tarkistus tiedostonimille
	 */
	@FXML
	public void recordAudio() {
		// controller.recordAudio(tf.getText());
	}

	@FXML
	public void stopRecord() {
		controller.stopRecord();
	}



	@FXML
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

	@FXML
	public void openSample() {
		//REGEX pattern for ".wav"
		Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());

			
			// File must be not null to add button
			if (file != null) {
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					controller.addSample(file.getAbsolutePath());
					addButton(controller.getSampleArrayLength() - 1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addButton(int index) {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApplication.class.getResource("SoundBoardButton.fxml"));
		
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
			configureSoundButton( (AnchorPane) soundButtonRoot,index);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	public void configureSoundButton (AnchorPane ap, int index) {
		Button play = (Button) ap.getChildren().get(0);
		Text description = (Text) ap.getChildren().get(1);
		MenuButton mp = (MenuButton) ap.getChildren().get(2);
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.playSound(index);
			}
		});
		description.setText(controller.getSampleName(index));
		description.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				renameButton(description,ap,index);
			}
			
		});
		
		MenuItem editButton = mp.getItems().get(0);
		editButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFile(index);
			}
			
		});
		
		MenuItem deleteButton = mp.getItems().get(1);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.removeSample(index);
				refreshButtons();
				removeLast();
			}
			
		});
		
		//add edit and delete functionality
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void renameButton(Text text, AnchorPane ap, int index) {
		
		TextField tf = new TextField();
		tf.layoutXProperty().set(text.getLayoutX());
		tf.layoutYProperty().set(text.getLayoutY());
		tf.setText(text.getText());
		tf.forward();
		
		ChangeListener cl = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(!tf.isFocused()) {
					text.setText(tf.getText());
					controller.setSampleName(index, tf.getText());
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
		};
		tf.focusedProperty().addListener(cl);
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER) {
					text.setText(tf.getText());
					controller.setSampleName(index, tf.getText());
					tf.focusedProperty().removeListener(cl);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
			
		});
		ap.getChildren().add(tf);
	}
	public void refreshButtons() {
		int length = controller.getSampleArrayLength();
		for(int i = 0; i < length; i++) {
			AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(i);
			AnchorPane root = (AnchorPane) gridRoot.getChildren().get(0);
			Text description = (Text) root.getChildren().get(1);
			description.setText(controller.getSampleName(i));
		}
	}
	public void removeLast() {
		int length = controller.getSampleArrayLength();
		AnchorPane gridRoot = (AnchorPane) buttonGrid.getChildren().get(length);
		System.out.println(buttonGrid.getChildren().size());
		if(length + 1 < buttonGrid.getChildren().size()) {
			AnchorPane newSoundRoot = (AnchorPane) buttonGrid.getChildren().get(length +1);
			Button temp = (Button) newSoundRoot.getChildren().remove(0);
			
			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(temp);
		}
		else {
			gridRoot.getChildren().remove(0);
			gridRoot.getChildren().add(newSoundButton);
		}
	}

}
