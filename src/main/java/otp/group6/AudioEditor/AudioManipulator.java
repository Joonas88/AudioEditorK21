package otp.group6.AudioEditor;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;

public class AudioManipulator {

	private File wavFile;
	private AudioDispatcher adp;
	private WaveformSimilarityBasedOverlapAdd wsola;
	private GainProcessor gain; // TODO: Halutaanko säätää??
	private AudioPlayer audioPlayer;
	private RateTransposer rateTransposer;
	private AudioFormat format;
	private WaveformWriter writer;
	private FlangerEffect flangerEffect = null;
	private DelayEffect delayEffect = null;
	private float pitchFactor = 1; // pitch factor 1 = alkuperäinen pitch
	private float sampleRate;

	// Konstruktori
	public AudioManipulator(String tiedostopolku) {
		// Haetaan tiedosto parametrin perusteella
		this.wavFile = new File(tiedostopolku).getAbsoluteFile();
		try {
			// formaatti ja sampleRate instanssimuuttujiin
			this.format = AudioSystem.getAudioFileFormat(wavFile).getFormat();
			this.sampleRate = format.getSampleRate();

			// WaveformSimilarityBasedOverlapAdd pitää tiedoston samanmittaisena
			// pitch-arvosta riippumatta
			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());
			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);

			// Pitch-arvon muuttaja
			rateTransposer = new RateTransposer(pitchFactor);
			adp.addAudioProcessor(rateTransposer);

		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPitchFactor(float pitchFactor) {
		// Vaihdetaan arvot Rate Trandposeriin..
		this.pitchFactor = pitchFactor;
		rateTransposer.setFactor(pitchFactor);
		// ..ja pituuden algoritmiin :D lol idk
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(pitchFactor, sampleRate));

	}

	// KEEESKEEn
	public void kirjoitaTiedostoon() {
		writer = new WaveformWriter(format, "src/audio/miksattuAudio.wav"); // Tiedoston nimi miksattuAudio!!
		adp.addAudioProcessor(writer);
		adp.run();
	}

	public void setFlangerEffect(double maxFlangerLength, double wet) { // vika parametri double lfoFrequency,
																		// halutaanko muokata??
		if (flangerEffect == null) {
			flangerEffect = new FlangerEffect(maxFlangerLength, wet, sampleRate, format.getSampleRate() * 0.4);
			adp.addAudioProcessor(flangerEffect);
		} else {
			flangerEffect.setFlangerLength(maxFlangerLength);
			flangerEffect.setWet(wet);
		}
	}

	/**
	 * Viiveen muokkaaminen
	 * 
	 * @param echoLength kaiun kesto sekunteina
	 * @param decay
	 */
	public void setDelayEffect(double echoLength, double decay) {
		if (delayEffect == null) {
			delayEffect = new DelayEffect(echoLength, decay, sampleRate);
			adp.addAudioProcessor(delayEffect);
		} else if (echoLength == 0) {
			System.out.println("Kaiun pituus on 0.");
		}

		else {
			delayEffect.setEchoLength(echoLength);
			delayEffect.setDecay(decay);
		}
	}

	public void playAudio() {
		try {
			audioPlayer = new AudioPlayer(format);
			adp.addAudioProcessor(audioPlayer);
			try {
				Thread t = new Thread(adp);
				t.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
