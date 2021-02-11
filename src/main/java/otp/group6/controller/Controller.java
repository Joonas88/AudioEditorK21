package otp.group6.controller;

import java.io.File;

import otp.group6.AudioEditor.AudioMuunnin;
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

	//-
	private AudioMuunnin soundManipulator;	
	//-
	

	public Controller() {
		initialConfig();
	}

	public void initialConfig() {
		soundboard = new Soundboard();
		mainPlayer = new AudioOutput();
		recorder = new AudioRecorder();
		//-
		soundManipulator = new AudioMuunnin();
		soundManipulator.setAudioSourceFile("src/audio/testiaani.wav");
		//-
	}

	
	// SoundManipulator methods start
	public void soundManipulatorAdjustParameters(float newPitch, double echoLength, double decay, double gain) {
		if (newPitch != -1) {
		soundManipulator.setPitchFactor(newPitch);
		}
		
		if (echoLength != -1) {
			soundManipulator.setDelayEffect(echoLength, -1);
		}
		
		if (decay != -1) {
			soundManipulator.setDelayEffect(-1, decay);
		}
		
		if (gain != -1) {
			soundManipulator.setGain(gain);
		}
	}
	
	public void soundManipulatorPlayAudio() {
		soundManipulator.playAudio();
	}
	
	public void soundManipulatorSaveFile() {
		soundManipulator.saveFile();
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

	public void removeSample(int index) {
		if (soundboard.checkSampleArray(index)) {
			soundboard.removeSample(index);
		} else {
			System.out.println("index not found - sample array: " + index);
		}
	}

	public int getSampleArrayLength() {
		return soundboard.getSampleArrayLength();
	}
	// Soundboard methods stop

	// AudioRecorder methods start
	public void recordAudio(String file_name) {
		try {
			recorder.setTargetFile(new File("src/audio/" + file_name + ".wav").getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		recorder.start();
	}

	public void stopRecord() {
		try {
			recorder.stopRecord();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// AudioRecorder methods stop
}
