package otp.group6.roosanTestit;

import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.FadeIn;
import be.tarsos.dsp.FadeOut;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.filters.IIRFilter;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.filters.LowPassSP;
import be.tarsos.dsp.granulator.Granulator;
import be.tarsos.dsp.io.jvm.*;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.resample.RateTransposer;
import be.tarsos.dsp.synthesis.NoiseGenerator;

public class PitchShiftingTest {

	static File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();
	static AudioFormat format;
	static AudioDispatcher adp;
	static AudioDispatcher liveDispatcher;

	public static void main(String[] args) {

		try {
			// Vaihtaa tiedoston äänenkorkeutta, arvot 0.1 - 4.0 (voi olla, että pienimmät
			// arvot eivät toimi)
			float pitchFactor = 1.3f;
			
			//Kaiun pituus sekunteina
			float delayLength = 0.006F; 
			//Kaiun heikentyvyys 0-1. 1 = ei heikenny, 0 = ei lainkaan kaikua.
			float delayDecay = 0.6f;

			format = AudioSystem.getAudioFileFormat(wavFile).getFormat();
			
			//Tiedoston nopeuden/pituuden muuttaja
			WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(
					Parameters.musicDefaults(pitchFactor, format.getSampleRate()));
			
			//Äänenkorkeuden muuttaja
			RateTransposer rateTransposer = new RateTransposer(1);
			
			//Tiedostoon kirjoittaja
			WaveformWriter writer = new WaveformWriter(format, "src/audio/test2.wav");
			
			//Kaikuefekti
			DelayEffect delay = new DelayEffect(delayLength, delayDecay, format.getSampleRate());
			
			//Häivytys (FadeOut ei jostain syystä toimi)
			FadeIn fadeIn = new FadeIn(2);
			FadeOut fadeOut = new FadeOut(2);
			
			//Volume
			GainProcessor gainProcessor = new GainProcessor(3);
			
			//Lowpass (veden alla, saattaa tarvita lisää volumea)
			LowPassFS lowPassFS = new LowPassFS(200, 44100);
			LowPassSP lowPassSP = new LowPassSP(6000, 44100);
			
			HighPass highPass = new HighPass(10000, 44100);
			
			AudioPlayer audioPlayer = new AudioPlayer(format);
			
			FlangerEffect flangerEffect = new FlangerEffect(0.03, 0.01, format.getSampleRate(), format.getSampleRate() * 0.4);
			
			
			
			System.out.println("Mono(1) vai Stereo(2): " + format.getChannels());
			

			// Tarkistaa onko tiedosto stereo vai mono
			if (format.getChannels() != 1) {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize() * format.getChannels(),
						wsola.getOverlap() * format.getChannels());

				// ***Ei toimi***, jos tiedosto on stereo täytyy se ensin muuttaa Javan audio
				// systeemillä
				adp.addAudioProcessor(new MultichannelToMono(format.getChannels(), true));
				System.out.println("Mono(1) vai Stereo(2): " + format.getChannels());
			} else {
				adp = AudioDispatcherFactory.fromFile(wavFile, wsola.getInputBufferSize(), wsola.getOverlap());
			}
			
			
			Scanner sc = new Scanner(System.in);
			Thread stopper = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                    	System.out.println("Syötä numero pysäyttääksesi");
                        Thread.sleep(sc.nextInt());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                 adp.stop();
                 liveDispatcher.stop();
                 float i = adp.secondsProcessed();
                 System.out.println("Pysähtyi kohdassa: " + i);
                 
                }
            });
			
			stopper.start();
			
//			adp.addAudioProcessor(pass);
//			adp.addAudioProcessor(fadeIn);
//			adp.addAudioProcessor(fadeOut);
//			adp.addAudioProcessor(gainProcessor);
//			adp.addAudioProcessor(lowPassSP);
//			adp.addAudioProcessor(highPass);
//			adp.addAudioProcessor(delay);
//			adp.addAudioProcessor(delay2);
//			adp.addAudioProcessor(delay3);
//			adp.addAudioProcessor(flangerEffect);
//			adp.addAudioProcessor(wsola);
//			adp.addAudioProcessor(rateTransposer);
//			adp.addAudioProcessor(writer);
//			adp.addAudioProcessor(audioPlayer);

			
//			adp.run();
			
			
			AudioFormat format2 = getAudioFormat();
	        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format2);
	        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
	        line.open(format2);
	        System.out.println(line.isOpen());
	        line.start();
	        AudioInputStream ais = new AudioInputStream(line);
	        JVMAudioInputStream audioStream = new JVMAudioInputStream(ais);
			liveDispatcher = new AudioDispatcher(audioStream, wsola.getInputBufferSize(),wsola.getOverlap()); 
			
			
			wsola.setDispatcher(liveDispatcher);
			liveDispatcher.addAudioProcessor(wsola);
			liveDispatcher.addAudioProcessor(rateTransposer);
			liveDispatcher.addAudioProcessor(audioPlayer);
			liveDispatcher.run();

			
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	public static AudioFormat getAudioFormat(){
        float sampleRate = 44100;
        int sampleSizeBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate,sampleSizeBits,channels,signed, bigEndian);
        

        return format;
    }

}
