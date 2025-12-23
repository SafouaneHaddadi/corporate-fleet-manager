package be.condorcet.web.api;

import be.condorcet.dto.LoginRequest;
import be.condorcet.dto.LoginResponse;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.User;
import be.condorcet.model.UserRole;
import be.condorcet.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/users") // Tous les endpoints seront sous /users
@Produces(MediaType.APPLICATION_JSON) // On renvoie du JSON
@Consumes(MediaType.APPLICATION_JSON) // On reçoit du JSON
public class UserRessource {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user) {
        try {
            User created = userService.registerUser(user);
            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            LoginResponse response = new LoginResponse(user.getUsername(), user.getEmail(), user.getRole());

            return Response.ok(response).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }
}
