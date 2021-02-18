package otp.group6.AudioEditor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.2
 *
 */

public class AudioOutput extends Thread {

	@SuppressWarnings("unused")
	private AudioDispatcher audioDispatcher;

	// Used to check if audioFile has been given before playing
	private boolean isReady;

	public AudioOutput() {
		isReady = false;
	}

	@Override
	public void run() {
		if (isReady) {
			try {
				audioDispatcher.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Opens Audio file with audio input stream
	 * 
	 * @param audio A new AudioInputStream
	 */
	public void openAudio(AudioInputStream audio) {
		JVMAudioInputStream ais = getMonoAudioInputStream(audio);

		WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(
				Parameters.musicDefaults(1.0F, ais.getFormat().getSampleRate()));

		audioDispatcher = new AudioDispatcher(ais, wsola.getInputBufferSize(), wsola.getOverlap());

		wsola.setDispatcher(audioDispatcher);

		audioDispatcher.addAudioProcessor(wsola);

		try {
			audioDispatcher.addAudioProcessor(new AudioPlayer(getAudioFormat()));

			isReady = true;
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops audioDispatcher and finishes AudioOutput thread
	 */
	public void closeAudio() {
		audioDispatcher.stop();
	}

	/**
	 * Converts audioInputStream to mono JVMAudioInputStream
	 * 
	 * @param ais
	 * @return
	 */
	public JVMAudioInputStream getMonoAudioInputStream(AudioInputStream ais) {

		return new JVMAudioInputStream(AudioSystem.getAudioInputStream(getAudioFormat(), ais));

	}

	/**
	 * returns default audio format
	 * 
	 * @return Default Audio format
	 */
	public AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}
}
