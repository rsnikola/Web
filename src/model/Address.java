package model;

public class Address {

	private Integer id;
	private int streetNumber;
	private String street; 
	private String town; 
	private String zipCode;
	private String country;
	
	public Address() {

	}

	public Address(Integer id, int streetNumber, String street, String town, String zipCode, String country) {
		super();
		this.id = id;
		this.streetNumber = streetNumber;
		this.street = street;
		this.town = town;
		this.zipCode = zipCode;
		this.country = country;
	}
	
	public int getStreetNumber() {
		return streetNumber;
	}
	
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getTown() {
		return town;
	}
	
	public void setTown(String town) {
		this.town = town;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCountry () {
		return country;
	}
	
	public void setCountry (String country) {
		this.country = country;
	}
	
	
}
