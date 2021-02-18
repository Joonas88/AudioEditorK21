package otp.group6.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

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
	private Text textFieldRecordDuration;
	
	/*
	//(double maxFlangerLength,double wet,
    double sampleRate,
    double lfoFrequency)*/
	
	
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); //kaikki luvut kahden desimaalin tarkkuuteen
	
	public void initializeMixer() {
		//Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetPitchFactor(newValue.doubleValue());
				textFieldPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				controller.soundManipulatorSetGain(newValue.doubleValue());
				textFieldGain.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//Echo length slider
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
		
		//Flanger length slider
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
		/*
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

}
