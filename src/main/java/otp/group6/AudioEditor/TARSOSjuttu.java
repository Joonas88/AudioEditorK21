package otp.group6.AudioEditor;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.synthesis.NoiseGenerator;

public class TARSOSjuttu {

	public static void main(String[] args) throws LineUnavailableException {

		AudioDispatcher d = AudioDispatcherFactory.fromDefaultMicrophone(1024, 0);
		
		float sr = d.getFormat().getSampleRate();
		// High pass filter, let everything pass above 110Hz
		final TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(sr, 16, 1, true, false);

		// Pitch detection, print estimated pitches on standard out
		PitchDetectionHandler printPitch = new PitchDetectionHandler() {
			@Override
			public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
				System.out.println(pitchDetectionResult.getPitch());
			}
		};
		PitchEstimationAlgorithm algo = PitchEstimationAlgorithm.YIN; // use YIN
		//AudioProcessor pitchEstimator = new PitchProcessor(algo, sr, 1024, printPitch);
		//d.addAudioProcessor(pitchEstimator);
		// Add an audio effect (delay)
		//d.addAudioProcessor(new DelayEffect(0.5, 0.3, sr));
		// Mix some noise with the audio (synthesis)
		//d.addAudioProcessor(new NoiseGenerator(0.3));
		// Play the audio on the loudspeakers
		d.addAudioProcessor(new AudioPlayer(new AudioFormat(sr, 16, 1, true, true)));
		d.run();// starts the dispatching process

	}
}
