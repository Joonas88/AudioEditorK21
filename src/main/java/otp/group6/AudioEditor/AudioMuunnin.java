package otp.group6.AudioEditor;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
	private LowPassSP lowPassSP;
	private float sampleRate;

	private double pitchFactor = 1; // pitch factor 1 = alkuperäinen pitch
	private double gain = 1;
	private double echoLength = 0.0001;
	private double decay = 0;
	private double flangerLength = 0.0001;
	private double wetness = 0;
	private double lfo = 0;
	private float lowPass = 44100;

	//Tämä pause-nappia varten
	private float secondsProcessed = (float) 0.0;

	// Konstruktori
	public AudioMuunnin() {

	}

	public void setAudioSourceFile(File file) {
		// Haetaan tiedosto parametrin perusteella
		this.wavFile = file.getAbsoluteFile();
		try {
			// formaatti ja sampleRate instanssimuuttujiin
			// TODO: tee audioformaatin tarkistus!! stereo vs mono
			this.format = AudioSystem.getAudioFileFormat(wavFile).getFormat();
			this.sampleRate = format.getSampleRate();

			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(sampleRate, sampleRate));
			adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
			audioPlayer = new AudioPlayer(format);

			// Pitch-arvon muuttaja
			rateTransposer = new RateTransposer(pitchFactor);
			adp.addAudioProcessor(rateTransposer);

			// Kaikuefekti
			delayEffect = new DelayEffect(echoLength, decay, sampleRate);
			adp.addAudioProcessor(delayEffect);

			// Gain
			gainProcessor = new GainProcessor(gain);
			adp.addAudioProcessor(gainProcessor);

			// Flangerefekti
			flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, (lfo));
			adp.addAudioProcessor(flangerEffect);

			// LowPass
			lowPassSP = new LowPassSP(lowPass, sampleRate);
			adp.addAudioProcessor(lowPassSP);

		} catch (UnsupportedAudioFileException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Virhe!");
			alert.setHeaderText("Väärä tiedostomuoto");
			alert.setContentText("Valitse vain WAV-tyyppisiä tiedostoja");

			alert.showAndWait();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sound effect methods

	public void setPitchFactor(double pitchFactor) {
		// Vaihdetaan arvot Rate Transposeriin..
		this.pitchFactor = pitchFactor;
		rateTransposer.setFactor(pitchFactor);
		// ..ja WaveFormSimilarityOverlappAddiin
		wsola.setParameters(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(pitchFactor, sampleRate));
	}

	public void setGain(double gain) {
		this.gain = gain;
		gainProcessor.setGain(gain);
	}

	public void setEchoLength(double echoLength) {
		// Echo length can't be zero
		if (echoLength == 0) {
			echoLength = 0.0001;
			this.echoLength = echoLength;
			delayEffect.setEchoLength(echoLength);
		} else {
			this.echoLength = echoLength;
			delayEffect.setEchoLength(echoLength);
		}

	}

	public void setDecay(double decay) {
		this.decay = decay;
		delayEffect.setDecay(decay);
	}

	public void setFlangerLength(double flangerLength) {
		// Flanger length cannot be zero
		if (flangerLength == 0) {
			flangerLength = 0.001;
			this.flangerLength = flangerLength;
			flangerEffect.setFlangerLength(flangerLength);
		}
		this.flangerLength = flangerLength;
		flangerEffect.setFlangerLength(flangerLength);
	}

	public void setWetness(double wetness) {
		this.wetness = wetness;
		flangerEffect.setWet(wetness);
	}

	public void setLFO(double lfo) {
		this.lfo = lfo;
		flangerEffect.setLFOFrequency(lfo);
	}

	public void setLowPass(float lowPass) {
		this.lowPass = lowPass;
		lowPassSP = new LowPassSP(lowPass, sampleRate);
		adp.addAudioProcessor(lowPassSP);

	}

	
	public void testFilter() {
		AudioFormat format2 = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format2);
		TargetDataLine line = null;
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format2);
			line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		AudioInputStream ais = new AudioInputStream(line);
		JVMAudioInputStream audioStream = new JVMAudioInputStream(ais);
		AudioDispatcher liveDispatcher = new AudioDispatcher(audioStream, wsola.getInputBufferSize(),
				wsola.getOverlap());
		wsola.setDispatcher(liveDispatcher);
		liveDispatcher.addAudioProcessor(wsola);
		liveDispatcher.addAudioProcessor(rateTransposer);
		liveDispatcher.addAudioProcessor(delayEffect);
		liveDispatcher.addAudioProcessor(gainProcessor);
		liveDispatcher.addAudioProcessor(flangerEffect);
		liveDispatcher.addAudioProcessor(audioPlayer);
		// liveDispatcher.run();

		
		
		try {
			Thread t = new Thread(liveDispatcher);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public void playAudio() {
		if (adp != null) {
			adp.stop();
		}
		// WaveformSimilarityBasedOverlapAdd pitää tiedoston samanmittaisena
		// pitch-arvosta riippumatta
		try {
			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Pitch-arvon muuttaja
		rateTransposer = new RateTransposer(pitchFactor);
		adp.addAudioProcessor(rateTransposer);

		// Kaikuefekti
		delayEffect = new DelayEffect(echoLength, decay, sampleRate); 
		// Kaiun oletusarvot: kesto 0.0001s (koska nollaa ei
		// voi laittaa), efekti ei käytössä eli 0 ja
		// normi sampleRate
		adp.addAudioProcessor(delayEffect);

		// Gain
		gainProcessor = new GainProcessor(gain);
		adp.addAudioProcessor(gainProcessor);

		// Flangerefekti
		flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, (lfo));
		adp.addAudioProcessor(flangerEffect);

		// LowPass
		lowPassSP = new LowPassSP(lowPass, sampleRate);
		adp.addAudioProcessor(lowPassSP);

		try {
			audioPlayer = new AudioPlayer(format);
			adp.addAudioProcessor(audioPlayer);
			adp.addAudioProcessor(new AudioProcessor() {
			@Override
				public void processingFinished() {
				//TODO Tähän pitäis laittaa viesti main controllerille play-napin aktivoinnista!
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
			if(secondsProcessed != (float)0.0) {
				adp.skip(secondsProcessed);
				secondsProcessed =(float)0.0;
			}
			

	}

	public void stopAudio() {
		if (adp != null) {
			adp.stop();
			secondsProcessed = (float)0.0;
		}
	}

	public void pauseAudio() {
		if (adp != null) {
			// kohta mihin jäätiin
			secondsProcessed = adp.secondsProcessed();
			adp.stop();
		}
	}

	public void saveFile() {
		writer = new WaveformWriter(format, "src/audio/miksattuAudio.wav"); // Tiedoston nimi miksattuAudio!!
		adp.addAudioProcessor(writer);
		Thread t = new Thread(adp);
		t.start();
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}
}
