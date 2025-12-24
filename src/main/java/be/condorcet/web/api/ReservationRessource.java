package be.condorcet.web.api;

import be.condorcet.model.Reservation;
import be.condorcet.exception.BusinessException;
import be.condorcet.service.ReservationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.HashMap;
import java.util.Map;

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
            String username = securityContext.getUserPrincipal().getName();
            Reservation created = reservationService.createReservation(r, username);

            // pour personnaliser la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("id", created.getId());
            response.put("startDate", created.getStartDate().toString()); // format ISO 8601 : 2025-03-10T09:00
            response.put("endDate", created.getEndDate().toString());
            response.put("reason", created.getReason());
            response.put("status", created.getStatus().name());

            // Véhicule
            Map<String, Object> vehicleMap = new HashMap<>();
            vehicleMap.put("brand", created.getVehicle().getBrand());
            vehicleMap.put("model", created.getVehicle().getModel());
            vehicleMap.put("licensePlate", created.getVehicle().getLicensePlate());
            response.put("vehicle", vehicleMap);

            // Employee
            Map<String, Object> employeeMap = new HashMap<>();
            employeeMap.put("id", created.getEmployee().getId());
            employeeMap.put("username", created.getEmployee().getUsername());
            employeeMap.put("role", created.getEmployee().getRole().name());
            response.put("employee", employeeMap);

            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
