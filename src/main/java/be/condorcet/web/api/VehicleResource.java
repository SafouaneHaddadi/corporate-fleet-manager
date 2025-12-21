package be.condorcet.web.api;

import be.condorcet.exception.BusinessException;
import be.condorcet.model.Vehicle;
import be.condorcet.service.VehicleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON) // ttes les réponses sont en JSON
@Consumes(MediaType.APPLICATION_JSON) // ttes les requêtes attendent du JSON
@RequestScoped
public class VehicleResource {

    @Inject
    private VehicleService vehicleService;

    @POST
    public Response createVehicle(Vehicle v) {
        try {
            Vehicle created = vehicleService.createVehicle(v);
            return Response.status(Response.Status.CREATED)
                    .entity(created) //corps de la réponse = véhicule
                    .build();
        } catch(BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error : " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateVehicle(@PathParam("id") Long id, Vehicle updated) {
        try {
            Vehicle modified = vehicleService.updateVehicle(id, updated); //on délégue la logique métier au service
            return Response.ok(modified).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error : " + e.getMessage())
                    .build();
        }
    }
}
