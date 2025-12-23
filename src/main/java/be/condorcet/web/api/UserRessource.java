package be.condorcet.web.api;

import be.condorcet.dto.LoginRequest;
import be.condorcet.dto.UserResponse;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.User;
import be.condorcet.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/users") // Tous les endpoints seront sous /users
@Produces(MediaType.APPLICATION_JSON) // On renvoie du JSON
@Consumes(MediaType.APPLICATION_JSON) // On reçoit du JSON
public class UserRessource {

    @Inject
    private UserService userService;

    @GET
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
}
