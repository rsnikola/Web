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
import javax.ws.rs.DELETE;
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
import dto.WelcomePageDTO;
import model.Address;
import model.Amenity;
import model.Apartment;
import model.Comment;
import model.Data;
import model.Location;
import model.Reservation;
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
			request.getSession().setAttribute("selected_apartment", retVal);
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
//				Utility.printMap(requestData);
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
		WelcomePageDTO welcomePageDTO = new WelcomePageDTO();
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
//		Map<String, String> requestData = Utility.getBodyMap(request);
		Map<String, Object> requestData = Utility.getWelcomeBodyMap(request);
//		Utility.printMap(requestData);
//		System.out.println("selectedAmenities: " + requestData.get("selectedAmenities").toString());
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
			retVal = filterFromDate(retVal, (String) requestData.get("fromDate"));
		}
		if (!requestData.get("priceMin").equals("unfiltered")) {
			retVal = filterPriceMin(retVal, (String) requestData.get("priceMin"));
		}
		if (!requestData.get("country").equals("unfiltered")) {
			retVal = filterCountry(retVal, (String) requestData.get("country"));
		}
		if (!requestData.get("priceMax").equals("unfiltered")) {
			retVal = filterPriceMax(retVal, (String) requestData.get("priceMax"));
		}
		if (!requestData.get("roomsMin").equals("unfiltered")) {
			retVal = filterRoomsMin(retVal, (String) requestData.get("roomsMin"));
		}
		if (!requestData.get("city").equals("unfiltered")) {
			retVal = filterCity(retVal, (String) requestData.get("city"));
		}
		if (!requestData.get("roomsMax").equals("unfiltered")) {
			retVal = filterRoomsMax(retVal, (String) requestData.get("roomsMax"));
		}
		if (!requestData.get("guestsMax").equals("unfiltered")) {
			retVal = filterGuestsMax(retVal, (String) requestData.get("guestsMax"));
		}
		if (!requestData.get("toDate").equals("unfiltered")) {
			retVal = filterToDate(retVal, (String) requestData.get("toDate"));
		}
		if (!requestData.get("guestsMin").equals("unfiltered")) {
			retVal = filterGuestsMin(retVal, (String) requestData.get("guestsMin"));
		}
		if (!requestData.get("apartmentType").equals("unfiltered")) {
			retVal = filterType(retVal, (String) requestData.get("apartmentType"));
		}
		if (requestData.get("selectedAmenities") != null) {
			ArrayList<String> amenityIds = (ArrayList<String>) requestData.get("selectedAmenities");
			for (String s : amenityIds) {
//				System.out.println(Data.getAmenities().get(Integer.valueOf(s)).getName());
				retVal = filterAmenity(retVal, Integer.valueOf(s));
				
			}
		}
		retVal = sort(retVal, (String) requestData.get("ascDesc"), (String) requestData.get("sort"));
		ArrayList<ApartmentOverviewDTO> dto = new ArrayList<ApartmentOverviewDTO> ();
		Integer page = Integer.valueOf((String) requestData.get("page"));
		for (int i = page * 5; i < (((retVal.size()) < (page * 5 + 5)) ? (retVal.size()) : (page * 5 + 5)); ++i) {
			ApartmentOverviewDTO newDto = new ApartmentOverviewDTO(retVal.get(i));
			dto.add(newDto);
		}
		welcomePageDTO.setApartments(dto);
		if (5 * page + 5 < retVal.size()) {
			welcomePageDTO.setHasNextPage(true);
		}
		else {
			welcomePageDTO.setHasNextPage(false);
		}
		welcomePageDTO.setPage(page);
		for (Amenity a : Data.getAmenities().values()) {
			if (!a.isDeleted()) {
				welcomePageDTO.getAmenities().add(a);
			}
		}
		return Response.ok(welcomePageDTO, MediaType.APPLICATION_JSON).build();
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
	
	private ArrayList<Apartment> filterAmenity (ArrayList<Apartment> input, Integer amenityId) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment> ();
		for (Apartment ap : input) {
			for (Integer am : ap.getAmenities()) {
				if (am == amenityId) {
					retVal.add(ap);
					continue;
				}
			}
		}
		return retVal;
	}
	
	private ArrayList<Apartment> sort (ArrayList<Apartment> input, String price, String criteria) {
		ArrayList<Apartment> retVal = new ArrayList<Apartment> ();
		Apartment temp;
		Apartment[] list = new Apartment[input.size()];
		for (int i = 0; i < input.size(); ++i) {
			list[i] = input.get(i);
		}
//		System.out.println("Criteria is: " + criteria);
		switch (criteria) {
			case "unfiltered":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if (list[i].getPricePerNight() > list[j].getPricePerNight()) {
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "firstAvailable":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if ((list[j].getFirstAvailable() == null) || (list[i].getFirstAvailable() == null)) {
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
							continue;
						}
						else if (((list[j].getFirstAvailable().before(list[i].getFirstAvailable()) && (price.equals("asc")) ||
							((list[i].getFirstAvailable().before(list[j].getFirstAvailable()) && (price.equals("desc")))))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "lastAvailable":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if ((list[j].getFirstAvailable() == null) || (list[i].getFirstAvailable() == null)) {
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
							continue;
						}
						if (((list[i].getLastAvailable().before(list[j].getLastAvailable()) && (price.equals("desc")) ||
							((list[j].getLastAvailable().before(list[i].getLastAvailable()) && (price.equals("asc")))))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "noOfRooms":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if (((list[i].getRooms() > list[j].getRooms()) && (price.equals("asc"))) ||
							((list[i].getRooms() < list[j].getRooms()) && (price.equals("desc"))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "noOfGuests":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if (((list[i].getGuests() > list[j].getGuests()) && (price.equals("asc"))) ||
							((list[i].getGuests() < list[j].getGuests()) && (price.equals("desc"))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "price":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if (((list[i].getPricePerNight() > list[j].getPricePerNight()) && (price.equals("asc"))) ||
							((list[i].getPricePerNight() < list[j].getPricePerNight()) && (price.equals("desc"))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "city":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						Location li = Data.getLocations().get(list[i].getLocation());
						Location lj = Data.getLocations().get(list[j].getLocation());
						Address ai = Data.getAddresses().get(li.getAddress());
						Address aj = Data.getAddresses().get(lj.getAddress());
						if (((ai.getTown().compareTo(aj.getTown()) > 0) && (price.equals("asc"))) ||
							((ai.getTown().compareTo(aj.getTown()) < 0) && (price.equals("desc"))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "country":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						Location li = Data.getLocations().get(list[i].getLocation());
						Location lj = Data.getLocations().get(list[j].getLocation());
						Address ai = Data.getAddresses().get(li.getAddress());
						Address aj = Data.getAddresses().get(lj.getAddress());
						if (((ai.getCountry().compareTo(aj.getCountry()) > 0) && (price.equals("asc"))) ||
							((ai.getCountry().compareTo(aj.getCountry()) < 0) && (price.equals("desc"))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			case "type":
				for (int i = 0; i < input.size() - 1; ++i) {
					for (int j = i + 1; j < input.size(); ++j) {
						if (((list[i].getType().compareTo(list[j].getType()) > 0) && (price.equals("asc"))) ||
							(((list[i].getType().compareTo(list[j].getType()) < 0) && (price.equals("desc")))))
						{
							temp = list[i];
							list[i] = list[j];
							list[j] = temp;
						}
					}
				}
				break;
			
		}
		for (Apartment a : list) {
			retVal.add(a);
		}
		return retVal;
	}
	
	@PUT
	@Path("/amenity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAmenity (@Context HttpServletRequest request) throws IOException {
		HashMap<String, String> requestData = Utility.getBodyMap(request);
		Amenity amenity = null;
		for (Amenity a : Data.getAmenities().values()) {
			if (a.getName().equals(requestData.get("name"))) {
				amenity = a;
				break;
			}
		}
		if (amenity == null) {
			return Response.status(404).build();
		}
		Apartment apartment = (Apartment) request.getSession().getAttribute("selected_apartment");
		boolean isNew = true;
		for (Integer i : apartment.getAmenities()) {
			if (i == amenity.getId()) {
				isNew = false;
				break;
			}
		}
		if (isNew) {
			apartment.getAmenities().add(amenity.getId());
			Data.saveApartments();
		}
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	
	@GET
	@Path("/amenities/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAmenities (@Context HttpServletRequest request, 
				@PathParam("id") Integer id) {
		Apartment apartment = Data.getApartments().get(id);
		if (apartment == null) {
			return Response.status(404).build();
		}
		ArrayList<Amenity> retVal = new ArrayList<Amenity>();
		if (apartment.getAmenities() == null) {
			apartment.setAmenities(new ArrayList<Integer>());
		}
 		for (Integer i : apartment.getAmenities()) {
			if (!Data.getAmenities().get(i).isDeleted()) {
				retVal.add(Data.getAmenities().get(i));
			}
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	
	@DELETE
	@Path("/amenities/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeAmenity (@Context HttpServletRequest request, 
				@PathParam("id") Integer id) throws IOException {
		Apartment apartment = Data.getApartments().get(id);
		if (apartment == null) {
			return Response.status(404).build();
		}
		HashMap<String, String> requestData = Utility.getBodyMap(request);
		ArrayList<Integer> newList = new ArrayList<Integer>();
		for (Integer i : apartment.getAmenities()) {
			if (i != Integer.valueOf(requestData.get("delete"))) {
				newList.add(i);
			}
		}
		apartment.setAmenities(newList);
		Data.saveApartments();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{id}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments (@Context HttpServletRequest request, 
			@PathParam("id") Integer id) {
		ArrayList<Comment> retVal = new ArrayList<Comment> ();
		if (Utility.getRole(request) == Role.HOST) {
			for (Comment c : Data.getComments().values()) {
				if (!c.isDeleted()) {
					Reservation r = Data.getReservations().get(id);
					if (r != null) {
						if (r.getApartment() == id) {
							retVal.add(c);
						}
					}
				}
			}
		}
		else if ((Utility.getRole(request) == Role.GUEST) || (Utility.getRole(request) == Role.UNREGISTERED)) {
			for (Comment c : Data.getComments().values()) {
				if (!c.isDeleted()) {
					if (c.isShow()) {
						retVal.add(c);
					}
				}
			}
		}
		else if (Utility.getRole(request) == Role.ADMIN) {
			for (Comment c : Data.getComments().values()) {
				if (!c.isDeleted()) {
					retVal.add(c);
				}
			}
		}
		
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
} 
