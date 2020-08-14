package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Data;
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
			retVal.add(Data.getUsers().get(s));
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
	
}
