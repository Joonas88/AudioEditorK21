package otp.group6.AudioEditor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

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
	 * User class is used to hold on logged in user
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class User {

		private static String user;

		public void setUser(String user) {
			User.user = user;
		}
		
		public String getUser() {
			return User.user;
		}

	}
	
	/**
	 * MixerSettings class is used to store the database data in and pass it to the view
	 * 
	 * @author Joonas Soininen
	 *
	 */
	public static class MixerSetting {
		
		
		private String mixName, description, dateDAO, creatorName;
		private double mix1, mix2, mix3, mix4, mix5, mix6;
		private int mixID;
		
		public MixerSetting( ) {			
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


		@Override
		public String toString() {
			return "MixerSetting date=" + dateDAO + ", mixName=" + mixName + ", description=" + description + ", Creator="
					+creatorName+", mix1="
					+ mix1 + ", mix2=" + mix2 + ", mix3=" + mix3 + ", mix4=" + mix4 + ", mix5=" + mix5 + ", mix6="
					+ mix6+ "mixID="+mixID;
		}

		public int getMixID() {
			return mixID;
		}

		public void setMixID(int mixID) {
			this.mixID = mixID;
		}

	}

	private Connection databaseConnection;
	private User userclass = new User();
	

	
	/**
	 * Connection to the database
	 */
	public AudioCloudDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			databaseConnection = DriverManager.getConnection("jdbc:mysql://172.17.0.1/audiocloud", "yleinen", "J0k3OnR0");
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
	public boolean chekcforUser(String user) {
		
		try (PreparedStatement myStatement =  databaseConnection.prepareStatement("SELECT * FROM accountsTEST WHERE username = ? ");){
			myStatement.setString(1, user);
			ResultSet rset = myStatement.executeQuery();
			if (!rset.next()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		
	}

	/**
	 * Method is used to deliver a new user in to the database. Method also makes sure that there are no duplicate users.
	 * @param user, new user name to be inputed into the database
	 * @param pw, hashed password
	 * @param salt, decipher key for the password
	 * @return true or false according to completion of the code
	 * @throws SQLException
	 */
	public boolean createUser(String user, String pw) throws SQLException {
		String salt = PasswordUtils.getSalt(30); 
		String securePW = PasswordUtils.generateSecurePassword(pw, salt);
		

				try (PreparedStatement query = databaseConnection.prepareStatement("INSERT INTO accountsTEST values (?,?,?,?)")) {
					query.setString(1, null);
					query.setString(2, user);
					query.setString(3, securePW);
					query.setString(4, salt);
					query.executeUpdate();
					return true;
				} catch (SQLException e) {
					do {
						System.err.println("Viesti: " + e.getMessage());
						System.err.println("Virhekoodi: " + e.getErrorCode());
						System.err.println("SQL-tilakoodi: " + e.getSQLState());
					} while (e.getNextException() != null);
				}
				return false;

	}
			
	/** 
	 * Method to login and check for correct credentials
	 * @param u user name
	 * @param p password
	 */
	public String loginUser(String u, String p){

		try (PreparedStatement myStatement = databaseConnection.prepareStatement("SELECT username, password, salt FROM accountsTEST WHERE username = ?");) {
			myStatement.setString(1, u);
			ResultSet rset = myStatement.executeQuery();
			
			if (!rset.next()) {
				// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
				//JOptionPane.showMessageDialog(null, "Validointi ei onnistu, koita uudelleen?"); //Ei voi kertoa ettei tunnusta ole //Nämä ponnahtaa myös testeissä!
				System.out.println("Käyttäjätunnusta ei ole olemassa"); //Poistettava
				return "No user";
			}
			
			String pw = rset.getString("password");
			String salt = rset.getString("salt");
			
			boolean pwMatch = PasswordUtils.verifyUserPassword(p, pw, salt);
			
			if (pwMatch) {
				// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
				//JOptionPane.showMessageDialog(null, "Tervetuloa! "+rset.getString("username")); //Nämä ponnahtaa myös testeissä!
				System.out.println("Tervetuloa "+rset.getString("username"));
				userclass.setUser((rset.getString("username")));
				return "Welcome "+rset.getString("username");
			} else {
				// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
				//JOptionPane.showMessageDialog(null, "Käyttäjätunnus tai salasana väärä!", "HUOMIO!", JOptionPane.WARNING_MESSAGE); //Nämä ponnahtaa myös testeissä!
				System.out.println("Käyttäjätunnus tai salasana väärä!");//Poistettava
				return "Incorrect user or pw";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
		return "Ei voi kirjautua sisälle!"; //Kunnollinen viesti!
	}
	
	public String loggedIn() {
		if ((userclass.getUser()==null)){
			return " ";
		} else {
			return userclass.getUser();
		}
	}
	
	/**
	 * Method to logout the user
	 * @return
	 */
	public boolean logoutUser() {
		if (!(userclass.getUser()==" ")) {
			// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
			userclass.setUser(" ");
			return true;	
		} else {
			// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
			return false;
		}
		
	}
	
	/**
	 * TODO Lopullinen muoto päättämättä!!
	 * 
	 * Create a new mix into the database
	 * @param mixName
	 * @param description
	 * @param mix1
	 * @param mix2
	 * @param mix3
	 * @param mix4
	 * @param mix5
	 * @param mix6
	 * @return true or false according to the process
	 * @throws SQLException
	 */
	public boolean createMix(String mixName, String description, double mix1, double mix2, double mix3, double mix4, double mix5, double mix6) throws SQLException { //Tarkemmat tiedot, kun mixeri on valmis

		LocalDate date =  LocalDate.now(); //TODO Päivämäärä asetetaan jossain muualla?
		
		System.out.println("KIRJATUNUT KÄYTTÄJÄ: "+userclass.getUser());
		
		if (!(userclass.getUser()==" ")) {
			
			try (PreparedStatement query = databaseConnection.prepareStatement("INSERT INTO mixerSETTINGSTEST values (?,?,?,?,?,?,?,?,?,?,?)")) {
				query.setString(1, null);
				query.setString(2, mixName);
				query.setString(3, description);
				query.setString(4, userclass.getUser());
				query.setString(5, date.toString());
				query.setDouble(6, mix1);
				query.setDouble(7, mix2);
				query.setDouble(8, mix3);
				query.setDouble(9, mix4);
				query.setDouble(10, mix5);
				query.setDouble(11, mix6);					
				query.executeUpdate();
				System.out.println("Mix tallennettu!"); //Poistetteava
				return true;
			} catch (SQLException e) {
				do {
					System.err.println("Viesti: " + e.getMessage());
					System.err.println("Virhekoodi: " + e.getErrorCode());
					System.err.println("SQL-tilakoodi: " + e.getSQLState());
				} while (e.getNextException() != null);
			}
			return false;
		} else {
			JOptionPane.showMessageDialog(null, "Not logged in! Please log in to use this function.","Alert",JOptionPane.WARNING_MESSAGE); //Nämä ponnahtaa myös testeissä!
			return false;
		}

	}
	
	/**
	 * Get all users from accounts table, only for development
	 * @return array of users
	 */
	public String[] getUsers() {
		Statement statement =null;
		ResultSet rs =null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			statement = databaseConnection.createStatement();
			rs = statement.executeQuery("SELECT * FROM accountsTEST");
			while (rs.next()) {
				String user = (rs.getString("username"));
				list.add(user);
			}
		} catch(SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		String[] returnArray = new String[list.size()];
		return (String[]) list.toArray(returnArray);
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
				ms.setMixID(rs.getInt("id"));
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
				ms.setMixID(rs.getInt("id"));
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
	 * TODO määritä millä tavalla poistetaan! ID?
	 * Used to delete a mixer setting.
	 * @param specify is a variable that specifies the id.
	 * @return true or false according to the success of the method.
	 */
	public boolean deleteMix(String specify) {
		
		if(!(userclass.getUser()==" ")) {
			try (PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM mixerSETTINGSTEST WHERE mixName = ?")) {
				statement.setString(1, specify);
				statement.executeUpdate();
				// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
				//JOptionPane.showMessageDialog(null, "Poistaminen onnistui! :)"); //Nämä ponnahtaa myös testeissä!
				System.out.println("Mix poistettu!"); //Tämä poistoon
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
				//JOptionPane.showMessageDialog(null, "Epäonnistui poistaminen :(("); //Nämä ponnahtaa myös testeissä!
				return false;
			}
		} else {
			// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
			return false;
		}

	}
	
	/**
	 * Used to delete a user from the database. Only functions for logged users.
	 * @return true or false
	 */
	public boolean deleteUser() {
		
		try (PreparedStatement statement = databaseConnection.prepareStatement("DELETE FROM accountsTEST WHERE username = ?")) {
			statement.setString(1, userclass.getUser());
			statement.executeUpdate();
			// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
			//JOptionPane.showMessageDialog(null, "Käyttäjätunnus poistettu! :)"); //Nämä ponnahtaa myös testeissä!
			System.out.println("Käyttäjä poistettu!"); //Tämä poistoon
			userclass.setUser(" ");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO käyttäjälle palaute, ehkäpä käyttöliittymään, ei tänne :)
			//JOptionPane.showMessageDialog(null, "Ei toimi ei tää poisto ei! :(("); //Nämä ponnahtaa myös testeissä!
			return false;
		}
	}
	
	//TODO Päätä missä salasana määritellään oikeaan muotoon!! Tarvitsee metodin isValid 
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	
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
	
}
