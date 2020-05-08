package old.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import old.Utility.Parser;
import old.beans.User;
import old.dao.UserDAO;

@Path("/")
public class LoginService {
	
	private UserDAO userDAO = new UserDAO();
	
	@Context
	ServletContext ctx;
	
	public LoginService() {
		System.out.println("'Postojim' - LoginService");
	}
	
	@PostConstruct
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	public void init() {
		// Ovaj objekat se instancira vi�e puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("userDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String logIn(@Context HttpServletRequest request) throws IOException {
		
		StringBuilder buffer = new StringBuilder();
		InputStream is = request.getInputStream();
		BufferedReader reader = new BufferedReader (new InputStreamReader(is));
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		Parser p = new Parser();
		HashMap<String, String> pairs = p.parse(buffer.toString());
		String username = pairs.get("username");
		String password = pairs.get("password");
		
		User u = userDAO.matches(username, password);
		request.getSession().setAttribute("logged_user", u);
		if (u != null) {
			System.out.println("Login: " + u.getUsername() + " uspeo!");
			return "{\"username\" : \"" + u.getUsername() + "\"}";
		}
		System.out.println("Login: " + username + " nije uspeo!");
		return null;
	}
	
	@POST
	@Path("logout")
	public void logout(@Context HttpServletRequest request) {
		request.getSession().setAttribute("logged_user", null);
	}
	
	@GET
	@Path("logged")
	@Produces(MediaType.APPLICATION_JSON)
	public String logged (@Context HttpServletRequest request) {
		
		if (request.getSession().getAttribute("logged_user") != null) {
			System.out.println(request.getSession().getAttribute("logged_user"));
			String s = request.getSession().getAttribute("logged_user").toString().split(",")[0].split(":")[1];
			return "{ \"username\" : " + s + " }";
		}
		return null;
	}
	
	@POST
	@Path("register")
	
	public String register (@Context HttpServletRequest request) {
		
		
		
		return null;
	}
	
}
