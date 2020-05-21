package model;

public class Comment {

	public Comment() {
		
	}
	public Comment(Integer id, Integer guest, Integer apartment, String text, Integer rating) {
		super();
		this.id = id;
		this.guest = guest;
		this.apartment = apartment;
		this.text = text;
		this.rating = rating;
	}
	private Integer id; 
	private Integer guest;
	private Integer apartment;
	private String text; 
	private Integer rating;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGuest() {
		return guest;
	}
	public void setGuest(Integer guest) {
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
	
}
