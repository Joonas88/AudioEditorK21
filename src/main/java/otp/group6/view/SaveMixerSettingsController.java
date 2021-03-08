package otp.group6.view;

import javafx.scene.control.TextArea;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import otp.group6.controller.Controller;
/**
 * 
 * Class handles saving mixer settings locally and to the database
 * 
 * @author Joonas Soininen
 *
 */
public class SaveMixerSettingsController {
	Controller controller;
	MainController mc;

	public SaveMixerSettingsController() {
		
	}
	
	/**
	 * Method sets the maincontroller used in the main view
	 * Opens the database connection
	 * @param mainController
	 */
	public void setMainController (MainController mainController) {
		this.mc=mainController;
		this.controller=mc.getController();
		controller.intializeDatabaseConnection();
	}
	
	@FXML
	private Button closeButton;
	@FXML
	private Label pitchValue;
	@FXML
	private Label echoValue;
	@FXML
	private Label decayValue;
	@FXML
	private Label gainValue;
	@FXML
	private Label flangerLenghtValue;
	@FXML
	private Label wetnessValue;
	@FXML
	private Label lfoFrequencyValue;
	@FXML
	private Label lowPassValue;
	@FXML
	private Label name;
	@FXML
	private TextField mixName;
	@FXML
	private TextArea description;
	
	private double pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency;
	private float lowPass;
	
	final Tooltip mixNameField = new Tooltip("Please give the setting a name");
	
	/**
	 * Method is used to set the values from the main views sliders
	 * @param mix1
	 * @param mix2
	 * @param mix3
	 * @param mix4
	 * @param mix5
	 * @param mix6
	 */
	public void getSettings(
			double sliderPitch, 
			double sliderEchoLength, 
			double sliderDecay, 
			double sliderGain,
    		double sliderFlangerLength, 
    		double sliderWetness, 
    		double sliderLfoFrequency, 
    		float sliderLowPass) {
		pitchValue.setText(String.valueOf(sliderPitch));
		echoValue.setText(String.valueOf(sliderEchoLength));
		decayValue.setText(String.valueOf(sliderDecay));
		gainValue.setText(String.valueOf(sliderGain));
		flangerLenghtValue.setText(String.valueOf(sliderFlangerLength));
		wetnessValue.setText(String.valueOf(sliderWetness));
		lfoFrequencyValue.setText(String.valueOf(sliderLfoFrequency));
		lowPassValue.setText(String.valueOf(sliderLowPass));
		name.setText(controller.loggedIn());
		
		this.pitch=sliderPitch;
		this.echo=sliderEchoLength;
		this.decay=sliderDecay;
		this.gain=sliderGain;
		this.flangerLenght=sliderFlangerLength;
		this.wetness=sliderWetness;
		this.lfoFrequency=sliderLfoFrequency;
		this.lowPass=sliderLowPass;
		
		mixNameField.setWrapText(true);
		mixNameField.setTextOverrun(OverrunStyle.ELLIPSIS);
		mixName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					mixNameField.show(mixName, //
                        // popup tooltip on the right, you can adjust these values for different positions
							mixName.getScene().getWindow().getX() + mixName.getLayoutX() + mixName.getWidth() -220, //
							mixName.getScene().getWindow().getY() + mixName.getLayoutY() + mixName.getHeight()+200);
            } else {
            	mixNameField.hide();
            }
			}
		});

	}

	/**
	 * Method stores mixer settings to the database
	 * @throws SQLException
	 */
	public void saveMix() throws SQLException {
		
		if (mixName.getText().length()<1) {
			mixNameField.setWrapText(true);
			mixNameField.setTextOverrun(OverrunStyle.ELLIPSIS);
			mixName.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						mixNameField.show(mixName, //
	                        // popup tooltip on the right, you can adjust these values for different positions
								mixName.getScene().getWindow().getX() + mixName.getLayoutX() + mixName.getWidth() -220, //
								mixName.getScene().getWindow().getY() + mixName.getLayoutY() + mixName.getHeight()+200);
		            } else {
		            	mixNameField.hide();
		            }
				}
			});

		} else {
			if (controller.createMix(mixName.getText(), description.getText(), pitch,echo,decay,gain,flangerLenght,wetness, lfoFrequency, lowPass)) {
				JOptionPane.showMessageDialog(null, "Mixer settings saved!","All Good!", JOptionPane.INFORMATION_MESSAGE); //Onko tämä kaikille ok?
			    Stage stage = (Stage) closeButton.getScene().getWindow();
			    stage.close();	
			} else {
				JOptionPane.showMessageDialog(null, "Something went wrong savin mixer settings, please try again","Alert",JOptionPane.WARNING_MESSAGE); //Onko tämä kaikille ok?
			}			
		    
		}
	
	}
	

	/**
	 * Method handles closing scene windows.
	 * @param event
	 */
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
}
