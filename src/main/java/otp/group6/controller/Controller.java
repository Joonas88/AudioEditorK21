package otp.group6.controller;

import java.io.File;

import otp.group6.AudioEditor.AudioPlayer;
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
	private AudioPlayer mainPlayer;
	private AudioRecorder recorder;
	
	public Controller() {
		initialConfig();
	}
	
	public void initialConfig() {
		soundboard = new Soundboard();
		mainPlayer = new AudioPlayer();
		recorder = new AudioRecorder();
	}
	
	// Soundboard methods start
	// TODO MainController tarvitsee try/catch-lohkon tätä metodia käyttäessä myös!
	public void playSound(int index) {
		soundboard.playSample(index);
	}
	
	public void stopSound() {
		try {
		if (soundboard.isPlaying()) {
			soundboard.closeSample();	
		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
