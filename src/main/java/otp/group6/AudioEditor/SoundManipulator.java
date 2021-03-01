package otp.group6.AudioEditor;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.UnsupportedAudioFileException;


import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;

public class SoundManipulator {
	static File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();
	static File wavFile2 = new File("src/audio/test7.wav").getAbsoluteFile();
	static AudioInputStream ais;
	static AudioFormat inFormat;
	static Clip clip;
	static PitchShifter pitchShifter;
	static AudioEvent audioEvent;

	public static void main(String[] args) {
		try {
			ais = AudioSystem.getAudioInputStream(wavFile);
			inFormat = getOutFormat(ais.getFormat());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		play();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			int i = scanner.nextInt();
			break;
		}
	}
	
	public static double centToFactor(double cents){
		return 1 / Math.pow(Math.E,cents*Math.log(2)/1200/Math.log(Math.E)); 
	}
	private static double factorToCents(double factor){
		return 1200 * Math.log(1/factor) / Math.log(2); 
		}
	

	
	public static AudioFormat getOutFormat(AudioFormat inFormat) {
		// vaihda muuttaaksesi nopeutta
		float multiplier = 0.5f;
		int ch = inFormat.getChannels();
		float sampleRate = inFormat.getSampleRate();
		float frameRate = inFormat.getFrameRate();
		System.out.println(sampleRate + " " + frameRate);

		// vaihda muuttaaksesi ääntä, default = 44100.0
		sampleRate = 5000;

		return new AudioFormat(inFormat.getSampleRate() * multiplier, inFormat.getSampleSizeInBits(),
				inFormat.getChannels(), true, inFormat.isBigEndian());
	}

	public static void play() {
		AudioInputStream ais2 = AudioSystem.getAudioInputStream(inFormat, ais);
//		 try {
//			AudioSystem.write(ais2, AudioFileFormat.Type.WAVE, wavFile2);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		try {
			clip = AudioSystem.getClip();
			clip.open(ais2);
			clip.loop(0);
			clip.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeSpeed() {
		try {

			// vaihda muuttaaksesi nopeutta (2 = tuplaa, 0.5 puolittaa)
			float multiplier = 1f;
			
			

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
			AudioFormat formatIn = audioInputStream.getFormat();
			AudioFormat format = new AudioFormat(formatIn.getSampleRate() * multiplier, formatIn.getSampleSizeInBits(),
					formatIn.getChannels(), true, formatIn.isBigEndian());
			
			TarsosDSPAudioFormat formatInTarsos = new TarsosDSPAudioFormat(formatIn.getSampleRate() * multiplier, formatIn.getSampleSizeInBits(),
					formatIn.getChannels(), true, formatIn.isBigEndian());
			
			audioEvent = new AudioEvent(formatInTarsos);
			pitchShifter = new PitchShifter(1, 44100, 16, 1);
			pitchShifter.setPitchShiftFactor(2);
			
			AudioFormat formatTaros = new AudioFormat(audioEvent.getSampleRate(), formatIn.getSampleSizeInBits(),
					formatIn.getChannels(), true, formatIn.isBigEndian());
			
			System.out.println(formatIn.toString());
			System.out.println(format.toString());
			byte[] data = new byte[1024];
			DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, formatTaros);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(dinfo);
			if (line != null) {
				line.open(format);
				line.start();
				while (true) {
					int k = audioInputStream.read(data, 0, data.length);
					
					if (k < 0)
						break;
					line.write(data, 0, k);
				}
				line.stop();
				line.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
