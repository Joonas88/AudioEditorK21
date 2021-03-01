package otp.group6.prototypes;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import otp.group6.AudioEditor.AudioCloudDAO;
import otp.group6.AudioEditor.PasswordUtils;
import otp.group6.AudioEditor.AudioCloudDAO.User;

public class AudioCloudDAOTest {

	// Simppeli testiluokka. Ajettaessa pyytää lisäämään uuden käyttäjän ja salasanan, sen jälkeen pyytää kirjautumaan!
	// HUOM! Vaatii tietokantayhteyden!!!
	public static void main(String[] args) throws SQLException {
		AudioCloudDAO dao = new AudioCloudDAO();
			
		String salt = PasswordUtils.getSalt(30); 
		
		boolean testikkeli = true;
		
		while (testikkeli) {
			String newuser = JOptionPane.showInputDialog("Uusi käyttäjä");
			if (!dao.chekcforUser(newuser)) {
				

				String newpw = new String(JOptionPane.showInputDialog("Password must contain 8-20 characters, at least 1 uppercase, 1 number, 1 specia", "Example1!"));
				
				if (AudioCloudDAO.isValid(newpw)) {
					String newpw2 = new String(JOptionPane.showInputDialog("Re-enter your password"));
					
					if (!newpw.equals(newpw2)) {
						JOptionPane.showMessageDialog(null, "Salasanat eivät täsmää!", "HUOM!", JOptionPane.ERROR_MESSAGE);
					} else {
						 String mySecurePassword = PasswordUtils.generateSecurePassword(newpw, salt); // TÄMÄ TARVITAAN!!
						 dao.createUser(newuser, mySecurePassword, salt);
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

	}

}
