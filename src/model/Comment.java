package model;

public class Comment {

	private Integer id; 
	private String guest;
	private Integer reservation;
	private String text; 
	private Integer rating;
	private boolean deleted = false; 
	private boolean show = false;

	public Comment() {
		
	}
	public Comment(Integer id, String guest, Integer reservation, String text, Integer rating) {
		super();
		this.id = id;
		this.guest = guest;
		this.reservation = reservation;
		this.text = text;
		this.rating = rating;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public Integer getReservation() {
		return reservation;
	}
	public void setReservation(Integer reservation) {
		this.reservation = reservation;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
	
}
