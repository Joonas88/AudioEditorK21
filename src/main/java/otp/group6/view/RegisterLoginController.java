package otp.group6.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * Class handles user registering and logging in
 * 
 * @author Joonas Soininen
 *
 */
public class RegisterLoginController implements Initializable {
	Controller controller;
	MainController mc;

	public RegisterLoginController() {

	}

	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField visiblePW;
	@FXML
	private Button closeButton;
	@FXML
	private Button loginButton;
	@FXML
	private ToggleButton showPW;
	private String lastPW;

	final Tooltip pwtooltip = new Tooltip("Password must contain 8-20 characters.\n"
			+ "Must contain one uppercase letter\n" + "Must contain at least one number");

	final Tooltip wuntooltip = new Tooltip("Can not set this username\nMake sure there are no white spaces or \nselect another one");

	final Tooltip logintip = new Tooltip("Username or password incorrect!\n Please try again :)");
	
	/**
	 * Method for user to see their password on hover
	 */
	@FXML
	public void showPW() {
		if (showPW.isSelected()) {
			password.setVisible(false);
			visiblePW.setVisible(true);
			visiblePW.setEditable(true);
			visiblePW.setText(password.getText());
			
		} else if (!showPW.isSelected()) {
			visiblePW.setVisible(false);
			password.setText(visiblePW.getText());
			password.setVisible(true);
		}

	}
	
	/**
	 * Method to hide the password when exiting the hover
	 */
	@FXML
	public void setFinalPW() {
		if (showPW.isSelected()) {
			setLastPW(visiblePW.getText());
		} else {
			setLastPW(password.getText());	
		}		
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
	 * Method to get mainController
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		controller.intializeDatabaseConnection();
		pwReminder();
		username.requestFocus();
	}

	/**
	 * Method called to remind of correct password format. Only called form username
	 * textfield.
	 */
	@FXML
	public void pwReminder() {
		pwtooltip.setWrapText(true);
		pwtooltip.setTextOverrun(OverrunStyle.ELLIPSIS);

		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					pwtooltip.show(password, //
							// popup tooltip on the right, you can adjust these values for different
							// positions
							password.getScene().getWindow().getX() + password.getLayoutX() + password.getWidth() + 0, //
							password.getScene().getWindow().getY() + password.getLayoutY() + password.getHeight());
				} else {
					pwtooltip.hide();
				}
			}
		});
	}

	/**
	 * Method registers new user to the database
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void registerUser(ActionEvent event) throws SQLException {
		// System.out.println(username.getText().toString().length());// Poistettava
		// System.out.println(password.getText());// Poistettava

		if ((unisValid(username.getText()))&&!(controller.chekcforUser(username.getText()))) {
			// System.out.println("VAPAA"); // Poistettava
			username.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
			wuntooltip.hide();
			if (pwIsValid(getLastPW())) {
				controller.createUser(username.getText(), getLastPW());
				loginUser();
			} else {
				// System.out.println("UUS PASSU"); // Poistettava
				password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				visiblePW.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				// password.setStyle("-fx-control-inner-background:#000000; -fx-font-family:
				// Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000;
				// -fx-text-fill: #00ff00; ");
				wuntooltip.hide();
				password.requestFocus();
				visiblePW.requestFocus();
			}

		} else {
			username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			wuntooltip.setWrapText(true);
			wuntooltip.setTextOverrun(OverrunStyle.ELLIPSIS);
			// System.out.println("Varattu!"); // Poistettava
			username.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						wuntooltip.show(username, //
								// popup tooltip on the right, you can adjust these values for different
								// positions
								username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth()
										+ 35, //
								username.getScene().getWindow().getY() + username.getLayoutY() + username.getHeight());

					} else {
						wuntooltip.hide();
					}
				}
			});
			username.requestFocus();
		}

	}

	/**
	 * Method logs user in to access the database save function
	 */
	public void loginUser() {
		logintip.setWrapText(true);
		logintip.setTextOverrun(OverrunStyle.ELLIPSIS);
		if (controller.loginUser(username.getText(), getLastPW()) == "No user") {
			username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			visiblePW.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			username.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						logintip.show(username, //
								// popup tooltip on the right, you can adjust these values for different
								// positions
								username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth()
										+ 35, //
								username.getScene().getWindow().getY() + username.getLayoutY() + username.getHeight());

					} else {
						logintip.hide();
					}
				}
			});
			username.requestFocus();
		} else {
			if (controller.loginUser(username.getText(), getLastPW()) == "Incorrect user or pw") {
				username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				visiblePW.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				username.focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							logintip.show(username, //
									// popup tooltip on the right, you can adjust these values for different
									// positions
									username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth()
											+ 35, //
									username.getScene().getWindow().getY() + username.getLayoutY()
											+ username.getHeight());

						} else {
							logintip.hide();
						}
					}
				});
				username.requestFocus();
			} else {
				controller.loginUser(username.getText(), getLastPW());
				Stage stage = (Stage) closeButton.getScene().getWindow();
				stage.close();
				// mc.openMixerSave();
				mc.setlogUserIn();
			}

		}

	}

	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
	private static final Pattern pwPattern = Pattern.compile(PASSWORD_PATTERN);

	/**
	 * Used to check for password security
	 * 
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean pwIsValid(final String password) {
		Matcher matcher = pwPattern.matcher(password);
		return matcher.matches();
	}
	
	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
	private static final Pattern unPattern = Pattern.compile(USERNAME_PATTERN);
	
	public static boolean unisValid(final String username) {
		Matcher matcher = unPattern.matcher(username);
		return matcher.matches();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		
	}

	public String getLastPW() {
		return lastPW;
	}

	public void setLastPW(String lastPW) {
		this.lastPW = lastPW;
	}

}
