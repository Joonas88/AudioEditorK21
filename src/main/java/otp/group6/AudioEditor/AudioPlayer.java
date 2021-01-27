package otp.group6.AudioEditor;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class AudioPlayer {
	@SuppressWarnings("unused")
	private Clip clip;
	private long framePosition;
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
				clip.loop(0);
			}  catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			clip.close();
			try {
				clip.open(audio);
				clip.loop(0);
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
	public void forward() {
		if(clip.isOpen() && clip.getMicrosecondPosition() + 3000000l < clip.getMicrosecondLength()) {
			System.out.println(clip.getMicrosecondPosition());
			clip.setMicrosecondPosition(clip.getMicrosecondPosition() + 3000000l);
		}else {
			System.out.println("cant fast-forward");
		}
	}
	public void rewind() {
		if(clip.isOpen() && clip.getMicrosecondPosition() - 3000000l > 0l) {
			clip.setMicrosecondPosition(clip.getMicrosecondPosition() - 3000000l);
		}else {
			clip.setMicrosecondPosition(0l);
		}
	}
}
