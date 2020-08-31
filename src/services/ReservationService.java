package services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
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

import dto.ReservationDTO;
import model.Address;
import model.Apartment;
import model.Data;
import model.Location;
import model.Reservation;
import model.enumerations.ReservationStatus;
import model.enumerations.Role;
import utility.Utility;

@Path("/reservation")
public class ReservationService {

	public ReservationService () {
		
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<Integer, Reservation> testReservations () {
		System.out.println("Reservation service: test");
		return Data.getReservations();
	}
	
	@GET
	@Path("/price/{non}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPrice (@Context HttpServletRequest request, 
			@PathParam("non") Integer numberOfNights) throws IOException{
		if (Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build();
		}
		Apartment apartment = (Apartment) request.getSession().getAttribute("selected_apartment");
		if (apartment == null) {
			return Response.status(404).build();
		}
		Double retVal = (double) Math. round(apartment.getPricePerNight() * numberOfNights * 100) / 100;
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReservation (@Context HttpServletRequest request) throws IOException, ParseException {
		if (Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
//		Utility.printMap(requestData);
		Apartment apartment = (Apartment) request.getSession().getAttribute("selected_apartment");
		if (apartment == null) {
			return Response.status(404).build();
		}
	    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  

	    Calendar cFirst = Calendar.getInstance();
	    cFirst.setTime(dateFormat.parse(dateFormat.format(apartment.getFirstAvailable())));
	    Calendar cLast = Calendar.getInstance();
	    cLast.setTime(dateFormat.parse(dateFormat.format(apartment.getLastAvailable())));
	    Calendar cRequestFirst = Calendar.getInstance();
	    cRequestFirst.setTime(dateFormat.parse(requestData.get("dat")));
	    Calendar cRequestLast = Calendar.getInstance();
	    cRequestLast.setTime(cRequestFirst.getTime());
	    cRequestLast.add(Calendar.DATE, Integer.valueOf(requestData.get("numberOfNights")));
	    if (!cLast.before(cRequestLast)) {
	    	Reservation newReservation = new Reservation();
	    	newReservation.setId(Data.getReservations().keySet().size() + 1);
	    	newReservation.setReservationMessage(requestData.get("message"));
	    	newReservation.setApartment(apartment.getId());
	    	newReservation.setGuest((String) request.getSession().getAttribute("username"));
	    	newReservation.setPrice(apartment.getPricePerNight() * Double.parseDouble(requestData.get("numberOfNights")));
	    	newReservation.setStatus(ReservationStatus.CREATED);
	    	newReservation.setStart(cRequestFirst.getTime());
	    	newReservation.setEnd(cRequestLast.getTime());
	    	Data.getReservations().put(newReservation.getId(), newReservation);
	    	Data.saveReservations();
//	    	apartment.setActive(false);
	    	Data.saveApartments();
	    	return Response.ok(0, MediaType.APPLICATION_JSON).build();
	    }
	    else {
	    	return Response.ok(ChronoUnit.DAYS.between(cRequestFirst.getTime().toInstant(), 
	    			cLast.getTime().toInstant()), MediaType.APPLICATION_JSON).build();
	    }
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loadReservations (@Context HttpServletRequest request) {
		ArrayList<Reservation> retVal = new ArrayList<Reservation> ();
		if (Utility.getRole(request) == Role.GUEST) {
			for (Reservation r : Data.getReservations().values()) {
				if (r.getGuest().equals(request.getSession().getAttribute("username"))) {
					if (!r.isDeleted()) {
						retVal.add(r);
					}
				}
			}
		}
		else if (Utility.getRole(request) == Role.HOST) {
			for (Reservation r : Data.getReservations().values()) {
				Apartment apartment = Data.getApartments().get(r.getApartment());
				if (apartment.getHost().equals(request.getSession().getAttribute("username"))) {
					if (!r.isDeleted()) {
						retVal.add(r);
					}
				}
			}
		}
		else if (Utility.getRole(request) == Role.ADMIN) {
			for (Reservation r : Data.getReservations().values()) {
				if (!r.isDeleted()) {
					retVal.add(r);
				}
			}
		}
		else {
			return Response.status(401).build();
		}
		ArrayList<ReservationDTO> dtoList = new ArrayList<ReservationDTO> ();
		for (Reservation r : retVal) {
			dtoList.add(new ReservationDTO(r));
		}
		return Response.ok(dtoList, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/address/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAddress (@Context HttpServletRequest request, 
			@PathParam("apartmentId") Integer apartmentId) throws IOException {
		if (Utility.getRole(request) == Role.UNREGISTERED) {
			return Response.status(401).build();
		}
		Apartment apartment = Data.getApartments().get(apartmentId);
		if (apartment == null) {
			return Response.status(404).build();
		}
		Location loc = Data.getLocations().get(apartment.getLocation());
		Address adr = Data.getAddresses().get(loc.getAddress());
//		retVal += adr.getStreet() + " " + adr.getStreetNumber() + " " + adr.getTown() + 
//					" " + adr.getCountry();
		return Response.ok(adr, MediaType.APPLICATION_JSON).build();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PUT
	@Path("/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancel (@Context HttpServletRequest request) throws IOException {
		if(Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
//		Utility.printMap(requestData);
		Reservation reservation = Data.getReservations().get(requestData.get("id"));
		if (reservation == null) {
			return Response.status(404).build();
		}
		if (!request.getSession().getAttribute("username").equals(reservation.getGuest())) {
			return Response.status(401).build();
		}
		reservation.setStatus(ReservationStatus.CANCELED);
		Data.saveReservations();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PUT
	@Path("/accept")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response accept (@Context HttpServletRequest request) throws IOException {
		if(Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
//		Utility.printMap(requestData);
		Reservation reservation = Data.getReservations().get(requestData.get("id"));
		if (reservation == null) {
			return Response.status(404).build();
		}
		
		// 2) 
		Apartment apartment = Data.getApartments().get(reservation.getApartment());
		if (apartment == null) {
			return Response.status(404).build();
		}
		apartment.setActive(false);
		
		for (Integer i : Data.getReservations().keySet()) {
			if (Data.getReservations().get(i).getApartment() == reservation.getApartment()) {
				if ((Data.getReservations().get(i).getStatus() == ReservationStatus.CREATED) ||
						(Data.getReservations().get(i).getStatus() == ReservationStatus.ACCEPTED)){
					Data.getReservations().get(i).setStatus(ReservationStatus.DENIED);	
				}
			}
		}
		// 1)
		reservation.setStatus(ReservationStatus.ACCEPTED);
		
		Data.saveReservations();
		Data.saveApartments();
		
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PUT
	@Path("/decline")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response decline (@Context HttpServletRequest request) throws IOException {
		if(Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Reservation reservation = Data.getReservations().get(requestData.get("id"));
		if (reservation == null) {
			return Response.status(404).build();
		}
		reservation.setStatus(ReservationStatus.DENIED);
		
		Data.saveReservations();
		
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@PUT
	@Path("/end")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response end (@Context HttpServletRequest request) throws IOException {
		if(Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Reservation reservation = Data.getReservations().get(requestData.get("id"));
		if (reservation == null) {
			return Response.status(404).build();
		}

//		Calendar c = Calendar.getInstance();
		Date dat = new Date();
		
		
//		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
//	    System.out.println(dateFormat.format(dat));
//	    System.out.println(dateFormat.format(reservation.getEnd()));
//	    cFirst.setTime(dateFormat.parse(dateFormat.format(apartment.getFirstAvailable())));
		
		System.out.println();
		if (reservation.getEnd().before(dat)) {
			reservation.setStatus(ReservationStatus.FINISHED);
			Data.saveReservations();
			return Response.ok(true, MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.ok(false, MediaType.APPLICATION_JSON).build();
		}
		
	}
	
	
	
}