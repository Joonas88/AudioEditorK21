package otp.group6.AudioEditor;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TestPlayer {

	public static void main(String[] args) {
		AudioPlayer ap = new AudioPlayer();
		AudioInputStream ais;
		try {
			 ais = AudioSystem.getAudioInputStream( new File("src/audio/test3.wav").getAbsoluteFile());
			 ap.openAudio(ais);
			 ap.play();
			 
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
			choice(ap,i);
		}
	  
	}
public static void choice(AudioPlayer app, int i) {
    	
    	switch (i) {
    	case 1 :
    	app.resume();
    	break;
    	case 2:
    	app.pause();
    	break;
    	case 3:
    	app.forward();
    	break;
    	case 4:
    	app.rewind();
    	break;
    	}
    }
    
}
