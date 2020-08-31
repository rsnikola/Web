package model;

import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;

import model.enumerations.ApartmentType;

public class Apartment {

	
	private Integer id;
	private ApartmentType type;
	private int rooms;
	private int guests;
	private Integer location;
	private Date firstAvailable;
	private Date lastAvailable;
	private String host;
	
	private ArrayList<Integer> comments; 
	
	private double pricePerNight;
	private Time checkinTime;
	private Time checkoutTime;
	private boolean active;
	private boolean deleted = false;
	
	private ArrayList<Integer> amenities = new ArrayList<Integer>();
	private ArrayList<Integer> reservations;
	
	public Apartment() {
		
	}
	
	public Apartment(Integer id, ApartmentType type, int rooms, int guests, Integer location, Date firstAvailable,
			Date lastAvailable, String host, ArrayList<Integer> comments, double pricePerNight, Time checkinTime,
			Time checkoutTime, boolean active, ArrayList<Integer> amenities, ArrayList<Integer> reservations) {
		super();
		this.id = id;
		this.type = type;
		this.rooms = rooms;
		this.guests = guests;
		this.location = location;
		this.firstAvailable = firstAvailable;
		this.lastAvailable = lastAvailable;
		this.host = host;
		this.comments = comments;
		this.pricePerNight = pricePerNight;
		this.checkinTime = checkinTime;
		this.checkoutTime = checkoutTime;
		this.active = active;
		this.amenities = amenities;
		this.reservations = reservations;
	}
	
	public Apartment(Integer id, ApartmentType type, int rooms, int guests, Integer location, String host) {
		this.id = id;
		this.type = type;
		this.rooms = rooms;
		this.guests = guests;
		this.location = location;
		this.host = host;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ApartmentType getType() {
		return type;
	}
	public void setType(ApartmentType type) {
		this.type = type;
	}
	public int getRooms() {
		return rooms;
	}
	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	public int getGuests() {
		return guests;
	}
	public void setGuests(int guests) {
		this.guests = guests;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public Date getFirstAvailable() {
		return firstAvailable;
	}
	public void setFirstAvailable(Date firstAvailable) {
		this.firstAvailable = firstAvailable;
	}
	public Date getLastAvailable() {
		return lastAvailable;
	}
	public void setLastAvailable(Date lastAvailable) {
		this.lastAvailable = lastAvailable;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public ArrayList<Integer> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Integer> comments) {
		this.comments = comments;
	}
	public double getPricePerNight() {
		return pricePerNight;
	}
	public void setPricePerNight(double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}
	public Time getCheckinTime() {
		return checkinTime;
	}
	public void setCheckinTime(Time checkinTime) {
		this.checkinTime = checkinTime;
	}
	public Time getCheckoutTime() {
		return checkoutTime;
	}
	public void setCheckoutTime(Time checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ArrayList<Integer> getAmenities() {
		return amenities;
	}
	public void setAmenities(ArrayList<Integer> amenities) {
		this.amenities = amenities;
	}
	public ArrayList<Integer> getReservations() {
		return reservations;
	}
	public void setReservations(ArrayList<Integer> reservations) {
		this.reservations = reservations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
