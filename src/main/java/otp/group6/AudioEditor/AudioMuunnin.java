package otp.group6.AudioEditor;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
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
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;
import otp.group6.controller.Controller;

public class AudioMuunnin {

	private Controller controller;

	private File file;
	private AudioDispatcher adp;
	private AudioDispatcher liveDispatcher;
	private AudioInputStream audioInputStream;
	private JVMAudioInputStream audioInputStreamForTarsos;
	private TarsosDSPAudioFormat format;
	private AudioPlayer audioPlayer;
	private WaveformWriter writer;
	private WaveformSimilarityBasedOverlapAdd wsola;

	private GainProcessor gainProcessor;
	private RateTransposer rateTransposer;
	private FlangerEffect flangerEffect;
	private DelayEffect delayEffect;
	private LowPassSP lowPassSP;

	private Timer timer;
	private TimerTask task;

	// Original values
	private float sampleRate;
	private double pitchFactor = 1;
	private double gain = 1;
	private double echoLength = 0.0001; // Cannot be zero
	private double decay = 0;
	private double flangerLength = 0.0001; // Cannot be zero
	private double wetness = 0;
	private double lfo = 0;
	private float lowPass = 44100;

	private float playbackStartingPoint = (float) 0.0;// pause-nappia varten
	private float kokonaiskesto;
	private boolean isPlaying = false;
	private boolean isTestingFilter = false;

	// Konstruktori
	public AudioMuunnin(Controller controller) {
		this.controller = controller;
	}

	public void setAudioSourceFile(File file) {
		try {// Haetaan tiedosto parametrin perusteella
			this.file = file.getAbsoluteFile();

			// Converts AudioInputStream to Tarsos compatible JVMAudioInputSteam
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));

