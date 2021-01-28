package otp.group6.controller;

import otp.group6.AudioEditor.AudioPlayer;
import otp.group6.AudioEditor.AudioRecorder;
import otp.group6.AudioEditor.Soundboard;
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
	}
	public void intialConfig() {
		
	}
}
