package dto;

import java.util.ArrayList;

public class ReservationPageDTO {

	private ArrayList<ReservationDTO> reservations;
	private boolean hasNextPage;
	private Integer page;
	
	public ReservationPageDTO () {
		this.reservations = new ArrayList<ReservationDTO>();
		this.hasNextPage = false;
		this.page = 0;
	}
	
	public ArrayList<ReservationDTO> getReservations() {
		return reservations;
	}
	public void setReservations(ArrayList<ReservationDTO> reservations) {
		this.reservations = reservations;
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
