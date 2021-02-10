package otp.group6.AudioEditor;

import java.util.ArrayList;

import otp.group6.prototypes.Car;

import java.sql.*;

public class TietokantaDAO {

	private Connection connection;

	// Konstruktori luo yhteyden tietokantaan
	public TietokantaDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// HUOM! database on test! TODO: Muuta polku oikeaan databaseen, kun sellainen
			// on määritetty
			connection = DriverManager.getConnection("jdbc:mysql://localhost:2280/test", "yleinen", "J0k3OnR0");
		} catch (Exception e) {
			// Jos virhe, niin lopetetaan
			System.err.println("Virhe tietokantayhteyden muodostamisessa.");
			System.exit(-1);
		}
	}

	@Override
	// Destruktori
	protected void finalize() {
		try { //
			if (connection != null) {
				System.out.println("Tietokantayhteys suljettu");
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tietokantakysely palauttaa auton tiedot rekisterinumeron perusteella
	 * 
	 * @param regno = rekisterinumero (String)
	 * @return palauttaa kyselyn mukaisen Car-olion tai null
	 */
	public Car readCar(String regno) {
		Car car = null;

		try (PreparedStatement myStatement = connection.prepareStatement("SELECT * FROM ajoneuvo WHERE rekno=?")) {
			myStatement.setString(1, regno);
			// suorita kysely ja ota vastaan tulos
			ResultSet myRs = myStatement.executeQuery();
			if (myRs.next()) { // vain yksi voi löytyä (yksikäsitteinen avain)
				String rs_regno = myRs.getString("rekno");
				String rs_make = myRs.getString("merkki");
				car = new Car(); // luo DTO ja vie saraketiedot ominaisuuskenttiin
				car.setMake(rs_make);
				car.setRegNo(rs_regno);
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		return car; // joko null tai tietokannasta haetut tiedot Car-oliona
	}

	// Palauttaa taulukollisen Car-olioita
	public Car[] readCars(String carMake) {
		ArrayList<Car> list = new ArrayList<Car>();
		try (PreparedStatement myStatement = connection
				.prepareStatement("SELECT * FROM ajoneuvo WHERE merkki LIKE ?")) {
			myStatement.setString(1, carMake);
			ResultSet myRs = myStatement.executeQuery();
			while (myRs.next()) { // nyt tulosjoukossa voi olla useita rivejä
				Car car = new Car(); // luo DTO ja vie saraketiedot ominaisuuskenttiin
				car.setMake(myRs.getString("rekno"));
				car.setRegNo(myRs.getString("merkki"));
				list.add(car); // lisää listaan
			}
		} catch (SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			} while (e.getNextException() != null);
		}
		Car[] returnArray = new Car[list.size()];
		return (Car[]) list.toArray(returnArray); // palauta taulukossa
	}

	public boolean create(Car car) throws SQLException { // vie uusi Car-olio tietokantaan
		// Tarkastetaan löytyykö tietokannasta jo vastaava rekkari
		try (PreparedStatement myStatement = connection.prepareStatement("SELECT * FROM ajoneuvo WHERE rekno = ? ");) {
			myStatement.setString(1, car.getRegNo());
			ResultSet rs = myStatement.executeQuery();

			// Jos ei löydy, niin lisätään tietokantaan
			if (!rs.next()) {
				try (PreparedStatement kysely2 = connection
						.prepareStatement("INSERT INTO ajoneuvo (rekno, merkki) values (?,?)")) {
					kysely2.setString(1, car.getRegNo());
					kysely2.setString(2, car.getMake());
					kysely2.executeUpdate();
					System.out.println("Onnistui");
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
				System.out.println("Tietokannassa jo kyseinen rekkari");
				return false; // palauta true, jos onnistui, muuten false
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Päivittää ajoneuvon merkin
	public boolean update(String rekno, String uusiMerkki) {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE ajoneuvo SET merkki= ? " + "WHERE rekno = ?")) {
			pstmt.setString(1, uusiMerkki);
			pstmt.setString(2, rekno);
			pstmt.executeUpdate();
			System.out.println("Onnistui");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Poistaa ajoneuvon taulukosta rekisterinumeron perusteella
	public boolean delete(String rekno) {
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ajoneuvo WHERE rekno = ?")) {
			statement.setString(1, rekno);
			statement.executeUpdate();
			System.out.println("Onnistui");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		TietokantaDAO dao = new TietokantaDAO();

		Car car = new Car("LOL-125", "Audi=)");

		// auton luonti
		// dao.create(car);

		// auton haku rekkarilla
		// System.out.println(dao.readCar("LOL-125"));

		// samanmerkkisten autojen haku
		Car taulukko[] = dao.readCars("Audi=)");
		for (Car car2 : taulukko) {
			System.out.println(car2);
		}

		// Merkin päivittäminen
		// dao.update("LOL-123", "Mersu");

		// Poisto
		// dao.delete("LOL-125");

	}

}
