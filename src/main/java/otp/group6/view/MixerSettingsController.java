package otp.group6.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.controller.Controller;

/**
 * 
 * @author Joonas Soininen
 *
 */
public class MixerSettingsController implements Initializable{
	Controller controller;
	MainController mc;
	
	public MixerSettingsController() {
		controller = new Controller();
		mc = new MainController();
	}
	
	@FXML
	private Button closeButton;
	@FXML
	private ListView<String> mixListView;
	@FXML
	private ObservableList<String> mixerSettings;
	
	private int mixerIndetification;
	
	@FXML
	private TextField searchField;
	@FXML
	private RadioButton radioCreator;
	@FXML
	private RadioButton radioName;
	@FXML
	private RadioButton radioDescription;
	
	/**
	 * Method gets all the mix settings from the database and prints them to a list view
	 */
	@FXML
	public void getMixes() {

		MixerSetting[] setlist = controller.getAllMixArray();
		mixerSettings = FXCollections.observableArrayList();
		ObservableList<Object> mixerID = FXCollections.observableArrayList();
		for (MixerSetting mix : setlist) {
			mixerSettings.add("Creator: "+mix.getCreatorName()+"\nMix Name: "+mix.getMixName()+"\nMix Description: "+mix.getDescription());
			mixerID.add(mix.getMixID());
			mixListView.setItems(mixerSettings);
			//mixListView.getSelectionModel().selectFirst();
		}
		
		mixListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) ->{
		    //String selectedItem = mixListView.getSelectionModel().getSelectedItem();	
		    int index = mixListView.getSelectionModel().getSelectedIndex();
		    int identification = (int) mixerID.get(index); 
		    //System.out.println("Item selected : " + selectedItem + ", Item index : " + index+", Mixer ID: "+identification);
		    setMixerIndetification(identification);
		});

		
	}
	
	/**
	 * Method lets user select any mixer setting from the list view and passes the selection to MainController class
	 */
	@FXML 
	private void selectMIX() {
		System.out.println(getMixerIndetification());
		if (getMixerIndetification()==0) {
			JOptionPane.showMessageDialog(null, "Please select one setting from the list.","Alert",JOptionPane.WARNING_MESSAGE); //Onko tämä kaikille ok?
		} else {
			MixerSetting[] setlist = controller.getAllMixArray();
			double set1 = 0, set2=0, set3=0, set4=0, set5=0, set6=0;
			for (MixerSetting mix : setlist) {
				
				if (mix.getMixID()==getMixerIndetification()) {
					set1=mix.getMix1();
					set2=mix.getMix2();
					set3=mix.getMix3();
					set4=mix.getMix4();
					set5=mix.getMix5();
					set6=mix.getMix6();
				}
			}
			System.out.println(set1+" "+set2+" "+set3+" "+set4+" "+set5+" "+set6);
			mc.setSliderValues(set1, set2, set3, set4, set5, set6);
		    Stage stage = (Stage) closeButton.getScene().getWindow();
		    stage.close();
		}

	}
	
	/**
	 * Method searches the database for any specific mix according to the creator, mix name or description
	 */
	@FXML
	public void searchMix() {
		
		if (radioCreator.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(1, searchField.getText());
			mixerSettings = FXCollections.observableArrayList();
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			for (MixerSetting mix : setlist) {
				mixerSettings.add("Creator: "+mix.getCreatorName()+"\nMix Name: "+mix.getMixName()+"\nMix Description: "+mix.getDescription());
				mixerID.add(mix.getMixID());
				mixListView.setItems(mixerSettings);
				//mixListView.getSelectionModel().selectFirst();
			}
			
			mixListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) ->{
			    int index = mixListView.getSelectionModel().getSelectedIndex();
			    int identification = (int) mixerID.get(index); 
			    setMixerIndetification(identification);
			});
		} else if (radioName.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(2, searchField.getText());
			mixerSettings = FXCollections.observableArrayList();
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			for (MixerSetting mix : setlist) {
				mixerSettings.add("Creator: "+mix.getCreatorName()+"\nMix Name: "+mix.getMixName()+"\nMix Description: "+mix.getDescription());
				mixerID.add(mix.getMixID());
				mixListView.setItems(mixerSettings);
				//mixListView.getSelectionModel().selectFirst();
			}
			
			mixListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) ->{
			    int index = mixListView.getSelectionModel().getSelectedIndex();
			    int identification = (int) mixerID.get(index); 
			    setMixerIndetification(identification);
			});
		} else if (radioDescription.isSelected()) {
			MixerSetting[] setlist = controller.getCertainMixesArray(3, searchField.getText());
			mixerSettings = FXCollections.observableArrayList();
			ObservableList<Object> mixerID = FXCollections.observableArrayList();
			for (MixerSetting mix : setlist) {
				mixerSettings.add("Creator: "+mix.getCreatorName()+"\nMix Name: "+mix.getMixName()+"\nMix Description: "+mix.getDescription());
				mixerID.add(mix.getMixID());
				mixListView.setItems(mixerSettings);
				//mixListView.getSelectionModel().selectFirst();
			}
			
			mixListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) ->{
			    int index = mixListView.getSelectionModel().getSelectedIndex();
			    int identification = (int) mixerID.get(index); 
			    setMixerIndetification(identification);
			});
		}
	}
	
	/**
	 * Method is used to close open scenes
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
		getMixes();
	}

	/**
	 * getter for the mixer id
	 * @return
	 */
	public int getMixerIndetification() {
		return mixerIndetification;
	}

	/**
	 * setter for the mixer id
	 * @param mixerIndetification
	 */
	public void setMixerIndetification(int mixerIndetification) {
		this.mixerIndetification = mixerIndetification;
	}
	
}
