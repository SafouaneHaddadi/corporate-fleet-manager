package be.condorcet.web.api;

import be.condorcet.dto.LoginRequest;
import be.condorcet.dto.ReservationResponse;
import be.condorcet.dto.UserResponse;
import be.condorcet.dto.VehicleResponse;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.Reservation;
import be.condorcet.model.User;
import be.condorcet.service.ReservationService;
import be.condorcet.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON) // On renvoie du JSON
@Consumes(MediaType.APPLICATION_JSON) // On reçoit du JSON
public class UserRessource {

    @Inject
    private UserService userService;

    @Inject
    private ReservationService reservationService;

    @Context
    private SecurityContext securityContext;

    @GET
    @RolesAllowed("MANAGER")
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();

        //api stream
        List<UserResponse> responses = users.stream()
                .map(u -> new UserResponse(u.getUsername(), u.getEmail(), u.getRole()))
                .toList();

        return Response.ok(responses).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response register(User user) {
        try {
            User created = userService.registerUser(user);
            UserResponse response = new UserResponse(created.getUsername(), created.getEmail(), created.getRole());
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            UserResponse response = new UserResponse(user.getUsername(), user.getEmail(), user.getRole());

            return Response.ok(response).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/me/reservations")
    @RolesAllowed("EMPLOYEE")
    public Response getMyReservations() {
        String username = securityContext.getUserPrincipal().getName();

        List<Reservation> reservations = reservationService.getReservationsByUser(username);

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
                        username,  // l'employé connecté,
                        r.getRefusalReason()
                ))
                .toList();

        return Response.ok(response).build();
    }

}
