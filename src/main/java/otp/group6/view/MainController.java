package otp.group6.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Platform;
import be.tarsos.dsp.StopAudioProcessor;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.controller.Controller;

/**
 * Main controller for the view
 * 
 * @author Kevin Akkoyun
 * @version 0.1
 */
public class MainController implements Initializable {
	Controller controller;

	public MainController() {
		controller = new Controller(this);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		controller.readSampleData();
		checkSavedSamples();
		soundboardInit();
		applyBackgroundColor();
	}

////// MIXER //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Sliders
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
	private Slider sliderAudioFileDuration;

	// TextFields connected to sliders
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

	// Text/label elements
	@FXML
	private Label textSelectedFile;
	@FXML
	private Text textAudioFileDuration;

	// Buttons
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
	private Button buttonInfoEcho;
	@FXML
	private Button buttonInfoFlanger;
	@FXML
	private Button buttonInfoLowPass;
	@FXML
	private Button buttonSaveSettings;
	@FXML
	private Button buttonLoadSettings;
	@FXML
	private Button buttonMixerFileOpener;
	@FXML
	private Button buttonMixerResetSliders;

	// Toggle buttons
	@FXML
	private ToggleButton toggleButtonMixerStartRecording;
	@FXML
	private ToggleButton toggleButtonTestFilter;
	@FXML
	private ToggleButton toggleButtonPitch;
	@FXML
	private ToggleButton toggleButtonGain;
	@FXML
	private ToggleButton toggleButtonEcho;
	@FXML
	private ToggleButton toggleButtonFlanger;
	@FXML
	private ToggleButton toggleButtonLowPass;

	// Panes
	@FXML
	private GridPane paneMixerSliders;
	@FXML
	private AnchorPane paneMixerAudioPlayer;
	@FXML
	private AnchorPane paneMixerMainControls;
	@FXML
	private AnchorPane panePitch;
	@FXML
	private AnchorPane paneDecay;
	@FXML
	private AnchorPane paneEchoLength;
	@FXML
	private AnchorPane paneWetness;
	@FXML
	private AnchorPane paneFlangerLength;
	@FXML
	private AnchorPane paneLfo;
	@FXML
	private AnchorPane paneLowPass;

	// Muuttujat tiedoston kokonaiskestolle ja toistetulle ajalle
	private String audioFileDurationString = "0:00";
	private String audioFileProcessedTimeString = "0:00";

	// TODO HOX TÄMÄN LOKALISOINTI
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // kaikki luvut kahden desimaalin tarkkuuteen

	/*
	 * 
	 */
	public void initializeMixer() {
		initializeSlidersAndTextFields();
		setTooltips();
		initializeRecorderListener();
	}

