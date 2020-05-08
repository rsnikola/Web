package old.beans;

import java.io.Serializable;

import old.enumarations.Role;

public class User implements Serializable {
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Role role = Role.CUSTOMER;
	private String phone;
	private String city;
	private String email;
	private String registrationDate;
	
	public User() {
	}

	public User(String username, String password, String firstName, String lastName, String email, 
				String role, String phone, String city, String registrationDate) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		switch (role) {
			case "SELLER":
				this.role = Role.SELLER;
				break;
			case "ADMIN":
				this.role = Role.ADMIN;
				break;
			default:
				this.role = Role.CUSTOMER;
		}
		this.phone = phone;
		this.city = city;
		this.registrationDate = registrationDate;
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		
		
		return result;
	}
*/
	
	@Override
	public boolean equals(Object obj) {

		if (! (obj instanceof User)) {
			return false;
		}
		
		User u = (User) obj;
		
		if (!this.username.equals(u.getUsername())) {
			return false;
		}
		if (!this.password.equals(u.getPassword())) {
			return false;
		}
		if (!this.firstName.equals(u.getFirstName())) {
			return false;
		}
		if (!this.lastName.equals(u.getLastName())) {
			return false;
		}
		if (!(this.role == u.getRole())) {
			return false;
		}
		if (!this.phone.equals(u.getPhone())) {
			return false;
		}
		if (!this.city.equals(u.getCity())) {
			return false;
		}
		if (!this.email.equals(u.getEmail())) {
			return false;
		}
		if (!this.registrationDate.equals(u.getRegistrationDate())) {
			return false;
		}
			
		return true;
	}

	@Override
	public String toString() {
		
		String retVal = "";
		
		retVal += "{";
		retVal += "\"username\":\"" + this.username + "\",";
		retVal += "\"password\":\"" + this.password + "\",";
		retVal += "\"firstName\":\"" + this.firstName + "\",";
		retVal += "\"lastName\":\"" + this.lastName + "\",";
		retVal += "\"role\":\"" + this.role + "\",";
		retVal += "\"phone\":\"" + this.phone + "\",";
		retVal += "\"city\":\"" + this.city + "\",";
		retVal += "\"email\":\"" + this.email + "\",";
		retVal += "\"registrationDate\":\"" + this.registrationDate + "\"";
		retVal += "}";
		
		return retVal;
	}

	private static final long serialVersionUID = 6640936480584723344L;

}
