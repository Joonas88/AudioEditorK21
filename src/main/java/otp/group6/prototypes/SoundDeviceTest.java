package otp.group6.prototypes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundDeviceTest {

	public static void main(String[] args) {

		Line.Info playbackLine = new Line.Info(SourceDataLine.class);

		ArrayList<Mixer.Info> result = new ArrayList<Mixer.Info>();

		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

		for (Mixer.Info info : mixerInfo) {

			Mixer mixer = AudioSystem.getMixer(info);
			if (mixer.isLineSupported(playbackLine)) {
				Line.Info[] li = mixer.getSourceLineInfo();
				System.out.println(info);
				for (Line.Info l : li) {
					System.out.println("\t " + l.toString());
				}
				result.add(info);
			}
		}
		AudioInputStream ais;

		try {
			ais = AudioSystem.getAudioInputStream(new File("src/audio/jumalauta.wav").getAbsoluteFile());

			Mixer m = AudioSystem.getMixer(result.get(0));
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			try {
				SourceDataLine l = (SourceDataLine) m.getLine(info);
				l.open(format);
				l.start();
				System.out.println("Write started");
				byte[] b = new byte[ais.available()];
				System.out.println(ais.available());
				while (ais.available() > 0) {
					int i = ais.read(b);
					l.write(b, 0, i);
				}
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		sc.next();

		// result.forEach(info -> System.out.println(info.getDescription()));

	}
}
