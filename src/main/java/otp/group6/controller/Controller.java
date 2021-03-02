package otp.group6.controller;

import java.io.File;
import java.sql.SQLException;
import otp.group6.AudioEditor.AudioCloudDAO;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioMuunnin;
import otp.group6.AudioEditor.AudioRecorder;
import otp.group6.AudioEditor.Soundboard;
/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 *
 */
public class Controller {

	private Soundboard soundboard;
	private AudioRecorder recorder;
	private AudioMuunnin soundManipulator;	
	private AudioCloudDAO audioDAO;

	public Controller() {
		initialConfig();
	}

	public void initialConfig() {
		soundboard = new Soundboard();
		recorder = new AudioRecorder();
		soundManipulator = new AudioMuunnin();
	}

	
	
	// SoundManipulator methods start

	//SoundManipulator parameter setters
	//Pitch
	public void soundManipulatorSetPitchFactor(double pitch) {
		soundManipulator.setPitchFactor(pitch);	
	}	
	//Gain
	public void soundManipulatorSetGain(double gain) {
		soundManipulator.setGain(gain);
	}
	//Echo length
	public void soundManipulatorSetEchoLength(double echoLength) {
		soundManipulator.setEchoLength(echoLength);
	}
	//Decay
	public void soundManipulatorSetDecay(double decay) {
		soundManipulator.setDecay(decay);
	}
	//Flanger length	
	public void soundManipulatorSetFlangerLength(double flangerLength) {
		soundManipulator.setFlangerLength(flangerLength);
	}
	//Flanger wetness
	public void soundManipulatorSetWetness(double wetness) {
		soundManipulator.setWetness(wetness);
	}
	//LFO
	public void soundManipulatorSetLFO(double lfo) {
		soundManipulator.setLFO(lfo);
	}
	public void soundManipulatorSetLowPass(float lowPass) {
		soundManipulator.setLowPass(lowPass);
	}
	
	//Mixer general methods
	public void soundManipulatorOpenFile(File file) {
		soundManipulator.setAudioSourceFile(file);
	}
	
	public void soundManipulatorPlayAudio() {
		soundManipulator.playAudio();
	}
	
	public void soundManipulatorStopAudio() {
		soundManipulator.stopAudio();
	}
	
	public void soundManipulatorPauseAudio() {
		soundManipulator.pauseAudio();
	}
	
	public void soundManipulatorSaveFile() {
		soundManipulator.saveFile();
	}
	
	public void testFilter() {
		soundManipulator.testFilter();
		
	}
	
	// SoundManipulator methods end
	
	// Soundboard methods start
	// TODO MainController tarvitsee try/catch-lohkon tätä metodia käyttäessä myös!
	public void playSound(int index) {
		if (soundboard.checkSampleArray(index)) {
			soundboard.playSample(index);
		} else {
			System.out.println("index not found - sample array: " + index);
		}

	}
	
	/**
	 * Adds sample to Soundboard SampleArray
	 * @param path filepath
	 */
	public void addSample(String path) {
		try {
			soundboard.addSample(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void editSample(String path, int index) {
		soundboard.editSample(path, index);
	}

	public void removeSample(int index) {
		if (soundboard.checkSampleArray(index)) {
			soundboard.removeSample(index);
		} else {
			System.out.println("index not found - sample array: " + index);
		}
	}
	public String getSampleName(int index) {
		return soundboard.getSampleName(index);
	}
	public void setSampleName(int index, String name) {
		soundboard.setSampleName(index, name);
	}

	public int getSampleArrayLength() {
		return soundboard.getSampleArrayLength();
	}
	// Soundboard methods stop

	// AudioRecorder methods start
	boolean isRecording = false;
	public void recordAudioToggle() {
		if(!isRecording) {
			recorder.recordAudio();
			isRecording = true;
		}else {
			recorder.stopRecord();
			isRecording = false;
		}
		
	}
	
	// AudioRecorder methods stop
	public void stopRecord() {
		try {
			recorder.stopRecord();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// AudioRecorder methods stop
	
	//AudioCloudDAO methods start
	public void intializeDatabase() {
		audioDAO = new AudioCloudDAO();
	}
	
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

	public boolean createMix(String mixName, String description, double pitch, double echo, double decay, double gain, double flangerLenght,
			double wetness, double lfoFrequency, float lowPass) throws SQLException {
		return audioDAO.createMix(mixName, description, pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency, lowPass);
	}

	public MixerSetting[] getAllMixArray() {
		return audioDAO.getAllMixArray();
	}

	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		return audioDAO.getCertainMixesArray(select, specify);
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
