package services;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Data;
import model.User;

@Path("/users")
public class UserService {

	public UserService () {
		System.out.println("User service");
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String, User> loadUsers () {
		
		System.out.println("User service : test");
		
		return Data.getUsers();
	}
	
}
