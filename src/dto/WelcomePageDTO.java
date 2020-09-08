package dto;

import java.util.ArrayList;

import model.Amenity;

public class WelcomePageDTO {

	private ArrayList<ApartmentOverviewDTO> apartments;
	private boolean hasNextPage;
	private Integer page;
	private ArrayList<Amenity> amenities;
	
	public WelcomePageDTO () {
		this.apartments = new ArrayList<ApartmentOverviewDTO>();
		this.hasNextPage = false;
		this.page = 0;
		amenities = new ArrayList<Amenity> ();
	}
	
	public ArrayList<ApartmentOverviewDTO> getApartments() {
		return apartments;
	}
	public void setApartments(ArrayList<ApartmentOverviewDTO> apartments) {
		this.apartments = apartments;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}

	public ArrayList<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		this.amenities = amenities;
	}

	
	
	
	
	
}
