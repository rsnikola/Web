package model;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.enumerations.ApartmentType;
import model.enumerations.Role;

public class Data {

	private static String pathPrefix = "";
//	private static String pathPrefix = "../webapps/";
	
	private static HashMap<String, User> users = null;
	private static HashMap<Integer, Address> addresses = null;
	private static HashMap<Integer, Location> locations = null;
	private static HashMap<Integer, Apartment> apartments = null;
	private static ObjectMapper obj = new ObjectMapper();
	
	public static HashMap<String, User> getUsers () {	
		if (users == null) {
			System.out.println("Data: Loading users. ");
			try {
				users = obj.readValue(new File(pathPrefix + "users.txt"), new TypeReference<Map<String, User>>(){});
			} catch (Exception e) {
				System.out.println("Data: Error while loading users! ");
			
				User u1 = new User("adi1", "1234", "Milan", "Milic", true, Role.ADMIN);
				User u2 = new User("adi2", "4321", "Jovana", "Jovic", false, Role.ADMIN);
				User u3 = new User("gue1", "1234", "Pero", "Jovic", true, Role.GUEST);
				User u4 = new User("gue2", "4321", "Gile", "Glisic", false, Role.GUEST);
				User u5 = new User("hos1", "1234", "Bojana", "Bojic", true, Role.HOST);
				User u6 = new User("hos2", "4321", "Vuk", "Vukovic", false, Role.HOST);
				users = new HashMap<String, User> ();
				users.put(u1.getUsername(), u1);
				users.put(u2.getUsername(), u2);
				users.put(u3.getUsername(), u3);
				users.put(u4.getUsername(), u4);
				users.put(u5.getUsername(), u5);
				users.put(u6.getUsername(), u6);
				
				Data.saveUsers();
			}
		}
		return users;
	}
	
	
	public static HashMap<Integer, Address> getAddresses () {
		if (addresses == null) {
			System.out.println("Data: Loading addresses. ");
			try {
				addresses = obj.readValue(new File(pathPrefix + "addresses.txt"), new TypeReference<Map<Integer, Address>>(){});
			} catch (Exception e) {
				System.out.println("Data: Error while loading addresses! ");
				
				Address a1 = new Address(1, 31, "Stevan Musica", "Novi Sad", "21000", "Serbia");
				Address a2 = new Address(2, 21, "Stevan Musica", "Novi Sad", "21000", "Serbia");
				Address a3 = new Address(3, 31, "Skadarska", "Beograd", "11000", "Serbia");
				addresses = new HashMap<Integer, Address> ();
				addresses.put(a1.getId(), a1);
				addresses.put(a2.getId(), a2);
				addresses.put(a3.getId(), a3);
				
				Data.saveAddresses();
			}
		}
		return addresses;
	}
	
	public static HashMap<Integer, Location> getLocations () {
		if (locations == null) {
			System.out.println("Data: Loading locations. ");
			try {
				locations = obj.readValue(new File(pathPrefix + "locations.txt"), new TypeReference<Map<Integer, Location>>(){});
			} catch (Exception e) {
				System.out.println("Data: Error while loading locations! ");
				
				Location l1 = new Location(1, 12.1, 11.9, 1);
				Location l2 = new Location(2, 13.3, 13.3, 2);
				Location l3 = new Location(3, 17.2, 11.8, 3);
				locations = new HashMap<Integer, Location> ();
				locations.put(l1.getId(), l1);
				locations.put(l2.getId(), l2);
				locations.put(l3.getId(), l3);
				
				Data.saveLocations();
			}
		}
		return locations;
	}

	@SuppressWarnings("deprecation")
	public static HashMap<Integer, Apartment> getApartments () {
		if (apartments == null) {
			System.out.println("Data: Loading apartments. ");
			try {
				apartments = obj.readValue(new File(pathPrefix + "apartments.txt"), new TypeReference<Map<Integer, Apartment>>(){});
			} catch (Exception e) {
				System.out.println("Data: Error while loading apartments! ");
				
				Apartment a1 = new Apartment(1, ApartmentType.APARTMENT, 3, 5, 1, new Date(2, 2, 2), new Date(3, 3, 3), "hos2", 
							new ArrayList<Integer>(), 12.99, new Time(2, 2, 2), new Time(3, 3, 3), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a2 = new Apartment(2, ApartmentType.APARTMENT, 2, 3, 2, new Date(2, 2, 2), new Date(3, 3, 3), "hos2", 
						new ArrayList<Integer>(), 12.99, new Time(2, 2, 2), new Time(3, 3, 3), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a3 = new Apartment(3, ApartmentType.ROOM, 1, 1, 3, new Date(2, 2, 2), new Date(3, 3, 3), "hos1", 
								new ArrayList<Integer>(), 12.99, new Time(2, 2, 2), new Time(3, 3, 3), true, new ArrayList<Integer> (), 
								new ArrayList<Integer>());
				apartments = new HashMap<Integer, Apartment> ();
				apartments.put(a1.getId(), a1);
				apartments.put(a2.getId(), a2);
				apartments.put(a3.getId(), a3);
				
				Data.saveApartments();
			}
		}
		return apartments;
	}
	
	public static void saveUsers () {
		try {
			String filePath = pathPrefix + "users.txt";
			System.out.println(obj.writeValueAsString(users));
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(users));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Users saved. ");
	}
	
	private static void saveAddresses() {
		try {
			String filePath = pathPrefix + "addresses.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(addresses));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Addresses saved. ");
	}
	
	private static void saveLocations() {
		try {
			String filePath = pathPrefix + "locations.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(locations));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Locations saved. ");
	}
	
	private static void saveApartments () {
		try {
			String filePath = pathPrefix + "apartments.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(apartments));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Apartments saved. ");
	}

}
