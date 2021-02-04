package otp.group6.AudioEditor;


public class TestiMain {

	// float pitchFactor = 0.7f;
	// DelayEffect(0.3, 0.4
	// FlangerEffect(0.02, 0.7, format.getSampleRate(), format.getSampleRate() *
	// 0.4);

	public static void main(String[] args) {

		AudioMuunnin audioMuunnin = new AudioMuunnin("src/audio/testiaani.wav"); //Huom! testiaani.wav!

		// audioMuunnin.setPitchFactor(0.5f);
		//audioMuunnin.setDelayEffect(10, 0);
		//audioMuunnin.setFlangerEffect(0.02, 0.7);

		audioMuunnin.playAudio();
		//audioMuunnin.kirjoitaTiedostoon();

	}
}
