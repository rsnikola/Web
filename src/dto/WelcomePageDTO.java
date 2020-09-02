package dto;

import java.util.ArrayList;

public class WelcomePageDTO {

	private ArrayList<ApartmentOverviewDTO> apartments;
	private boolean hasNextPage;
	private Integer page;
	
	public WelcomePageDTO () {
		this.apartments = new ArrayList<ApartmentOverviewDTO>();
		this.hasNextPage = false;
		this.page = 0;
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
	
	
	
	
}
