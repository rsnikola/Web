package model;

import java.util.Date;

import model.enumerations.ReservationStatus;

public class Reservation {


	private Integer id;
	private Integer apartment;
	private String guest;
	private Date start;
	private Date end; 
	private double price;
	private String reservationMessage;
	private ReservationStatus status;
	private boolean deleted = false;
	
	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reservation(Integer id, Integer apartment, String guest, Date start, Date end, double price,
			String reservationMessage, ReservationStatus status) {
		super();
		this.id = id;
		this.apartment = apartment;
		this.guest = guest;
		this.start = start;
		this.end = end;
		this.price = price;		// Total price
		this.reservationMessage = reservationMessage;
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getReservationMessage() {
		return reservationMessage;
	}
	public void setReservationMessage(String reservationMessage) {
		this.reservationMessage = reservationMessage;
	}
	public ReservationStatus getStatus() {
		return status;
	}
	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
