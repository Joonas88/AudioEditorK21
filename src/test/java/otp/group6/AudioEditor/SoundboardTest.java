package otp.group6.AudioEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import otp.group6.AudioEditor.Soundboard.Sample;

/**
 * Test class for sounboard
 * @author Kevin Akkoyun
 *
 */
class SoundboardTest {
	private Soundboard soundboard = new Soundboard();
	
	@DisplayName("Trying to add a sample to sampleArray")
	@Test
	final void sampleTest() {
		assertEquals(soundboard.getSampleArrayLength(), 0, "check if sampleArray is empty");
		
		Sample testSample = soundboard.addSample("/audio/jumalauta.wav");
		
		assertTrue(soundboard.checkSampleArray(testSample),"Checks if sample array contains added sample");
	}
	
	@DisplayName("Removing a sample from sampleArray")
	@Test
	final void sampleRemovalTest() {
		assertEquals(soundboard.getSampleArrayLength(), 0, "check if sampleArray is empty");
		
		Sample testSample = soundboard.addSample("/audio/jumalauta.wav");
		
		assertTrue(soundboard.removeSample(testSample),"removing a sample from sampleArray");
	}
}
