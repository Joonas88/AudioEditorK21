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
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.controller.Controller;

/**
 * Class handles database stored mixer settings, displaying them, adding them to favorites and storing the favorites list locally.
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
	private ListView<String> mixListView;
	@FXML
	private ObservableList<String> mixerSettings;

	private int mixerIndetification;

	private List<String> hlist = new ArrayList<>();

	private List<String> localList = new ArrayList<>();

	@FXML
	private ListView<HBoxCell> listView;
	@FXML
	private ObservableList<HBoxCell> myObservableList;

	@FXML
	private TextField searchField;
	@FXML
	private RadioButton radioCreator;
	@FXML
	private RadioButton radioName;
	@FXML
	private RadioButton radioDescription;

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
				favoriteButton(button.getId(), labelText);
				button.setDisable(true);
			});

			this.getChildren().addAll(label, button);
		}
	}
	
	/**
	 * TODO Metodi suosikkien tallentamiseen tietokantaan puuttuu
	 * TODO Metodi suosikin poistamiseen puuttuu
	 */
	
	/**
	 * Method to set the favorite buttons function
	 * 
	 * @param id
	 * @param title
	 */
	public void favoriteButton(String id, String title) {
		ObservableList<Object> mixerID = FXCollections.observableArrayList();
		hlist.add(title);
		localList.add(id);
		mixerID.add(Integer.valueOf(id));
		mixerSettings = FXCollections.observableArrayList(hlist);
		mixListView.setItems(mixerSettings);

		mixListView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					int index = mixListView.getSelectionModel().getSelectedIndex();
					int identification = (int) mixerID.get(index);
					setMixerIndetification(identification);
				});

	}

	/**
	 * Method to save locally users favorite mixer settings
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
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

		}
	}

	/**
	 * Method to load from local storage users favorite mixer settings
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	public void read() throws IOException {
		//TODO Varmista oikea tiedostomuoto
		File file = AudioFileHandler.openFileExplorer(mainContainer.getScene().getWindow());
		FileInputStream fin = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fin);
		try {
			localList = (ArrayList<String>) ois.readObject();
			//System.out.println(localList);
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			MixerSetting[] setlist = controller.getAllMixArray();
			for (MixerSetting mix : setlist) {
				for (int i = 0; i < localList.size(); i++) {
					if (mix.getMixID() == Integer.valueOf(localList.get(i))) {
						hlist.add("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
								+ "\nMix Description: " + mix.getDescription());
						mixerID.add(mix.getMixID());
					}
				}

			}
			mixerSettings = FXCollections.observableArrayList(hlist);
			mixListView.setItems(mixerSettings);

			mixListView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
						int index = mixListView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						setMixerIndetification(identification);
					});
			getMixes();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fin.close();
	}

	/**
	 * Method gets all the mix settings from the database and prints them to a list
	 * view
	 */
	@FXML
	public void getMixes() {
		controller.intializeDatabaseConnection();
		MixerSetting[] setlist = controller.getAllMixArray();
		ObservableList<Object> mixerID = FXCollections.observableArrayList();
		List<HBoxCell> list = new ArrayList<>();
		for (MixerSetting mix : setlist) {
			list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
					+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
			mixerID.add(mix.getMixID());
		}
		myObservableList = FXCollections.observableArrayList(list);
		listView.setItems(myObservableList);

		listView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
					// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
					int index = listView.getSelectionModel().getSelectedIndex();
					int identification = (int) mixerID.get(index);
					// System.out.println("Item selected : " + selectedItem + ", Item index : " +
					// index+", Mixer ID: "+identification);
					setMixerIndetification(identification);
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
			//System.out.println(pitch + " " + echo + " " + decay + " " + gain + " " + flangerLenght + " " + wetness + " "+ lfoFrequency + " " + lowPass); // POistettava
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
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
			}
			myObservableList = FXCollections.observableArrayList(list);
			listView.setItems(myObservableList);

			listView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = listView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
					});
		} else if (radioName.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(2, searchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
			}
			myObservableList = FXCollections.observableArrayList(list);
			listView.setItems(myObservableList);

			listView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = listView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
					});
		} else if (radioDescription.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(3, searchField.getText());
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			List<HBoxCell> list = new ArrayList<>();
			for (MixerSetting mix : setlist) {
				list.add(new HBoxCell("Creator: " + mix.getCreatorName() + "\nMix Name: " + mix.getMixName()
						+ "\nMix Description: " + mix.getDescription(), "STAR", mix.getMixID()));
				mixerID.add(mix.getMixID());
			}
			myObservableList = FXCollections.observableArrayList(list);
			listView.setItems(myObservableList);

			listView.getSelectionModel().selectedItemProperty()
					.addListener((ObservableValue<? extends HBoxCell> ov, HBoxCell old_val, HBoxCell new_val) -> {
						// String selectedItem = mixListView.getSelectionModel().getSelectedItem();
						int index = listView.getSelectionModel().getSelectedIndex();
						int identification = (int) mixerID.get(index);
						// System.out.println("Item selected : " + selectedItem + ", Item index : " +
						// index+", Mixer ID: "+identification);
						setMixerIndetification(identification);
					});
		}
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
	 * Method to initialize mixer settings window
	 * 
	 * @param mainController
	 */
	public void setMainController(MainController mainController) {
		this.mc = mainController;
		this.controller = mc.getController();
		getMixes();
	}

}
