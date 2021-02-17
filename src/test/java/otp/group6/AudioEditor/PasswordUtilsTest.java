package otp.group6.AudioEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 * 
 * @author Joonas Soininen
 *
 */
class PasswordUtilsTest {

	private PasswordUtils pwtest = new PasswordUtils();
	private String pw = "Password";	
	private String salt = PasswordUtils.getSalt(30);
	private String securepw = PasswordUtils.generateSecurePassword(pw, salt);
	@Test
	final void testGetSalt() {
		assertEquals(salt.length(), PasswordUtils.getSalt(30).length(), "Comparison of lenghts in salt");
	}

	@Test
	final void testGenerateSecurePassword() {
		assertEquals(securepw.length(), PasswordUtils.generateSecurePassword(pw, salt).length(), "Comparison of lenghts with passwords");
	}

	@Test
	final void testVerifyUserPassword() {
		assertTrue(PasswordUtils.verifyUserPassword(pw, securepw, salt), "True if the password is the secured one");
	}

}
