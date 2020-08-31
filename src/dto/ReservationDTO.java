package dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.Address;
import model.Apartment;
import model.Comment;
import model.Data;
import model.Location;
import model.Reservation;

public class ReservationDTO {

	private Integer id;
	private String address;
	private Integer apartment;
	private String guest;
	private String startDate; 
	private String endDate;
	private Double price;
	private String status;
	private String comment; 
	private Integer rating;
	private Integer commentId;
	
	public ReservationDTO (Reservation reservation) {
		this.id = reservation.getId();
		Apartment apartment = Data.getApartments().get(reservation.getApartment());
		Location loc = Data.getLocations().get(apartment.getLocation());
		Address address = Data.getAddresses().get(loc.getAddress());
		this.address = address.getStreetNumber() + " " + address.getStreet() + " " + address.getTown() + " " + address.getCountry();
		this.apartment = apartment.getId();
		this.guest = reservation.getGuest();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if (reservation.getStart() != null) {
			this.startDate = dateFormat.format(reservation.getStart());
		}
		else {
			this.startDate = "-";
		}
		if (reservation.getEnd() != null) {
			this.endDate = dateFormat.format(reservation.getEnd());
		}
		else {
			this.endDate = "-";
		}
		this.price = reservation.getPrice();
		switch (reservation.getStatus()) {
			case CREATED:
				this.status = "Created";
				break;
			case DENIED: 
				this.status = "Denied";
				break;
			case CANCELED: 
				this.status = "Canceled"; 
				break;
			case ACCEPTED: 
				this.status = "Accepted";
				break;
			case FINISHED: 
				this.status = "Finished";
				break;
		}
		Comment comment = null;
		for (Comment c : Data.getComments().values()) {
			if (c.getReservation() == reservation.getId()) {
				if (!c.isDeleted()) {
					comment = c;
					break;
				}
			}
		}
		if (comment == null) {
			this.comment = "-";
			this.rating = 0;
			this.commentId = 0;
		}
		else if (comment.isDeleted()) {
			this.comment = "-";
			this.rating = 0;
			this.commentId = 0;
		}
		else {
			this.comment = comment.getText();
			this.rating = comment.getRating();
			this.commentId = comment.getId();
		}
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getApartment() {
		return apartment;
	}
	public void setApartment(Integer apartment) {
		this.apartment = apartment;
	}
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	
	
	
}
