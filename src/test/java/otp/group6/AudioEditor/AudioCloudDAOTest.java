package otp.group6.AudioEditor;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioCloudDAO.User;
import org.junit.jupiter.api.Disabled;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/**
 * 
 * @author Joonas Soininen
 *
 */
@Disabled
class AudioCloudDAOTest {

	private AudioCloudDAO dao = new AudioCloudDAO();
	private final String user1 = "test1";
	private final String user2 = "Joonas";
	private final String pw1 = "test";
	private final String pw2 = "Password1!";
	private final String salt = PasswordUtils.getSalt(30);
	String mySecurePassword = PasswordUtils.generateSecurePassword(pw2, salt);
	

	@Test
	@DisplayName("Cheking the database for availability in the username")
	@Order(1)
	void testChekcforUser() {		
		assertTrue(dao.chekcforUser(user1), "chekforUser(user): true, when the username is in the database");		
		assertFalse(dao.chekcforUser(user2),"chekforUser(user): false, when the username is not in the database");
	}

	@Test
	@DisplayName("Regitsering a new user")
	@Order(2)
	void testCreateUser() throws SQLException {			
		assertFalse(AudioCloudDAO.isValid(pw1), "isValid(String): false, when the password does not match the requirements");		
		assertTrue(AudioCloudDAO.isValid(pw2), "isValid(String): true, when the password is in a correct format");				
		assertTrue(dao.createUser(user2, salt), "createUser(user): true when there is a problem creatin the user");
	}

	@Test
	@DisplayName("User login")
	@Order(3)
	void testLoginUser() {
		assertEquals("Tervetuloa Joonas", dao.loginUser(user2, pw2), "loginUser(String, String): ");
	}
	
	@Test
	@DisplayName("Creating a new mixer setting")
	@Order(6)
	void testCreateMix() throws SQLException {
		assertFalse(dao.createMix("testi", "Filtteriä kuvaava teksti", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6), "createMix(Mixersetting): false, User needs to be logged in");
		dao.loginUser("test6", "Example1!");
		assertTrue(dao.createMix("testi", "Filtteriä kuvaava teksti", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6), "createMix(Mixersetting): true, when setting was created");
	}

	@Test
	@DisplayName("Logout test")
	@Order(4)
	void testLogout() {
		assertTrue(dao.logoutUser(), "logoutUser(): true, if the logout was a success");
	}

	@Test
	@DisplayName("Getting all registered users")
	void testGetUsers() {
		int expected = 8;
		int actual = dao.getUsers().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	@Test
	@DisplayName("Getting all mixer settings")
	void testGetAllMixArray() {
		int expected = 4;
		int actual = dao.getAllMixArray().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	@Test
	@DisplayName("Getting specific mixer setting")
	void testGetCertainMixesArray() {
		assertEquals(dao.getCertainMixesArray(1, "69").length, 4, "Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(2, "7").length, 2,"Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(3, "uvaava").length, 4, "Checks for the amount of entries including specified symbols");
	}

	@Test
	@DisplayName("Deleting mixer setting")
	@Order(7)
	void testDeleteMix() {
		assertTrue(dao.deleteMix("testi"), "deleteMix(String): true, if the specified mix was deleted");
	}
	
	@Test
	@DisplayName("Deleting the just created user")
	@Order(5)
	void testDeleteUser() {
		dao.loginUser(user2, pw2);
		assertTrue(dao.deleteUser(), "deleteUser(): true, when user deletion is ok");
	}

	@Test
	@DisplayName("Password is valid")
	void testIsValid() {
		assertTrue(AudioCloudDAO.isValid(pw2), "YES YES");
	}
	

}
