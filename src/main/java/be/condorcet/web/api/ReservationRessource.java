package be.condorcet.web.api;

import be.condorcet.dto.ReservationResponse;
import be.condorcet.dto.VehicleResponse;
import be.condorcet.model.Reservation;
import be.condorcet.dto.UserResponse;
import be.condorcet.exception.BusinessException;
import be.condorcet.service.ReservationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationRessource {

    @Inject
    private ReservationService reservationService;

    @Context
    private SecurityContext securityContext;

    @GET
    @RolesAllowed("MANAGER")
    public Response getAllReservations() {

        List<Reservation> reservations = reservationService.getAllReservations();

        List<ReservationResponse> response = reservations.stream()
                .map(r -> new ReservationResponse(
                        r.getId(),
                        r.getStartDate(),
                        r.getEndDate(),
                        r.getReason(),
                        r.getStatus().name(),
                        new VehicleResponse(
                                r.getVehicle().getBrand(),
                                r.getVehicle().getModel(),
                                r.getVehicle().getLicensePlate()
                        ),
                        r.getEmployee() != null ? r.getEmployee().getUsername() : null
                ))
                .toList();

        return Response.ok(response).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed("MANAGER")
    public Response getReservationsByStatus(@QueryParam("status") String statusParam) {

        try {
            List<Reservation> reservations = reservationService.getReservationsByStatus(statusParam);

            List<ReservationResponse> response = reservations.stream()
                    .map(r -> new ReservationResponse(
                            r.getId(),
                            r.getStartDate(),
                            r.getEndDate(),
                            r.getReason(),
                            r.getStatus().name(),
                            new VehicleResponse(
                                    r.getVehicle().getBrand(),
                                    r.getVehicle().getModel(),
                                    r.getVehicle().getLicensePlate()
                            ),
                            r.getEmployee().getUsername()
                    ))
                    .toList();

            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid reservation status")
                    .build();
        }
    }


    @POST
    @RolesAllowed("EMPLOYEE")
    public Response createReservation(Reservation r) {

        try {
            String username = securityContext.getUserPrincipal().getName();

            Reservation created = reservationService.createReservation(r, username);

            VehicleResponse vehicleResponse = new VehicleResponse(
                    created.getVehicle().getBrand(),
                    created.getVehicle().getModel(),
                    created.getVehicle().getLicensePlate()
            );

            String employee = created.getEmployee().getUsername();

            ReservationResponse reservationResponse = new ReservationResponse(
                    created.getId(),
                    created.getStartDate(),
                    created.getEndDate(),
                    created.getReason(),
                    created.getStatus().name(),
                    vehicleResponse,
                    employee
            );

            return Response.status(Response.Status.CREATED)
                    .entity(reservationResponse)
                    .build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/approve")
    @RolesAllowed("MANAGER")
    public Response approveReservation(@PathParam("id") Long id) {

        String managerUsername = securityContext.getUserPrincipal().getName();

        Reservation approved = reservationService.approveReservation(id, managerUsername);

        ReservationResponse response = new ReservationResponse(
                approved.getId(),
                approved.getStartDate(),
                approved.getEndDate(),
                approved.getReason(),
                approved.getStatus().name(),
                null,
                approved.getEmployee().getUsername()
        );

        return Response.ok(response).build();
    }



}
