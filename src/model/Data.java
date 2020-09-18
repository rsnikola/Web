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

//	private static String pathPrefix = "";
	private static String pathPrefix = "../webapps/WebProjekat/";
	
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
//			System.out.println("Data: Loading users. ");
			try {
				users = obj.readValue(new File(pathPrefix + "users.txt"), new TypeReference<Map<String, User>>(){});
			} catch (Exception e) {
//				System.out.println("Data: Error while loading users! ");
			
				User admin1 = new User("luka@mail.com", "1234", "Luka", "Jovanovic", true, Role.ADMIN);
				User admin2 = new User("milica@mail.com", "1234", "Milica", "Petrovic", false, Role.ADMIN);
				User admin3 = new User("milan@mail.com", "1234", "Milan", "Nikolic", true, Role.ADMIN);
				User admin4 = new User("ana@mail.com", "1234", "Ana", "Markovic", false, Role.ADMIN);
				User admin5 = new User("vuk@mail.com", "1234", "Vuk", "Đorđevic", true, Role.ADMIN);
				
				User guest1 = new User("teodora@mail.com", "1234", "Teodora", "Stojanovic", false, Role.GUEST);
				User guest2 = new User("dusan@mail.com", "1234", "Dušan", "Ilic", true, Role.GUEST);
				User guest3 = new User("sara@mail.com", "1234", "Sara", "Stankovic", false, Role.GUEST);
				User guest4 = new User("marko@mail.com", "1234", "Marko", "Pavlovic", true, Role.GUEST);
				User guest5 = new User("nina@mail.com", "1234", "Nina", "Miloševic", false, Role.GUEST);
				
				User host1 = new User("david@mail.com", "1234", "David", "Tijanic", true, Role.HOST);
				User host2 = new User("jana@mail.com", "1234", "Jana", "Obradovic", false, Role.HOST);
				User host3 = new User("matija@mail.com", "1234", "Matija", "Maničic", true, Role.HOST);
				User host4 = new User("katarina@mail.com", "1234", "Katarina", "Anđelic", false, Role.HOST);
				User host5 = new User("miroslav@mail.com", "1234", "Miroslav", "Obradov", true, Role.HOST);
				
				users = new HashMap<String, User> ();
				
				users.put(admin1.getUsername(), admin1);
				users.put(admin2.getUsername(), admin2);
				users.put(admin3.getUsername(), admin3);
				users.put(admin4.getUsername(), admin4);
				users.put(admin5.getUsername(), admin5);
				
				users.put(guest1.getUsername(), guest1);
				users.put(guest2.getUsername(), guest2);
				users.put(guest3.getUsername(), guest3);
				users.put(guest4.getUsername(), guest4);
				users.put(guest5.getUsername(), guest5);
				
				users.put(host1.getUsername(), host1);
				users.put(host2.getUsername(), host2);
				users.put(host3.getUsername(), host3);
				users.put(host4.getUsername(), host4);
				users.put(host5.getUsername(), host5);
				
				Data.saveUsers();
			}
		}
		return users;
	}
	
	
	public static HashMap<Integer, Address> getAddresses () {
		if (addresses == null) {
//			System.out.println("Data: Loading addresses. ");
			try {
				addresses = obj.readValue(new File(pathPrefix + "addresses.txt"), new TypeReference<Map<Integer, Address>>(){});
			} catch (Exception e) {
//				System.out.println("Data: Error while loading addresses! ");
				
				Address a1 = new Address(1, 11, "Stevana Milovanova", "Novi Sad", "21000", "Serbia");
				Address a2 = new Address(2, 15, "Save Vukovica", "Novi Sad", "21000", "Serbia");
				Address a3 = new Address(3, 6, "Zlatne grede", "Novi Sad", "21000", "Serbia");
				Address a4 = new Address(4, 10, "Zemljane Cuprije", "Novi Sad", "21000", "Serbia");
				Address a5 = new Address(5, 1, "Almaska", "Novi Sad", "21000", "Serbia");
				
				Address a6 = new Address(6, 19, "Drinska", "Novi Sad", "21000", "Serbia");
				Address a7 = new Address(7, 84, "Branka Bajica", "Novi Sad", "21000", "Serbia");
				Address a8 = new Address(8, 7, "Janka Cmelika", "Novi Sad", "21000", "Serbia");
				Address a9 = new Address(9, 38, "Jug Bogdana", "Zrenjanin", "23000", "Serbia");
				Address a10 = new Address(10, 3, "Ljubljanska", "Zrenjanin", "23000", "Serbia");
				
				Address a11 = new Address(11, 21, "Kosmajska", "Beograd", "11000", "Serbia");
				Address a12 = new Address(12, 32, "Frontovska", "Beograd", "11000", "Serbia");
				Address a13 = new Address(13, 6, "Lazara Socice", "Beograd", "11000", "Serbia");
				Address a14 = new Address(14, 135, "Sumatovacka", "Beograd", "11000", "Serbia");
				Address a15 = new Address(15, 8, "Severni Bedem", "Sremska Mitrovica", "22000", "Serbia");
				
				Address a16 = new Address(16, 54, "Akifa Seremeta", "Sarajevo", "71000", "Bosna i Hercegovina");
				Address a17 = new Address(17, 163, "Lepenicka", "Sarajevo", "71000", "Bosna i Hercegovina");
				Address a18 = new Address(18, 22, "Izeta Fazlinovica", "Tuzla", "75000", "Bosna i Hercegovina");
				Address a19 = new Address(19, 12, "Carice Milice", "Banja Luka", "78000", "Bosna i Hercegovina");
				Address a20 = new Address(20, 36, "Koste Vujanovica", "Zagreb", "10000", "Hrvatska");
				
				Address a21 = new Address(21, 43, "Ziehrerstrasse", "Graz", "8041", "Osterreich");
				Address a22 = new Address(22, 7, "Viola ut", "Budapest", "1094", "Magyarorszag");
				Address a23 = new Address(23, 58, "Soroksari ut", "Budapest", "1095", "Magyarorszag");
				Address a24 = new Address(24, 65, "Szabad Sajto u.", "Szeged", "6725", "Magyarorszag");
				Address a25 = new Address(25, 17, "Janos u.", "Budapest", "7621", "Magyarorszag");
				
				addresses = new HashMap<Integer, Address> ();
				
				addresses.put(a1.getId(), a1);
				addresses.put(a2.getId(), a2);
				addresses.put(a3.getId(), a3);
				addresses.put(a4.getId(), a4);
				addresses.put(a5.getId(), a5);

				addresses.put(a6.getId(), a6);
				addresses.put(a7.getId(), a7);
				addresses.put(a8.getId(), a8);
				addresses.put(a9.getId(), a9);
				addresses.put(a10.getId(), a10);

				addresses.put(a11.getId(), a11);
				addresses.put(a12.getId(), a12);
				addresses.put(a13.getId(), a13);
				addresses.put(a14.getId(), a14);
				addresses.put(a15.getId(), a15);

				addresses.put(a16.getId(), a16);
				addresses.put(a17.getId(), a17);
				addresses.put(a18.getId(), a18);
				addresses.put(a19.getId(), a19);
				addresses.put(a20.getId(), a20);

				addresses.put(a21.getId(), a21);
				addresses.put(a22.getId(), a22);
				addresses.put(a23.getId(), a23);
				addresses.put(a24.getId(), a24);
				addresses.put(a25.getId(), a25);
				
				Data.saveAddresses();
			}
		}
		return addresses;
	}
	
	public static HashMap<Integer, Location> getLocations () {
		if (locations == null) {
//			System.out.println("Data: Loading locations. ");
			try {
				locations = obj.readValue(new File(pathPrefix + "locations.txt"), new TypeReference<Map<Integer, Location>>(){});
			} catch (Exception e) {
//				System.out.println("Data: Error while loading locations! ");
				
				Location l1 = new Location(1, 19.851576, 45.258678, 1);
				Location l2 = new Location(2, 19.847601, 45.260853, 2);
				Location l3 = new Location(3, 19.847837, 45.259177, 3);
				Location l4 = new Location(4, 19.848743, 45.261225, 4);
				Location l5 = new Location(5, 19.844669, 45.263261, 5);
				
				Location l6 = new Location(6, 19.819664, 45.253703, 6);
				Location l7 = new Location(7, 19.815516, 45.253678, 7);
				Location l8 = new Location(8, 19.815229, 45.254854, 8);
				Location l9 = new Location(9, 20.397366, 45.371386, 9);
				Location l10 = new Location(10, 20.392232, 45.386586, 10);

				Location l11 = new Location(11, 20.417579, 44.753214, 11);
				Location l12 = new Location(12, 20.408267, 44.758800, 12);
				Location l13 = new Location(13, 20.457283, 44.793576, 13);
				Location l14 = new Location(14, 20.483614, 44.794217, 14);
				Location l15 = new Location(15, 19.612152, 44.975351, 15);

				Location l16 = new Location(16, 18.339876, 43.828462, 16);
				Location l17 = new Location(17, 18.317790, 43.832919, 17);
				Location l18 = new Location(18, 18.670240, 44.531445, 18);
				Location l19 = new Location(19, 17.191265, 44.764039, 19);
				Location l20 = new Location(20, 15.991265, 45.810776, 20);

				Location l21 = new Location(21, 15.449603, 47.041735, 21);
				Location l22 = new Location(22, 19.074449, 47.478975, 22);
				Location l23 = new Location(23, 19.075359, 47.470010, 23);
				Location l24 = new Location(24, 20.130074, 46.239884, 24);
				Location l25 = new Location(25, 18.233362, 46.077562, 25);
				
				locations = new HashMap<Integer, Location> ();
				
				locations.put(l1.getId(), l1);
				locations.put(l2.getId(), l2);
				locations.put(l3.getId(), l3);
				locations.put(l4.getId(), l4);
				locations.put(l5.getId(), l5);
				
				locations.put(l6.getId(), l6);
				locations.put(l7.getId(), l7);
				locations.put(l8.getId(), l8);
				locations.put(l9.getId(), l9);
				locations.put(l10.getId(), l10);
				
				locations.put(l11.getId(), l11);
				locations.put(l12.getId(), l12);
				locations.put(l13.getId(), l13);
				locations.put(l14.getId(), l14);
				locations.put(l15.getId(), l15);
				
				locations.put(l16.getId(), l16);
				locations.put(l17.getId(), l17);
				locations.put(l18.getId(), l18);
				locations.put(l19.getId(), l19);
				locations.put(l20.getId(), l20);
				
				locations.put(l21.getId(), l21);
				locations.put(l22.getId(), l22);
				locations.put(l23.getId(), l23);
				locations.put(l24.getId(), l24);
				locations.put(l25.getId(), l25);
				
				Data.saveLocations();
			}
		}
		return locations;
	}

	@SuppressWarnings("deprecation")
	public static HashMap<Integer, Apartment> getApartments () {
		if (apartments == null) {
//			System.out.println("Data: Loading apartments. ");
			try {
				apartments = obj.readValue(new File(pathPrefix + "apartments.txt"), new TypeReference<Map<Integer, Apartment>>(){});
			} catch (Exception e) {
//				System.out.println("Data: Error while loading apartments! ");
				
				Apartment a1 = new Apartment(1, ApartmentType.APARTMENT, 2, 7, 1, new Date(120, 10, 2), new Date(120, 10, 20), "david@mail.com", 
							new ArrayList<Integer>(), 20.95, new Time(14, 0, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a2 = new Apartment(2, ApartmentType.APARTMENT, 4, 7, 2, new Date(120, 10, 2), new Date(120, 10, 20), "david@mail.com", 
						new ArrayList<Integer>(), 31.19, new Time(11, 30, 0), new Time(9, 0, 0), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a3 = new Apartment(3, ApartmentType.ROOM, 1, 4, 3, new Date(120, 10, 2), new Date(120, 10, 20), "david@mail.com", 
						new ArrayList<Integer>(), 25.85, new Time(14, 0, 0), new Time(11, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a4 = new Apartment(4, ApartmentType.ROOM, 1, 2, 4, new Date(120, 10, 2), new Date(120, 10, 20), "david@mail.com", 
						new ArrayList<Integer>(), 43.34, new Time(11, 0, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a5 = new Apartment(5, ApartmentType.APARTMENT, 6, 4, 5, new Date(120, 10, 2), new Date(120, 10, 20), "david@mail.com", 
						new ArrayList<Integer>(), 49.68, new Time(17, 0, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());

				Apartment a6 = new Apartment(6, ApartmentType.APARTMENT, 4, 7, 6, new Date(120, 10, 2), new Date(120, 10, 20), "jana@mail.com", 
							new ArrayList<Integer>(), 2.04, new Time(10, 30, 0), new Time(9, 0, 0), false, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a7 = new Apartment(7, ApartmentType.ROOM, 1, 3, 7, new Date(120, 10, 2), new Date(120, 10, 20), "jana@mail.com", 
						new ArrayList<Integer>(), 25.19, new Time(14, 30, 0), new Time(11, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a8 = new Apartment(8, ApartmentType.ROOM, 1, 2, 8, new Date(120, 10, 2), new Date(120, 10, 20), "jana@mail.com", 
						new ArrayList<Integer>(), 24.21, new Time(11, 30, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a9 = new Apartment(9, ApartmentType.APARTMENT, 7, 5, 9, new Date(120, 10, 2), new Date(120, 10, 20), "jana@mail.com", 
						new ArrayList<Integer>(), 36.42, new Time(14, 0, 0), new Time(10, 30, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a10 = new Apartment(10, ApartmentType.APARTMENT, 3, 5, 10, new Date(120, 10, 2), new Date(120, 10, 20), "jana@mail.com", 
						new ArrayList<Integer>(), 10.20, new Time(10, 30, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());

				Apartment a11 = new Apartment(11, ApartmentType.ROOM, 1, 4, 11, new Date(120, 10, 2), new Date(120, 10, 20), "matija@mail.com", 
							new ArrayList<Integer>(), 5.97, new Time(14, 0, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a12 = new Apartment(12, ApartmentType.ROOM, 1, 1, 12, new Date(120, 10, 2), new Date(120, 10, 20), "matija@mail.com", 
						new ArrayList<Integer>(), 49.16, new Time(11, 0, 0), new Time(9, 0, 0), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a13 = new Apartment(13, ApartmentType.APARTMENT, 7, 5, 13, new Date(120, 10, 2), new Date(120, 10, 20), "matija@mail.com", 
						new ArrayList<Integer>(), 23.95, new Time(15, 0, 0), new Time(11, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a14 = new Apartment(14, ApartmentType.APARTMENT, 2, 6, 14, new Date(120, 10, 2), new Date(120, 10, 20), "matija@mail.com", 
						new ArrayList<Integer>(), 40.24, new Time(11, 30, 0), new Time(9, 30, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a15 = new Apartment(15, ApartmentType.ROOM, 1, 3, 15, new Date(120, 10, 2), new Date(120, 10, 20), "matija@mail.com", 
						new ArrayList<Integer>(), 2.00, new Time(14, 15, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());

				Apartment a16 = new Apartment(16, ApartmentType.ROOM, 1, 2, 16, new Date(120, 10, 2), new Date(120, 10, 20), "katarina@mail.com", 
							new ArrayList<Integer>(), 44.67, new Time(11, 00, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a17 = new Apartment(17, ApartmentType.APARTMENT, 5, 9, 17, new Date(120, 10, 2), new Date(120, 10, 20), "katarina@mail.com", 
						new ArrayList<Integer>(), 12.55, new Time(13, 0, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a18 = new Apartment(18, ApartmentType.APARTMENT, 3, 7, 18, new Date(120, 10, 2), new Date(120, 10, 20), "katarina@mail.com", 
						new ArrayList<Integer>(), 18.63, new Time(11, 30, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a19 = new Apartment(19, ApartmentType.ROOM, 1, 1, 19, new Date(120, 10, 2), new Date(120, 10, 20), "katarina@mail.com", 
						new ArrayList<Integer>(), 40.61, new Time(14, 0, 0), new Time(10, 30, 0), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a20 = new Apartment(20, ApartmentType.ROOM, 1, 3, 20, new Date(120, 10, 2), new Date(120, 10, 20), "katarina@mail.com", 
						new ArrayList<Integer>(), 0.42, new Time(11, 00, 0), new Time(9, 30, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());

				Apartment a21 = new Apartment(21, ApartmentType.APARTMENT, 3, 6, 21, new Date(120, 10, 2), new Date(120, 10, 20), "miroslav@mail.com", 
							new ArrayList<Integer>(), 27.81, new Time(14, 30, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
							new ArrayList<Integer>());
				Apartment a22 = new Apartment(22, ApartmentType.APARTMENT, 2, 4, 22, new Date(120, 10, 2), new Date(120, 10, 20), "miroslav@mail.com", 
						new ArrayList<Integer>(), 39.89, new Time(11, 30, 0), new Time(9, 0, 0), false, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a23 = new Apartment(23, ApartmentType.ROOM, 1, 3, 23, new Date(120, 10, 2), new Date(120, 10, 20), "miroslav@mail.com", 
						new ArrayList<Integer>(), 39.86, new Time(14, 0, 0), new Time(10, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a24 = new Apartment(24, ApartmentType.ROOM, 1, 1, 24, new Date(120, 10, 2), new Date(120, 10, 20), "miroslav@mail.com", 
						new ArrayList<Integer>(), 17.85, new Time(11, 00, 0), new Time(9, 0, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				Apartment a25 = new Apartment(25, ApartmentType.APARTMENT, 3, 7, 25, new Date(120, 10, 2), new Date(120, 10, 20), "miroslav@mail.com", 
						new ArrayList<Integer>(), 6.96, new Time(12, 0, 0), new Time(11, 30, 0), true, new ArrayList<Integer> (), 
						new ArrayList<Integer>());
				
				Data.getAmenities();
				
				a1.getAmenities().add(1);
				a1.getAmenities().add(2);

				a2.getAmenities().add(3);
				a2.getAmenities().add(4);
				a2.getAmenities().add(5);

				a3.getAmenities().add(6);
				a3.getAmenities().add(7);
				a3.getAmenities().add(8);
				a3.getAmenities().add(9);

				a4.getAmenities().add(10);

				a5.getAmenities().add(11);
				a5.getAmenities().add(12);
				
				a6.getAmenities().add(13);
				a6.getAmenities().add(14);

				a7.getAmenities().add(15);
				a7.getAmenities().add(1);
				a7.getAmenities().add(2);

				a8.getAmenities().add(3);
				a8.getAmenities().add(4);

				a9.getAmenities().add(5);
				a9.getAmenities().add(6);
				a9.getAmenities().add(7);

				a10.getAmenities().add(8);
				a10.getAmenities().add(9);

				a11.getAmenities().add(10);
				a11.getAmenities().add(11);

				a12.getAmenities().add(12);
				a12.getAmenities().add(13);
				a12.getAmenities().add(14);

				a13.getAmenities().add(15);
				a13.getAmenities().add(1);
				a13.getAmenities().add(2);

				a14.getAmenities().add(3);
				a14.getAmenities().add(4);
				
				a15.getAmenities().add(5);
				a15.getAmenities().add(6);
				a15.getAmenities().add(7);

				a16.getAmenities().add(8);
				a16.getAmenities().add(9);

				a17.getAmenities().add(10);
				a17.getAmenities().add(11);
				a17.getAmenities().add(12);

				a18.getAmenities().add(13);
				a18.getAmenities().add(14);
				a18.getAmenities().add(15);
				a18.getAmenities().add(1);

				a19.getAmenities().add(2);
				a19.getAmenities().add(3);

				a20.getAmenities().add(4);
				a20.getAmenities().add(5);

				a21.getAmenities().add(6);

				a22.getAmenities().add(7);
				a22.getAmenities().add(8);
				a22.getAmenities().add(9);

				a23.getAmenities().add(10);
				a23.getAmenities().add(11);

				a24.getAmenities().add(12);
				a24.getAmenities().add(13);
				a24.getAmenities().add(14);

				a25.getAmenities().add(15);
				
				apartments = new HashMap<Integer, Apartment> ();
				
				apartments.put(a1.getId(), a1);
				apartments.put(a2.getId(), a2);
				apartments.put(a3.getId(), a3);
				apartments.put(a4.getId(), a4);
				apartments.put(a5.getId(), a5);

				apartments.put(a6.getId(), a6);
				apartments.put(a7.getId(), a7);
				apartments.put(a8.getId(), a8);
				apartments.put(a9.getId(), a9);
				apartments.put(a10.getId(), a10);

				apartments.put(a11.getId(), a11);
				apartments.put(a12.getId(), a12);
				apartments.put(a13.getId(), a13);
				apartments.put(a14.getId(), a14);
				apartments.put(a15.getId(), a15);

				apartments.put(a16.getId(), a16);
				apartments.put(a17.getId(), a17);
				apartments.put(a18.getId(), a18);
				apartments.put(a19.getId(), a19);
				apartments.put(a20.getId(), a20);

				apartments.put(a21.getId(), a21);
				apartments.put(a22.getId(), a22);
				apartments.put(a23.getId(), a23);
				apartments.put(a24.getId(), a24);
				apartments.put(a25.getId(), a25);
				
				Data.saveApartments();
			}
		}
		return apartments;
	}
	
	
	public static HashMap<Integer, Amenity> getAmenities () {
		if (amenities == null) {
//			System.out.println("Data: Loading amenities. ");
			try {
				amenities = obj.readValue(new File(pathPrefix + "amenities.txt"), new TypeReference<Map<Integer, Amenity>>() {});
			} catch (Exception e) {
//				System.out.println("Error while loading amenities! ");
				
				Amenity a1 = new Amenity(1, "towels");
				Amenity a2 = new Amenity(2, "bath robe");
				Amenity a3 = new Amenity(3, "air conditioning");
				Amenity a4 = new Amenity(4, "bed sheets");
				Amenity a5 = new Amenity(5, "wifi");

				Amenity a6 = new Amenity(6, "television");
				Amenity a7 = new Amenity(7, "parking");
				Amenity a8 = new Amenity(8, "room service");
				Amenity a9 = new Amenity(9, "elevator");
				Amenity a10 = new Amenity(10, "bike rental");

				Amenity a11 = new Amenity(11, "rent-a-car");
				Amenity a12 = new Amenity(12, "cleaning service");
				Amenity a13 = new Amenity(13, "kitchen");
				Amenity a14 = new Amenity(14, "first aid kit");
				Amenity a15 = new Amenity(15, "small child friendly");
				
				amenities = new HashMap<Integer, Amenity> ();
				
				amenities.put(a1.getId(), a1);
				amenities.put(a2.getId(), a2);
				amenities.put(a3.getId(), a3);
				amenities.put(a4.getId(), a4);
				amenities.put(a5.getId(), a5);

				amenities.put(a6.getId(), a6);
				amenities.put(a7.getId(), a7);
				amenities.put(a8.getId(), a8);
				amenities.put(a9.getId(), a9);
				amenities.put(a10.getId(), a10);

				amenities.put(a11.getId(), a11);
				amenities.put(a12.getId(), a12);
				amenities.put(a13.getId(), a13);
				amenities.put(a14.getId(), a14);
				amenities.put(a15.getId(), a15);
				
				Data.saveAmenities();
			}
		}
		return amenities;
	}

	@SuppressWarnings("deprecation")
	public static HashMap<Integer, Reservation> getReservations () {
		if (reservations == null) {
			try {
				reservations = obj.readValue(new File(pathPrefix + "reservations.txt"), new TypeReference<Map<Integer, Reservation>>() {});
			} catch (Exception e) {
				// Finished, stare
				Reservation r1 = new Reservation(1, 20, "teodora@mail.com", new Date(119, 10, 15), new Date(119, 10, 25), 37.8, "I WILL clean up after myself. ", ReservationStatus.FINISHED);
				Reservation r2 = new Reservation(2, 20, "dusan@mail.com", new Date(119, 9, 15), new Date(119, 9, 25), 37.8, "Please. may I stay? ", ReservationStatus.FINISHED);
				Reservation r3 = new Reservation(3, 20, "sara@mail.com", new Date(119, 8, 15), new Date(119, 8, 25), 37.8, "I clean up after myself. ", ReservationStatus.FINISHED);
				
				Reservation r4 = new Reservation(4, 15, "teodora@mail.com", new Date(119, 7, 15), new Date(119, 7, 25), 18.0, "I would like to pary. ", ReservationStatus.FINISHED);
				Reservation r5 = new Reservation(5, 15, "dusan@mail.com", new Date(119, 6, 15), new Date(119, 6, 25), 18.0, "I just wanna have some fun ;). ", ReservationStatus.FINISHED);
				Reservation r6 = new Reservation(6, 15, "sara@mail.com", new Date(119, 10, 15), new Date(119, 10, 25), 18.0, "I intend to spend 9 nights in your place. ", ReservationStatus.FINISHED);
				
				Reservation r7 = new Reservation(7, 11, "dusan@mail.com", new Date(119, 9, 15), new Date(119, 9, 25), 53.73, "May I please stay at your place? ", ReservationStatus.FINISHED);
				Reservation r8 = new Reservation(8, 11, "marko@mail.com", new Date(119, 8, 15), new Date(119, 8, 25), 53.73, "So can I come? ", ReservationStatus.FINISHED);
				Reservation r9 = new Reservation(9, 11, "nina@mail.com", new Date(119, 7, 15), new Date(119, 7, 25), 53.73, "Would you take me in? ", ReservationStatus.FINISHED);
				
				Reservation r10 = new Reservation(10, 25, "marko@mail.com", new Date(119, 6, 15), new Date(119, 6, 25), 62.64, "Can I come in? ", ReservationStatus.FINISHED);
				Reservation r11 = new Reservation(11, 25, "nina@mail.com", new Date(119, 10, 15), new Date(119, 10, 25), 62.64, "Would you take me in? ", ReservationStatus.FINISHED);
				Reservation r12 = new Reservation(12, 25, "sara@mail.com", new Date(119, 9, 15), new Date(119, 9, 25), 62.64, "I promise I vacuum up after myself. ", ReservationStatus.FINISHED);
				
				Reservation r13 = new Reservation(13, 10, "marko@mail.com", new Date(119, 8, 15), new Date(119, 8, 25), 91.8, "Please, please, please ley me stay, please. ", ReservationStatus.FINISHED);
				Reservation r14 = new Reservation(14, 10, "nina@mail.com", new Date(119, 7, 15), new Date(119, 7, 25), 91.8, "Will you plese let me stay? ", ReservationStatus.FINISHED);
				Reservation r15 = new Reservation(15, 10, "teodora@mail.com", new Date(119, 6, 15), new Date(119, 6, 25), 91.8, "A am very tidy and relatively quiet. ", ReservationStatus.FINISHED);
				// Denied, stare
				Reservation r16 = new Reservation(16, 20, "teodora@mail.com", new Date(118, 10, 15), new Date(118, 10, 25), 37.8, "Please, may I stay? ", ReservationStatus.DENIED);
				Reservation r17 = new Reservation(17, 15, "dusan@mail.com", new Date(118, 10, 15), new Date(118, 10, 25), 18.0, "I would love to see your city! =D ", ReservationStatus.DENIED);
				Reservation r18 = new Reservation(18, 11, "sara@mail.com", new Date(118, 10, 15), new Date(118, 10, 25), 53.73, "Excited to come over! ", ReservationStatus.DENIED);
				Reservation r19 = new Reservation(19, 25, "marko@mail.com", new Date(118, 10, 15), new Date(118, 10, 25), 62.64, "I really love to travel! ", ReservationStatus.DENIED);
				Reservation r20 = new Reservation(20, 10, "nina@mail.com", new Date(118, 10, 15), new Date(118, 10, 25), 91.8, "I solemnly sware I will clean up my room. ", ReservationStatus.DENIED);
				// Canceled, stare
				Reservation r21 = new Reservation(21, 20, "marko@mail.com", new Date(117, 10, 15), new Date(117, 10, 25), 37.8, "Won't you please let me in? ", ReservationStatus.CANCELED);
				Reservation r22 = new Reservation(22, 15, "nina@mail.com", new Date(117, 10, 15), new Date(117, 10, 25), 18.0, "You better vacuum up the place before I get there! ", ReservationStatus.CANCELED);
				Reservation r23 = new Reservation(23, 11, "teodora@mail.com", new Date(117, 10, 15), new Date(117, 10, 25), 53.73, "I hope you have a big TV!", ReservationStatus.CANCELED);
				Reservation r24 = new Reservation(24, 25, "dusan@mail.com", new Date(117, 10, 15), new Date(117, 10, 25), 62.64, "I can't wait to see the town! ", ReservationStatus.CANCELED);
				Reservation r25 = new Reservation(25, 10, "sara@mail.com", new Date(117, 10, 15), new Date(117, 10, 25), 91.8, "Looking forward to this a lot! =D. ", ReservationStatus.CANCELED);
				// Accepted, prosli mesec, treba da odu u finished
				Reservation r26 = new Reservation(26, 20, "dusan@mail.com", new Date(120, 8, 15), new Date(117, 8, 25), 37.8, "Oh, please let me stay? ", ReservationStatus.ACCEPTED);
				Reservation r27 = new Reservation(27, 15, "sara@mail.com", new Date(120, 8, 16), new Date(117, 8, 26), 18.0, "May I come in? ", ReservationStatus.ACCEPTED);
				// Tekuce, sledeci mesec, treba odbiti ili prihvatiti
				Reservation r28 = new Reservation(28, 11, "dusan@mail.com", new Date(120, 10, 17), new Date(117, 10, 27), 53.73, "Are your cleaning ladies thorough?", ReservationStatus.CREATED);
				Reservation r29 = new Reservation(29, 11, "marko@mail.com", new Date(120, 10, 16), new Date(117, 10, 26), 53.73, "Please may I stay?", ReservationStatus.CREATED);
				Reservation r30 = new Reservation(30, 11, "nina@mail.com", new Date(120, 10, 18), new Date(117, 10, 28), 53.73, "I snore a bit, will that be a problem? ", ReservationStatus.CREATED);

				Reservation r31 = new Reservation(31, 25, "dusan@mail.com", new Date(120, 11, 17), new Date(120, 10, 27), 62.64, "I hope you have soft beds!", ReservationStatus.CREATED);
				Reservation r32 = new Reservation(32, 25, "marko@mail.com", new Date(120, 11, 16), new Date(120, 10, 26), 62.64, "I would like to rent your place. ", ReservationStatus.CREATED);
				Reservation r33 = new Reservation(33, 25, "sara@mail.com", new Date(120, 11, 18), new Date(120, 10, 28), 62.64, "Please may I stay?", ReservationStatus.CREATED);

				Reservation r34 = new Reservation(34, 10, "dusan@mail.com", new Date(120, 12, 17), new Date(120, 10, 27), 91.8, "I wish to rent out a temporary residence! ", ReservationStatus.CREATED);
				Reservation r35 = new Reservation(35, 10, "marko@mail.com", new Date(120, 12, 16), new Date(120, 10, 26), 91.8, "LET ME IN PLZ", ReservationStatus.CREATED);
				Reservation r36 = new Reservation(36, 10, "sara@mail.com", new Date(120, 12, 18), new Date(120, 10, 28), 91.8, "I want to stay in your place for a while. ", ReservationStatus.CREATED);
				
				reservations = new HashMap<Integer, Reservation> ();
				
				reservations.put(r1.getId(), r1);
				reservations.put(r2.getId(), r2);
				reservations.put(r3.getId(), r3);
				
				reservations.put(r4.getId(), r4);
				reservations.put(r5.getId(), r5);
				reservations.put(r6.getId(), r6);
				
				reservations.put(r7.getId(), r7);
				reservations.put(r8.getId(), r8);
				reservations.put(r9.getId(), r9);
				
				reservations.put(r10.getId(), r10);
				reservations.put(r11.getId(), r11);
				reservations.put(r12.getId(), r12);
				
				reservations.put(r13.getId(), r13);
				reservations.put(r14.getId(), r14);
				reservations.put(r15.getId(), r15);
				
				reservations.put(r16.getId(), r16);
				reservations.put(r17.getId(), r17);
				reservations.put(r18.getId(), r18);
				reservations.put(r19.getId(), r19);
				reservations.put(r20.getId(), r20);
				
				reservations.put(r21.getId(), r21);
				reservations.put(r22.getId(), r22);
				reservations.put(r23.getId(), r23);
				reservations.put(r24.getId(), r24);
				reservations.put(r25.getId(), r25);

				reservations.put(r26.getId(), r26);
				reservations.put(r27.getId(), r27);
				
				reservations.put(r28.getId(), r28);
				reservations.put(r29.getId(), r29);
				reservations.put(r30.getId(), r30);
				
				reservations.put(r31.getId(), r31);
				reservations.put(r32.getId(), r32);
				reservations.put(r33.getId(), r33);
				
				reservations.put(r34.getId(), r34);
				reservations.put(r35.getId(), r35);
				reservations.put(r36.getId(), r36);
				
				Data.saveReservations();
			}
		}
		return reservations;
	}
	
	
	public static HashMap<Integer, Comment> getComments () {
		if (comments == null) {
//			System.out.println("Data: Loading comments");
			try {
				comments = obj.readValue(new File(pathPrefix + "comments.txt"), new TypeReference<Map<Integer, Comment>>() {});
			} catch (Exception e) {
//				System.out.println("Error while loading comments! ");
				
				Comment c1 = new Comment(1, "teodora@mail.com", 1, "Was nice, thx!", 4);
				Comment c2 = new Comment(2, "dusan@mail.com", 2, "I had fun.", 3);
				Comment c3 = new Comment(3, "sara@mail.com", 3, "The town was beautiful!", 5);

				Comment c4 = new Comment(4, "teodora@mail.com", 4, "Wifi was wayyy to slow", 1);
				Comment c5 = new Comment(5, "dusan@mail.com", 5, "I had an okay time. ", 3);
				Comment c6 = new Comment(6, "sara@mail.com", 6, "You have loud neibours", 2);

				Comment c7 = new Comment(7, "dusab@mail.com", 7, "We barely left the apartment!", 5);
				Comment c8 = new Comment(8, "marko@mail.com", 8, "Very tidy and clean!", 4);
				Comment c9 = new Comment(9, "nina@mail.com", 9, "Strongly recommended. ", 5);

				Comment c10 = new Comment(10, "marko@mail.com", 10, "0/10, would not recommend :p", 1);
				Comment c11 = new Comment(11, "nina@mail.com", 11, "I barely spent any time in the room. ", 2);
				Comment c12 = new Comment(12, "sara@mail.com", 12, "Meh", 2);

				Comment c13 = new Comment(13, "marko@mail.com", 13, "T'was great =D", 5);
				Comment c14 = new Comment(14, "nina@mail.com", 14, "Was fein", 4);
				Comment c15 = new Comment(15, "teodora@mail.com", 15, "Was a bit dirty", 4);
				
				Comment c16 = new Comment(16, "marko@mail.com", 16, "Why don't you let me in?", 1);
				Comment c17 = new Comment(17, "marko@mail.com", 17, "This man is a fraud", 1);
				Comment c18 = new Comment(18, "marko@mail.com", 18, "Why was I declined?", 1);
				Comment c19 = new Comment(19, "marko@mail.com", 19, "I get it...", 1);
				Comment c20 = new Comment(20, "marko@mail.com", 20, "I didn't even wanna come; joke's on you!", 1);
				
				c1.setShow(true);
				c3.setShow(true);
				c5.setShow(true);
				c7.setShow(true);
				c9.setShow(true);
				c11.setShow(true);
				c13.setShow(true);
				c15.setShow(true);
				c17.setShow(true);
				c19.setShow(true);
				
				comments = new HashMap<Integer, Comment> ();
				
				comments.put(c1.getId(), c1);
				comments.put(c2.getId(), c2);
				comments.put(c3.getId(), c3);

				comments.put(c4.getId(), c4);
				comments.put(c5.getId(), c5);
				comments.put(c6.getId(), c6);

				comments.put(c7.getId(), c7);
				comments.put(c8.getId(), c8);
				comments.put(c9.getId(), c9);

				comments.put(c10.getId(), c10);
				comments.put(c11.getId(), c11);
				comments.put(c12.getId(), c12);

				comments.put(c13.getId(), c13);
				comments.put(c14.getId(), c14);
				comments.put(c15.getId(), c15);
				
				comments.put(c16.getId(), c16);
				comments.put(c17.getId(), c17);
				comments.put(c18.getId(), c18);
				comments.put(c19.getId(), c19);
				comments.put(c20.getId(), c20);
				
				Data.saveComments();
			}
		}
		return comments;
	}

	
	public static void saveUsers () {
		try {
			String filePath = pathPrefix + "users.txt";
//			System.out.println(obj.writeValueAsString(users));
			
			FileWriter fileWriter = new FileWriter(filePath);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj.writeValueAsString(users));
		    printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("Data: Users saved. ");
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
//		System.out.println("Data: Addresses saved. ");
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
//		System.out.println("Data: Locations saved. ");
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
//		System.out.println("Data: Apartments saved. ");
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
//		System.out.println("Data: Amenities saved. ");
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
//		System.out.println("Data: Reservations saved. ");
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
//		System.out.println("Data: Comments saved. ");
	}

}
