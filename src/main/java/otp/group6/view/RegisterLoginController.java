package otp.group6.view;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import otp.group6.controller.Controller;

/**
 * Class handles user registering and logging in
 * 
 * @author Joonas Soininen
 *
 */
public class RegisterLoginController {
	Controller controller;
	MainController mc;
	
	public RegisterLoginController() {
		
	}

	@FXML
	private TextField username;	
	@FXML
	private TextField password;
	@FXML
	private Button closeButton;
	@FXML
	private Button loginButton;
	
	final Tooltip pwtooltip = new Tooltip(
			"Paswords must contain 8-20 characters.\n"
			+"Must contain one uppercase letter\n"
			+"Must contain at least one number");
	
	final Tooltip wuntooltip =  new Tooltip("Can not set this username\n"+"Please select another one");
	
	final Tooltip logintip = new Tooltip("Username or password incorrect!\n Please try again :)");

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
	 * Method to get mainController
	 * @param mainController
	 */
	public void setMainController (MainController mainController) {
		this.mc=mainController;
		this.controller=mc.getController();
		controller.intializeDatabaseConnection();
	}
	
	/**
	 * Method called to remind of correct password format. Only called form username textfield.
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
                        // popup tooltip on the right, you can adjust these values for different positions
                        password.getScene().getWindow().getX() + password.getLayoutX() + password.getWidth() + 35, //
                        password.getScene().getWindow().getY() + password.getLayoutY() + password.getHeight());
            } else {
                pwtooltip.hide();
            }
			}
		});
	}
	
	/**
	 * Method registers new user to the database
	 * @param event
	 * @throws SQLException
	 */
	public void registerUser(ActionEvent event) throws SQLException {
		System.out.println(username.getText().toString().length());//Poistettava
		System.out.println(password.getText());//Poistettava		

			if((!controller.chekcforUser(username.getText()))&&username.getText().toString().length()>0) {
				System.out.println("VAPAA"); //Poistettava
				username.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
				wuntooltip.hide();
				if(isValid(password.getText())) {
					controller.createUser(username.getText(), password.getText());
					loginUser();
				} else {
					System.out.println("UUS PASSU"); //Poistettava
					password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
					//password.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
					wuntooltip.hide();
					password.requestFocus();
				}
							
			} else {
				username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			    wuntooltip.setWrapText(true);
			    wuntooltip.setTextOverrun(OverrunStyle.ELLIPSIS);
				System.out.println("Varattu!"); //Poistettava
				username.focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if (newValue) {
						wuntooltip.show(username, //
		                        // popup tooltip on the right, you can adjust these values for different positions
		                        username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth() + 35, //
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
		if (controller.loginUser(username.getText(), password.getText())=="No user"){
			username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
			username.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
					logintip.show(username, //
	                        // popup tooltip on the right, you can adjust these values for different positions
	                        username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth() + 35, //
	                        username.getScene().getWindow().getY() + username.getLayoutY() + username.getHeight());
					
	            } else {
	                logintip.hide();
	            }
				}
			});
			username.requestFocus();
		} else {
			if(controller.loginUser(username.getText(), password.getText())=="Incorrect user or pw") {
				username.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				password.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
				username.focusedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if (newValue) {
						logintip.show(username, //
		                        // popup tooltip on the right, you can adjust these values for different positions
		                        username.getScene().getWindow().getX() + username.getLayoutX() + username.getWidth() + 35, //
		                        username.getScene().getWindow().getY() + username.getLayoutY() + username.getHeight());
						
		            } else {
		                logintip.hide();
		            }
					}
				});
				username.requestFocus();
			} else {
				controller.loginUser(username.getText(), password.getText());
			    Stage stage = (Stage) closeButton.getScene().getWindow();
			    stage.close();
			    //mc.openMixerSave();
			    mc.setlogUserIn();
			}

		}

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
