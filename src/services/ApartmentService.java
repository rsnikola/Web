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
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
			else {
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
		if (retVal == null) {
			return Response.status(404).build();
		} else {
			return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
		}
	}
	
} 
