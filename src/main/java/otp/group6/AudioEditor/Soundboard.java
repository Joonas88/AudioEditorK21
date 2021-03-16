package otp.group6.AudioEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineListener;

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

		public Sample(String name, String filepath) {
			this.filepath = filepath;
			this.name = name;
		}

		public void setSamplePath(String filepath) {
			this.filepath = filepath;
		}

		public String getSamplePath() {
			return this.filepath;
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

	private String sampleData = "";

	private SoundboardPlayer player;

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
		player = new SoundboardPlayer();
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
		// update button positions
	}

	/**
	 * Plays a sample with SoundboardPlayer
	 * 
	 * @param sampleIndex
	 */
	public void playSample(int sampleIndex) {
		player.playAudio(sampleArray.get(sampleIndex).getSample());
	}

	/**
	 * @author Kevin Akkoyun Stops the sample output and closes audio
	 */
	public void stopSample() {
		if (player != null || !player.isAlive()) {
			player.closeAudio();
		}
	}

	/**
	 * @author Kevin Akkoyun Function to determine if player is active
	 * @return returns true if player is active; otherwise returns false if player
	 *         is null or not playing
	 */
	public boolean isPlaying() {
		if (player == null || !player.isAlive()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @author Kevin Akkoyun Shortens sampleArray until its size is 20
	 */
	public void validateSampleArray() {
		while (sampleArray.size() > 20) {
			sampleArray.remove(sampleArray.size() - 1);
		}
	}

	/**
	 * @author Kevin Akkoyun Saves sample data to a text file uses ; to separate
	 *         sample name and path.
	 */
	public void saveSampleData() {

		try {
			File targetFile = new File("sampledata.txt");
			targetFile.createNewFile();

			FileWriter dataWriter = new FileWriter(targetFile);
			sampleArray.forEach((sample) -> {
				sampleData = sampleData.concat(sample.getName() + ";" + sample.getSamplePath() + "\n");
			});
			dataWriter.write(sampleData);
			dataWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Kevin Akkoyun Reads sample data from text file and adds valid lines
	 *         to sample array Checks if sampleArray is oversized and reduces it to
	 *         20 if needed
	 */
	public void readSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			Scanner fileReader = new Scanner(targetFile);

			while (fileReader.hasNextLine()) {
				String temp = fileReader.nextLine();
				String[] sampleParts = temp.split(";");
				File tester = new File(sampleParts[1]);
				if (tester.exists()) {
					sampleArray.add(new Sample(sampleParts[0], sampleParts[1]));
				}
				validateSampleArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Kevin Akkoyun Clears sampleData and sampleArray Writes blank to
	 *         sampledata.txt
	 */
	public void clearSampleData() {
		try {
			File targetFile = new File("sampledata.txt");
			FileWriter writer = new FileWriter(targetFile);
			writer.write("");
			writer.close();
			sampleArray = new ArrayList<Sample>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setListener(LineListener listener) {
		player.setOnClose(listener);
	}
	public void removeListener(LineListener listener) {
		player.removeListeners(listener);
	}
}
