package be.condorcet.web.api;

import be.condorcet.model.Maintenance;
import be.condorcet.service.MaintenanceService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/maintenances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceRessource {

    @Inject
    private MaintenanceService maintenanceService;

    @POST
    @RolesAllowed("MANAGER")
    public Response createMaintenance(Maintenance m) {
        try {
            Maintenance created = maintenanceService.createMaintenance(m);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @RolesAllowed("MANAGER")
    public Response getAll() {
        return Response.ok(maintenanceService.getAllMaintenances()).build();
    }
}
