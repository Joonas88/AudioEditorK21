package otp.group6.prototypes;

import javax.sound.sampled.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class App {
	AudioInputStream audioInputStream;
	Clip clip;
	Long currentFrame;
	String status;
	static TargetDataLine line;

	// audio tiedosto johon data tallennetaan
	File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();

	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	static String path = "src/audio/";
	static Scanner sc = new Scanner(System.in);

	public App() {

	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			App tt = new App();
			int i;
			while (true) {
				i = sc.nextInt();
				choice(tt, i);
				if (i == 0) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void recordAudio() {
		try {

			FileOutputStream fs = new FileOutputStream(new File("/tmp/music.input"));

			AudioInputStream ais = AudioSystem
					.getAudioInputStream(new File("src/audio/jumalauta.wav").getAbsoluteFile());

			// AudioFormat format = getAudioFormat();
			// System.out.print(format.getFrameSize());

			// DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			// SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			// Line.Info li = sourceLine.getLineInfo();

			// sourceLine.open(format);
			// sourceLine.start();
			DataInputStream dis = new DataInputStream(ais);
			AudioFormat format = ais.getFormat();
			byte[] b = new byte[(int) ais.getFrameLength() * format.getFrameSize()];
			dis.readFully(b);
			for (byte by : b) {
				System.out.println(by);
				fs.write(by);
				System.out.println("kirjoitettu");
			}
			fs.close();
			System.out.println("Done");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
		return format;
	}

	public void stop() {
		App.line.stop();
		App.line.close();
	}

	public static void choice(App app, int i) {

		switch (i) {
		case 1:
			System.out.println("nauhoittaa");
			createThread(app);
			break;
		case 2:
			System.out.println("seis");
			app.stop();
			break;
		}
	}

	public static void createThread(App app) {
		Thread stopper = new Thread(new Runnable() {
			@Override
			public void run() {
				app.recordAudio();
			}
		});
		stopper.start();
	}
}