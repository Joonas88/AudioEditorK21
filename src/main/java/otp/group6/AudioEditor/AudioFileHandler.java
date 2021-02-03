package otp.group6.AudioEditor;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.swing.JFileChooser;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 
 * @author Kevin Akkoyun, Joonas Soininen
 * @version 0.1
 *
 */

public class AudioFileHandler{
	
	
	//Constructor
	public void AudioFileHandler() {
		
	}
	

	public static AudioInputStream OpenFile(String name) {
		AudioInputStream a = null;
		try {
			a = AudioSystem.getAudioInputStream(new File(name).getAbsoluteFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} 
		return a;
		
	}
	
	public static void CloseFile(AudioInputStream file) {
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void SaveFile(AudioInputStream audio, File targetFile, Type fileFormat) {
		try {
			AudioSystem.write(audio, fileFormat, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//TODO Tiedostonimi tai Inputstreami! 
	// Muista varmitaa lopullinen versio!
	// Muista varmistaa käyttäjältä ennen tiedoston poistoa!
	public static void DeleteFile(String name) {
		try {
			File tiedosto =  new File(name).getAbsoluteFile();
			tiedosto.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static File openFileExplorer(Window window) {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(window);
		return file;
		
	}
	
}
