package dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.Address;
import model.Apartment;
import model.Data;
import model.Location;

public class ApartmentOverviewDTO {

	private Integer id;
	private String type;
	private String firstDate;
	private String lastDate;
	private String rooms;
	private String guests;
	private String address;
	private String owner; 
	private String price;
	
	public ApartmentOverviewDTO (Apartment apartment) {
		this.id = apartment.getId();
		switch (apartment.getType()) {
			case ROOM:
				this.type = "Single room"; 
				break;
			case APARTMENT:
				this.type = "Full apartment";
				break;
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if (apartment.getFirstAvailable() != null) {
			this.firstDate = dateFormat.format(apartment.getFirstAvailable());
		}
		else {
			this.firstDate = "-";
		}
		if (apartment.getLastAvailable() != null) {
			this.lastDate = dateFormat.format(apartment.getLastAvailable());
		}
		else {
			this.lastDate = "-";
		}
		this.rooms = apartment.getRooms() + "";
		this.guests = apartment.getGuests() + "";
		Location loc = Data.getLocations().get(apartment.getLocation());
		Address adr = Data.getAddresses().get(loc.getAddress());
		this.address = adr.getStreetNumber() + " " + adr.getStreet() + " " + 
					adr.getTown() + " " + adr.getCountry();
		this.owner = apartment.getHost();
		this.price = apartment.getPricePerNight() + "";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getRooms() {
		return rooms;
	}
	public void setRooms(String rooms) {
		this.rooms = rooms;
	}
	public String getGuests() {
		return guests;
	}
	public void setGuests(String guests) {
		this.guests = guests;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	
}
