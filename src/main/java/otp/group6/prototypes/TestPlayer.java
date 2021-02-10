package otp.group6.prototypes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
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
			sb.addSample("src/audio/test3.wav");
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			for (Mixer.Info info : mixerInfos) {
				// System.out.println( info.getName());
				Mixer m = AudioSystem.getMixer(info);
				System.out.println(info.toString());
				Line.Info[] lineInfos = m.getSourceLineInfo();
				for (Line.Info l : lineInfos) {
					System.out.println("\t " + l.toString());

				}
				/*
				 * Mixer m = AudioSystem.getMixer(info);
				 * 
				 * for (Line.Info lineInfo:lineInfos){ System.out.println
				 * (info.getName()+" "+lineInfo); Line line = m.getLine(lineInfo);
				 * System.out.println("\t "+line); } lineInfos = m.getTargetLineInfo(); for
				 * (Line.Info lineInfo:lineInfos){ System.out.println (m+"---"+lineInfo); Line
				 * line = m.getLine(lineInfo); System.out.print(" \t-----"+line);
				 * 
				 * }
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		int i;
		while (true) {
			i = sc.nextInt();
			if (i == 5) {
				break;
			}
			choice(i);
		}

	}

	public static void choice(int i) {

		switch (i) {
		case 2:
			if (ar.isAlive()) {
				ar.stopRecord();
			} else {
				ar.start();
			}
			break;
		case 3:
			try {
				Desktop.getDesktop().open(new File("src/audio").getAbsoluteFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:

			break;
		}
	}
}
