package otp.group6.AudioEditor;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.json.simple.*;

import java.sql.*;
import java.time.LocalDate;

/**
 * AudioCloudDAO accesses the database and transports data in and out of it
 * 
 * @author Joonas Soininen
 *
 */
public class AudioCloudDAO {
	
	/**
	 * User class is used to create new users into the database.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class User {

		private String username, password, salt;
			
		public User() {
		}
		
		public User(String username, String password, String salt) {
			this.username=username;
			this.password=password;
			this.salt=salt;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		public void setSalt(String salt) {
			this.salt = salt;
		}
		
		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
		
		public String getSalt() {
			return salt;
		}

		@Override
		public String toString() {
			return "User " + username;
		}
		
		
				
	}
	
	/**
	 * MixerSettings class is used to create new settings for the mixer and store them into the database.
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class MixerSetting {
		
		private LocalDate date =  LocalDate.now();
		private String mixName, description, dateDAO, creatorName;
		private double mix1, mix2, mix3, mix4, mix5, mix6;
		
		public MixerSetting( ) {			
		}
		
		public MixerSetting(String name, String description, double d, double e, double f, double g, double h, double i) {
			this.mixName = name;
			this.description = description;
			this.mix1 = d;
			this.mix2 = e;
			this.mix3 = f;
			this.mix4 = g;
			this.mix5 = h;
			this.mix6 = i;
		}
		
		public void setMixName(String mixName) {
			this.mixName = mixName;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		public void setCreatorName(String creatorName) {
			this.creatorName = creatorName;
		}

		public void setMix1(double d) {
			this.mix1 = d;
		}

		public void setMix2(double mix2) {
			this.mix2 = mix2;
		}

		public void setMix3(double mix3) {
			this.mix3 = mix3;
		}

		public void setMix4(double mix4) {
			this.mix4 = mix4;
		}

		public void setMix5(double mix5) {
			this.mix5 = mix5;
		}

		public void setMix6(double mix6) {
			this.mix6 = mix6;
		}
		
		public void setDateDAO(String daoDate) {
			this.dateDAO=daoDate;
		}
		
		public String getDateDAO() {
			return dateDAO;
		}
		
		public String getMixName() {
			return mixName;
		}
		
		public String getDescription() {
			return description;
		}
		
		public String getCreatorName() {
			return creatorName;
		}

		public double getMix1() {
			return mix1;
		}

		public double getMix2() {
			return mix2;
		}

		public double getMix3() {
			return mix3;
		}

		public double getMix4() {
			return mix4;
		}

		public double getMix5() {
			return mix5;
		}

		public double getMix6() {
			return mix6;
		}
		
		public LocalDate getDate() {
			return date;
		}

		@Override
		public String toString() {
			return "MixerSetting date=" + dateDAO + ", mixName=" + mixName + ", description=" + description + ", Creator="
					+creatorName+", mix1="
					+ mix1 + ", mix2=" + mix2 + ", mix3=" + mix3 + ", mix4=" + mix4 + ", mix5=" + mix5 + ", mix6="
					+ mix6;
		}

	}

	private Connection databaseConnection;
	
	//TODO Päätä missä salasana määritellään oikeaan muotoon!! Tarvitsee metodin isValid 
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	
	/**
	 * Connection to the database
	 */
	public AudioCloudDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:2280/audiocloud", "yleinen", "J0k3OnR0");
		} catch (Exception e) {
			System.err.println("Virhe tietokantayhteyden muodostamisessa.");
			System.exit(-1);
		}
	}
	
	/**
	 * Method to close the database connection
	 */
	@Override
	protected void finalize() {
		try {
			if (databaseConnection != null){
				databaseConnection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to check for user name availability from the database.
	 * @param user is the inputed user name
	 * @return statement return true is the user name exist and false if it is available
	 */
	public boolean chekcforUser(User user) {
		
		try (PreparedStatement myStatement =  databaseConnection.prepareStatement("SELECT * FROM accountsTEST WHERE username = ? ");){
			myStatement.setString(1, user.getUsername());
			ResultSet rset = myStatement.executeQuery();
			if (!rset.next()) {
				return false;
			} else {
				// TODO käyttäjälle palaute
				// JOptionPane.showMessageDialog(null, "Käyttäjänimi ei kelpaa! Valitse toinen :)", "HUOMIO!", JOptionPane.INFORMATION_MESSAGE);//Nämä ponnahtaa myös testeissä!
				System.out.println("KÄYTTÄJÄNIMI JO KÄYTÖSSÄ!");//Poistetteava
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		
	}
	
	/**
	 * Method is used to deliver a new user in to the database. Method also makes sure that there are no dublicate users.
	 * @param user 
	 * @return tells the user that creating a new user has been granted or it has failed
	 * @throws SQLException
	 */
	public boolean createUser(User user) throws SQLException {

				try (PreparedStatement query = databaseConnection.prepareStatement("INSERT INTO accountsTEST values (?,?,?,?)")) {
					query.setString(1, null);
					query.setString(2, user.getUsername());
					query.setString(3, user.getPassword());
					query.setString(4, user.getSalt());
					query.executeUpdate();
					// TODO Käyttäjälle palaute
					//JOptionPane.showMessageDialog(null, "Uusi käyttäjä luotu!"); //Nämä ponnahtaa myös testeissä!
					System.out.println("Uusi käyttäjä luotu!"); //Poistetteava
					return true;
				} catch (SQLException e) {
					do {
						System.err.println("Viesti: " + e.getMessage());
						System.err.println("Virhekoodi: " + e.getErrorCode());
						System.err.println("SQL-tilakoodi: " + e.getSQLState());
					} while (e.getNextException() != null);
				}
				// JOptionPane.showMessageDialog(null, "Jokin meni pahasti vikaan!", "ERROR", JOptionPane.WARNING_MESSAGE);//Nämä ponnahtaa myös testeissä!
				return false;

	}
	
	/**
	 * TODO salasanan kryptaus, token tms markkeri kirjautumisesta, jokin boolean tms true, että saa mahdollisuuden tallentaa.
	 * 
	 * Method to login and check for correct credentials
	 * @param u user name
	 * @param p password
	 */
	public String loginUser(String u, String p){

		try (PreparedStatement myStatement = databaseConnection.prepareStatement("SELECT username, password, salt FROM accountsTEST WHERE username = ?");) {
			myStatement.setString(1, u);
			ResultSet rset = myStatement.executeQuery();

			
			if (!rset.next()) {
				//JOptionPane.showMessageDialog(null, "Validointi ei onnistu, koita uudelleen?"); //Ei voi kertoa ettei tunnusta ole //Nämä ponnahtaa myös testeissä!
				System.out.println("Käyttäjätunnusta ei ole olemassa"); //Poistettava
			}
			
			String pw = rset.getString("password");
			String salt = rset.getString("salt");
			
			boolean pwMatch = PasswordUtils.verifyUserPassword(p, pw, salt);
			
			if (pwMatch) {
				//TODO muuttuja kirjautuneelle käyttäjälle?
				//JOptionPane.showMessageDialog(null, "Tervetuloa! "+rset.getString("username")); //Nämä ponnahtaa myös testeissä!
				System.out.println("Tervetuloa "+rset.getString("username"));
				return "Tervetuloa "+rset.getString("username");
			} else {
				//JOptionPane.showMessageDialog(null, "Käyttäjätunnus tai salasana väärä!", "HUOMIO!", JOptionPane.WARNING_MESSAGE); //Nämä ponnahtaa myös testeissä!
				System.out.println(false);
				return "Käyttäjätunnus tai salasana väärä!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Ei voi kirjautua sisälle!"; //Kunnollinen viesti!
	}
	
	/**
	 * TODO Kirjautuneen aktiivisen käyttäjän haku
	 * 
	 * Create a new mix into the database
	 * @param mix  is an instance of the class and possesses all necessary inputs
	 * @return true or false according to the process
	 * @throws SQLException
	 */
	public boolean createMix(MixerSetting mix) throws SQLException {

				try (PreparedStatement query = databaseConnection.prepareStatement("INSERT INTO mixerSETTINGSTEST values (?,?,?,?,?,?,?,?,?,?,?)")) {
					query.setString(1, null);
					query.setString(2, mix.getMixName());
					query.setString(3, mix.getDescription());
					query.setString(4, "69_-_YBERMIXAAJA_-_69");//TODO Kun kirjautunut käyttäjä, haetaan se tähän!!
					query.setString(5, mix.getDate().toString());
					query.setDouble(6, mix.getMix1());
					query.setDouble(7, mix.getMix2());
					query.setDouble(8, mix.getMix3());
					query.setDouble(9, mix.getMix4());
					query.setDouble(10, mix.getMix5());
					query.setDouble(11, mix.getMix6());					
					query.executeUpdate();
					// TODO Käyttäjälle palaute
					// JOptionPane.showMessageDialog(null, "Mikseriasetus tallennettu! :)"); //Nämä ponnahtaa myös testeissä!
					System.out.println("Mix tallennettu!"); //Poistetteava
					return true;
				} catch (SQLException e) {
					do {
						System.err.println("Viesti: " + e.getMessage());
						System.err.println("Virhekoodi: " + e.getErrorCode());
						System.err.println("SQL-tilakoodi: " + e.getSQLState());
					} while (e.getNextException() != null);
				}
				//TODO Käyttäjälle palaute
//				JOptionPane.showMessageDialog(null, "Tallennus epäonnistui :( Yritä uudelleen!"); //Nämä ponnahtaa myös testeissä!
				return false;
	}
	
	/**
	 * Get all users from accounts table, only for development
	 * @return array of users
	 */
	public User[] getUsers() {
		Statement statement =null;
		ResultSet rs =null;
		ArrayList<User> list = new ArrayList<User>();
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM accountsTEST");
			while (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				list.add(user);
			}
		} catch(SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		User[] returnArray = new User[list.size()];
		return (User[]) list.toArray(returnArray);
	}
	
	/**
	 * Get all mixer settings in an array
	 * @return array of mixer settings
	 */
	public MixerSetting[] getAllMixArray() {
		
		Statement statement =null;
		ResultSet rs =null;
		ArrayList<MixerSetting> list = new ArrayList<MixerSetting>();
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM mixerSETTINGSTEST");

			while (rs.next()) {
				MixerSetting ms = new MixerSetting();		
				ms.setMixName(rs.getString("mixName"));
				ms.setDescription(rs.getString("mixDescribtion"));
				ms.setDateDAO(rs.getString("dateAdded"));
				ms.setCreatorName(rs.getString("mixCreator"));
				ms.setMix1(rs.getDouble("mix1"));
				ms.setMix2(rs.getDouble("mix2"));
				ms.setMix3(rs.getDouble("mix3"));
				ms.setMix4(rs.getDouble("mix4"));
				ms.setMix5(rs.getDouble("mix5"));
				ms.setMix6(rs.getDouble("mix6"));
				list.add(ms);
			}

		} catch(SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		
		MixerSetting[] returnArray = new MixerSetting[list.size()];
		return (MixerSetting[]) list.toArray(returnArray);

	}
	
	/**
	 * Get all mixer settings in JSON
	 * @return JSON of mixer settings
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAllMixJSON() {		
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();		
		Statement statement =null;
		ResultSet rs =null;
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM mixerSETTINGSTEST");
			
			while (rs.next()) {
				JSONObject mixer = new JSONObject();
				mixer.put("ID", rs.getInt("id"));		
				mixer.put("Mix_name", rs.getObject("mixName"));
				mixer.put("Description", rs.getString("mixDescribtion"));
				mixer.put("Date", rs.getString("dateAdded"));
				mixer.put("Creator", rs.getString("mixCreator"));
				mixer.put("Mix_1", rs.getDouble("mix1"));
				mixer.put("Mix_2", rs.getDouble("mix2"));
				mixer.put("Mix_3", rs.getDouble("mix3"));
				mixer.put("Mix_4", rs.getDouble("mix4"));
				mixer.put("Mix_5", rs.getDouble("mix5"));
				mixer.put("Mix_6", rs.getDouble("mix6"));
				jsonArray.add(mixer);
			}
			
		} catch(SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		
		jsonObject.put("Mixer_Settings", jsonArray); // Ei tarpeellinen lisää vain koko listan objektin sisälle

		return jsonObject;
	}
		
	/**
	 * Method is used to get specific mixer setting from a user, including a certain name or something in it's description.
	 * @param select is a variable used to specify what are being searched.
	 * @param specify is a variable that is searched for.
	 * @return returns the search in an array.
	 */
	public MixerSetting[] getCertainMixesArray(int select, String specify) {
		ArrayList<MixerSetting> list = new ArrayList<MixerSetting>();
		String statement = null;
		if (select==1) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixCreator LIKE '%"+specify+"%'";	
		} else if (select==2) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixName LIKE '%"+specify+"%'";
		} else if (select==3) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixDescribtion LIKE '%"+specify+"%'";
		}
		
		try (PreparedStatement query = databaseConnection.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				MixerSetting ms = new MixerSetting();
				ms.setMixName(rs.getString("mixName"));
				ms.setDescription(rs.getString("mixDescribtion"));
				ms.setDateDAO(rs.getString("dateAdded"));
				ms.setCreatorName(rs.getString("mixCreator"));
				ms.setMix1(rs.getDouble("mix1"));
				ms.setMix2(rs.getDouble("mix2"));
				ms.setMix3(rs.getDouble("mix3"));
				ms.setMix4(rs.getDouble("mix4"));
				ms.setMix5(rs.getDouble("mix5"));
				ms.setMix6(rs.getDouble("mix6"));
				list.add(ms);
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		MixerSetting[] returnArray = new MixerSetting[list.size()];
		return (MixerSetting[]) list.toArray(returnArray);
	}
	
	/**
	 * Method searches for specific user, name or description part,
	 * @param select is a variable used to specify what are being searched.
	 * @param specify is a variable that is searched for.
	 * @return returns the search in JSON.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getCertainMixesJSON(int select, String specify) {		
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String statement = null;
		if (select==1) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixCreator LIKE '%"+specify+"%'";	
		} else if (select==2) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixName LIKE '%"+specify+"%'";
		} else if (select==3) {
			statement = "SELECT * FROM mixerSETTINGSTEST where mixDescribtion LIKE '%"+specify+"%'";
		}
		try (PreparedStatement query = databaseConnection.prepareStatement(statement)) {
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				JSONObject mixer = new JSONObject();
				mixer.put("ID", rs.getInt("id"));		
				mixer.put("Mix_name", rs.getObject("mixName"));
				mixer.put("Description", rs.getString("mixDescribtion"));
				mixer.put("Date", rs.getString("dateAdded"));
				mixer.put("Creator", rs.getString("mixCreator"));
				mixer.put("Mix_1", rs.getDouble("mix1"));
				mixer.put("Mix_2", rs.getDouble("mix2"));
				mixer.put("Mix_3", rs.getDouble("mix3"));
				mixer.put("Mix_4", rs.getDouble("mix4"));
				mixer.put("Mix_5", rs.getDouble("mix5"));
				mixer.put("Mix_6", rs.getDouble("mix6"));
				jsonArray.add(mixer);
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}

		jsonObject.put("Mixer_Settings", jsonArray); // Ei tarpeellinen lisää vain koko listan objektin sisälle

		return jsonObject;
		
	}
	
	/**
	 * Used to delete a mixer setting.
	 * @param specify is a variable that specifies the id.
	 * @return true or false according to the success of the method.
	 */
	public boolean deleteMix(String specify) {
		try (PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM mixerSETTINGSTEST WHERE mixName = ?")) {
			statement.setString(1, specify);
			statement.executeUpdate();
			// TODO Käyttäjälle palaute
			//JOptionPane.showMessageDialog(null, "Poistaminen onnistui! :)"); //Nämä ponnahtaa myös testeissä!
			System.out.println("Mix poistettu!"); //Tämä poistoon
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//JOptionPane.showMessageDialog(null, "Epäonnistui poistaminen :(("); //Nämä ponnahtaa myös testeissä!
			return false;
		}
	}
	
	/**
	 * Used to delete a user from the database.
	 * @param specify variable for the user name.
	 * @return true or false
	 */
	public boolean deleteUser(String specify) {
		try (PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM accountsTEST WHERE username = ?")) {
			statement.setString(1, specify);
			statement.executeUpdate();
			// TODO Käyttäjälle palaute
			//JOptionPane.showMessageDialog(null, "Käyttäjätunnus poistettu! :)"); //Nämä ponnahtaa myös testeissä!
			System.out.println("Käyttäjä poistettu!"); //Tämä poistoon
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//JOptionPane.showMessageDialog(null, "Ei toimi ei tää poisto ei! :(("); //Nämä ponnahtaa myös testeissä!
			return false;
		}
	}
	
	/**
	 * TODO Pitää päättää missä tarkitsetaan salasanan oikeellisuus!
	 * Used to check for password security
	 * @param password is the inputed password
	 * @return true if it matches requirements
	 */
	public static boolean isValid(final String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	// TODO filtteriasetuksia lisätessä on käyttäjän kirjauduttava sisälle ja siitä otetaan käyttäjätunnus lisättävän asetuksen mukaan!!
	
	// TESTIYMPÄRISTÖ
	public static void main(String[] args) throws SQLException {

		String salt = PasswordUtils.getSalt(30); //Tämä tarvitaan uuden käyttäjän luomiseksi
		
		AudioCloudDAO dao = new AudioCloudDAO();
		
		User user = new User();
		/*
		boolean testikkeli = true;
		
		while (testikkeli) {
			String newuser = JOptionPane.showInputDialog("Uusi käyttäjä");
			user.setUsername(newuser);
			if (!dao.chekcforUser(user)) {
				

				String newpw = new String(JOptionPane.showInputDialog("Password must contain 8-20 characters, at least 1 uppercase, 1 number, 1 specia", "Example1!"));
				
				if (AudioCloudDAO.isValid(newpw)) {
					String newpw2 = new String(JOptionPane.showInputDialog("Re-enter your password"));
					
					if (!newpw.equals(newpw2)) {
						JOptionPane.showMessageDialog(null, "Salasanat eivät täsmää!", "HUOM!", JOptionPane.ERROR_MESSAGE);
					} else {
						 String mySecurePassword = PasswordUtils.generateSecurePassword(newpw, salt); // TÄMÄ TARVITAAN!!
						 user.setPassword(mySecurePassword);
						 user.setSalt(salt);
						 dao.createUser(user);
						 testikkeli = false;
					}
				} else {
					JOptionPane.showMessageDialog(null, "Salasanan pitää sisältää sitä ja tätä!", "HUOM!", JOptionPane.ERROR_MESSAGE); //Pitää määrittää minkälainen salasana halutaan! Nyt on vitusti liikaa vaatimuksia :D
				}
			}
		}
		
		String userLogin;
		userLogin = JOptionPane.showInputDialog("Käyttäjä");
		String pw;
		pw  = JOptionPane.showInputDialog("Salasana");
		
		dao.loginUser(userLogin, pw);
		
		//Random rand = new Random();
		//int random = rand.nextInt(10000);
		
		//User user = new User("testikäyttäjä1", "salasana", salt);
		
		//MixerSetting mix = new MixerSetting("testi", "Filtteriä kuvaava teksti", 1.1+random, 2.2+random, 3.3+random, 4.4+random, 5.5+random, 6.6+random);				
		
		/*
		if (!dao.chekcforUser(user)) {
			dao.createUser(user);
		}
		*/
		
		//dao.createUser(new User("testUser", "1234"));
		
		//dao.createMix(new MixerSetting("testi", "Filtteriä kuvaava teksti", 1.1+random, 2.2+random, 3.3+random, 4.4+random, 5.5+random, 6.6+random));
		
		//dao.deleteMix("2");
		
		//dao.deleteUser("85");
		
		//dao.loginUser("testikäyttäjä1234", "1234");
		
		/*
		User taulukko[] = dao.getUsers();
		for (User testUser : taulukko) {
			System.out.println(testUser);
		}
		 */
		
		/*
		MixerSetting allList[] = dao.getAllMixArray();
		for (MixerSetting ms : allList) {
			System.out.println(ms);
		}
		*/			
		
		/*
		MixerSetting someList[] = dao.getCertainMixesArray(2,"8");
		for (MixerSetting ms : someList) {
			System.out.println(ms);
		}
		*/
		
		//System.out.println(dao.getCertainMixesJSON(1, "69"));
		
		//System.out.println(dao.getAllMixJSON());				
	}
	
}
