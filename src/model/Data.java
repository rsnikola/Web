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
import model.enumerations.ReservationStatus;
import model.enumerations.Role;

public class Data {

	private static String pathPrefix = "";
//	private static String pathPrefix = "../webapps/";
	
	private static HashMap<String, User> users = null;
	private static HashMap<Integer, Address> addresses = null;
	private static HashMap<Integer, Location> locations = null;
	private static HashMap<Integer, Apartment> apartments = null;
	private static HashMap<Integer, Amenity> amenities = null;
	private static HashMap<Integer, Reservation> reservations = null;
	private static HashMap<Integer, Comment> comments = null;
	private static ObjectMapper obj = new ObjectMapper();
	
	public static HashMap<String, User> getUsers () {	
		if (users == null) {
			System.out.println("Data: Loading users. ");
			try {
				users = obj.readValue(new File(pathPrefix + "users.txt"), new TypeReference<Map<String, User>>(){});
			} catch (Exception e) {
				System.out.println("Data: Error while loading users! ");
			
				User u1 = new User("adi1@mail.com", "1234", "Milan", "Milic", true, Role.ADMIN);
				User u2 = new User("adi2@mail.com", "4321", "Jovana", "Jovic", false, Role.ADMIN);
				User u3 = new User("gue1@mail.com", "1234", "Pero", "Jovic", true, Role.GUEST);
				User u4 = new User("gue2@mail.com", "4321", "Gile", "Glisic", false, Role.GUEST);
				User u5 = new User("hos1@mail.com", "1234", "Bojana", "Bojic", true, Role.HOST);
				User u6 = new User("hos2@mail.com", "4321", "Vuk", "Vukovic", false, Role.HOST);
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
				Address a1 = new Address(1, 12, "Stevana Musica", "Novi Sad", "21000", "Serbia");
				Address a2 = new Address(2, 41, "Stevan Musica", "Novi Sad", "21000", "Serbia");
				Address a3 = new Address(3, 25, "Skadarska", "Beograd", "11000", "Serbia");
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
				
				Apartment a1 = new Apartment(1, ApartmentType.APARTMENT, 3, 5, 1, new Date(120, 10, 2), new Date(120, 10, 20), "hos2@mail.com", 
							new ArrayList<Integer>(), 11.99, new Time(2, 2, 2), new Time(3, 3, 3), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a2 = new Apartment(2, ApartmentType.APARTMENT, 2, 3, 2, new Date(120, 10, 12), new Date(120, 11, 13), "hos2@mail.com", 
						new ArrayList<Integer>(), 12.99, new Time(2, 2, 2), new Time(3, 3, 3), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a3 = new Apartment(3, ApartmentType.ROOM, 1, 1, 3, new Date(120, 10, 12), new Date(120, 11, 9), "hos1@mail.com", 
								new ArrayList<Integer>(), 13.99, new Time(2, 2, 2), new Time(3, 3, 3), true, new ArrayList<Integer> (), 
								new ArrayList<Integer>());
				Data.getAmenities();
				a1.getAmenities().add(1);
				a1.getAmenities().add(2);
				a1.getAmenities().add(3);
				a2.getAmenities().add(2);
				a2.getAmenities().add(3);
				a3.getAmenities().add(1);
				a3.getAmenities().add(3);
				apartments = new HashMap<Integer, Apartment> ();
				apartments.put(a1.getId(), a1);
				apartments.put(a2.getId(), a2);
				apartments.put(a3.getId(), a3);
				
				Data.saveApartments();
			}
		}
		return apartments;
	}
	
	
	public static HashMap<Integer, Amenity> getAmenities () {
		if (amenities == null) {
			System.out.println("Data: Loading amenities. ");
			try {
				amenities = obj.readValue(new File(pathPrefix + "amenities.txt"), new TypeReference<Map<Integer, Amenity>>() {});
			} catch (Exception e) {
				System.out.println("Error while loading amenities! ");
				Amenity a1 = new Amenity(1, "towels");
				Amenity a2 = new Amenity(2, "shower");
				Amenity a3 = new Amenity(3, "sheets");
				amenities = new HashMap<Integer, Amenity> ();
				amenities.put(a1.getId(), a1);
				amenities.put(a2.getId(), a2);
				amenities.put(a3.getId(), a3);
				Data.saveAmenities();
			}
		}
		return amenities;
	}

	@SuppressWarnings("deprecation")
	public static HashMap<Integer, Reservation> getReservations () {
		if (reservations == null) {
			System.out.println("Data: Loading reservations. ");
			try {
				reservations = obj.readValue(new File(pathPrefix + "reservations.txt"), new TypeReference<Map<Integer, Reservation>>() {});
			} catch (Exception e) {
				System.out.println("Error while loading reservations! ");
				Reservation r1 = new Reservation(1, 1, "gue1@mail.com", new Date(120, 10, 15), new Date(120, 10, 25), 11.45, "Please", ReservationStatus.CREATED);
				Reservation r2 = new Reservation(2, 3, "gue1@mail.com", new Date(120, 9, 15), new Date(120, 9, 25), 12.45, "Please", ReservationStatus.DENIED);
				Reservation r3 = new Reservation(3, 1, "gue1@mail.com", new Date(120, 8, 15), new Date(120, 8, 25), 13.45, "Please", ReservationStatus.CANCELED);
				Reservation r4 = new Reservation(4, 3, "gue1@mail.com", new Date(120, 11, 15), new Date(120, 11, 25), 14.45, "Please", ReservationStatus.ACCEPTED);
				Reservation r5 = new Reservation(5, 3, "gue1@mail.com", new Date(120, 02, 15), new Date(120, 02, 25), 14.45, "Please", ReservationStatus.ACCEPTED);
				Reservation r6 = new Reservation(6, 1, "gue1@mail.com", new Date(120, 7, 15), new Date(120, 7, 25), 15.45, "Please", ReservationStatus.FINISHED);
				reservations = new HashMap<Integer, Reservation> ();
				reservations.put(r1.getId(), r1);
				reservations.put(r2.getId(), r2);
				reservations.put(r3.getId(), r3);
				reservations.put(r4.getId(), r4);
				reservations.put(r5.getId(), r5);
				reservations.put(r6.getId(), r6);
				Data.saveReservations();
			}
		}
		return reservations;
	}
	
	
	public static HashMap<Integer, Comment> getComments () {
		if (comments == null) {
			System.out.println("Data: Loading comments");
			try {
				comments = obj.readValue(new File(pathPrefix + "comments.txt"), new TypeReference<Map<Integer, Comment>>() {});
			} catch (Exception e) {
				System.out.println("Error while loading comments! ");
				Comment c1 = new Comment(1, "gue1@mail.com", 6, "Was nice, thx!", 4);
				Comment c2 = new Comment(2, "gue1@mail.com", 2, "You bailed on me!", 1);
				comments = new HashMap<Integer, Comment> ();
				comments.put(c1.getId(), c1);
				comments.put(c2.getId(), c2);
				Data.saveComments();
			}
		}
		return comments;
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
	
	public static void saveAddresses() {
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
	
	public static void saveLocations() {
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
	
	public static void saveApartments () {
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
	
	public static void saveAmenities () {
		try {
			String filePath = pathPrefix + "amenities.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(amenities));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Amenities saved. ");
	}
	
	public static void saveReservations() {
		try {
			String filePath = pathPrefix + "reservations.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(reservations));
		    printWriter.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Reservations saved. ");
	}
	
	public static void saveComments () {
		try {
			String filePath = pathPrefix + "comments.txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(comments));
		    printWriter.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Data: Comments saved. ");
	}

}
