package services;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dto.ApartmentOverviewDTO;
import model.Address;
import model.Apartment;
import model.Data;
import model.Location;
import model.enumerations.ApartmentType;
import model.enumerations.Role;
import utility.Utility;

@Path("/apartments")
public class ApartmentService {

	public ApartmentService () {
//		System.out.println("Apartment service");
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<Integer, Apartment> loadApartments () {
		System.out.println("Apartment service: test");
		return Data.getApartments();
	}
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getApartments (@Context HttpServletRequest request) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		switch (Utility.getRole(request)) {
			case UNREGISTERED:
			case GUEST: 
				for (Apartment a : Data.getApartments().values()) {
					if (a.isActive()) {
						retVal.add(a);
					}
				}
				break;
			case HOST:
				for (Apartment a : Data.getApartments().values()) {
					if (a.getHost().equals(request.getSession().getAttribute("username"))) {
						retVal.add(a);
					}
				}
				break;
			case ADMIN: 
				for (Apartment a : Data.getApartments().values()) {
					retVal.add(a);
				}
				break;
			default:
				return Response.status(Response.Status.FORBIDDEN).build();
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/address/{location_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAddress (@PathParam("location_id") Integer location_id) {
		Location location = Data.getLocations().get(location_id);
		Address address = Data.getAddresses().get(location.getAddress());
		String retVal = "{\"retVal\":\"" + address.getStreetNumber() + " " + address.getStreet() + ",  " + 
					address.getZipCode() + " " + address.getTown() + ", " + address.getCountry() + "\"}";
		return retVal;
	}
	
	@GET
	@Path("/{apartment_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAparment (@PathParam("apartment_id") Integer apartment_id, @Context HttpServletRequest request) {
		Apartment retVal = Data.getApartments().get(apartment_id);
		// Ako nisam nasao apartman, ili ako je obrisan
		if ((retVal == null) || (retVal.isDeleted())) {
			return Response.status(404).build();
		}
		// Ako sam domacin, smem da vidim samo MOJE apartmane
		else if (Utility.getRole(request) == Role.HOST) {
			if (retVal.getHost().equals(request.getSession().getAttribute("username"))) {
				request.getSession().setAttribute("selected_apartment", retVal);
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
			else {
				request.getSession().setAttribute("selected_apartment", null);
				return Response.status(401).build();
			}
		}
		// Ako sam obican korisnik, ne smem da vidim neaktivne apartmane
		else if ((Utility.getRole(request) == Role.GUEST) || (Utility.getRole(request) == Role.UNREGISTERED)) {
			if (retVal.isActive()) {
				request.getSession().setAttribute("selected_apartment", retVal);
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
			else {
				request.getSession().setAttribute("selected_apartment", null);
				return Response.status(401).build();
			}
		}
		// Admin je svevideci
		else {
			return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
		}
	}
	
	@GET
	@Path("/selected")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSelectedApartment (@Context HttpServletRequest request) {
		Apartment retVal = (Apartment) request.getSession().getAttribute("selected_apartment");
		if (retVal != null) {
			if (!retVal.isDeleted()) {
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
		}
		return Response.status(404).build();
	}
	
	@SuppressWarnings("deprecation")
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addApartment (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build(); 
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
//		for (String s : requestData.keySet()) {
//			System.out.println("    " + s + ": " + requestData.get(s));
//		}
		Apartment newApartment = new Apartment();
		newApartment.setHost((String) request.getSession().getAttribute("username"));
		newApartment.setId(Data.getApartments().keySet().size() + 1);
		if (requestData.get("room").equals("apartment")) {
			newApartment.setType(ApartmentType.APARTMENT);
			newApartment.setRooms(Integer.valueOf(requestData.get("noOfRooms")));
		}
		else {
			newApartment.setType(ApartmentType.ROOM);
			newApartment.setRooms(1);
		}
		newApartment.setGuests(Integer.valueOf(requestData.get("noOfGuests")));

		Location newLocation = new Location();
		newLocation.setId(Data.getLocations().keySet().size() + 1);
		newLocation.setLongitude(Double.valueOf(requestData.get("longitude")));
		newLocation.setLatitude(Double.valueOf(requestData.get("latitude")));
		newLocation.setDeleted(false);
		
		Address newAddress = new Address();
		newAddress.setId(Data.getAddresses().keySet().size() + 1);
		newAddress.setStreetNumber(Integer.valueOf(requestData.get("streetNumber")));
		newAddress.setStreet(requestData.get("streetName"));
		newAddress.setTown(requestData.get("town"));
		newAddress.setZipCode(requestData.get("zipCode"));
		newAddress.setCountry(requestData.get("country"));
		newAddress.setDeleted(false);

		newLocation.setAddress(newAddress.getId());
		newApartment.setLocation(newLocation.getId());
		/*
		 * 
		 * */
		newApartment.setCheckinTime(new Time((int) Integer.valueOf(requestData.get("checkinTime").split(":")[0]), 
				(int) Integer.valueOf(requestData.get("checkinTime").split(":")[1]), 0));
		newApartment.setCheckoutTime(new Time((int) Integer.valueOf(requestData.get("checkoutTime").split(":")[0]), 
				(int) Integer.valueOf(requestData.get("checkoutTime").split(":")[1]), 0));
		newApartment.setFirstAvailable(null);
		newApartment.setLastAvailable(null);
		newApartment.setComments(null);
		newApartment.setPricePerNight(Double.valueOf(requestData.get("pricePerNight")));
		newApartment.setActive(false);
		newApartment.setDeleted(false);
		newApartment.setAmenities(null);
		newApartment.setReservations(null);
		Data.getAddresses().put(newAddress.getId(), newAddress);
		Data.getLocations().put(newLocation.getId(), newLocation);
		Data.getApartments().put(newApartment.getId(), newApartment);
		Data.saveAddresses();
		Data.saveLocations();
		Data.saveApartments();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@SuppressWarnings("deprecation")
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateApartment (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build(); 
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Apartment apartment = Data.getApartments().get(Integer.valueOf(requestData.get("id").split(" ")[0]));
		if (apartment == null) {
			return Response.status(404).build();
		}
		apartment.setCheckinTime(new Time((int) Integer.valueOf(requestData.get("checkinTime").split(":")[0]), 
				(int) Integer.valueOf(requestData.get("checkinTime").split(":")[1]), 0));
		apartment.setCheckoutTime(new Time((int) Integer.valueOf(requestData.get("checkoutTime").split(":")[0]), 
				(int) Integer.valueOf(requestData.get("checkoutTime").split(":")[1]), 0));
		apartment.setGuests(Integer.valueOf(requestData.get("noOfGuests").split(" ")[0]));
		apartment.setPricePerNight(Double.valueOf(requestData.get("pricePerNight").split(" ")[0]));
		if (requestData.get("room").equals("apartment")) {
			apartment.setType(ApartmentType.APARTMENT);
			apartment.setRooms(Integer.valueOf(requestData.get("noOfRooms").split(" ")[0]));
		}
		else {
			apartment.setType(ApartmentType.ROOM);
			apartment.setRooms(1);
		}
		Data.saveApartments();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();	
	}
	
	@PUT
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateActive (@Context HttpServletRequest request) throws IOException, ParseException {
		if (Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build(); 
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Apartment apartment = Data.getApartments().get(Integer.valueOf(requestData.get("id").split(" ")[0]));
		if (apartment == null) {
			return Response.status(404).build();
		}
		if (Utility.getRole(request) == Role.HOST) {
			if (apartment.getHost().equals(request.getSession().getAttribute("username"))) {
				Utility.printMap(requestData);
				if (requestData.get("isActive").equals("true ")) {
					Date ativeFrom = new SimpleDateFormat("yyyy-MM-dd").parse(requestData.get("activeFrom"));
					Date activeTo = new SimpleDateFormat("yyyy-MM-dd").parse(requestData.get("activeTo"));
					Calendar c = Calendar.getInstance();
					c.setTime(ativeFrom);
					c.add(Calendar.DATE, 1);
					ativeFrom = c.getTime();
					c.setTime(activeTo);
					c.add(Calendar.DATE, 1);
					activeTo = c.getTime();
					apartment.setActive(true);
					apartment.setFirstAvailable(ativeFrom);
					apartment.setLastAvailable(activeTo);
					Data.saveApartments();
				}
				else {
					apartment.setActive(false);
					Data.saveApartments();
				}
				return Response.ok(true, MediaType.APPLICATION_JSON).build();
			}
			return Response.status(401).build();
		}
		return Response.status(401).build();
	}
	
	@GET
	@Path("/dates")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFreeDates (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build(); 
		}
		Apartment apartment = (Apartment) request.getSession().getAttribute("selected_apartment");
		if (apartment == null) {
			return Response.status(404).build();
		}
		Date firstDate = apartment.getFirstAvailable();
		Date lastAvailable = apartment.getLastAvailable();
		ArrayList<ArrayList<String>> retVal = new ArrayList<ArrayList<String>> ();
		Date temp = firstDate;
		Calendar c = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<String> tempList;
		while (temp.before(lastAvailable)) {
			System.out.println(temp.toString() + ": " + dateFormat.format(temp));
			tempList = new ArrayList<String>();
			tempList.add(temp.toString());
			tempList.add(dateFormat.format(temp));
			retVal.add(tempList);
			c.setTime(temp);
			c.add(Calendar.DATE, 1);
			temp = c.getTime();
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response filter (@Context HttpServletRequest request) throws IOException {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		Map<String, String> requestData = Utility.getBodyMap(request);
		Utility.printMap(requestData);
		if (Utility.getRole(request) == Role.ADMIN) {
			for (Apartment a : Data.getApartments().values()) {
				if (!a.isDeleted()) {
					retVal.add(a);
				}
			}
		}
		else if ((Utility.getRole(request) == Role.GUEST) || (Utility.getRole(request) == Role.UNREGISTERED)) {
			for (Apartment a : Data.getApartments().values()) {
				if (!a.isDeleted()) {
					if (a.isActive()) {
						retVal.add(a);
					}
				}
			}
		}
		else if (Utility.getRole(request) == Role.HOST) {
			for (Apartment a : Data.getApartments().values()) {
				if (!a.isDeleted()) {
					if (a.getHost().equals(request.getSession().getAttribute("username"))) {
						retVal.add(a);
					}
				}
			}
		}
		if (!requestData.get("fromDate").equals("unfiltered")) {
			retVal = filterFromDate(retVal, requestData.get("fromDate"));
		}
		if (!requestData.get("priceMin").equals("unfiltered")) {
			retVal = filterPriceMin(retVal, requestData.get("priceMin"));
		}
		if (!requestData.get("country").equals("unfiltered")) {
			retVal = filterCountry(retVal, requestData.get("country"));
		}
		if (!requestData.get("priceMax").equals("unfiltered")) {
			retVal = filterPriceMax(retVal, requestData.get("priceMax"));
		}
		if (!requestData.get("roomsMin").equals("unfiltered")) {
			retVal = filterRoomsMin(retVal, requestData.get("roomsMin"));
		}
		if (!requestData.get("city").equals("unfiltered")) {
			retVal = filterCity(retVal, requestData.get("city"));
		}
		if (!requestData.get("roomsMax").equals("unfiltered")) {
			retVal = filterRoomsMax(retVal, requestData.get("roomsMax"));
		}
		if (!requestData.get("guestsMax").equals("unfiltered")) {
			retVal = filterGuestsMax(retVal, requestData.get("guestsMax"));
		}
		if (!requestData.get("toDate").equals("unfiltered")) {
			retVal = filterToDate(retVal, requestData.get("toDate"));
		}
		if (!requestData.get("guestsMin").equals("unfiltered")) {
			retVal = filterGuestsMin(retVal, requestData.get("guestsMin"));
		}
		if (!requestData.get("apartmentType").equals("unfiltered")) {
			retVal = filterType(retVal, requestData.get("apartmentType"));
		}
		// Nakon sto sam profiltrirao, vreme je da sortiram
		if (requestData.get("ascDesc").equals("asc")) {
			retVal = sortAsc(retVal);
		}
		else {
			retVal = sortDesc(retVal);
		}
		ArrayList<ApartmentOverviewDTO> dto = new ArrayList<ApartmentOverviewDTO> ();
		for (int i = 0; i < retVal.size(); ++i) {
			ApartmentOverviewDTO newDto = new ApartmentOverviewDTO(retVal.get(i));
			dto.add(newDto);
		}
		return Response.ok(dto, MediaType.APPLICATION_JSON).build();
	}

	private ArrayList<Apartment> filterFromDate (ArrayList<Apartment> input, String fromDate) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment> ();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dat = new Date();
		try {
			dat = dateFormat.parse(fromDate);
		} 
		catch (Exception e) {
			System.out.println("Nisam uspeo da parsiram " + fromDate);
			return null;
		}
		for (Apartment a : input) {
			if (!a.getFirstAvailable().before(dat)) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> filterToDate (ArrayList<Apartment> input, String fromDate) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment> ();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dat = new Date();
		try {
			dat = dateFormat.parse(fromDate);
		

		    Calendar cLastAvailable = Calendar.getInstance();
			for (Apartment a : input) {
			    cLastAvailable.setTime(a.getLastAvailable());
			    cLastAvailable.set(Calendar.HOUR, 0);
			    cLastAvailable.set(Calendar.MINUTE, 0);
			    cLastAvailable.set(Calendar.SECOND, 0);
			    cLastAvailable.set(Calendar.MILLISECOND, 0);
				if (!cLastAvailable.getTime().after(dat)) {
					retVal.add(a);
				    System.out.println(dateFormat.format(cLastAvailable.getTime()));
				    System.out.println("V");
				    System.out.println(dateFormat.format(dat));
				}
			}
		} 
		catch (Exception e) {
			System.out.println("Nisam uspeo da parsiram " + fromDate);
			return null;
		}  
		return retVal;
	}
	
	private ArrayList<Apartment> filterPriceMin (ArrayList<Apartment> input, String priceMin) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getPricePerNight() >= Double.parseDouble(priceMin)) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> filterPriceMax (ArrayList<Apartment> input, String priceMax) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getPricePerNight() <= Double.parseDouble(priceMax)) {
				retVal.add(a);
			}
		}
		return retVal;
	}

	private ArrayList<Apartment> filterCountry(ArrayList<Apartment> input, String country) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			Location loc = Data.getLocations().get(a.getLocation());
			Address adr = Data.getAddresses().get(loc.getAddress());
			if (adr.getCountry().toLowerCase().contains(country.toLowerCase())) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> filterCity(ArrayList<Apartment> input, String city) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			Location loc = Data.getLocations().get(a.getLocation());
			Address adr = Data.getAddresses().get(loc.getAddress());
			if (adr.getTown().toLowerCase().contains(city.toLowerCase())) {
				retVal.add(a);
			}
		}
		return retVal;
	}

	private ArrayList<Apartment> filterRoomsMin (ArrayList<Apartment> input, String roomsMin) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getRooms() >= Integer.valueOf(roomsMin)) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> filterRoomsMax (ArrayList<Apartment> input, String roomsMax) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getRooms() <= Integer.valueOf(roomsMax)) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> filterGuestsMax (ArrayList<Apartment> input, String guestsMax) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getGuests() <= Integer.valueOf(guestsMax)) {
				retVal.add(a);
			}
		}
		return retVal;
	}

	private ArrayList<Apartment> filterGuestsMin (ArrayList<Apartment> input, String guestsMin) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : input) {
			if (a.getGuests() >= Integer.valueOf(guestsMin)) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> sortAsc (ArrayList<Apartment> input) {
		Apartment temp;
		Apartment[] list = new Apartment[input.size()];
		for (int i = 0; i < input.size(); ++i) {
			list[i] = input.get(i);
		}
		for (int i = 0; i < input.size() - 1; ++i) {
			for (int j = i + 1; j < input.size(); ++j) {
				if (list[i].getPricePerNight() > list[j].getPricePerNight()) {
					temp = list[i];
					list[i] = list[j];
					list[j] = temp;
				}
			}
		}
		input = new ArrayList<Apartment> ();
		for (int i = 0; i < list.length; ++i) {
			input.add(list[i]);
		}
		return input;
	}
	
	private ArrayList<Apartment> sortDesc (ArrayList<Apartment> input) {
		Apartment temp;
		Apartment[] list = new Apartment[input.size()];
		for (int i = 0; i < input.size(); ++i) {
			list[i] = input.get(i);
		}
		for (int i = 0; i < input.size() - 1; ++i) {
			for (int j = i + 1; j < input.size(); ++j) {
				if (list[i].getPricePerNight() < list[j].getPricePerNight()) {
					temp = list[i];
					list[i] = list[j];
					list[j] = temp;
				}
			}
		}
		input = new ArrayList<Apartment> ();
		for (int i = 0; i < list.length; ++i) {
			input.add(list[i]);
		}
		return input;
	}
	
	private ArrayList<Apartment> filterType (ArrayList<Apartment> input, String type) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment> ();
		ApartmentType t = ((type.equals("apartment")) ? (ApartmentType.APARTMENT) : (ApartmentType.ROOM));
		for (Apartment a : input) {
			if (a.getType() == t) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
} 
