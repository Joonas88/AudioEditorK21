package otp.group6.AudioEditor;

public class Car {
	
/*
 * TÄMÄ LUOKKA ON LUOTU VAIN TIETOKANNAN TESTAUSTA VARTEN!
 * Poista, kun tietokanta määritetty sovelluksen mukaiseksi.
 */
	
	private String maker;
	private String regNo;
	
	public Car(String regNo, String maker) {
		this.maker = maker;
		this.regNo = regNo;
	}
	
	public Car() {
		
	}
	
	public String getMake() {
		return maker;
	}
	
	public String getRegNo() {
		return this.regNo;
	}
		
	public void setMake(String string) {
		this.maker = string;
		
	}

	public void setRegNo(String string) {
		this.regNo = string;
		
	}
	
	@Override
	public String toString() {
		return maker + ' ' + regNo; 
	}

}
