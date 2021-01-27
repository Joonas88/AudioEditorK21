package otp.group6.AudioEditor;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.*;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.resample.RateTransposer;

public class PitchShiftingTest {

	static File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();
	static AudioFormat format;
	static AudioDispatcher adp;

	public static void main(String[] args) {

		try {

			// Vaihtaa tiedoston äänenkorkeutta, arvot 0.1 - 4.0 (voi olla, että pienimmät
			// arvot eivät toimi)
			float pitchFactor = 1.3f;

			format = AudioSystem.getAudioFileFormat(wavFile).getFormat();
			WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(
					Parameters.musicDefaults(pitchFactor, format.getSampleRate()));
			RateTransposer rateTransposer = new RateTransposer(pitchFactor);
			WaveformWriter writer = new WaveformWriter(format, "src/audio/test2.wav");
			System.out.println("Mono(1) vai Stereo(2): " + format.getChannels());

			// Tarkistaa onko tiedosto stereo vai mono
			if (format.getChannels() != 1) {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize() * format.getChannels(),
						wsola.getOverlap() * format.getChannels());

				// ***Ei toimi***, jos tiedosto on stereo täytyy se ensin muuttaa Javan audio
				// systeemillä
				adp.addAudioProcessor(new MultichannelToMono(format.getChannels(), false));
				System.out.println(format.getChannels());
			} else {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());
			}

			adp.addAudioProcessor(wsola);
			adp.addAudioProcessor(rateTransposer);
			adp.addAudioProcessor(writer);
			adp.run();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
