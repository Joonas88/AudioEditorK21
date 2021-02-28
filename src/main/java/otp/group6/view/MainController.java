package otp.group6.view;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	private Text textCurrentPitch;
	
	private DecimalFormat decimalFormat = new DecimalFormat("#0.00"); //kaikki luvut kahden desimaalin tarkkuuteen
	
	public void alustaMikseri() {
		//Pitch slider
		sliderPitch.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("pitch " + newValue);
				controller.soundManipulatorAdjustParameters(newValue.floatValue(), -1, -1, -1);
				
				textCurrentPitch.setText(decimalFormat.format(newValue.doubleValue()));
			}
		});
		
		//Echo length slider
		sliderEchoLength.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("echo length " + newValue);
				controller.soundManipulatorAdjustParameters(-1, newValue.doubleValue(), -1, -1);
			}
		});
		
		// Echo decay slider
		sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				System.out.println("decay " + newValue);
				controller.soundManipulatorAdjustParameters(-1, -1, newValue.doubleValue(), -1);
			}
		});
		
		//Gain slider
		sliderGain.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				controller.soundManipulatorAdjustParameters(-1, -1, -1, newValue.doubleValue());
				
			}
		});
	}

	
	@FXML
	public void toistaAani() {
		controller.soundManipulatorPlayAudio();
	}
	
	@FXML
	public void saveMixedFile() {
		controller.soundManipulatorSaveFile();
	}
	
	
		///////////////////////////////////////////////////////////

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
	int indexOfAddButton = 0;
	
	public Button addButton(int index) {
		AnchorPane ap = (AnchorPane) newSoundButton.getParent();
		Button temp = (Button) ap.getChildren().remove(0);
		Button button = new Button();
		button.layoutXProperty().set(65);
		button.layoutYProperty().set(45);
		button.setText("Play");
		button.setId(Integer.toString(index));
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.playSound(index);
			}
		});
		ap.getChildren().add(button);
		setButtonDescription(ap);
		
		createEditMenuButton(ap);
		
		if (buttonGrid.getChildren().indexOf(ap) < buttonGrid.getChildren().size() - 1) {
			ap = (AnchorPane) buttonGrid.getChildren().get(buttonGrid.getChildren().indexOf(ap) + 1);
			ap.getChildren().add(temp);
			indexOfAddButton = buttonGrid.getChildren().indexOf((AnchorPane)temp.getParent());
			System.out.println(indexOfAddButton);
			return temp;
		} else {
			indexOfAddButton = (Integer) null;
			return null;
		}
	}
	
	public void createEditMenuButton(AnchorPane ap) {
		MenuButton mb = new MenuButton();
		mb.layoutXProperty().set(10);
		mb.layoutYProperty().set(10);
		mb.setText("");
		
		MenuItem deleteButton = new MenuItem();
			deleteButton.setText("Delete");
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("delete button pressed " + this.getClass().toString());
					ContextMenu cm = deleteButton.getParentPopup();
					MenuButton parentMenu = (MenuButton) cm.getStyleableParent();
					AnchorPane parentAnchor = (AnchorPane) parentMenu.getParent();
					int index = Integer.parseInt(parentAnchor.getChildren().get(0).getId());
					//controller.removeSample(index);
					rearrangeButtons(index);
					/**
					 * TODO AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaa
					 * miten vitussa
					 * ei helvetti
					 * hakee ylimmän parentin?, muuttaa muiden nappien indexin??
					 * TODO Keksi miten muuttaa kaikki muut sample arrayn indexit tai miten pitää kirjaa olemattomista napeista yms......
					 */
				}
			});
			
		MenuItem editButton = new MenuItem();
			editButton.setText("Edit");
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Edit button pressed " + this.getClass().toString());
					ContextMenu cm = editButton.getParentPopup();
					MenuButton parentMenu = (MenuButton) cm.getStyleableParent();
					AnchorPane parentAnchor = (AnchorPane) parentMenu.getParent();
					int index = Integer.parseInt(parentAnchor.getChildren().get(0).getId());
					try {
						openFile(index);
					}catch(Exception e) {
						e.printStackTrace();
					}
					
					//System.out.println(parentAnchor.getChildren().toString());
				}
			});
			
		/**	UNUSED 
		MenuItem renameButton = new MenuItem();
			renameButton.setText("Rename");
			renameButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Rename button pressed " + this.getClass().toString());
			
					
					// TODO lisää joko teksti prompt tai valitse kuvausteksti ja tee siitä tilapäisesti muokattava
					// ??tarpeellinen?? -- ehkä parempi tehdä kuvauksesta suoraan muokattava. 
					 
				}
			});*/
			
		mb.getItems().add(editButton);
		
		//UNUSED
		//mb.getItems().add(renameButton);
		mb.getItems().add(deleteButton);
		
		ap.getChildren().add(mb);
	}
	
	@FXML
	AnchorPane test1,test2;
	
	@SuppressWarnings("static-access")
	public void rearrangeButtons(int index) {
		int size = controller.getSampleArrayLength();
		for(int i = index; i < size;  i++) {
			System.out.println("index: " + i);
				
		}
	}
	public void setButtonDescription(AnchorPane ap) {
		Text text = new Text();
		text.setText("Insert name");
		text.layoutXProperty().set(65);
		text.layoutYProperty().set(30);
		ap.getChildren().add(text);
		text.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				renameButton(text,ap);
			}
		});
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void renameButton(Text text, AnchorPane ap) {
		
		TextField tf = new TextField();
		tf.layoutXProperty().set(text.getLayoutX());
		tf.layoutYProperty().set(text.getLayoutY());
		tf.setText(text.getText());
		tf.forward();
		
		ChangeListener cl = new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				// TODO Auto-generated method stub
				if(!tf.isFocused()) {
					text.setText(tf.getText());
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
		};
		tf.focusedProperty().addListener(cl);
		tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER) {
					text.setText(tf.getText());
					tf.focusedProperty().removeListener(cl);
					ap.getChildren().remove(ap.getChildren().indexOf(tf));
				}
			}
			
		});
		ap.getChildren().add(tf);
	}
	
}
