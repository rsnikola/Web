package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Apartment;
import model.Data;
import model.Reservation;
import model.User;
import model.enumerations.Role;

public class Utility {

	private static ObjectMapper obj = new ObjectMapper();
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> jsonToMap (String input) throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, String> retVal = obj.readValue(input, HashMap.class);
		return retVal;
	}
	
	public static HashMap<String, String> getBodyMap (HttpServletRequest request) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String line = reader.readLine();
		HashMap<String, String> retVal = jsonToMap(line);
		return retVal;
	}
	
	public static Role getRole (HttpServletRequest request) {
		if (request.getSession().getAttribute("username") == null) {
			return Role.UNREGISTERED;
		}
		else  {
			return Data.getUsers().get(request.getSession().getAttribute("username")).getRole();
		}
	}
	
	public static ArrayList<User> getAllUsers () {
		ArrayList<User> retVal = new ArrayList<User> ();
		for (String s : Data.getUsers().keySet()) {
			if (!Data.getUsers().get(s).isDeleted()) {
				retVal.add(Data.getUsers().get(s));
			}
		}
		return retVal;
	}

	public static ArrayList<User> getMyGuests (String host) {
		HashMap<String, Boolean> userCheck = new HashMap<String, Boolean> ();
		ArrayList<User> retVal = new ArrayList<User> ();
		// Izvuci mi sve goste
		for (User u : Data.getUsers().values()) {
			if (!u.isDeleted()) {
				if (u.getRole() == Role.GUEST) {
					userCheck.put(u.getUsername(), false);
				}
			}
		}
		// Prodji kroz sve moje rezervacije, i u spisku otkaci sve moje goste
		for (Reservation r : Data.getReservations().values()) {
			if (!r.isDeleted()) {
				Apartment a = Data.getApartments().get(r.getApartment());
				if (a != null) {
					if (!a.isDeleted()) {
						if (a.getHost().equals(host)) {
							userCheck.put(r.getGuest(), true);
						}
					}
				}
			}
		}
		for (String s : userCheck.keySet()) {
			if (userCheck.get(s)) {
				retVal.add(Data.getUsers().get(s));
			}
		}
		return retVal;
	}
 	
	public static ArrayList<User> paginateUsers (ArrayList<User> users, Integer page) {
		ArrayList<User> retVal = new ArrayList<User> ();
		for (int i = page * 5; i < page * 5 + 5; ++i) {
			User tempUser;
			System.out.println("i: " + i);
			if (users.size() > i) {
				tempUser = new User(users.get(i));
				tempUser.setPassword(null);
				retVal.add(tempUser);
			}
			else {
				retVal.add(new User());
			};
		}
		return retVal;
	}
	
	public static void printMap (Map<String, String> map) {
		for (String s : map.keySet()) {
			System.out.println("    " + s + ": " + map.get(s));
		}
	}
	
}
