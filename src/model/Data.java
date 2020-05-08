package model;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.enumerations.Role;

public class Data {

	private static HashMap<String, User> users = null;
	
	public static void saveUsers () {
		ObjectMapper obj = new ObjectMapper();
		try {
			String fileName = "../webapps/users.txt";
			System.out.println(obj.writeValueAsString(users));
			
			
			
			FileWriter fileWriter = new FileWriter(fileName);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print("Some String");
		    printWriter.printf(obj.writeValueAsString(users));
		    printWriter.close();
		    
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, User> getUsers () {	
		if (users == null) {
			try {
				FileInputStream fin = new FileInputStream ("users.txt");
				ObjectInputStream ois = new ObjectInputStream (fin);
				users = (HashMap<String, User>) ois.readObject();
				ois.close();
				fin.close();
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
