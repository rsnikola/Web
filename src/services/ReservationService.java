package services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Apartment;
import model.Data;
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
	public HashMap<Integer, Reservation> loadReservations () {
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
		Utility.printMap(requestData);
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
	    	apartment.setActive(false);
	    	Data.saveApartments();
	    	return Response.ok(0, MediaType.APPLICATION_JSON).build();
	    }
	    else {
	    	return Response.ok(ChronoUnit.DAYS.between(cRequestFirst.getTime().toInstant(), 
	    			cLast.getTime().toInstant()), MediaType.APPLICATION_JSON).build();
	    }
	}
	
	
	
}
