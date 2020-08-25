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
} 
