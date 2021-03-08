package otp.group6.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class UserSettingsController {
	Controller controller;
	MainController mc;	
	
	@FXML
	private Button closeButton;
	@FXML
	private Label userName;
	@FXML
	private TextField password;
	@FXML
	private TextField npassword;
	
	final Tooltip pwtooltip = new Tooltip(
			"Paswords must contain 8-20 characters.\n"
			+"Must contain one uppercase letter\n"
			+"Must contain at least one number");
	
	/**
	 * Method to get mainController
	 * @param mainController
	 */
	public void setMainController (MainController mainController) {
		this.mc=mainController;
		this.controller=mc.getController();
		userName.setText(controller.loggedIn());
	}
	
	/**
	 * Method to close opened scenes
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Method deletes user from the database.
	 */
	@FXML
	public void deleteUser() {
		int reply  = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "+controller.loggedIn(), "WARNING", JOptionPane.YES_NO_CANCEL_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			controller.deleteUser();
			mc.setlogUserOut();
			JOptionPane.showMessageDialog(null, "User deletion succesful");
		    Stage stage = (Stage) closeButton.getScene().getWindow();
		    stage.close();
		}	
	}
	
	/**
	 * Method for changin password
	 */
	@FXML
	public void changePassword() {
		if(isValid(npassword.getText())) {
			controller.changePW(controller.loggedIn(), password.getText(), npassword.getText());
			JOptionPane.showMessageDialog(null, "Password changed succesfully!");
		    Stage stage = (Stage) closeButton.getScene().getWindow();
		    stage.close();
		} else {
			JOptionPane.showMessageDialog(null, "Something went wrong!\nPlease try again.\nIf this error continues, please contact support.");	
		}
		
	}
	
	/**
	 * Method called to remind of correct password format. Only called form username textfield.
	 */
	@FXML
	public void pwReminder() {
	    pwtooltip.setWrapText(true);
	    pwtooltip.setTextOverrun(OverrunStyle.ELLIPSIS);

		npassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
				pwtooltip.show(npassword, //
                        // popup tooltip on the right, you can adjust these values for different positions
                        npassword.getScene().getWindow().getX() + npassword.getLayoutX() + npassword.getWidth(), //
                        npassword.getScene().getWindow().getY() + npassword.getLayoutY() + npassword.getHeight()+130);
            } else {
                pwtooltip.hide();
            }
			}
		});
	}
	
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	
	/**
	 * Used to check for password security
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean isValid(final String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

}
