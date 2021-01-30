package otp.group6.AudioEditor;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 *
 */

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
	//HUOM! MUISTA LUODA UUSI AUDIO INPUT STREAM OLIO AINA KUN TOISTAT TIEDOSTOA UUDESTAAN
	// ^ paitsi jos et sulje klippiä. Saman audioInputStreamin syöttö uudestaan klipille EI TOIMI
	/**
	 * 
	 * @param audio A new AudioInputStream
	 */
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
	
	public void forward() {
		if(clip.isOpen() && clip.getMicrosecondPosition() + 3000000l < clip.getMicrosecondLength()) {			
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
	
	public void closeAudio() {
		if(clip.isOpen()) {			
			clip.close();
		} else {
			System.out.println("No audiofile to close!");
		}
	}
	public boolean isPlaying() {
		if(clip.isActive()) {
			return true;
		}
		return false;
	}
}
