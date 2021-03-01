package otp.group6.view;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.controller.*;

/**
 * Main controller for the view
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController {
	Controller controller;

	public MainController() {
		controller = new Controller();
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
	public void recordAudio() {
		// controller.recordAudio(tf.getText());
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
			controller.soundManipulatorOpenFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openSample() {
		try {
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			controller.addSample(file.getAbsolutePath());
			addButton(controller.getSampleArrayLength() - 1);
		} catch (Exception e) {
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

		if (buttonGrid.getChildren().indexOf(ap) < buttonGrid.getChildren().size() - 1) {
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

////// MIXER //////
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
	private Slider sliderLfoFrequency;
	@FXML
	private Slider sliderLowPass;
	@FXML
	private TextField textFieldPitch;
	@FXML
	private TextField textFieldGain;
	@FXML
	private TextField textFieldEchoLength;
	@FXML
	private TextField textFieldDecay;
	@FXML
	private TextField textFieldFlangerLength;
	@FXML
	private TextField textFieldWetness;
	@FXML
	private TextField textFieldLfo;
	@FXML
	private TextField textFieldLowPass;
	@FXML
	private Text textSelectedFile;
	@FXML
	private Text textAudioFileDuration;
	@FXML
	private GridPane paneMixerSliders;
	@FXML
	private AnchorPane paneMixerAudioPlayer;
	@FXML
	private Button buttonPlay;
	@FXML
	private Button buttonPause;
	@FXML
	private Button buttonStop;
	@FXML
	private Button buttonInfoPitch;
	@FXML
	private Button buttonInfoGain;
	@FXML
	private Button buttonInfoEchoLength;
	@FXML
	private Button buttonInfoDecay;
	@FXML
	private Button buttonInfoFlangerLength;
	@FXML
	private Button buttonInfoWetness;
	@FXML
	private Button buttonInfoLfo;
	@FXML
	private Button buttonInfoLowPass;

	
	//Muuttujat tiedoston kokonaiskestolle ja toistetulle ajalle
	private String audioFileDurationString;
	private String audioFileProcessedTimeString;
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // kaikki luvut kahden desimaalin tarkkuuteen

	
	
	public void initializeMixer() {
		initializeSlidersAndTextFields();
		setTooltips();
	}

	//Methods for buttons
	@FXML
	public void soundManipulatorPlayAudio() {
		controller.soundManipulatorPlayAudio();
		buttonPlay.setDisable(true);
		buttonPause.setDisable(false);
		buttonStop.setDisable(false);
	}

	@FXML
	public void soundManipulatorStopAudio() {
		controller.soundManipulatorStopAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
	}

	@FXML
	public void soundManipulatorPauseAudio() {
		controller.soundManipulatorPauseAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(false);
	}

	@FXML
	public void soundManipulatorTestFilter() {
		controller.testFilter();
	}

	@FXML
	public void soundManipulatorSaveMixedFile() {
		controller.soundManipulatorSaveFile();
	}

	@FXML
	public void soundManipulatorSaveMixerSettings() {
		//TODO Tämä tekemättä!!!
	}
	
	@FXML
	public void soundManipulatorOpenFile() {
		try {
			// Avataan file AudioFileHandlerilla ja välitetään kontrollerille
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			controller.soundManipulatorOpenFile(file);
			// Shows the name of the file in textSelectedFile element
			textSelectedFile.setText("Selected file: " + file.getName());
			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
			
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);
			textAudioFileDuration.setText(": / " + audioFileDurationString);
			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Methods for getting TextField input values
	@FXML
	public void getTextFieldPitch() {
		String text = textFieldPitch.getText();
		try {
			double number = Double.parseDouble(text);
			System.out.println(number);
			if (number <= 4.0 && number >= 0.1) {
				controller.soundManipulatorSetPitchFactor(number);
				sliderPitch.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldGain() {
		String text = textFieldGain.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 5) {
				controller.soundManipulatorSetGain(number);
				sliderGain.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldEchoLength() {
		String text = textFieldEchoLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 5) {
				controller.soundManipulatorSetEchoLength(number);
				sliderEchoLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldDecay() {
		String text = textFieldDecay.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetDecay(number);
				sliderDecay.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldFlangerLength() {
		String text = textFieldFlangerLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetFlangerLength(number);
				sliderFlangerLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldWetness() {
		String text = textFieldWetness.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 1) {
				controller.soundManipulatorSetWetness(number);
				sliderWetness.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldLfo() {
		String text = textFieldLfo.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 100) {
				controller.soundManipulatorSetLFO(number);
				sliderLfoFrequency.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	public void getTextFieldLowPass() {
		String text = textFieldLowPass.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= 0 && number <= 44100) {
				controller.soundManipulatorSetLowPass((float) number);
				sliderLowPass.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}


	@FXML
	public void soundManipulatorResetAllSliders() {
		sliderPitch.setValue(1);
		sliderGain.setValue(1);
		sliderEchoLength.setValue(0);
		sliderDecay.setValue(0);
		sliderFlangerLength.setValue(0);
		sliderWetness.setValue(0);
		sliderLfoFrequency.setValue(0);
		sliderLowPass.setValue(44100);
	}

	// Apumetodeja
	private String secondsToMinutesAndSeconds(double seconds) {
		int numberOfminutes = (int) (seconds / 60);
		int numberOfSeconds = (int) (seconds % 60);
		return numberOfminutes + ":" + numberOfSeconds;
	}

	private void enableMixerSlidersAndAudioPlayer() {
		paneMixerAudioPlayer.setDisable(false);
		paneMixerSliders.setDisable(false);
	}

	//TODO: Lisää tooltipit kaikille
	private void setTooltips() {
		final Tooltip tooltip = new Tooltip();
		tooltip.setText("Lorem ipsum bla bla bla");
		
		buttonInfoPitch.setTooltip(tooltip);
	}

	private void initializeSlidersAndTextFields() {
		// Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetPitchFactor(newValue.doubleValue());
				textFieldPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		// Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetGain(newValue.doubleValue());
				textFieldGain.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		// Echo length slider
		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetEchoLength(newValue.doubleValue());
				textFieldEchoLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		// Echo decay slider
		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetDecay(newValue.doubleValue());
				textFieldDecay.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		// Flanger length slider
		sliderFlangerLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetFlangerLength(newValue.doubleValue());
				textFieldFlangerLength.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		sliderWetness.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetWetness(newValue.doubleValue());
				textFieldWetness.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		sliderLfoFrequency.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetLFO(newValue.doubleValue());
				textFieldLfo.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		sliderLowPass.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSet(newValue.floatValue());
				textFieldLfo.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});*/
	}
	
	//Methods for getting TextField input values
	@FXML
	public void getTextFieldPitch() {
		String text = textFieldPitch.getText();
		
		try {
			double number = Double.parseDouble(text);
			System.out.println(number);
			if (number <=4.0 && number >=0.1) {
				System.out.println("hyväksytty numero");
				controller.soundManipulatorSetPitchFactor((float) number);
				sliderPitch.setValue(number);
			} else {
				System.out.println("Numero ei kelpaa");
			}
		} catch (Exception e) {
			System.out.println("väärä syöte!!");
		}
		
		
	}
	
	@FXML
	public void testFilter() {
		controller.testFilter();
	}
	
	@FXML
	public void soundManipulatorPlayAudio() {
		controller.soundManipulatorPlayAudio();
	}
	
	@FXML
	public void soundManipulatorStopAudio() {
		controller.soundManipulatorStopAudio();
	}
	
	@FXML
	public void saveMixedFile() {
		controller.soundManipulatorSaveFile();
	}
	
	@FXML
	public void resetAllSliders() {
		controller.soundManipulatorResetAllSliders();
	}
	
	@FXML
	public void saveMixerSettings() {
		
	}
	
	
		///////////////////////////////////////////////////////////

	/**
	 * TODO REGEX tarkistus tiedostonimille
	 */
	@FXML
	
	public void recordAudio() {
		controller.recordAudioToggle();
		
	}
	@FXML
	public void stopRecord() {
		controller.stopRecord();
	}
	
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
		
		MenuItem editButton = (MenuItem) mp.getItems().get(0);
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
		tf.setText(text.getText());
		tf.forward();
		ChangeListener cl = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				
				if(!tf.isFocused()) {
					String temp = tf.getText();
					if(temp.length() > 20) {
						temp = temp.substring(0,20);
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
				if(event.getCode() == KeyCode.ENTER) {
					String temp = tf.getText();
					if(temp.length() > 20) {
						temp = temp.substring(0,20);
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
