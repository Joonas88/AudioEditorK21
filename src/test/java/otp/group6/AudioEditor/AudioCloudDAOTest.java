package otp.group6.AudioEditor;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import otp.group6.AudioEditor.AudioCloudDAO.MixerSetting;
import otp.group6.AudioEditor.AudioCloudDAO.User;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/**
 * 
 * @author Joonas Soininen
 *
 */
class AudioCloudDAOTest {

	private AudioCloudDAO dao = new AudioCloudDAO();
	private AudioCloudDAO.User user =  new User();
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
		user.setUsername(user1);
		assertTrue(dao.chekcforUser(user), "chekforUser(user): true, when the username is in the database");
		user.setUsername(user2);
		assertFalse(dao.chekcforUser(user),"chekforUser(user): false, when the username is not in the database");
	}

	@Test
	@DisplayName("Regitsering a new user")
	@Order(2)
	void testCreateUser() throws SQLException {
		user.setUsername(user2);
		user.setPassword(pw1);
		assertFalse(AudioCloudDAO.isValid(pw1), "isValid(String): false, when the password does not match the requirements");
		user.setPassword(pw2);
		assertTrue(AudioCloudDAO.isValid(pw2), "isValid(String): true, when the password is in a correct format");
		user.setPassword(mySecurePassword);
		user.setSalt(salt);
		assertTrue(dao.createUser(user), "createUser(user): true when there is a problem creatin the user");
	}

	@Test
	@DisplayName("User login")
	@Order(3)
	void testLoginUser() {
		assertEquals("Tervetuloa Joonas", dao.loginUser(user2, pw2), "loginUser(String, String): ");
	}
	
	@Test
	@DisplayName("Creating a new mixer setting")
	@Order(5)
	void testCreateMix() throws SQLException {
		assertTrue(dao.createMix(new MixerSetting("testi", "Filtteriä kuvaava teksti", 1.1, 2.2, 3.3, 4.4, 5.5, 6.6)), "createMix(Mixersetting): true, if the new setting was ok");
	}


	@Test
	@DisplayName("Gettting all registered users")
	void testGetUsers() {
		int expected = 6;
		int actual = dao.getUsers().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	@Test
	@DisplayName("Getting all mixwe settings")
	void testGetAllMixArray() {
		int expected = 4;
		int actual = dao.getAllMixArray().length;
		assertEquals(expected, actual, "Length is set for the testing, must be cheked if tested later on");
	}

	@Test
	@DisplayName("Gettign mixer settings in JSON")
	void testGetAllMixJSON() {
		int expected = 1;
		int actual = dao.getAllMixJSON().size();
		assertEquals(expected, actual, "Only one JSON String should be created");
	}

	@Test
	@DisplayName("Getting specific mixer setting")
	void testGetCertainMixesArray() {
		assertEquals(dao.getCertainMixesArray(1, "69").length, 4, "Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(2, "7").length, 2,"Checks for the amount of entries including specified symbols");
		assertEquals(dao.getCertainMixesArray(3, "uvaava").length, 4, "Checks for the amount of entries including specified symbols");
	}

	@Test
	@DisplayName("Getting specific mixer settings JSON")
	void testGetCertainMixesJSON() {
		assertEquals(dao.getCertainMixesJSON(1, "69").size(), 1);
	}

	@Test
	@DisplayName("Deleting mixer setting")
	@Order(6)
	void testDeleteMix() {
		assertTrue(dao.deleteMix("testi"), "deleteMix(String): true, if the specified mix was deleted");
	}
	
	@Test
	@DisplayName("Deleting the just created user")
	@Order(4)
	void testDeleteUser() {
		assertTrue(dao.deleteUser(user2), "deleteUser(String): true, when user deletion is ok");
	}

	@Test
	@DisplayName("Password is valid")
	@Order(7)
	void testIsValid() {
		assertTrue(AudioCloudDAO.isValid(pw2), "YES YES");
	}

}
