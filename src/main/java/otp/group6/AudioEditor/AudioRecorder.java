package otp.group6.AudioEditor;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 *
 */

//

public class AudioRecorder {
	private AudioFormat format;
	private File targetFile;
	private TargetDataLine line;
	
	public AudioRecorder() {
		this.setFormat(getDefaultAudioFormat());
		
	}
	
	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public File getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(File targetFile) {
		//TODO testiversion poisto
		//this.targetFile = targetFile;
		this.targetFile = new File("src/audio/test3.wav").getAbsoluteFile();
	}
	
	//TODO Tutki mahdollisuutta antaa käyttäjälle muokkaus äänitaajuuteen yms, onko se mahdollista
    public AudioFormat getDefaultAudioFormat(){
        float sampleRate = 44100;
        int sampleSizeBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate,sampleSizeBits,channels,signed, bigEndian);
        return format;
    }
    
    public void recordAudio() {
    	try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            //System.out.println(App.line.isOpen());
            line.start();
            AudioInputStream ais = new AudioInputStream(line);
            AudioFileHandler.SaveFile(ais, targetFile, AudioFileFormat.Type.WAVE);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    }
    
    public void stop(){
        line.stop();
        line.close();
    }

    
    
    
}
