package otp.group6.controller;

import java.io.File;
import java.sql.SQLException;

import javax.sound.sampled.LineListener;

import otp.group6.AudioEditor.AudioCloudDAO;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioManipulator;
import otp.group6.AudioEditor.AudioRecorder;
import otp.group6.AudioEditor.Soundboard;
import otp.group6.view.MainController;

/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 *
 */
public class Controller {

	private Soundboard soundboard;
	private AudioRecorder recorder;
	private AudioManipulator audioManipulator;
	private AudioCloudDAO audioDAO;
	private MainController mainController;

	public Controller(MainController mainController) {
		this.mainController = mainController;
		initialConfig();
	}

	public void initialConfig() {
		soundboard = new Soundboard();
		recorder = new AudioRecorder(this);
		audioManipulator = new AudioManipulator(this);
	}

	// SoundManipulator methods start

	// FROM VIEW TO SOUNDMANIPULATOR

	// SoundManipulator parameter setters
	// Pitch
	public void audioManipulatorSetPitchFactor(double pitch) {
		audioManipulator.setPitchFactor(pitch);
	}

	// Gain
	public void audioManipulatorSetGain(double gain) {
		audioManipulator.setGain(gain);
	}

	// Echo length
	public void audioManipulatorSetEchoLength(double echoLength) {
		audioManipulator.setEchoLength(echoLength);
	}

	// Decay
	public void audioManipulatorSetDecay(double decay) {
		audioManipulator.setDecay(decay);
	}

	// Flanger length
	public void audioManipulatorSetFlangerLength(double flangerLength) {
		audioManipulator.setFlangerLength(flangerLength);
	}

	// Flanger wetness
	public void audioManipulatorSetWetness(double wetness) {
		audioManipulator.setWetness(wetness);
	}

	// LFO
	public void audioManipulatorSetLFO(double lfo) {
		audioManipulator.setLFO(lfo);
	}

	// LowPass
	public void audioManipulatorSetLowPass(float lowPass) {
		audioManipulator.setLowPass(lowPass);
	}

	// GENERAL MIXER METHODS
	public void audioManipulatorOpenFile(File file) {
		audioManipulator.setAudioSourceFile(file);
	}
	
	public void audioManipulatorOpenRecordedFile() {
		mainController.audioManipulatorOpenRecordedFile();
	}
	
	public void audioManipulatorStartRecord() {
		audioManipulator.recordAudio();
	}
	
	public void audioManipulatorStopRecord() {
		audioManipulator.stopRecord();
	}

	public void audioManipulatorPlayAudio() {
		audioManipulator.playAudio();
	}

	public void audioManipulatorStopAudio() {
		audioManipulator.stopAudio();
	}

	public void audioManipulatorPauseAudio() {
		audioManipulator.pauseAudio();
	}

	public void audioManipulatorPlayFromDesiredSec(double seconds) {
		audioManipulator.playFromDesiredSec(seconds);
	}

	public void audioManipulatorSaveFile(String path) {
		audioManipulator.saveFile(path);
	}

	public void testFilter() {
		audioManipulator.testFilter();
	}
	
	public void setAudioFileLengthInSec(double audioFileLengthInSec) {
		audioManipulator.setAudioFileLengthInSec(audioFileLengthInSec);
		
	}

	public void audioManipulatorResetMediaPlayer() {
		audioManipulator.resetMediaPlayer();
	}
	
	public void audioManipulatorAudioFileReachedEnd() {
		mainController.audioManipulatorAudioFileReachedEnd();
	}

	public void timerCancel() {
		audioManipulator.timerCancel();
	}

	public void audioManipulatorUsePitchProcessor(boolean trueOrFalse) {
		audioManipulator.usePitchProcessor(trueOrFalse);

	}

	public void audioManipulatorUseDelayProcessor(boolean trueOrFalse) {
		audioManipulator.useDelayProcessor(trueOrFalse);

	}

	public void audioManipulatorUseGainProcessor(boolean trueOrFalse) {
		audioManipulator.useGainProcessor(trueOrFalse);

	}

