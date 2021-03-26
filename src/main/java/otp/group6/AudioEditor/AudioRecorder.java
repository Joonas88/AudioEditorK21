package otp.group6.AudioEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.filters.LowPassSP;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;
import javafx.concurrent.Task;
import otp.group6.controller.Controller;

/**
 * Provides tools for basic audio recording
 * 
 * @authors Onni Lukkarila, Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 *
 */

public class AudioRecorder extends Thread {
	private Controller controller;
	private AudioFormat format;
	private File targetFile;
	private TargetDataLine line;
	private AudioDispatcher adp;
	private AudioPlayer audioPlayer;
	private AudioInputStream ais;
	private JVMAudioInputStream jvmAudioStream;
	private WaveformSimilarityBasedOverlapAdd wsola;
	private WaveformWriter writer;
	private Thread t;
	private float secondsProcessed = (float) 0.0;
	private Boolean isPlaying = false;
	private Boolean isPressed = false;
	private Timer timer;
	private TimerTask task;

	public AudioRecorder(Controller controller) {
		this.controller = controller;
		this.setFormat(getDefaultAudioFormat());
		// Sets default file
		this.setTargetFile(new File("src/audio/default.wav").getAbsoluteFile());
		wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(1.0, format.getSampleRate()));
		wsola.setDispatcher(adp);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public File getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

	/**
	 * Method to start recording your microphone
	 */
	public void recordAudio() {
		try {
			writer = new WaveformWriter(format, "src/audio/default.wav");

			line.open(format);
			System.out.println(line.isOpen());
			line.start();

			ais = new AudioInputStream(line);
			jvmAudioStream = new JVMAudioInputStream(ais);

			adp = new AudioDispatcher(jvmAudioStream, wsola.getInputBufferSize(), wsola.getOverlap());
			adp.addAudioProcessor(writer);
			if (t == null || !t.isAlive()) {
				t = new Thread(adp);
			} else {
				adp.stop();
				t = new Thread(adp);
			}
			t.start();
			System.out.println("Recording started");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to stop recording your microphone
	 */
	public void stopRecord() {
		adp.stop();
		System.out.println("Record duration: " + adp.secondsProcessed());
		System.out.println("Recording stopped");
	}

	/**
	 * Method for playing recorded file
	 */
	public void playAudio() {
		if (adp != null) {
			adp.stop();
		}

		try {
			adp = AudioDispatcherFactory.fromFile(targetFile, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
			audioPlayer = new AudioPlayer(format);
			adp.addAudioProcessor(audioPlayer);
			adp.addAudioProcessor(new AudioProcessor() {
				@Override
				public void processingFinished() {
					// TODO Tähän pitäis laittaa viesti main controllerille play-napin
					// aktivoinnista!
				}

				@Override
				public boolean process(AudioEvent audioEvent) {
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		Thread t = new Thread(adp);
		t.start();
		isPlaying = true;
		if (secondsProcessed != (float) 0.0) {
			adp.skip(secondsProcessed);
			secondsProcessed = (float) 0.0;
		}
		isPressed = false;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if (!isPressed) {
					System.out.println(adp.secondsProcessed());
					setCurrentPositionToRecordFileDurationSlider(adp.secondsProcessed());

				} else {
					System.out.println(isPressed);
				}
			}
		};
		timer.schedule(task, 0, 500);

	}

	/**
	 * Method to stop playing audio
	 */
	public void stopAudio() {
		if (adp != null) {
			adp.stop();
			isPlaying = false;
			secondsProcessed = (float) 0.0;
			setCurrentPositionToRecordFileDurationSlider(0);
		}
	}

	/**
	 * Method to pause playing audio
	 */
	public void pauseAudio() {
		if (adp != null) {
			secondsProcessed = adp.secondsProcessed();
			adp.stop();
			isPlaying = false;
		}
	}

	/**
	 * Method to play recorded file desired position
	 * @param seconds
	 */
	public void playFromDesiredSec(double seconds) {
		secondsProcessed = (float) seconds;
		System.out.println(seconds);
		if (isPlaying == true) {
			playAudio();
		}
	}

	/**
	 * Method to save recorded file
	 * @param path
	 */
	public void saveAudioFile(String path) {
		File source = new File("src/audio/default.wav");
		File dest = new File(path);

		try (InputStream fis = new FileInputStream(source); OutputStream fos = new FileOutputStream(dest)) {

			byte[] buffer = new byte[1024];
			int length;

			while ((length = fis.read(buffer)) > 0) {

				fos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to cancel the timer that shows the correct position to mediaplayerslider
	 */
	public void timerCancel() {
		if (timer != null) {
			timer.cancel();
			task.cancel();
			System.out.println("timer cancelled");
		}
	}

	/**
	 * Method that tells if mediaplayerslider has been pressed
	 */
	public void recorderSliderPressed() {
		isPressed = true;
		System.out.println("Slider pressed");
	}

	/**
	 * Method to set the current position to mediaplayerslider
	 * @param seconds
	 */
	private void setCurrentPositionToRecordFileDurationSlider(double seconds) {
		controller.setCurrentValueToRecordFileDurationSlider(seconds);
	}

	/**
	 * Method to return the default audio format, which the software uses
	 * @return correct audio format
	 */
	public AudioFormat getDefaultAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}
}
