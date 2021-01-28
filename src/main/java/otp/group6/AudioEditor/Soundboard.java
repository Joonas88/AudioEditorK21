package otp.group6.AudioEditor;

import java.util.ArrayList;
/**
 * @author Kevin Akkoyun, Joonas Soininen
 */
public class Soundboard {
	
	private AudioPlayer player;
	private ArrayList<Sample> sampleArray = new ArrayList<Sample>();
	
	public Soundboard() {
		player = new AudioPlayer();
	}
	public void addSample(Sample sample) {
		sampleArray.add(sample);
	}
	public void removeSample(int sampleIndex) {
		sampleArray.remove(sampleIndex);
	}
	public void playSample(int sampleIndex) {
			player.openAudio(sampleArray.get(sampleIndex).getSample());
			player.play();
	}
	public void closeSample() {
		player.pause();
		player.closeAudio();
	}
}
