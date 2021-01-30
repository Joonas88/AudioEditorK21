package otp.group6.prototypes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import otp.group6.AudioEditor.*;


public class TestPlayer {
	static AudioRecorder ar;
	static Soundboard sb;
	public static void main(String[] args) {
		sb = new Soundboard();
		AudioInputStream ais;
		ar = new AudioRecorder();
		try {
			 sb.addSample(sb.new Sample("src/audio/test3.wav"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		int i;
		while(true) {
			i = sc.nextInt();
			if(i == 5) {
				break;
			}
			choice(i);
		}
	  
	}
public static void choice(int i) {
    	
    	switch (i) {
    	case 1 :
    	if(sb.isPlaying()) {
    		sb.closeSample();
    	} else {
    		sb.playSample(0);
    	}
    	break;
    	case 2:
    	if(ar.isAlive()) {
    		ar.stopRecord();
    	}else {
    		ar.start();
    	}
    	break;
    	case 3:
    	
    	break;
    	case 4:
    	
    	break;
    	}
    }
}