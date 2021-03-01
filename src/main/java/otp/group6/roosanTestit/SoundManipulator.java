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
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.resample.RateTransposer;

public class SoundManipulator {
	static File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();
	static File wavFile2 = new File("src/audio/test4.wav").getAbsoluteFile();
	static AudioInputStream ais;
	static AudioFormat inFormat;
	static Clip clip;
	public static void main(String[] args) {
		try{
			ais = AudioSystem.getAudioInputStream(wavFile);
			inFormat = getOutFormat(ais.getFormat());
			muunnaAani(inFormat, 900);
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		play();
		Scanner scanner = new Scanner(System.in);
		while(true) {
			int i = scanner.nextInt();
			break;
		}
	}
	
	/////////////////////////////////////////
	
	public static double centToFactor(double cents){
		return 1 / Math.pow(Math.E,cents*Math.log(2)/1200/Math.log(Math.E)); 
	}
	private static double factorToCents(double factor){
		return 1200 * Math.log(1/factor) / Math.log(2); 
		}
	
	public static void muunnaAani(AudioFormat inFormat, double cents) throws UnsupportedAudioFileException, IOException {
		
		//Testi
		cents = 900;
		String targetFile = "src/audio/test4.wav";
		
		
		double sampleRate = inFormat.getSampleRate();
		double factor = SoundManipulator.centToFactor(cents);
		RateTransposer rateTransposer = new RateTransposer(factor);
		
		WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(Parameters.musicDefaults(factor, sampleRate));
		WaveformWriter writer = new WaveformWriter(inFormat,targetFile);
		AudioDispatcher dispatcher;
		if(inFormat.getChannels() != 1){
			dispatcher = AudioDispatcherFactory.fromFile(wavFile,wsola.getInputBufferSize() * inFormat.getChannels(),wsola.getOverlap() * inFormat.getChannels());
			dispatcher.addAudioProcessor(new MultichannelToMono(inFormat.getChannels(),true));
		}else{
			dispatcher = AudioDispatcherFactory.fromFile(wavFile,wsola.getInputBufferSize(),wsola.getOverlap());
		}
		wsola.setDispatcher(dispatcher);
		dispatcher.addAudioProcessor(wsola);
		dispatcher.addAudioProcessor(rateTransposer);
		dispatcher.addAudioProcessor(writer);
		dispatcher.run();
	}

	//////////////////////////////////////////////////
	public static AudioFormat getOutFormat(AudioFormat inFormat) {
		int ch = inFormat.getChannels();
		float sampleRate = inFormat.getSampleRate();
		float frameRate = inFormat.getFrameRate();
		System.out.println(sampleRate + " " + frameRate);
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 1000,
				16, ch, ch*2, frameRate * 2, inFormat.isBigEndian());
		
	}
	
	 public static void play() {
		 AudioInputStream ais2 = AudioSystem.getAudioInputStream(inFormat, ais);
//		 try {
//			AudioSystem.write(ais2, AudioFileFormat.Type.WAVE, wavFile2);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
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
}
