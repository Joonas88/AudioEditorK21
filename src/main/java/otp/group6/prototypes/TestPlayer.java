package otp.group6.prototypes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import otp.group6.AudioEditor.*;


public class TestPlayer {

	public static void main(String[] args) {
		Soundboard sb = new Soundboard();
		AudioInputStream ais;
		try {
			 ais = AudioFileHandler.OpenFile("src/audio/test3.wav");
			 sb.addSample(new Sample(ais));
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		int i;
		while(true) {
			i = sc.nextInt();
			if(i == 5) {
				sb.closeSample();
				break;
			}
			choice(sb,i);
		}
	  
	}
public static void choice(Soundboard app, int i) {
    	
    	switch (i) {
    	case 1 :
    	app.playSample(0);
    	break;
    	case 2:
    	
    	break;
    	case 3:
    	
    	break;
    	case 4:
    	
    	break;
    	}
    }
    
}