			// formaatti ja sampleRate instanssimuuttujiin
			this.format = audioInputStreamForTarsos.getFormat();
			this.sampleRate = format.getSampleRate();

			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());

			wsola.setDispatcher(adp);
			adp.addAudioProcessor(wsola);

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

			audioPlayer = new AudioPlayer(format);

		} catch (LineUnavailableException e) {
		} catch (NullPointerException e) {
			System.out.println("Tiedostoa ei valittu");
		} catch (Exception e) {
		}
	}

	// SOUND EFFECT METHODS
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
		if (adp != null) {
			adp.addAudioProcessor(lowPassSP);
		}
		if (liveDispatcher != null) {
			liveDispatcher.addAudioProcessor(lowPassSP);
		}

	}

	public void testFilter() {
		// Stop liveDispatcher if playing and disable mixer sliders
		if (isTestingFilter == true) {
			liveDispatcher.stop();
			isTestingFilter = false;
			if (file == null) {
				controller.setDisableMixerSliders(true);
			}

		} else { // Start liveDispatcher and enable mixer sliders
			isTestingFilter = true;
			AudioFormat format2 = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format2);
			TargetDataLine line = null;
			try {
				line = (TargetDataLine) AudioSystem.getLine(info);
				line.open(format2);
				line.start();
				AudioInputStream ais = new AudioInputStream(line);
				JVMAudioInputStream audioStream = new JVMAudioInputStream(ais);
				//
				// formaatti ja sampleRate instanssimuuttujiin
				this.format = audioStream.getFormat();
				this.sampleRate = format.getSampleRate();

				wsola = new WaveformSimilarityBasedOverlapAdd(
						Parameters.musicDefaults(pitchFactor, audioStream.getFormat().getSampleRate()));
				//
				liveDispatcher = new AudioDispatcher(audioStream, wsola.getInputBufferSize(), wsola.getOverlap());
				wsola.setDispatcher(liveDispatcher);
				liveDispatcher.addAudioProcessor(wsola);

				rateTransposer = new RateTransposer(pitchFactor);
				delayEffect = new DelayEffect(echoLength, decay, sampleRate);
				gainProcessor = new GainProcessor(gain);
				flangerEffect = new FlangerEffect(flangerLength, wetness, sampleRate, lfo);
				liveDispatcher.addAudioProcessor(flangerEffect);
				lowPassSP = new LowPassSP(lowPass, sampleRate);
				audioPlayer = new AudioPlayer(format);

				liveDispatcher.addAudioProcessor(rateTransposer);
				liveDispatcher.addAudioProcessor(delayEffect);
				liveDispatcher.addAudioProcessor(gainProcessor);
				liveDispatcher.addAudioProcessor(flangerEffect);
				liveDispatcher.addAudioProcessor(lowPassSP);
				liveDispatcher.addAudioProcessor(audioPlayer);
				controller.setDisableMixerSliders(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread t = new Thread(liveDispatcher);
				t.start();
				System.out.println("test filter started");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// MEDIAPLAYER METHODS
	public void playAudio() {
		// Stops audio dispatcher if already playing
		if (adp != null) {
			adp.stop();
		}
		isPlaying = true;
		createAudioProcessors();

		adp.addAudioProcessor(new AudioProcessor() {
			@Override
			public void processingFinished() {
				task.cancel();
				timer.cancel();
			}

			@Override
			public boolean process(AudioEvent audioEvent) {
				return false;
			}
		});

		Thread t = new Thread(adp);
		t.start();

		if (playbackStartingPoint != (float) 0.0) {
			adp.skip(playbackStartingPoint);
			System.out.println("aloitetaan toisto kohdasta " + playbackStartingPoint);
		}

		// Luodaan uusi Timer, joka kuuntelee adp:n toistokohtaa ja välittää sen
		// kontrollerin kautta näkymään oikeaan slideriin
		timer = new Timer();

		task = new TimerTask() {
			@Override
			public void run() {
				kokonaiskesto = playbackStartingPoint;
				kokonaiskesto += (float) (audioPlayer.getMicroSecondPosition() / 1000000.0);
				controller.setCurrentValueToAudioDurationSlider(kokonaiskesto);
				controller.setCurrentPositionToAudioDurationText(kokonaiskesto);
			}
		};
		timer.schedule(task, 100, 500); // käynnistää timerin, joka suorittaa taskin 0,5 sekunnin välein alkaen kohdasta
										// 0s

	}

	public void playFromDesiredSec(double seconds) {
		playbackStartingPoint = (float) seconds;
		System.out.println("asetetaan muuttujaan " + playbackStartingPoint);
		if (isPlaying == true) {
			playAudio();
		}
	}

	public void stopAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			adp.stop();
			isPlaying = false;
			playbackStartingPoint = (float) 0.0;
			// Asettaa kestosliderin nollaan
			controller.setCurrentValueToAudioDurationSlider(0.0);
			controller.setCurrentPositionToAudioDurationText(0.0);

		}
	}

	public void pauseAudio() {
		if (task != null) {
			task.cancel();
			timer.cancel();
		}
		if (adp != null) {
			// kohta mihin jäätiin
			isPlaying = false;
			System.out.println(kokonaiskesto);
			playbackStartingPoint = adp.secondsProcessed();
			adp.stop();
		}

	}

	public void resetMediaPlayer() {
		controller.setCurrentValueToAudioDurationSlider(0.0);
		controller.setCurrentPositionToAudioDurationText(0.0);
		playbackStartingPoint = 0;
		kokonaiskesto = 0;
		isPlaying = false;

	}

	// Timer metodit
	public void timerCancel() {
		task.cancel();
		timer.cancel();
		timer.purge();
	}

	public void timerWait() {
		try {
			task.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void timerNotify() {
		timer.notify();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void saveFile(String path) {
		createAudioProcessors();
		writer = new WaveformWriter(format, path);
		adp.removeAudioProcessor(audioPlayer);
		adp.addAudioProcessor(writer);
		try {
			Thread t = new Thread(adp);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// PRIVATE METHODS
	/**
	 * Creates following audio processors: WaveformSimilarityBasedOverlapAdd,
	 * RateTransposer, DelayEffect, GainProcessor, FlangerEffect, LowPassSP and
	 * AudioPlayer
	 */
	private void createAudioProcessors() {
		try {
			// WaveformSimilarityBasedOverlapAdd pitää tiedoston samanmittaisena
			// pitch-arvosta riippumatta
			wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(pitchFactor, sampleRate));
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioInputStreamForTarsos = new JVMAudioInputStream(
					AudioSystem.getAudioInputStream(getAudioFormat(), audioInputStream));
			adp = new AudioDispatcher(audioInputStreamForTarsos, wsola.getInputBufferSize(), wsola.getOverlap());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}
}
