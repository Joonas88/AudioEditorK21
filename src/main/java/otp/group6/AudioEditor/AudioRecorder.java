package otp.group6.AudioEditor;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;

/**
 * Provides tools for basic audio recording
 * 
 * @author Kevin Akkoyun, Joonas Soininen, Onni Lukkarila
 * @version 0.1
 *
 */

//

public class AudioRecorder extends Thread {
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

	public AudioRecorder() {
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
			if(t == null || !t.isAlive()) {
				t = new Thread(adp);
			}
			else {
				adp.stop();
				t.join();
				t = new Thread(adp);
			}
			t.start();
			
			System.out.println("Recording started");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopRecord() {
		
		adp.stop();
		System.out.println(adp.secondsProcessed());
		System.out.println("Recording stopped");
	}

	
	
	
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
