package otp.group6.AudioEditor;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;
public class App {
    AudioInputStream audioInputStream;
    Clip clip;
    Long currentFrame;
    String status;
    static TargetDataLine line;

    //audio tiedosto johon data tallennetaan
    File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    static String path = "src/audio/";
    static Scanner sc = new Scanner(System.in);
    public App() {

    }
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
        try{
            App tt = new App();
                 int i;
                 while (true) {
                	i = sc.nextInt();
                	choice(tt, i);
                	 	if (i==0) {                	
                	 		break;
                	 	}
                 	}

        }catch (Exception ex){
           ex.printStackTrace();
        }
    }
    public void recordAudio(){
    try{
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        App.line = (TargetDataLine) AudioSystem.getLine(info);
        App.line.open(format);
        System.out.println(App.line.isOpen());
        App.line.start();
        AudioInputStream ais = new AudioInputStream(App.line);
        AudioSystem.write(ais, fileType,wavFile);

    }catch(Exception ex){
        ex.printStackTrace();
        }
    }
    public AudioFormat getAudioFormat(){
        float sampleRate = 44100;
        int sampleSizeBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate,sampleSizeBits,channels,signed, bigEndian);
        return format;
    }
    public void stop(){
        App.line.stop();
        App.line.close();
    }
    
    public static void choice(App app, int i) {
    	
    	switch (i) {
    	case 1 : System.out.println("nauhoittaa");
    	createThread(app);
    	break;
    	case 2: System.out.println("seis");
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