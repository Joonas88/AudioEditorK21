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

public class SaveMixerSettingsController {
	Controller controller;

	public SaveMixerSettingsController() {
	}
	
	@FXML
	private Button closeButton;
	@FXML
	private Label value1;
	@FXML
	private Label value2;
	@FXML
	private Label value3;
	@FXML
	private Label value4;
	@FXML
	private Label value5;
	@FXML
	private Label value6;
	@FXML
	private Label name;
	@FXML
	private TextField mixName;
	@FXML
	private TextArea description;
	
	private double set1, set2, set3, set4, set5, set6;
	
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
	public void getSettings(double mix1,double mix2,double mix3,double mix4,double mix5,double mix6) {
		value1.setText(String.valueOf(mix1));
		value2.setText(String.valueOf(mix2));
		value3.setText(String.valueOf(mix3));
		value4.setText(String.valueOf(mix4));
		value5.setText(String.valueOf(mix5));
		value6.setText(String.valueOf(mix6));
		name.setText(controller.loggedIn());
		
		this.set1=mix1;
		this.set2=mix2;
		this.set3=mix3;
		this.set4=mix4;
		this.set5=mix5;
		this.set6=mix6;
		
		mixNameField.setWrapText(true);
		mixNameField.setTextOverrun(OverrunStyle.ELLIPSIS);
		mixName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					mixNameField.show(mixName, //
                        // popup tooltip on the right, you can adjust these values for different positions
							mixName.getScene().getWindow().getX() + mixName.getLayoutX() + mixName.getWidth() -250, //
							mixName.getScene().getWindow().getY() + mixName.getLayoutY() + mixName.getHeight());
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
								mixName.getScene().getWindow().getX() + mixName.getLayoutX() + mixName.getWidth() -250, //
								mixName.getScene().getWindow().getY() + mixName.getLayoutY() + mixName.getHeight());
	            } else {
	            	mixNameField.hide();
	            }
				}
			});

		} else {
			if (controller.createMix(mixName.getText(), description.getText(), set1,set2,set3,set4,set5,set6)) {
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
