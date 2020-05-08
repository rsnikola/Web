package model;

import java.util.ArrayList;

import model.enumerations.Role;

public class User {

	

	private String username;
	private String password;
	private String firstName; 
	private String lastName;
	private boolean isMale; 
	private Role role; 
	
	private ArrayList<Apartment> apartmentsToOffer = new ArrayList<Apartment> (); 
	private ArrayList<Apartment> rentetAppartments = new ArrayList<Apartment> ();
	private ArrayList<Reservation> reservations = new ArrayList<Reservation> ();

	public User() {
		super();
	}

	public User(String username, String password, String firstName, String lastName, boolean isMale, Role role,
			ArrayList<Apartment> apartmentsToOffer, ArrayList<Apartment> rentetAppartments,
			ArrayList<Reservation> reservations) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isMale = isMale;
		this.role = role;
		this.apartmentsToOffer = apartmentsToOffer;
		this.rentetAppartments = rentetAppartments;
		this.reservations = reservations;
	}
	
	public User(String username, String password, String firstName, String lastName, boolean isMale, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isMale = isMale;
		this.role = role;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ArrayList<Apartment> getApartmentsToOffer() {
		return apartmentsToOffer;
	}

	public void setApartmentsToOffer(ArrayList<Apartment> apartmentsToOffer) {
		this.apartmentsToOffer = apartmentsToOffer;
	}

	public ArrayList<Apartment> getRentetAppartments() {
		return rentetAppartments;
	}

	public void setRentetAppartments(ArrayList<Apartment> rentetAppartments) {
		this.rentetAppartments = rentetAppartments;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	
//	private String username;
//	private String password;
//	private String firstName; 
//	private String lastName;
//	private boolean isMale; 
//	private Role role; 
//	
//	private ArrayList<Apartment> apartmentsToOffer = new ArrayList<Apartment> (); 
//	private ArrayList<Apartment> rentetAppartments = new ArrayList<Apartment> ();
//	private ArrayList<Reservation> reservations = new ArrayList<Reservation> ();

}
