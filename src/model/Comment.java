package model;

public class Comment {

	private Integer id; 
	private String guest;
	private Integer apartment;
	private String text; 
	private Integer rating;
	private boolean deleted = false; 

	public Comment() {
		
	}
	public Comment(Integer id, String guest, Integer apartment, String text, Integer rating) {
		super();
		this.id = id;
		this.guest = guest;
		this.apartment = apartment;
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
	public Integer getApartment() {
		return apartment;
	}
	public void setApartment(Integer apartment) {
		this.apartment = apartment;
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
	
	
}
