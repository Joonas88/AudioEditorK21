package otp.group6.prototypes;

import otp.group6.AudioEditor.AudioMuunnin;

public class TestiMain {

	// float pitchFactor = 0.7f;
	// DelayEffect(0.3, 0.4
	// FlangerEffect(0.02, 0.7, format.getSampleRate(), format.getSampleRate() *
	// 0.4);

	public static void main(String[] args) {


		AudioMuunnin audioMuunnin = new AudioMuunnin(); //Huom! testiaani.wav!

		//audioMuunnin.setAudioFile("src/audio/testiaani.wav");
		 //audioMuunnin.setPitchFactor(4.f);
		audioMuunnin.setDelayEffect(10, 0);
		//audioMuunnin.setFlangerEffect(0.02, 0.7);


		audioMuunnin.playAudio();
		// audioMuunnin.kirjoitaTiedostoon();

	}
}
