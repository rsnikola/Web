package services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import dto.ReservationPageDTO;
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
	
//	@GET
//	@Path("/test")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public HashMap<Integer, Reservation> testReservations () {
//		System.out.println("Reservation service: test");
//		return Data.getReservations();
//	}
	
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
	@Path("/{sortBy}/{page}/{userToFind}/{reservationStatus}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loadReservations (@Context HttpServletRequest request, 
				@PathParam("sortBy") String sortBy, @PathParam("page") Integer page, 
				@PathParam("userToFind") String userToFind, @PathParam("reservationStatus") String reservationStatus) {
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
		if (!userToFind.equals("unfiltered")) {
			retVal = filterByGuest(retVal, userToFind);
		}
		if (!reservationStatus.equals("unfiltered")) {
			retVal = filterByStatus(retVal, reservationStatus);
		}
		ReservationPageDTO reservationPageDTO = new ReservationPageDTO();
		retVal = sort(retVal, sortBy);
		ArrayList<ReservationDTO> dtoList = new ArrayList<ReservationDTO> ();
		for (int i = page * 5; i < (((retVal.size()) < (page * 5 + 5)) ? (retVal.size()) : (page * 5 + 5)); ++i) {
			ReservationDTO newDto = new ReservationDTO(retVal.get(i));
			dtoList.add(newDto);
		}
		reservationPageDTO.setReservations(dtoList);
		if (5 * page + 5 < retVal.size()) {
			reservationPageDTO.setHasNextPage(true);
		}
		else {
			reservationPageDTO.setHasNextPage(false);
		}
		return Response.ok(reservationPageDTO, MediaType.APPLICATION_JSON).build();
	}
	
	
	private ArrayList<Reservation> filterByGuest (ArrayList<Reservation> reservations, String guestId) {
		ArrayList<Reservation> retVal = new ArrayList<Reservation> ();
		for (Reservation r : reservations) {
			if (r.getGuest().toLowerCase().contains(guestId.toLowerCase())) {
				retVal.add(r);
			}
		}
		return retVal; 
	}
	
	
	private ArrayList<Reservation> filterByStatus(ArrayList<Reservation> input, String reservationStatus) {
		ReservationStatus status; 
		switch (reservationStatus) {
			case "CREATED":
				status = ReservationStatus.CREATED;
				break; 
			case "DENIED": 
				status = ReservationStatus.DENIED;
				break;
			case "CANCELED": 
				status = ReservationStatus.CANCELED;
				break;
			case "ACCEPTED":
				status = ReservationStatus.ACCEPTED;
				break;
			case "FINISHED": 
				status = ReservationStatus.FINISHED;
				break;
			default: 
				return null;
		}
		ArrayList<Reservation> retVal = new ArrayList<Reservation> ();
		for (Reservation r : input) {
			if (r.getStatus() == status) {
				retVal.add(r);
			}
		}
		return retVal;
	}
	
	
	private ArrayList<Reservation> sort (ArrayList<Reservation> input, String sortBy) {
		ArrayList<Reservation> retVal = new ArrayList<Reservation>();
		Reservation temp;
		Reservation[] list = new Reservation[input.size()];
		for (int i = 0; i < input.size(); ++i) {
			list[i] = input.get(i);
		}
		for (int i = 0; i < input.size() - 1; ++i) {
			for (int j = i + 1; j < input.size(); ++j) {
				if (((list[i].getPrice() > list[j].getPrice()) && (sortBy.equals("asc"))) ||
					((list[i].getPrice() < list[j].getPrice()) && (sortBy.equals("desc"))))
				{
					temp = list[i];
					list[i] = list[j];
					list[j] = temp;
				}
			}
		}
		for (Reservation r : list) {
			retVal.add(r);
		}
		return retVal;
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
		
//		System.out.println();
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
