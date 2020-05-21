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
	
	private ArrayList<Integer> apartmentsToOffer = new ArrayList<Integer> (); 
	private ArrayList<Integer> rentetAppartments = new ArrayList<Integer> ();
	private ArrayList<Integer> reservations = new ArrayList<Integer> ();

	public User() {
		super();
	}

	public User(String username, String password, String firstName, String lastName, boolean isMale, Role role,
			ArrayList<Integer> apartmentsToOffer, ArrayList<Integer> rentetAppartments,
			ArrayList<Integer> reservations) {
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

	public ArrayList<Integer> getApartmentsToOffer() {
		return apartmentsToOffer;
	}

	public void setApartmentsToOffer(ArrayList<Integer> apartmentsToOffer) {
		this.apartmentsToOffer = apartmentsToOffer;
	}

	public ArrayList<Integer> getRentetAppartments() {
		return rentetAppartments;
	}

	public void setRentetAppartments(ArrayList<Integer> rentetAppartments) {
		this.rentetAppartments = rentetAppartments;
	}

	public ArrayList<Integer> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Integer> reservations) {
		this.reservations = reservations;
	}
	
}
