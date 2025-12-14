package Abit.abitcll;

public class Abiturient {
	private int id;
	private String name;
	private String surname;
	private String fath_name;
	private String adress;
	private String phone_number;
	private double assesment;
		
	public Abiturient() {
		this.id = 1;
		this.name = "Ім'я";
		this.surname = "Прізвище";
		this.fath_name = "По-батькові";
		this.adress = "Адреса";
		this.phone_number = "+0 00 000 00 00";
		this.assesment = 67.67;
	}
	
	public Abiturient(int id, String name, String surname, String fath_name, String adress, String phone_number, double assesment) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.fath_name = fath_name;
		this.adress = adress;
		this.phone_number = phone_number;
		this.assesment = assesment;
	}
	
	public int getID() {
		return this.id;
	}
	public void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getFath_Name() {
		return this.fath_name;
	}
	public void setFath_name(String fath_name) {
		this.fath_name = fath_name;
	}
	
	public String getAdress() {
		return this.adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	public String getNumber() {
		return this.phone_number;
	}
	public void setNumber(String phone_number) {
		this.phone_number = phone_number;
	}
	
	public double getAssesment() {
		return this.assesment;
	}
	public void setAssesment(double assesment) {
		this.assesment = assesment;
	}
	
	public boolean isNamesake(String name) {
		if (this.name.equalsIgnoreCase(name)) 
			return true;
		else
			return false;
	}
	
	public boolean isHigher(double mark) {
		if (this.assesment > mark) 
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		return "Абітурієнт #" + this.id + ": " + this.surname + " " + this.name + " " +
				this.fath_name + ", " + this.adress + ", " + this.phone_number + ", Оцінка: " + this.assesment; 
	}
}
