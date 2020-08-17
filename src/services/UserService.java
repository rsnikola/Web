package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Data;
import model.User;
import model.enumerations.Role;
import utility.Utility;

@Path("/users")
public class UserService {
	
	public UserService () {
//		System.out.println("User service");
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, User> loadUsers () {
		
//		System.out.println("User service : test");
		
		return Data.getUsers();
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role login (@Context HttpServletRequest request) throws IOException {
		
//		System.out.println("User service : login");

		if (request.getSession().getAttribute("username") != null) {
			return null;
		}
		
		Map<String, String> requestData = Utility.getBodyMap(request);
		if (Data.getUsers().containsKey(requestData.get("username"))) {
			if (Data.getUsers().get(requestData.get("username")).getPassword().equals(requestData.get("password"))) {
				request.getSession().setAttribute ("username", requestData.get("username"));
				return Data.getUsers().get(requestData.get("username")).getRole();
			}
		}
		return null;
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean logout (@Context HttpServletRequest request) {
		
//		System.out.println("User service : logout");

		if (request.getSession().getAttribute("username") != null) {
			System.out.println("Logging " + request.getSession().getAttribute("username") + " out");
			request.getSession().setAttribute ("username", null);
			return true;
		}
		
		return false;
	}
	
	@GET
	@Path("/isLoggedIn")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role isLoggedIn (@Context HttpServletRequest request) {
		
//		System.out.println("User service : isLoggedIn");

		if (request.getSession().getAttribute("username") != null) {
			return Data.getUsers().get(request.getSession().getAttribute("username")).getRole();
		}
		
		return null;
	}
	
	@GET
	@Path("/details")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User getLoggedUser (@Context HttpServletRequest request) {
		
		if (request.getSession().getAttribute("username") != null) {
			return Data.getUsers().get(request.getSession().getAttribute("username"));
		}
		
		return null;
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void update (@Context HttpServletRequest request) throws IOException {
		
//		System.out.println("User service : login");

		if (request.getSession().getAttribute("username") == null) {
			return;
		}
		
		Map<String, String> requestData = Utility.getBodyMap(request);
		for (String s : requestData.keySet()) {
			System.out.println(s + ": " + requestData.get(s));
		}
		if (request.getSession().getAttribute("username") != null) {
			User currentUser = Data.getUsers().get(request.getSession().getAttribute("username"));
			System.out.println("Email to update: " + currentUser.getUsername());
			currentUser.setFirstName(requestData.get("firstName"));
			currentUser.setLastName(requestData.get("lastName"));
			currentUser.setPassword(requestData.get("password"));
			currentUser.setMale((requestData.get("male").equals("true")) ? (true) : (false));
			Data.saveUsers();
		}
		else {
			return;
		}
	}
	
	@PUT
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean register (@Context HttpServletRequest request) throws IOException {
		Map<String, String> requestData = Utility.getBodyMap(request);
		// Ukoliko je mail adresa vec zauzeta, ne treba je ponovo dodavati u sistem
		if (Data.getUsers().containsKey(requestData.get("email"))) {
			return false;
		}

		User newUser = new User(requestData.get("email"), requestData.get("newPassword"), 
					requestData.get("firstName"), requestData.get("lastName"), 
					((requestData.get("male").equals("true")) ? (true) : (false)), Role.GUEST);
		Data.getUsers().put(newUser.getUsername(), newUser);
		Data.saveUsers();
		return true;
	}
	
	@GET
	@Path("/page/{p}/{email}/{firstName}/{lastName}/{gender}/{role}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUsers (@Context HttpServletRequest request, 
				@PathParam("p") Integer p, @PathParam("email") String email, 
				@PathParam("firstName") String firstName, @PathParam("lastName") String lastName, 
				@PathParam("gender") String gender, @PathParam("role") String role) {
		ArrayList<User> retVal;
		if (Utility.getRole(request) == Role.ADMIN) {
			ArrayList<User> allUsers = Utility.getAllUsers();
			
			retVal = Utility.paginateUsers(filter(allUsers, email, firstName, 
					lastName, gender, role), p);
		}
		else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();	
	}

	private ArrayList<User> filter (ArrayList<User> users, String email, String firstName, 
				String lastName, String gender, String role) {
		ArrayList<User> retVal = new ArrayList<User> (users);
		if (!email.equals("unfiltered")) {
			retVal = filterByEmail(users, email);
		}
		if (!firstName.equals("unfiltered")) {
			retVal = filterByfirstName(retVal, firstName);
		}
		if (!lastName.equals("unfiltered")) {
			retVal = filterByLastName(retVal, lastName);
		}
		if (!gender.equals("unfiltered")) {
			retVal = filterByGender(retVal, gender);
		}
		if (!role.equals("unfiltered")) {
			retVal = filterByRole(retVal, role);
		}
		
		
		
		return retVal;
	}
	
	private ArrayList<User> filterByEmail (ArrayList<User> users, String email) {
		ArrayList<User> retVal = new ArrayList<User>();
		for (User u : users) {
			if (u.getUsername().toLowerCase().contains(email.toLowerCase())) {
				retVal.add(u);
			}
		}
		return retVal;
	}

	private ArrayList<User> filterByfirstName (ArrayList<User> users, String firstName) {
		ArrayList<User> retVal = new ArrayList<User>();
		for (User u : users) {
			if (u.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
				retVal.add(u);
			}
		}
		return retVal;
	}

	private ArrayList<User> filterByLastName (ArrayList<User> users, String lastName) {
		ArrayList<User> retVal = new ArrayList<User>();
		for (User u : users) {
			if (u.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
				retVal.add(u);
			}
		}
		return retVal;
	}

	private ArrayList<User> filterByGender (ArrayList<User> users, String gender) {
		ArrayList<User> retVal = new ArrayList<User>();
		boolean male = ((gender.equals("true")) ? (true) : (false));
		for (User u : users) {
			if (u.isMale() == male) {
				retVal.add(u);
			}
		}
		return retVal;
	}

	private ArrayList<User> filterByRole (ArrayList<User> users, String role) {
		ArrayList<User> retVal = new ArrayList<User>();
		for (User u : users) {
			if (u.getRole().toString().equals(role)) {
				retVal.add(u);
			}
		}
		return retVal;
	}
	
	@PUT
	@Path("/{username}/{role}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUsers (@Context HttpServletRequest request, 
				@PathParam("username") String username, @PathParam("role") String role) {
		System.out.println("================================================================================");
		System.out.println("Username: " + username);
		System.out.println("Role: " + role);
		System.out.println("================================================================================");
		if (Utility.getRole(request) == Role.ADMIN) {
			User user = Data.getUsers().get(username);
			switch (role) {
				case "ADMIN":
					user.setRole(Role.ADMIN);
					Data.saveUsers();
					break;
				case "GUEST":
					user.setRole(Role.GUEST);
					Data.saveUsers();
					break;
				case "HOST":
					user.setRole(Role.HOST);
					Data.saveUsers();
					break;
				default: 
					return Response.ok(false, MediaType.APPLICATION_JSON).build();
			}
		}
		else { 
			return Response.ok(false, MediaType.APPLICATION_JSON).build();
		}

		
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}

}

