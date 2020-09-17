package services;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Address;
import model.Data;
import utility.Utility;

@Path("/address")
public class AddressService {

//	@GET
//	@Path("/test")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response loadAddresses () {
////		System.out.println("Address service: test");
//		return Response.ok(Data.getAddresses(), MediaType.APPLICATION_JSON).build();
//	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAddress (@Context HttpServletRequest request, 
				@PathParam("id") String id) {
		Address retVal = Data.getAddresses().get(Integer.valueOf(id));
		if (retVal != null) {
			if (!retVal.isDeleted()) {
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
		}
		return Response.status(404).build();
	}
	
	@PUT
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAddress (@Context HttpServletRequest request) throws IOException {
		Map<String, String> requestData = Utility.getBodyMap(request);
		Address address = Data.getAddresses().get(Integer.valueOf(requestData.get("id").split(" ")[0]));
		address.setStreetNumber(Integer.valueOf(requestData.get("streetNumber").split(" ")[0]));
		address.setStreet(requestData.get("streetName"));
		address.setTown(requestData.get("town"));
		address.setZipCode(requestData.get("zipCode"));
		address.setCountry(requestData.get("country"));
		Data.saveAddresses();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	
	
	
}
