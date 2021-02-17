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
	public void soundManipulatorSetLowPass(double lowPass) {
		//TODO
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
	
	public void soundManipulatorSaveFile() {
		soundManipulator.saveFile();
	}
	
	public void soundManipulatorResetAllSliders() {
		
	}
	public void testFilter() {
		soundManipulator.testFilter();
		
	}
	
	// SoundManipulator methods end
	
	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// AudioRecorder methods stop

	
}
