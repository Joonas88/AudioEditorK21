package otp.group6.AudioEditor;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;

import java.io.IOException;

/**
 * Audio player class for soundboard
 * @author Kevin Akkoyun
 *
 */
public class SoundboardPlayer {
	Clip clip;
	
	public SoundboardPlayer() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Plays audio with given AudioInputStream
	 * @param sample AudioInputStream of file to be played
	 */
	public void playAudio(AudioInputStream sample) {
		closeAudio();
		try {
			clip.open(sample);
			clip.loop(0);
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Closes clip if open
	 */
	public void closeAudio() {
		if (clip.isOpen()) {
			clip.close();
		}
	}
	public boolean isAlive() {
		return clip.isActive();
	}
	public void setOnClose(LineListener listener) {
		clip.addLineListener(listener);
	}
	public void removeListeners(LineListener listener) {
		clip.removeLineListener(listener);
	}
}