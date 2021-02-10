package otp.group6.AudioEditor;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.resample.RateTransposer;

public class Audiohiommeli122 {

	static File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();
	static AudioFormat format;


	public static void main(String[] args) {
		
		PitchDetectionHandler handler = new PitchDetectionHandler() {
			@Override
			public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
				System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
			}
		};

		AudioDispatcher adp;
		

		try {
			adp = AudioDispatcherFactory.fromFile(wavFile, 2048, 0);
			adp.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, 44100, 2048, handler));
			adp.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			format = AudioSystem.getAudioFileFormat(wavFile).getFormat();
			WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(
					Parameters.musicDefaults(3, format.getSampleRate()));
			if (format.getChannels() != 1) {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize() * format.getChannels(),
						wsola.getOverlap() * format.getChannels());
				adp.addAudioProcessor(new MultichannelToMono(format.getChannels(), true));
			} else {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());
			}
			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
			adp.addAudioProcessor(new RateTransposer(3));
			adp.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, 44100, 2048, handler));
			adp.addAudioProcessor(new WaveformWriter(format, "src/audio/test2.wav"));
			adp.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
