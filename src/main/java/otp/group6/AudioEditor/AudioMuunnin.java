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

public class AudioMuunnin {

	private File wavFile;
	private AudioDispatcher adp;
	private WaveformSimilarityBasedOverlapAdd wsola;
	private GainProcessor gainProcessor; // TODO: Halutaanko säätää??
	private AudioPlayer audioPlayer;
	private RateTransposer rateTransposer;
	private AudioFormat format;
	private WaveformWriter writer;
	private FlangerEffect flangerEffect;
	private DelayEffect delayEffect;
	private float pitchFactor = 1; // pitch factor 1 = alkuperäinen pitch
	private float sampleRate;

	// Konstruktori
	public AudioMuunnin() {
		
	}
	
	public void setAudioFile(String tiedostopolku) {
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

					// Kaikuefekti
					delayEffect = new DelayEffect(1, 0, sampleRate); // Kaiun oletusarvot: kesto 1s, efekti ei käytössä eli 0 ja
																		// normi sampleRate
					adp.addAudioProcessor(delayEffect);
					
					//Gain
					gainProcessor = new GainProcessor(1); //1 on normaali gain
					adp.addAudioProcessor(gainProcessor);

				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	//Sound effect methods
	
	public void setPitchFactor(float pitchFactor) {
		// Vaihdetaan arvot Rate Trandposeriin..
		this.pitchFactor = pitchFactor;
		rateTransposer.setFactor(pitchFactor);
		System.out.println("muutettu pitch");
		// ..ja pituuden algoritmiin :D lol idk
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(pitchFactor, sampleRate));

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
	 * Viiveen/kaiun muokkaaminen
	 * 
	 * @param echoLength kaiun kesto sekunteina
	 * @param decay      TÄHÄN JOKU FIKSU KUVAUS
	 */
	public void setDelayEffect(double echoLength, double decay) {
		if(echoLength != -1) {
			if (echoLength == 0) {
				echoLength = 0.0001; // JOO HAHAHÖHÖ koska tarsos ei hyväksy nollaa
			}
			delayEffect.setEchoLength(echoLength);
			System.out.println("muutettu echo length");
		}
		
		if(decay != -1) {
			delayEffect.setDecay(decay);
			System.out.println("muutettu decay");
		}
		
	}
	
	/**
	 * Gainin muokkaus välillä
	 * @param gain
	 */
	public void setGain(double newGain) {
		gainProcessor.setGain(newGain);
		System.out.println("gain säädetty");
	}

	
	public void playAudio() {
		try {
			System.out.println("play audio klikattu");
			audioPlayer = new AudioPlayer(format);
			adp.addAudioProcessor(audioPlayer);
			try {
				Thread t = new Thread(adp);
				t.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}	
	
	// KEEESKEEn
	public void saveFile() {
		writer = new WaveformWriter(format, "src/audio/miksattuAudio.wav"); // Tiedoston nimi miksattuAudio!!
		adp.addAudioProcessor(writer);
		adp.run();
		System.out.println("Mikserin tiedosto tallennettu");
	}

}
