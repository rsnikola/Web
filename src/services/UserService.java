package services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import model.Data;
import model.User;
import model.enumerations.Role;
import utility.Utility;

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
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role login (@Context HttpServletRequest request) throws IOException {
		
		System.out.println("User service : login");

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
		
		System.out.println("User service : logout");

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
		
		System.out.println("User service : isLoggedIn");

		if (request.getSession().getAttribute("username") != null) {
			return Data.getUsers().get(request.getSession().getAttribute("username")).getRole();
		}
		
		return null;
	}
	
	
}

