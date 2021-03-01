package otp.group6.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
				controller.soundManipulatorSetLowPass(newValue.floatValue());
				textFieldLowPass.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
	}

	///// MIXER ENDS HERE //////

}
