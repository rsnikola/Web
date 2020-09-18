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

import model.Data;
import model.Location;
import utility.Utility;

@Path("/location")
public class LocationService {

//	@GET
//	@Path("/test")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response loadApartments () {
//		System.out.println("Location service: test");
//		return Response.ok(Data.getLocations(), MediaType.APPLICATION_JSON).build();
//	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocation (@Context HttpServletRequest request, 
				@PathParam("id") String id) {
		Location retVal = Data.getLocations().get(Integer.valueOf(id));
		if (retVal != null) {
			if (!retVal.isDeleted()) {
				return Response.ok(retVal, MediaType.APPLICATION_JSON).build();
			}
		}
		return Response.status(404).build();
	}
	
	@PUT
//	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateLocation (@Context HttpServletRequest request) throws IOException {
		Map<String, String> requestData = Utility.getBodyMap(request);
		Location location = Data.getLocations().get(Integer.valueOf(requestData.get("id").split(" ")[0]));
//		Utility.printMap(requestData);
		String longi = requestData.get("longitude").split(" ")[0];
		String lat = requestData.get("latitude").split(" ")[0];
		location.setLatitude(Double.parseDouble(lat));
		location.setLongitude(Double.parseDouble(longi));
		Data.saveLocations();
		return Response.ok(true, MediaType.APPLICATION_JSON).build();
	}
	
	
}
