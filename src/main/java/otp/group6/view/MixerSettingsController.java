package otp.group6.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.controller.Controller;

/**
 * Class handles database stored mixer settings, displaying them, adding them to
 * favorites and storing the favorites list locally.
 * 
 * @author Joonas Soininen
 *
 */
public class MixerSettingsController implements Initializable {
	Controller controller;
	MainController mc;

	public MixerSettingsController() {

	}

	@FXML
	AnchorPane mainContainer;
	@FXML
	private Button closeButton;
	@FXML
	private ListView<String> favoritesListView;
	@FXML
	private ObservableList<String> mixerSettings = FXCollections.observableArrayList();

	private int mixerIndetification;
	
	private String mixerCreatorName;
	
	private List<String> localList = new ArrayList<>();

	@FXML
	private ListView<HBoxCell> cloudListView;
	@FXML
	private ObservableList<HBoxCell> myObservableList = FXCollections.observableArrayList();;

	@FXML
	private TextField searchField;
	@FXML
	private RadioButton radioCreator;
	@FXML
	private RadioButton radioName;
	@FXML
	private RadioButton radioDescription;
	@FXML
	private Button removeFav;
	@FXML
	private Button deleteMixButton;
	
	/**
	 * Inner class to handle buttons on the ListView.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public class HBoxCell extends HBox {
		Label label = new Label();
		Button button = new Button();

		/**
		 * Method to create buttons into the ListView
		 * 
		 * @param labelText
		 * @param buttonText
		 * @param id
		 */
		HBoxCell(String labelText, String buttonText, int id) {
			super();

			label.setText(labelText);
			label.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(label, Priority.ALWAYS);

			button.setText(buttonText);
			button.setId(String.valueOf(id));
			/*
			 * if (!(controller.loggedIn()==" ")) { button.setDisable(false); } else {
			 * button.setDisable(true); }
			 */
			if (localList.contains(String.valueOf(id))) {
				button.setDisable(true);
			}
			button.setOnAction(e -> {
				try {
					favoriteButton(button.getId(), labelText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				button.setDisable(true);
			});

			this.getChildren().addAll(label, button);
		}
	}

	/**
	 * TODO Metodi suosikkien tallentamiseen tietokantaan puuttuu TODO Metodi
	 * suosikin poistamiseen puuttuu
	 */

	/**
	 * Method to set the favorite buttons function
	 * 
	 * @param id
	 * @param title
	 * @throws IOException 
	 */
	public void favoriteButton(String id, String title) throws IOException {		
		mixerSettings.add(title);
		localList.add(id);
		save();
		//mixerSettings.removeAll(localList);
		//favoritesListView.getItems().clear();
		localList.clear();
		read();
		
		/*
		ObservableList<Object> mixerID = FXCollections.observableArrayList();
		mixerID.add(Integer.valueOf(id));
		mixerSettings = FXCollections.observableArrayList(hlist);
		favoritesListView.setItems(mixerSettings);
		save();
		favoritesListView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					int index = favoritesListView.getSelectionModel().getSelectedIndex();
					int identification = (int) mixerID.get(index);
					setMixerIndetification(identification);
					//String selectedItem = mixListView.getSelectionModel().getSelectedItem(); //POISTETTAVA
					//System.out.println(selectedItem); //POISTETTAVA
					removeFav.setDisable(false);
				});
				*/

	}

	/**
	 * Method to save locally users favorite mixer settings
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		//Automatic save method
		File file1 = new File("src/localfav/Fav1.txt");
		try {
			FileOutputStream fout = new FileOutputStream(file1);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(localList);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("Something went wrong!");
			alert.setContentText("If this keeps happening, contact support! :)");
			alert.showAndWait();
		}
		//Manual save method
		/*
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
				FileOutputStream fout = new FileOutputStream(fullPath);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(localList);
				fout.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Setting saved succesfully!");
				alert.showAndWait();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Something went wrong!");
				alert.setContentText("If this keeps happening, contact support! :)");
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * Method to load from local storage users favorite mixer settings
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	public void read() throws IOException {		
		favoritesListView.getItems().clear();
		localList.clear();
		mixerSettings.clear();
		//File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
		File autofile= new File("src/localfav/Fav1.txt");
		FileInputStream fin = new FileInputStream(autofile);
		ObjectInputStream ois = new ObjectInputStream(fin);
		try {
			localList = (ArrayList<String>) ois.readObject();
			System.out.println(localList);
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			MixerSetting[] setlist = controller.getAllMixArray();
			for (MixerSetting mix : setlist) {
				for (int i = 0; i < localList.size(); i++) {
					if (mix.getMixID() == Integer.valueOf(localList.get(i))) {
						mixerSettings.add("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
								+ "\nMix Description: " + mix.getDescription());
						mixerID.add(mix.getMixID());
					}
				}

			}		
	
			favoritesListView.setItems(mixerSettings);

			favoritesListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						int index = favoritesListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						setMixerIndetification(identification);
						//String selectedItem = mixListView.getSelectionModel().getSelectedItem(); //POISTETTAVA
						System.out.println(index); //POISTETTAVA
						removeFav.setDisable(false);
					});
			getMixes();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fin.close();
	}
	
	/**
	 * Method to remove favorite settings from the shown list
	 * @throws IOException
	 */
	public void removeFav() throws IOException {
		localList.remove(localList.lastIndexOf(String.valueOf(getMixerIndetification())));
		save();
		mixerSettings.clear();
		read();
		removeFav.setDisable(true);
	}

	/**
	 * Method gets all the mix settings from the database and prints them to a list
	 * view
	 */
	@FXML
	public void getMixes() {
		controller.intializeDatabaseConnection();
		MixerSetting[] setlist = controller.getAllMixArray();
		ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
		ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
		//List<HBoxCell> list = new ArrayList<>();
		for (MixerSetting mix : setlist) {
			myObservableList.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
					+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
			mixerID.add(mix.getMixID());
			mixCretor.add(mix.getCreatorName());
		}
		//myObservableList = FXCollections.observableArrayList(list);
		cloudListView.setItems(myObservableList);

		cloudListView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
					// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
					int index = cloudListView.getSelectionModel().getSelectedIndex();
					int identification = (int) mixerID.get(index);
					String name = (String) mixCretor.get(index);
					// System.out.println("Item selected : " + selectedItem + ", Item index : " +
					// index+", Mixer ID: "+identification);
					System.out.println(index);//POISTETTAVA
					setMixerIndetification(identification);
					setMixerCreatorName(name);
					checkUp();
				});

	}

	/**
	 * Method lets user select any mixer setting from the list view and passes the
	 * selection to MainController class
	 */
	@FXML
	private void selectMIX() {
		controller.intializeDatabaseConnection();
		if (getMixerIndetification() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No input detected!");
			alert.setContentText("Please select one setting from the list.");
			alert.showAndWait();
		} else {
			MixerSetting[] setlist = controller.getAllMixArray();
			double pitch = 0, echo = 0, decay = 0, gain = 0, flangerLenght = 0, wetness = 0, lfoFrequency = 0;
			float lowPass = 0;
			for (MixerSetting mix : setlist) {

				if (mix.getMixID() == getMixerIndetification()) {
					pitch = mix.getPitch();
					echo = mix.getEcho();
					decay = mix.getDecay();
					gain = mix.getGain();
					flangerLenght = mix.getFlangerLenght();
					wetness = mix.getWetness();
					lfoFrequency = mix.getLfoFrequency();
					lowPass = mix.getLowPass();
				}
			}
			// System.out.println(pitch + " " + echo + " " + decay + " " + gain + " " +
			// flangerLenght + " " + wetness + " "+ lfoFrequency + " " + lowPass); //
			// POistettava
			mc.setSliderValues(pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency, lowPass);
			Stage stage = (Stage) closeButton.getScene().getWindow();
			stage.close();
		}

	}

	/**
	 * Method searches the database for any specific mix according to the creator,
	 * mix name or description
	 */
	@FXML
	public void searchMix() {
		controller.intializeDatabaseConnection();
		if (radioCreator.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(1, searchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			myObservableList = FXCollections.observableArrayList(list);
			cloudListView.setItems(myObservableList);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		} else if (radioName.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(2, searchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			myObservableList = FXCollections.observableArrayList(list);
			cloudListView.setItems(myObservableList);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		} else if (radioDescription.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(3, searchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList(); //List to save specific mixer id
			ObservableList<Object> mixCretor = FXCollections.observableArrayList(); //List to save specific mixer creator name
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
				mixCretor.add(mix.getCreatorName());
			}
			myObservableList = FXCollections.observableArrayList(list);
			cloudListView.setItems(myObservableList);

			cloudListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = cloudListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						String name = (String) mixCretor.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
						setMixerCreatorName(name);
						checkUp();
					});
		}
	}
	
	/**
	 * Method checks for the logged in user name and the mixer settings creator name
	 * if a match, user can delete the mixer setting.
	 */
	public void checkUp() {
	
		if (controller.loggedIn().equals(getMixerCreatorName())) {
			deleteMixButton.setVisible(true);
			deleteMixButton.setDisable(false);
		} else {
			deleteMixButton.setDisable(true);
			deleteMixButton.setVisible(false);
		}
	}
	
	/**
	 * Method to delete user created mixer setting from the database  
	 * @throws IOException
	 */
	@FXML
	public void deleteMix() throws IOException {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete mixzer setting?");
		alert.setHeaderText(
				"You are about to permanently delete a mixer settingt!\nMixer setting will be permanentyl deleted and cannot be returned.");
		alert.setContentText("Are you sure you want to delete this mixer setting?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			controller.deleteMix(getMixerCreatorName(), getMixerIndetification());
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Information");
			alert2.setHeaderText("Mixer setting deleted succesfully");
			alert2.showAndWait();
			if (localList.contains(String.valueOf(getMixerIndetification()))) {
				localList.remove(localList.lastIndexOf(String.valueOf(getMixerIndetification())));
			}			
			save();
			getMixes();
			mixerSettings.clear();
			//favoritesListView.getItems().clear();
			read();
			removeFav.setDisable(true);
			deleteMixButton.setDisable(true);
			deleteMixButton.setVisible(false);
		} else {
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle("Error!");
			alert3.setHeaderText("Something went wrong saving mixer settings, please try again");
			alert3.setContentText("If this error continues, please contact support");
			alert3.showAndWait();
		}
	}


	/**
	 * Method to initialize mixer settings window
	 * 
	 * @param mainController
	 * @throws IOException 
	 */
	public void setMainController(MainController mainController) throws IOException {
		this.mc = mainController;
		this.controller = mc.getController();
		//getMixes();
		controller.intializeDatabaseConnection();
		read();
	}
	
	/**
	 * Method is used to close open scenes
	 * 
	 * @param event
	 */
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * Method initializes this classes necessary things
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * getter for the mixer id
	 * 
	 * @return
	 */
	public int getMixerIndetification() {
		return mixerIndetification;
	}

	/**
	 * setter for the mixer id
	 * 
	 * @param mixerIndetification
	 */
	public void setMixerIndetification(int mixerIndetification) {
		this.mixerIndetification = mixerIndetification;
	}
	
	/**
	 * Used to get the mix creators name for deleting purposes
	 * @param name
	 */
	public void setMixerCreatorName(String name) {
		this.mixerCreatorName=name;
	}
	
	/**
	 * Returns the set mix creator name
	 * @return
	 */
	public String getMixerCreatorName() {
		return mixerCreatorName;
	}
	
	/**
	 * Method to reload the scene, possibly will be put into use in the future development.
	 * @throws IOException
	 */
	public void reload() throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MixerSettingsView.fxml")); 
	    Parent root = fxmlLoader.load();
	    Stage stage = (Stage) mainContainer.getScene().getWindow();
	    stage.getScene().setRoot(root);
	    setMainController(mc);
	}

}
