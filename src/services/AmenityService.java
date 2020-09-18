package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Amenity;
import model.Data;
import model.enumerations.Role;
import utility.Utility;

@Path("/amenities")
public class AmenityService {

	public AmenityService() {
		
	}
	
//	@GET
//	@Path("/test")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public HashMap<Integer, Amenity> loadAmenities () {
//		System.out.println("Amenities: test");
//		return Data.getAmenities();
//	}
	
	@POST
//	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAmenity (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.ADMIN) {
			return Response.status(401).build();
		}
		boolean unique = true;
		Map<String, String> requestData = Utility.getBodyMap(request);
		// Ako je amenity jedinstvenog imena, dodaj ga i memorisi
		for (Integer i : Data.getAmenities().keySet()) {
			System.out.println("Compare: ");
			System.out.println("    " + Data.getAmenities().get(i).getName().toLowerCase());
			System.out.println("    with");
			System.out.println("    " + requestData.get("newAmenity").toLowerCase());
			if (Data.getAmenities().get(i).getName().toLowerCase().equals(requestData.get("newAmenity").toLowerCase())) {
				if (!Data.getAmenities().get(i).isDeleted()) {
					unique = false; 
					break;
				}
			}
		}
		if (unique) {
			Amenity newAmenity = new Amenity();
			newAmenity.setId(Data.getAmenities().keySet().size() + 1);
			newAmenity.setName(requestData.get("newAmenity"));
			Data.getAmenities().put(newAmenity.getId(), newAmenity);
			Data.saveAmenities();
			return Response.ok(true, MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.status(409).build();
		}
	}
	
	@GET
//	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAmenities (@Context HttpServletRequest request) {
//		if (Utility.getRole(request) != Role.ADMIN) {
//			return Response.status(401).build();
//		}
		ArrayList<Amenity> retVal = new ArrayList<Amenity> ();
		for (Amenity a : Data.getAmenities().values()) {
			if (!a.isDeleted()) {
				retVal.add(a);
			}
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@PUT
//	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAmenity (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.ADMIN) {
			return Response.status(401).build();
		}
		boolean unique = true;
		Map<String, String> requestData = Utility.getBodyMap(request);
//		Utility.printMap(requestData);
//		System.out.println("Neko pokusava da preimenuje " + Data.getAmenities().get(
//					Integer.valueOf(requestData.get("id").split(" ")[0])).getName() + " u " + requestData.get("newName"));
		for (Amenity a :Data.getAmenities().values() ) {
			if (a.getName().toLowerCase().equals(requestData.get("newName").toLowerCase())) {
				if (a.getId() != Integer.valueOf(requestData.get("id").split(" ")[0])) {
					if (!a.isDeleted()) {
						unique = false;
						break;
					}
				}
			}
		}
		if (unique) {
			Data.getAmenities().get(Integer.valueOf(requestData.get("id").split(" ")[0])).setName(requestData.get("newName"));
			Data.saveAmenities();
			return Response.ok(true, MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.status(409).build();
		}
	}
	
	@DELETE 
//	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAmenity (@Context HttpServletRequest request) throws IOException {
		if (Utility.getRole(request) != Role.ADMIN) {
			return Response.status(401).build();
		}
		Map<String, String> requestData = Utility.getBodyMap(request);
		Data.getAmenities().get(Integer.valueOf(requestData.get("id").split(" ")[0])).setDeleted(true);
		Data.saveAmenities();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
}
