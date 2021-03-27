package otp.group6.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * Class is used to change users password or delete users account from our
 * database
 * 
 * @author Joonas Soininen
 *
 */
public class UserSettingsController implements Initializable{
	Controller controller;
	MainController mc;

	@FXML
	private Button closeButton;
	@FXML
	private Label userName;
	@FXML
	private TextField password;
	@FXML
	private TextField showOldPW;
	@FXML
	private TextField npassword;
	@FXML
	private TextField showNewPW;
	@FXML
	private ToggleButton showPW1;
	@FXML
	private ToggleButton showPW2;
	

	final Tooltip pwtooltip = new Tooltip("Passwords must contain 8-20 characters.\n"
			+ "Must contain one uppercase letter\n" + "Must contain at least one number");

	@FXML
	public void showPW1() {
		
		if (showPW1.isSelected()) {
			password.setVisible(false);
			showOldPW.setVisible(true);
			showOldPW.setEditable(true);
			showOldPW.setText(password.getText());
		} else if (!showPW1.isSelected()) {
			password.setText(showOldPW.getText());
			password.setVisible(true);			
			showOldPW.setVisible(false);
		}

		

	}
	
	@FXML
	public void showPW2() {
		if (showPW2.isSelected()) {
			npassword.setVisible(false);
			showNewPW.setVisible(true);
			showNewPW.setEditable(true);
			showNewPW.setText(npassword.getText());
		} else if (!showPW2.isSelected()) {
			npassword.setText(showNewPW.getText());
			npassword.setVisible(true);
			showNewPW.setVisible(false);
		}

	}
	
	/**
	 * Method to get mainController
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		userName.setText(controller.loggedIn());
	}

	/**
	 * Method to close opened scenes
	 * 
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
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete user?");
		alert.setHeaderText(
				"You are about to permanently delete your account!\nAll user data will be lost and can not be returned!");
		alert.setContentText("Are you sure you want to delete your account?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			controller.deleteUser();
			mc.setlogUserOut();
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Information");
			alert2.setHeaderText("User deleted succesfully");
			alert2.setContentText("Thank you for using our software!");
			alert2.showAndWait();
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.close();
		} else {

		}
	}

	/**
	 * Method for changing password
	 */
	@FXML
	public void changePassword() {
		if (isValid(npassword.getText())) {
			controller.changePW(controller.loggedIn(), password.getText(), npassword.getText());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Password changed succesfully!");
			alert.showAndWait();
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.close();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("Something went wrong changing password, please try again");
			alert.setContentText("If this error continues, please contact support");
			alert.showAndWait();
		}

	}

	/**
	 * Method called to remind of correct password format. Only called form username
	 * textfield.
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
							// popup tooltip on the right, you can adjust these values for different
							// positions
							npassword.getScene().getWindow().getX() + npassword.getLayoutX() + npassword.getWidth(), //
							npassword.getScene().getWindow().getY() + npassword.getLayoutY() + npassword.getHeight()
									+ 0);
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
	 * 
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean isValid(final String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pwReminder();
		
	}

}
