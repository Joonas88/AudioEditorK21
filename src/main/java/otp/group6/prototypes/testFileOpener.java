package otp.group6.prototypes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class testFileOpener extends Thread
{
	public testFileOpener() {
		
	}
	
	@Override
	public void run() {
		try {
			Desktop.getDesktop().open(new File("src/audio").getAbsoluteFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
