package services;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import model.enumerations.Role;

@Path("/apartments")
public class ApartmentService {

	public ApartmentService () {
		System.out.println("Apartment service");
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
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> getAll (@Context HttpServletRequest request) {
		System.out.println("Apartment service: get all");
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : Data.getApartments().values()) {
			if (a.isActive()) {
				retVal.add(a);
			}
		}
		return retVal;
	}
	
	@GET
	@Path("/getAllMy")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllMy (@Context HttpServletRequest request) {
		System.out.println("Apartment service: get all host");
		if (Data.getUsers().get(request.getSession().getAttribute("username")).getRole() != Role.HOST) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : Data.getApartments().values()) {
			if (a.getHost().equals(request.getSession().getAttribute("username"))) {
				retVal.add(a);
			}
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/getAllAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllAdmin (@Context HttpServletRequest request) {
		System.out.println("Apartment service: get all admin");
		if (Data.getUsers().get(request.getSession().getAttribute("username")).getRole() != Role.ADMIN) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		ArrayList<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment a : Data.getApartments().values()) {
			retVal.add(a);
		}
		return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/getAddress/{location_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAddress (@PathParam("location_id") Integer location_id) {
		System.out.println("Apartment service: getting address: " + location_id);
		Location location = Data.getLocations().get(location_id);
		Address address = Data.getAddresses().get(location.getAddress());
		String retVal = "{\"retVal\":\"" + address.getStreetNumber() + " " + address.getStreet() + ",  " + 
					address.getZipCode() + " " + address.getTown() + ", " + address.getCountry() + "\"}";
		return retVal;
	}
	
}
