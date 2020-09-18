package services;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Apartment;
import model.Comment;
import model.Data;
import model.Reservation;
import model.enumerations.Role;
import utility.Utility;

@Path("/comments")
public class CommentService {
	
	public CommentService() {
		
	}

//	@GET
//	@Path("/test")	
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public HashMap<Integer, Comment> loadComments () {
////		System.out.println("Comment service: test");
//		return Data.getComments();
//	}
	
	@DELETE
//	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteComment (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Comment comment = Data.getComments().get(Integer.valueOf(requestData.get("id")));
		if (comment == null) {
			return Response.status(404).build();
		}
		if (comment.isDeleted()) {
			return Response.status(404).build();
		}
		comment.setDeleted(true);
		Data.saveComments();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
//	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postComment (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.GUEST) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Reservation reservation = Data.getReservations().get(Integer.valueOf(requestData.get("reservation")));
		if (reservation == null) {
			return Response.status(404).build();
		}
		if (requestData.get("rating").equals("-")) {
			return Response.status(400).build();
		}
		if (requestData.get("text").equals("")) {
			return Response.status(400).build();
		}
		Comment comment = new Comment(Data.getComments().keySet().size() + 1, 
				(String) request.getSession().getAttribute("username"), reservation.getId(), requestData.get("text"), 
				((!requestData.get("rating").equals("-")) ? (Integer.valueOf(requestData.get("rating"))) : (0) ) );
		Data.getComments().put(comment.getId(), comment);
		Data.saveComments();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	
	@POST
	@Path("/show/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response showComment (@Context HttpServletRequest request, 
			@PathParam("id") Integer id) {
		if (Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build();
		}
		Comment comment = Data.getComments().get(id);
		if (comment == null) {
			return Response.status(404).build();
		}
		Reservation reservation = Data.getReservations().get(comment.getReservation());
		Apartment apartment = Data.getApartments().get(reservation.getApartment());
		if (request.getSession().getAttribute("username").equals(apartment.getHost())) {
			comment.setShow(true);
			Data.saveComments();
			return Response.ok(true, MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.status(401).build();
		}
	}
	
	@POST
	@Path("/hide/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response hideComment (@Context HttpServletRequest request, 
				@PathParam("id") Integer id) {
		if (Utility.getRole(request) != Role.HOST) {
			return Response.status(401).build();
		}
		Comment comment = Data.getComments().get(id);
		if (comment == null) {
			return Response.status(404).build();
		}
		Reservation reservation = Data.getReservations().get(comment.getReservation());
		Apartment apartment = Data.getApartments().get(reservation.getApartment());
		if (request.getSession().getAttribute("username").equals(apartment.getHost())) {
			comment.setShow(false);
			Data.saveComments();
			return Response.ok(true, MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.status(401).build();
		}
	}
	
}
