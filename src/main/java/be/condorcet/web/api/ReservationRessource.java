package be.condorcet.web.api;

import be.condorcet.model.Reservation;
import be.condorcet.exception.BusinessException;
import be.condorcet.service.ReservationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationRessource {

    @Inject
    private ReservationService reservationService;

    @Context
    private SecurityContext securityContext;

    @POST
    @RolesAllowed("EMPLOYEE")
    public Response createReservation(Reservation r) {

        try {
            //on recup l'user qui a reservé
            String username = securityContext.getUserPrincipal().getName();

            Reservation created = reservationService.createReservation(r, username);

            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
