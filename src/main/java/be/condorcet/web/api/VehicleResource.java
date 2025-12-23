package be.condorcet.web.api;

import be.condorcet.exception.BusinessException;
import be.condorcet.model.Vehicle;
import be.condorcet.service.VehicleService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class VehicleResource {

    @Inject
    private VehicleService vehicleService;

    @GET
    @RolesAllowed("MANAGER")
    public Response getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"MANAGER"})
    public Response getVehicleById(@PathParam("id") Long id) {
        try {
            Vehicle vehicle = vehicleService.findById(id);
            return Response.ok(vehicle).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @RolesAllowed("MANAGER")
    public Response createVehicle(Vehicle v) {
        try {
            Vehicle created = vehicleService.createVehicle(v);
            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("MANAGER")
    public Response updateVehicle(@PathParam("id") Long id, Vehicle updated) {
        try {
            Vehicle modified = vehicleService.updateVehicle(id, updated);
            return Response.ok(modified).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("MANAGER")
    public Response deleteVehicle(@PathParam("id") Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return Response.noContent().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/available")
    @PermitAll
    public Response getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailable();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/search")
    @PermitAll
    public Response searchByBrand(@QueryParam("brand") String brand) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(brand);
        return Response.ok(vehicles).build();
    }
}
