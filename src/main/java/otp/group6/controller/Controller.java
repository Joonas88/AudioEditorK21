package otp.group6.controller;

import java.io.File;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import otp.group6.AudioEditor.AudioCloudDAO;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioFileHandler;
import otp.group6.AudioEditor.AudioOutput;
import otp.group6.AudioEditor.AudioRecorder;
import otp.group6.AudioEditor.Soundboard;
import otp.group6.AudioEditor.Soundboard.Sample;
/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 *
 */
public class Controller {
	
	private Soundboard soundboard;
	private AudioOutput mainPlayer;
	private AudioRecorder recorder;
	private AudioCloudDAO audioDAO;
	
	public Controller() {
		initialConfig();
	}
	
	public void initialConfig() {
		soundboard = new Soundboard();
		mainPlayer = new AudioOutput();
		recorder = new AudioRecorder();
		audioDAO = new AudioCloudDAO();
	}
	
	

	// Soundboard methods start
	// TODO MainController tarvitsee try/catch-lohkon tätä metodia käyttäessä myös!
	public void playSound(int index) {
		soundboard.playSample(index);
	}
	//TODO STOPSOUND metodi
	
	public void addSample(String path) {
		try {
		soundboard.addSample(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// TODO MainController tarvitsee try/catch-lohkon tätä metodia käyttäessä myös!
	public void removeSample(int index) {
		try {
		soundboard.removeSample(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int getSampleArrayLength() {
		return soundboard.getSampleArrayLength();
	}
	//Soundboard methods stop
	
	// AudioRecorder methods start
	//TODO vaihda tiedoston tallennuksen tapa
	public void recordAudio(String file_name) {
		try {
			recorder.setTargetFile(new File("src/audio/" + file_name + ".wav").getAbsoluteFile());
		}catch(Exception e) {
			e.printStackTrace();
		}
		recorder.start();
	}
	
	public void stopRecord() {
		try {
		recorder.stopRecord();
		getRecordedAudio(recorder.getTargetFile().getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getRecordedAudio (String file_name) {
		try {
			mainPlayer.openAudio(AudioFileHandler.OpenFile(file_name));
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	public void playRecordedAudio () {
		try {
			mainPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pauseRecordedAudio () {
		try {
			mainPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resumeRecordedAudio () {
		try {
			mainPlayer.resume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rewindRecordedAudio () {
		try {
			mainPlayer.rewind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void forwardRecordedAudio () {
		try {
			mainPlayer.forward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecordedAudio () {
		try {
			mainPlayer.closeAudio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// AudioRecorder methods stop
	
	//TODO hävitä nämä ennen puskua!!!!!
	
	//AudioCloudDAO methods start
	public boolean chekcforUser(String user) {
		return audioDAO.chekcforUser(user);
	}

	public boolean createUser(String user, String pw) throws SQLException {
		return audioDAO.createUser(user, pw);
	}

	public String loginUser(String u, String p) {
		return audioDAO.loginUser(u, p);
	}

	public boolean logoutUser() {
		return audioDAO.logoutUser();
	}

	public boolean createMix(String mixName, String description, double mix1, double mix2, double mix3, double mix4,
			double mix5, double mix6) throws SQLException {
		return audioDAO.createMix(mixName, description, mix1, mix2, mix3, mix4, mix5, mix6);
	}

	public MixerSetting[] getAllMixArray() {
		return audioDAO.getAllMixArray();
	}

	public JSONObject getAllMixJSON() {
		return audioDAO.getAllMixJSON();
	}

	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		return audioDAO.getCertainMixesArray(select, specify);
	}

	public JSONObject getCertainMixesJSON(int select, String specify) {
		return audioDAO.getCertainMixesJSON(select, specify);
	}

	public boolean deleteMix(String specify) {
		return audioDAO.deleteMix(specify);
	}

	public boolean deleteUser() {
		return audioDAO.deleteUser();
	}

	public String toString() {
		return audioDAO.toString();
	}
	
	public String loggedIn() {
		return audioDAO.loggedIn();
	}
	//AudioCloudDAO methods stop
}
