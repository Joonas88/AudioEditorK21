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
		soundboard = new Soundboard();
		mainPlayer = new AudioPlayer();
		recorder = new AudioRecorder();
		
		//dev test - Remove from final
		soundboard.addSample(soundboard.new Sample("src/audio/test3.wav"));
		soundboard.addSample(soundboard.new Sample("src/audio/testi5.wav"));
		soundboard.addSample(soundboard.new Sample("src/audio/test5.wav"));
		soundboard.addSample(soundboard.new Sample("src/audio/jumalauta.wav"));
	}
	public void intialConfig() {
		
	}
	
	public void playSound(int index) {
		soundboard.playSample(index);
	}
	
	public void recordAudio(String file_name) {
		try {
			recorder.setTargetFile(new File("src/audio/" + file_name + ".wav").getAbsoluteFile());
		}catch(Exception e) {
			e.printStackTrace();
		}
		recorder.start();
	}
	
	public void stopRecord() {
		recorder.stopRecord();
	}
	public int getSampleAmount() {
		return soundboard.getSampleAmount();
	}
}