	// Methods for buttons

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorPlayButton() {
		controller.audioManipulatorPlayAudio();
		buttonPlay.setDisable(true);
		buttonPause.setDisable(false);
		buttonStop.setDisable(false);
		paneMixerMainControls.setDisable(true);
		sliderLowPass.setDisable(true);
		textFieldLowPass.setDisable(true);
	}

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorPauseButton() {
		controller.audioManipulatorPauseAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(false);
		paneMixerMainControls.setDisable(false);
		sliderLowPass.setDisable(false);
		textFieldLowPass.setDisable(false);
	}

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorStopButton() {
		controller.audioManipulatorStopAudio();
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
		paneMixerMainControls.setDisable(false);
		sliderLowPass.setDisable(false);
		textFieldLowPass.setDisable(false);
	}

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorTestFilterButton() {
		if (toggleButtonTestFilter.isSelected() == true) {
			System.out.println(toggleButtonTestFilter.isSelected());
			controller.testFilter();
			buttonMixerFileOpener.setDisable(true);
			toggleButtonMixerStartRecording.setDisable(true);
			paneMixerAudioPlayer.setDisable(true);
			sliderLowPass.setDisable(true);
			textFieldLowPass.setDisable(true);
		} else {
			controller.testFilter();
			System.out.println(toggleButtonTestFilter.isSelected());
			buttonMixerFileOpener.setDisable(false);
			toggleButtonMixerStartRecording.setDisable(false);
			paneMixerAudioPlayer.setDisable(false);
			sliderLowPass.setDisable(false);
			textFieldLowPass.setDisable(false);
		}
	}

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorSaveMixedFileButton() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("WAV files (*.wav)", "*.wav");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".wav")) {
				fullPath = fullPath + ".wav";
			}
			controller.audioManipulatorSaveFile(fullPath);
			System.out.println("saved to " + fullPath);
		} catch (Exception e) {
		}
	}

	/*
	 * 
	 */
	@FXML
	public void handleAudioManipulatorOpenFileButton() {
		try {
			Pattern pattern = Pattern.compile("(\\.wav)$", Pattern.CASE_INSENSITIVE);

			// Avataan file AudioFileHandlerilla ja välitetään file kontrollerille
			File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
			Matcher matcher = pattern.matcher(file.getName());
			if (matcher.find()) {
				audioManipulatorResetMediaPlayer();
				controller.audioManipulatorOpenFile(file);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Wrong audio format");
				alert.setContentText("Please select only WAV files");
				alert.showAndWait();
			}
			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
			controller.setAudioFileLengthInSec(audioFileLengthInSec);
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);

			setMaxValueToAudioDurationSlider(audioFileLengthInSec);
			textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);

			// Shows the name of the file in textSelectedFile element
			textSelectedFile.setText("Selected file:\n" + file.getName());

			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();

		} catch (Exception e) {
		}
	}
	
	@FXML
	public void handleMixerRecordButton() {
		if (toggleButtonMixerStartRecording.isSelected() == true) {
			controller.audioManipulatorStartRecord();
			paneMixerAudioPlayer.setDisable(true);
			paneMixerSliders.setDisable(true);
			toggleButtonMixerStartRecording.setText("Stop recording");
		} else {
			controller.audioManipulatorStopRecord();
			paneMixerAudioPlayer.setDisable(false);
			paneMixerSliders.setDisable(false);
			toggleButtonMixerStartRecording.setText("Start recording");
		}
	}
	
	public void audioManipulatorOpenRecordedFile() {
		try {
			File file = new File("src/audio/mixer_default.wav").getAbsoluteFile();
			file.deleteOnExit();
			audioManipulatorResetMediaPlayer();
			controller.audioManipulatorOpenFile(file);

			// Length of the audio file in seconds (file.length / (format.frameSize *
			// format.frameRate))
			AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
			double audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
			controller.setAudioFileLengthInSec(audioFileLengthInSec);
			audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);

			setMaxValueToAudioDurationSlider(audioFileLengthInSec);
			textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);

			// Shows the name of the file in textSelectedFile element
			textSelectedFile.setText("Selected file:\nYour recording");

			// Enables all sliders and audio player
			enableMixerSlidersAndAudioPlayer();

		} catch (Exception e) {
		}
	}
	
	
	// Methods for toggle buttons

		public void handleToggleButtonPitch() {
			if (toggleButtonPitch.isSelected()) {
				controller.audioManipulatorUsePitchProcessor(true);
				sliderPitch.setDisable(false);
				textFieldPitch.setDisable(false);
			} else if (!toggleButtonPitch.isSelected()) {
				controller.audioManipulatorUsePitchProcessor(false);
				sliderPitch.setDisable(true);
				textFieldPitch.setDisable(true);
			}
		}

		public void handleToggleButtonEcho() {
			if (toggleButtonEcho.isSelected() == true) {
				controller.audioManipulatorUseDelayProcessor(true);
				sliderEchoLength.setDisable(false);
				sliderDecay.setDisable(false);
				textFieldEchoLength.setDisable(false);
				textFieldDecay.setDisable(false);
			} else {
				controller.audioManipulatorUseDelayProcessor(false);
				sliderEchoLength.setDisable(true);
				sliderDecay.setDisable(true);
				textFieldEchoLength.setDisable(true);
				textFieldDecay.setDisable(true);
			}
			

		}

		public void handleToggleButtonGain() {
			if (toggleButtonGain.isSelected() == true) {
				controller.audioManipulatorUseGainProcessor(true);
				sliderGain.setDisable(false);
				textFieldGain.setDisable(false);
			} else {
				controller.audioManipulatorUseGainProcessor(false);
				sliderGain.setDisable(true);
				textFieldGain.setDisable(true);
			}
			

		}

		public void handleToggleButtonFlanger() {
			if (toggleButtonFlanger.isSelected() == true) {
				controller.audioManipulatorUseFlangerProcessor(true);
				sliderWetness.setDisable(false);
				sliderFlangerLength.setDisable(false);
				sliderLfoFrequency.setDisable(false);
				textFieldWetness.setDisable(false);
				textFieldFlangerLength.setDisable(false);
				textFieldLfo.setDisable(false);
			} else {
				controller.audioManipulatorUseFlangerProcessor(false);
				sliderWetness.setDisable(true);
				sliderFlangerLength.setDisable(true);
				sliderLfoFrequency.setDisable(true);
				textFieldWetness.setDisable(true);
				textFieldFlangerLength.setDisable(true);
				textFieldLfo.setDisable(true);
			}
			

		}

		public void handleToggleButtonLowPass() {
			if (toggleButtonLowPass.isSelected() == true) {
				controller.audioManipulatorUseLowPassProcessor(true);
				sliderLowPass.setDisable(false);
				textFieldLowPass.setDisable(false);
			} else {
				controller.audioManipulatorUseLowPassProcessor(false);
				sliderLowPass.setDisable(true);
				textFieldLowPass.setDisable(true);
			}
			
		}
	



	// Methods for getting TextField input values

	/*
	 * 
	 */
	@FXML
	private void getTextFieldPitch() {
		String text = textFieldPitch.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderPitch.getMin() && number <= sliderPitch.getMax()) {
				controller.audioManipulatorSetPitchFactor(number);
				sliderPitch.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	/*
	 * 
	 */
	@FXML
	private void getTextFieldGain() {
		String text = textFieldGain.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderGain.getMin() && number <= sliderGain.getMax()) {
				controller.audioManipulatorSetGain(number);
				sliderGain.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	/*
	 * 
	 */
	@FXML
	private void getTextFieldEchoLength() {
		String text = textFieldEchoLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderEchoLength.getMin() && number <= sliderEchoLength.getMax()) {
				controller.audioManipulatorSetEchoLength(number);
				sliderEchoLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	/*
	 * 
	 */
	@FXML
	private void getTextFieldDecay() {
		String text = textFieldDecay.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderDecay.getMin() && number <= sliderDecay.getMax()) {
				controller.audioManipulatorSetDecay(number);
				sliderDecay.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	/*
	 * 
	 */
	@FXML
	private void getTextFieldFlangerLength() {
		String text = textFieldFlangerLength.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderFlangerLength.getMin() && number <= sliderFlangerLength.getMax()) {
				controller.audioManipulatorSetFlangerLength(number);
				sliderFlangerLength.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	private void getTextFieldWetness() {
		String text = textFieldWetness.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderWetness.getMin() && number <= sliderWetness.getMax()) {
				controller.audioManipulatorSetWetness(number);
				sliderWetness.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	private void getTextFieldLfo() {
		String text = textFieldLfo.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderLfoFrequency.getMin() && number <= sliderLfoFrequency.getMax()) {
				controller.audioManipulatorSetLFO(number);
				sliderLfoFrequency.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	@FXML
	private void getTextFieldLowPass() {
		String text = textFieldLowPass.getText();
		try {
			double number = Double.parseDouble(text);
			if (number >= sliderLowPass.getMin() && number <= sliderLowPass.getMax()) {
				controller.audioManipulatorSetLowPass((float) number);
				sliderLowPass.setValue(number);
			} else {
				System.out.println("Arvo yli viiterajojen");
			}
		} catch (Exception e) {
			System.out.println("Virheellinen syöte");
		}
	}

	/*
	 * 
	 */
	public void audioManipulatorAudioFileReachedEnd() {
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(0);
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
		sliderAudioFileDuration.setValue(0);
		buttonPlay.setDisable(false);
		buttonPause.setDisable(true);
		buttonStop.setDisable(true);
		paneMixerMainControls.setDisable(false);
		paneLowPass.setDisable(false);
	}
	
	@FXML
	public void handleAudioFileDurationSliderClick() {
		controller.timerCancel();

		System.out.println("slideria klikattu " + sliderAudioFileDuration.getValue());
		controller.audioManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

		// Nyk kesto tekstinä
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	

	@FXML
	public void audioManipulatorResetAllSliders() {
		sliderPitch.setValue(1);
		sliderGain.setValue(1);
		sliderEchoLength.setValue(1);
		sliderDecay.setValue(0);
		sliderFlangerLength.setValue(0.01);
		sliderWetness.setValue(0);
		sliderLfoFrequency.setValue(5);
		sliderLowPass.setValue(44100);
	}

	private void audioManipulatorResetMediaPlayer() {
		controller.audioManipulatorResetMediaPlayer();
	}

	/*
	 * 
	 */
	public void setMaxValueToAudioDurationSlider(double audioFileLengthInSec) {
		sliderAudioFileDuration.setMax(audioFileLengthInSec);
	}

	/*
	 * 
	 */
	public void setCurrentValueToAudioDurationSlider(double currentSeconds) {
		sliderAudioFileDuration.setValue(currentSeconds);
	}

	/*
	 *
	 * 
	 */
	public void setCurrentValueToAudioDurationText(double currentSeconds) {
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(currentSeconds);
		textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	/*
	 * @ author Roosa Laukkanen
	 * 
	 * Converts seconds to minutes and seconds. Returns String in XX:XX format
	 */
	private String secondsToMinutesAndSeconds(double seconds) {
		int numberOfminutes = (int) (seconds / 60);
		int numberOfSeconds = (int) (seconds % 60);
		if (numberOfSeconds < 10) {
			return numberOfminutes + ":0" + numberOfSeconds;
		} else {
			return numberOfminutes + ":" + numberOfSeconds;
		}
	}

	public void enableMixerSlidersAndAudioPlayer() {
		paneMixerAudioPlayer.setDisable(false);
		paneMixerSliders.setDisable(false);
	}

	public void setDisableMixerSliders(boolean trueOrFalse) {
		paneMixerSliders.setDisable(trueOrFalse);
	}

	/*
	 * Sets value property listeners to every slider and sets event listeners to
	 * input text fields
	 */
	private void initializeSlidersAndTextFields() {
		// Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetPitchFactor(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldPitch.setText(value.replace(",", "."));
			}
		});

		// Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetGain(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldGain.setText(value.replace(",", "."));
			}
		});

		// Echo length slider
		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetEchoLength(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldEchoLength.setText(value.replace(",", "."));
			}
		});

		// Echo decay slider
		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetDecay(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldDecay.setText(value.replace(",", "."));
			}
		});

		// Flanger length slider
		sliderFlangerLength.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetFlangerLength(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldFlangerLength.setText(value.replace(",", "."));
			}
		});

		// Wetness slider
		sliderWetness.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetWetness(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldWetness.setText(value.replace(",", "."));
			}
		});

		// Lfo slider
		sliderLfoFrequency.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetLFO(newValue.doubleValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldLfo.setText(value.replace(",", "."));
			}
		});

		// LowPass slider
		sliderLowPass.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.audioManipulatorSetLowPass(newValue.floatValue());
				String value = decimalFormat.format(newValue.doubleValue());
				textFieldLowPass.setText(value.replace(",", "."));
			}
		});

		// AudioFileDuration slider
		sliderAudioFileDuration.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if (sliderAudioFileDuration.isPressed()) {
					controller.timerCancel();

					System.out.println("slideria klikattu " + sliderAudioFileDuration.getValue());
					controller.audioManipulatorPlayFromDesiredSec(sliderAudioFileDuration.getValue());

					// Nyk kesto tekstinä
					audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderAudioFileDuration.getValue());
					textAudioFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
				}
			}
		});

		// On key methods for every text field

		textFieldPitch.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldPitch();
			}
		});

		textFieldGain.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldGain();
			}
		});

		textFieldEchoLength.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldEchoLength();
			}
		});

		textFieldDecay.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldDecay();
			}
		});

		textFieldFlangerLength.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldFlangerLength();
			}
		});

		textFieldWetness.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldWetness();
			}
		});

		textFieldLfo.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldLfo();
			}
		});

		textFieldLowPass.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				getTextFieldLowPass();
			}
		});
	}

	/*
	 * @author Roosa Laukkanen
	 * 
	 * Sets a tooltip to every info button
	 */
	private void setTooltips() {
		final Tooltip tooltipPitch = new Tooltip();
		tooltipPitch.setText("Pitch muuttaa kappaleen sävelkorkeutta");

		final Tooltip tooltipGain = new Tooltip();
		tooltipGain.setText("Gain muuttaa kappaleen äänenvoimakkuutta");

		final Tooltip tooltipEcho = new Tooltip();
		tooltipEcho.setText("Echoon kuuluu decay ja length");

		final Tooltip tooltipFlanger = new Tooltip();
		tooltipFlanger.setText("Flangeriin kuuluu wetness, length ja lfo");

		final Tooltip tooltipLowPass = new Tooltip();
		tooltipLowPass.setText("Low Pass on ihan :) ominaisuus");

		buttonInfoPitch.setTooltip(tooltipPitch);
		buttonInfoGain.setTooltip(tooltipGain);
		buttonInfoLowPass.setTooltip(tooltipLowPass);
	}

	//// MIXER METHODS END HERE

	// RECORDER METHODS START HERE/////////////////////////////////////

	@FXML
	private Button recorderButtonPlay;
	@FXML
	private Button recorderButtonPause;
	@FXML
	private Button recorderButtonStop;
	@FXML
	private Button recorderButtonSave;
	@FXML
	private ToggleButton recorderToggleButtonStartRecording;
	@FXML
	private ToggleButton recorderButtonPauseRecord;
	@FXML
	private Text textRecordFileDuration;
	@FXML
	private Text textRecordingDuration;
	@FXML
	private Slider sliderRecordedFileDuration;

	private Timer timer;

	@FXML
	public void recordAudioToggle() {
		if (recorderToggleButtonStartRecording.isSelected()) {

			textRecordFileDuration.setDisable(true);
			sliderRecordedFileDuration.setDisable(true);
			recorderButtonPause.setDisable(true);
			recorderButtonPlay.setDisable(true);
			recorderButtonStop.setDisable(true);
			recorderButtonSave.setDisable(true);
			recorderToggleButtonStartRecording.setText("Stop");

			controller.recordAudio();
			timer = new Timer();
			TimerTask task = new TimerTask() {
				int i = 0;

				@Override
				public void run() {
					textRecordingDuration.setText("" + i);
					i++;
				}
			};
			timer.schedule(task, 0, 1000);
		} else {
			timer.cancel();
			textRecordingDuration.setText("0");
			controller.stopRecord();
			enableRecorderPlayer();
			recorderToggleButtonStartRecording.setText("Record");
			File file = null;
			file = new File("src/audio/recorder_default.wav").getAbsoluteFile();
			file.deleteOnExit();

			if (file != null) {
				try {
					AudioFormat format = AudioSystem.getAudioFileFormat(file.getAbsoluteFile()).getFormat();
					float audioFileLengthInSec = file.length() / (format.getFrameSize() * format.getFrameRate());
					controller.recorderSetAudioFileDuration(audioFileLengthInSec);
					setMaxValueToRecordDurationSlider(audioFileLengthInSec);
					audioFileDurationString = secondsToMinutesAndSeconds(audioFileLengthInSec);
					textRecordFileDuration.setText("0:00 / " + audioFileDurationString);
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

	}

	@FXML
	public void recorderPlayAudio() {
		controller.audioRecorderPlayAudio();
		recorderButtonPlay.setDisable(true);
		recorderButtonPause.setDisable(false);
		recorderButtonStop.setDisable(false);
	}

	@FXML
	public void recorderStopAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(true);
		controller.audioRecorderStopAudio();
	}

	@FXML
	public void recorderPauseAudio() {
		controller.recorderTimerCancel();
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(false);
		controller.audioRecorderPauseAudio();
	}

	private void enableRecorderPlayer() {
		sliderRecordedFileDuration.setDisable(false);
		textRecordFileDuration.setDisable(false);
		recorderButtonPlay.setDisable(false);
		recorderButtonSave.setDisable(false);
	}

	public void recorderSliderPressed() {
		controller.recorderSliderPressed();
	}

	public void initializeRecorderListener() {
		sliderRecordedFileDuration.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {

				if (sliderRecordedFileDuration.isPressed()) {
					recorderSliderPressed();
					controller.recorderTimerCancel();
					double currentPoint = sliderRecordedFileDuration.getValue();
					sliderRecordedFileDuration.setValue(currentPoint);
					controller.recorderPlayFromDesiredSec(currentPoint);
					audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderRecordedFileDuration.getValue());
					textRecordFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
				}

			}
		});
	}

	public void saveRecordedFile() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("WAV files (*.wav)", ".wav");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".wav")) {
				fullPath = fullPath + ".wav";
			}
			controller.saveAudioFile(fullPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setMaxValueToRecordDurationSlider(double audioFileLengthInSec) {
		sliderRecordedFileDuration.setMax(audioFileLengthInSec);
	}

	public void setCurrentValueToRecordDurationSlider(double currentSeconds) {
		sliderRecordedFileDuration.setValue(currentSeconds);
		audioFileProcessedTimeString = secondsToMinutesAndSeconds(sliderRecordedFileDuration.getValue());
		textRecordFileDuration.setText(audioFileProcessedTimeString + " / " + audioFileDurationString);
	}

	public void recorderAudioFileReachedEnd() {
		setCurrentValueToRecordDurationSlider(0);
		recorderButtonPlay.setDisable(false);
		recorderButtonPause.setDisable(true);
		recorderButtonStop.setDisable(true);
	}

	//// RECORDER METHODS END
	//// HERE////////////////////////////////////////////////////////////////
	// Used in play button methods
	public static Button lastButton,currentButton;

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

	/**
	 * @author Joonas Soininen
	 */

	/**
	 * Following variables and methods mostly open new stages and handle slider
	 * values back and forth from files. More specific explanations with each
	 * method.
	 */
	@FXML
	private Label loggedinuser;
	@FXML
	private MenuItem userSettings;
	private MenuButton userMenuButton = new MenuButton();
	private MenuItem menu1 = new MenuItem("User settings");
	private MenuItem menu2 = new MenuItem("log out");
	@FXML
	private MenuItem loginoption;
	@FXML
	private Button closeButton;

	/**
	 * Method opens a new scene Login and Register form
	 */
	public void openLoginRegister() {
		controller.intializeDatabaseConnection();
		if (controller.isConnected()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisterLoginView.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				RegisterLoginController rlc = fxmlLoader.getController();
				rlc.setMainController(this);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setTitle("Login or Register");
				stage.setScene(new Scene(root1));
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Method opens a new scene, the mixer settings from the database
	 */
	public void openMixerSettings() {
		controller.intializeDatabaseConnection();
		if (controller.isConnected()) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MixerSettingsView.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				MixerSettingsController msc = fxmlLoader.getController();
				msc.setMainController(this);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setTitle("Mixer Settings Loader");
				stage.setScene(new Scene(root1));
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Controller getController() {
		return this.controller;
	}

	/**
	 * Opens a new scene where the mixer settings can be saved to the database
	 */
	public void openMixerSave() {
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SaveMixerSettings.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			SaveMixerSettingsController smsc = fxmlLoader.getController();
			smsc.setMainController(this);
			smsc.getSettings(sliderPitch.getValue(), sliderEchoLength.getValue(), sliderDecay.getValue(),
					sliderGain.getValue(), sliderFlangerLength.getValue(), sliderWetness.getValue(),
					sliderLfoFrequency.getValue(), (float) sliderLowPass.getValue());
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
	 * Opens the save selection scene where user can decide to save settings locally
	 * or to the database.
	 */
	public void openSaveSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SaveSelectionView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			SaveSelectionController ssc = fxmlLoader.getController();
			ssc.setMainController(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Mixer Settings Saving");
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a new scene where user can select where to load mixer settings, locally
	 * or from the database.
	 */
	public void openLoadSelection() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoadSelectionView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			LoadSelectionController lsc = fxmlLoader.getController();
			lsc.setMainController(this);
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
	 * Opens user settings view where password can be changed or the user deleted.
	 */
	public void openUserSettings() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UserSettingsView.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			UserSettingsController usc = fxmlLoader.getController();
			usc.setMainController(this);
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
	 * Method to close open scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method to set the slider values with data from the database
	 * 
	 * @param pitch
	 * @param echo
	 * @param decay
	 * @param gain
	 * @param flangerLenght
	 * @param wetness
	 * @param lfoFrequency
	 * @param lowPass
	 */
	public void setSliderValues(double pitch, double echo, double decay, double gain, double flangerLenght,
			double wetness, double lfoFrequency, float lowPass) {
		sliderPitch.setValue(pitch);
		sliderEchoLength.setValue(echo);
		sliderDecay.setValue(decay);
		sliderGain.setValue(gain);
		sliderFlangerLength.setValue(flangerLenght);
		sliderWetness.setValue(wetness);
		sliderLfoFrequency.setValue(lfoFrequency);
		sliderLowPass.setValue(lowPass);
	}

	/**
	 * Method to store mixer settings locally
	 */
	public void saveMixerSettingsLocally() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showSaveDialog(mainContainer.getScene().getWindow());
		String fullPath;
		try {
			fullPath = file.getAbsolutePath();
			if (!fullPath.endsWith(".txt")) {
				fullPath = fullPath + ".txt";
			}
			try {
				FileWriter writeFile = new FileWriter(fullPath);
				writeFile.write(Double.toString(sliderPitch.getValue()) + "\n");
				writeFile.write(Double.toString(sliderEchoLength.getValue()) + "\n");
				writeFile.write(Double.toString(sliderDecay.getValue()) + "\n");
				writeFile.write(Double.toString(sliderGain.getValue()) + "\n");
				writeFile.write(Double.toString(sliderFlangerLength.getValue()) + "\n");
				writeFile.write(Double.toString(sliderWetness.getValue()) + "\n");
				writeFile.write(Double.toString(sliderLfoFrequency.getValue()) + "\n");
				writeFile.write(Float.toString((float) sliderLowPass.getValue()) + "\n");
				writeFile.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Setting saved succesfully!");
				alert.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Something went wrong!");
				alert.setContentText("If this keeps happening, contact support! :)");
				alert.showAndWait();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Method to set visible user into main view and also set up different functions
	 * for logging out
	 */
	public void setlogUserIn() {
		menu1.setOnAction(event -> {
			openUserSettings();
		});
		menu2.setOnAction(event -> {
			setlogUserOut();
		});
		userMenuButton.setText(controller.loggedIn());
		userMenuButton.setStyle("-fx-font-size: 10pt; -fx-text-fill:black;"); //MUOTOILU CSSSSSÄÄÄÄN
		userMenuButton.getItems().addAll(menu1, menu2);
		loggedinuser.setVisible(true);
		loggedinuser.setText("Logged in as: ");
		loggedinuser.setGraphic(userMenuButton);
		loggedinuser.setContentDisplay(ContentDisplay.RIGHT);
		userSettings.setVisible(true);
		loginoption.setVisible(false);
	}

	/**
	 * Method to change visibility of certain labels and menu items.
	 */
	public void setlogUserOut() {
		controller.logoutUser();
		userMenuButton.setText("");
		userMenuButton.setStyle("");
		userMenuButton.getItems().removeAll(menu1, menu2);
		loggedinuser.setVisible(false);
		userSettings.setVisible(false);
		loginoption.setVisible(true);

	}
}
