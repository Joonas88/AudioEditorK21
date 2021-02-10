package otp.group6.prototypes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

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
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		play();
		System.out.println(clip.isRunning());
		Scanner scanner = new Scanner(System.in);
		while(true) {
			int i = scanner.nextInt();
			break;
		}
	}
	
	
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
