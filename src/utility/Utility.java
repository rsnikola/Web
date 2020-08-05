package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Data;
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
	
	
}
