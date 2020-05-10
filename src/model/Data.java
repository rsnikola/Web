package model;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.enumerations.Role;

public class Data {

//	private static String pathPrefix = "";
	private static String pathPrefix = "../webapps/";
	
	private static HashMap<String, User> users = null;
	private static ObjectMapper obj = new ObjectMapper();
	
	public static void saveUsers () {
		try {
//			String filePath = "../webapps/users.txt";
			String filePath = pathPrefix + "users.txt";
			System.out.println(obj.writeValueAsString(users));
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(users));
		    printWriter.close();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, User> getUsers () {	
		if (users == null) {
			try {
				users = obj.readValue(new File(pathPrefix + "users.txt"), new TypeReference<Map<String, User>>(){});
				System.out.println("Uspeo da ucitam: ");
				for (User u : users.values()) {
					System.out.println(u.getUsername());
				}
			} catch (Exception e) {
				System.out.println("Error while loading users! ");
			
				User u1 = new User("adi1", "1234", "Milan", "Milic", true, Role.ADMIN);
				User u2 = new User("adi2", "4321", "Jovana", "Jovic", false, Role.ADMIN);
				users = new HashMap<String, User> ();
				users.put(u1.getUsername(), u1);
				users.put(u2.getUsername(), u2);
				
				Data.saveUsers();
			}
		}
		return users;
	}
}
