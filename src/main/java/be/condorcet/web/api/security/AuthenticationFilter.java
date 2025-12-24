package be.condorcet.web.api.security;

import be.condorcet.exception.BusinessException;
import be.condorcet.model.User;
import be.condorcet.service.UserService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
@Priority(Priorities.AUTHENTICATION) //ce filtre s’exécute avant les autres
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String path = requestContext.getUriInfo().getPath();

        if (path.startsWith("/users/login") || path.startsWith("/users/register")) {
            return; //on laisse passer
        }

        //on lit le header
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Missing or invalid Authorization header").build());
            return;
        }

        // décoder le contenu Base64
        String base64credentials = authHeader.substring("Basic ".length());
        String credentials;

        try {
            credentials = new String (
                    Base64.getDecoder().decode(base64credentials),
                    StandardCharsets.UTF_8
            );
        } catch(IllegalArgumentException e) { //si la chaîne n’est pas une chaîne Base64 valide
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid Authorization header").build());
            return;
        }

        // séparer username/password
        String[] values = credentials.split(":", 2);
        String username = values[0];
        String password = values[1];

        try {
            User user = userService.authenticate(username, password);

            // injection du SecurityContext dans la requête
            requestContext.setSecurityContext(new AppSecurityContext(user)); //permet à jax-rs de vérifier les rôles

            requestContext.setProperty("authenticatedUser", user); //comme une session HTTP mais pour une requête API
        } catch (BusinessException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password").build());
            return;
        }

    }
}
