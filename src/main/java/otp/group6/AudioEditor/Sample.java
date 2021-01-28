package otp.group6.AudioEditor;

import javax.sound.sampled.AudioInputStream;
/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 *
 */
public class Sample {
	
	private AudioInputStream file;
	/**
	 * 
	 * TODO lisää error handling
	 */
	public Sample(AudioInputStream ais) {
		file = ais;
	}
	public AudioInputStream getSample() {
		return file;
	}
}
