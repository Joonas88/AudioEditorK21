package otp.group6.controller;

import java.io.File;

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
	
	public Controller() {
		initialConfig();
	}
	
	public void initialConfig() {
		soundboard = new Soundboard();
		mainPlayer = new AudioOutput();
		recorder = new AudioRecorder();
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
}
