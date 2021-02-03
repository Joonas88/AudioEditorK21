package otp.group6.AudioEditor;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
/**
 * Soundboard class for storing and playing sampled audio
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 */
public class Soundboard {
	/**
	 * Class for handling playable audio files as objects
	 * @author Kevin Akkoyun, Joonas Soininen
	 * @version 0.1
	 */
	public class Sample {
		
		private AudioInputStream file;
		private String filepath;
		/**
		 * 
		 * TODO lisää error handling
		 */
		public Sample(String filepath) {
			this.filepath = filepath;
		}
		/**
		 * Uses AudioFileHandler to open a new audio file into AudioInputStream
		 * Closes existing AudioInputStream
		 * @return New AudioInputStream with samples specified file path
		 */
		public AudioInputStream getSample() {
			try {
				if(file != null) {
					file.close();
				}
				file = AudioFileHandler.OpenFile(filepath);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return file;
		}
	}

	private AudioPlayer player;
	private ArrayList<Sample> sampleArray = new ArrayList<Sample>();
	
	public int getSampleArrayLength() {
		return sampleArray.size();
	}
	
	public Soundboard() {
		player = new AudioPlayer();
	}
	public void addSample(String path) {
		sampleArray.add(new Sample(path));
	}
	public void removeSample(int sampleIndex) {
		sampleArray.remove(sampleIndex);
	}
	public void playSample(int sampleIndex) {
		try {
			player.openAudio(sampleArray.get(sampleIndex).getSample());
			player.play();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void closeSample() {
		player.pause();
		player.closeAudio();
	}
	public boolean isPlaying() {
		return player.isPlaying();
	}
}