	public void audioManipulatorUseFlangerProcessor(boolean trueOrFalse) {
		audioManipulator.useFlangerProcessor(trueOrFalse);

	}

	public void audioManipulatorUseLowPassProcessor(boolean trueOrFalse) {
		audioManipulator.useLowPassProcessor(trueOrFalse);

	}

	// FROM SOUNDMANIPULATOR TO VIEW
	// Audio file sliderin metodit
	/**
	 * Sets max value to audio duration slider in MainController
	 * 
	 * @param maxLenghthInSeconds
	 */
	public void setMaxValueToAudioDurationSlider(double maxLenghthInSeconds) {
		mainController.setCurrentValueToAudioDurationSlider(maxLenghthInSeconds);
	}

	/**
	 * Sets current position to the audio duration slider
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentValueToAudioDurationSlider(double currentSeconds) {
		mainController.setCurrentValueToAudioDurationSlider(currentSeconds);
	}

	/**
	 * Sets the current progress of the song in audio duration text
	 * 
	 * @param currentSeconds
	 */
	public void setCurrentPositionToAudioDurationText(double currentSeconds) {
		mainController.setCurrentValueToAudioDurationText(currentSeconds);
	}

	public void setDisableMixerSliders(boolean trueOrFalse) {
		mainController.setDisableMixerSliders(trueOrFalse);
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

	public void stopSound() {
		soundboard.stopSample();
	}

	public boolean isPlaying() {
		return soundboard.isPlaying();
	}

	/**
	 * Adds sample to Soundboard SampleArray
	 * 
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

	public void saveSampleData() {
		soundboard.saveSampleData();
	}

	public void readSampleData() {
		soundboard.readSampleData();
	}

	public void clearSampleData() {
		soundboard.clearSampleData();
	}
	public void setListener(LineListener listener) {
		soundboard.setListener(listener);
	}
	public void removeListener(LineListener listener) {
		soundboard.removeListener(listener);
	}
	// Soundboard methods stop -------------------------

	// AudioRecorder methods start
	public void recordAudio() {
		recorder.recordAudio();
	}

	public void stopRecord() {
		recorder.stopRecord();
	}

	public void audioRecorderPlayAudio() {
		recorder.playAudio();
	}

	public void audioRecorderStopAudio() {
		recorder.stopAudio();
	}

	public void audioRecorderPauseAudio() {
		recorder.pauseAudio();
	}

	public void recorderPlayFromDesiredSec(double seconds) {
		recorder.playFromDesiredSec(seconds);
	}

	public void recorderTimerCancel() {
		recorder.timerCancel();
	}

	public void recorderSliderPressed() {
		recorder.recorderSliderPressed();
	}

	public void saveAudioFile(String path) {
		recorder.saveAudioFile(path);
	}

	public void setCurrentValueToRecordFileDurationSlider(Double currentSeconds) {
		mainController.setCurrentValueToRecordDurationSlider(currentSeconds);
	}
	
	public void recorderSetAudioFileDuration(float audioFileDuration) {
		recorder.setAudioFileDuration(audioFileDuration);
	}
	
	public void recorderAudioFileReachedEnd() {
		mainController.recorderAudioFileReachedEnd();
	}

	// AudioRecorder methods stop

	// AudioCloudDAO methods start
	public void intializeDatabaseConnection() {
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

	public boolean createMix(String mixName, String description, double pitch, double echo, double decay, double gain,
			double flangerLenght, double wetness, double lfoFrequency, float lowPass) throws SQLException {
		return audioDAO.createMix(mixName, description, pitch, echo, decay, gain, flangerLenght, wetness, lfoFrequency,
				lowPass);
	}

	public MixerSetting[] getAllMixArray() {
		return audioDAO.getAllMixArray();
	}

	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		return audioDAO.getCertainMixesArray(select, specify);
	}

	public boolean deleteMix(String name, int id) {
		return audioDAO.deleteMix(name, id);
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

	public boolean changePW(String u, String p, String np) {
		return audioDAO.changePassword(u, p, np);
	}
	
	public boolean isConnected() {
		return audioDAO.isHasconnected();
	}
	// AudioCloudDAO methods stop

	
}
