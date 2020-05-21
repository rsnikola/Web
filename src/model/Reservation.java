package model;

import java.sql.Date;

public class Reservation {

	public Reservation(Integer id, Integer apartment, Date start, Date end) {
		super();
		this.id = id;
		this.apartment = apartment;
		this.start = start;
		this.end = end;
	}
	public Reservation() {
		
	}
	Integer id;
	private Integer apartment; 
	private Date start;
	private Date end;
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
	
}
