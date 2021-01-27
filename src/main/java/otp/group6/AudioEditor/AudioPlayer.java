package otp.group6.AudioEditor;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class AudioPlayer {
	@SuppressWarnings("unused")
	private Clip clip;
	
	public AudioPlayer() {
		
		try {
			clip=AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void openAudio(AudioInputStream audio) {
		if (!clip.isOpen()) {
			try {
				clip.open(audio);
			}  catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			clip.close();
			try {
				clip.open(audio);
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void play() {
		
		if(clip.isOpen()) {
			clip.start();	
		} else {
			System.out.println("No file selected!");
		}
				
	}
	
	
	public void pause() {
		if(clip.isOpen()&&clip.isRunning()) {
			clip.stop();	
		} else {
			System.out.println("Can't pause the track!");
		}
	}
	
	public void resume() {
		if(clip.isOpen()&&!clip.isRunning()) {
			clip.start();	
		} else {
			System.out.println("Can't resume the track!");
		}
	}
	
	//TODO metodit taaksepäin ja eteenpäin kelaukselle!!
}
