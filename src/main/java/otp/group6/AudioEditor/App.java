package otp.group6.AudioEditor;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;
public class App {
    AudioInputStream audioInputStream;
    Clip clip;
    Long currentFrame;
    String status;
    TargetDataLine line;

    //audio tiedosto johon data tallennetaan
    File wavFile = new File("src/audio/test3.wav").getAbsoluteFile();

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    static String path = "src/audio/";
    static Scanner sc = new Scanner(System.in);
    public App() {

    }
    public static void main(String[] args){
        try{
            App tt = new App();
            Thread stopper = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(5000);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                 tt.stop();
                }
            });
            stopper.start();
            tt.recordAudio();

        }catch (Exception ex){
           ex.printStackTrace();
        }
    }
    public void recordAudio(){
    try{
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        System.out.println(line.isOpen());
        line.start();
        AudioInputStream ais = new AudioInputStream(line);
        AudioInputStream monoAis = convertToMono(ais);
        AudioSystem.write(monoAis, fileType,wavFile);

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
        line.stop();
        line.close();
    }
    
    //Muuttaa äänen stereosta monoksi
    public static AudioInputStream convertToMono(AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();

        // is already mono?
        if(sourceFormat.getChannels() == 1) {
            return sourceStream;
        }

        AudioFormat targetFormat = new AudioFormat(
                sourceFormat.getEncoding(),
                sourceFormat.getSampleRate(),
                sourceFormat.getSampleSizeInBits(),
                1,
                // this is the important bit, the framesize needs to change as well,
                // for framesize 4, this calculation leads to new framesize 2
                (sourceFormat.getSampleSizeInBits() + 7) / 8,
                sourceFormat.getFrameRate(),
                sourceFormat.isBigEndian());
        return AudioSystem.getAudioInputStream(targetFormat, sourceStream);
    }
}