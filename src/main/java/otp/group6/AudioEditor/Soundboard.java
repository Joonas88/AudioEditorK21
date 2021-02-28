package otp.group6.AudioEditor;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

/**
 * Soundboard class for storing and playing sampled audio
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 */
public class Soundboard {
	/**
	 * Class for handling playable audio files as objects
	 * 
	 * @author Kevin Akkoyun, Joonas Soininen
	 * @version 0.1
	 */
	public class Sample {

		private AudioInputStream file;
		private String filepath;
		private String name;

		/**
		 * 
		 * TODO lisää error handling
		 */
		public Sample(String filepath) {
			this.filepath = filepath;
			this.name = "New Sound(" + sampleArray.size() + ")";
		}
		public void setSamplePath(String filepath) {
			this.filepath = filepath;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return this.name;
		}

		/**
		 * Uses AudioFileHandler to open a new audio file into AudioInputStream Closes
		 * existing AudioInputStream
		 * 
		 * @return New AudioInputStream with samples specified file path
		 */
		public AudioInputStream getSample() {
			try {
				if (file != null) {
					file.close();
				}
				file = AudioFileHandler.OpenFile(filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return file;
		}
	}

	private AudioOutput player;

	private ArrayList<Sample> sampleArray = new ArrayList<Sample>();

	/**
	 * Checks if sampleArray contains specified sample
	 * 
	 * @param sampleIndex index of the sample
	 * @return returns true if array contains a sample with given index
	 */
	public boolean checkSampleArray(int sampleIndex) {
		if (sampleArray.get(sampleIndex) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the length of sample array
	 * 
	 * @return length of the array as integer
	 */
	public int getSampleArrayLength() {
		return sampleArray.size();
	}

	public Soundboard() {
		player = null;
	}

	/**
	 * Add sample via file path
	 * 
	 * @param path Filepath as String
	 */
	public void addSample(String path) {
		sampleArray.add(new Sample(path));
	}
	public void editSample(String path, int index) {
		sampleArray.get(index).setSamplePath(path);
	}
	public String getSampleName(int index) {
		return sampleArray.get(index).getName();
	}
	public void setSampleName(int index, String name) {
		sampleArray.get(index).setName(name);
	}

	/**
	 * Removes sample with a given index value from the sample array
	 * 
	 * @param sampleIndex
	 */
	public void removeSample(int sampleIndex) {
		sampleArray.remove(sampleIndex);
		//update button positions
	}

	/**
	 * Plays a sample with AudioOutput
	 * 
	 * @param sampleIndex
	 */
	public void playSample(int sampleIndex) {
		if (player == null || !player.isAlive()) {
			player = new AudioOutput();
			player.openAudio(sampleArray.get(sampleIndex).getSample());
			player.start();
		} else {
			player.closeAudio();
			player = new AudioOutput();
			player.openAudio(sampleArray.get(sampleIndex).getSample());
			player.start();
		}

	}
}
