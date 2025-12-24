package be.condorcet.web.api.security;

import be.condorcet.model.User;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class AppSecurityContext implements SecurityContext {

    private final User user;

    public AppSecurityContext(User user) {
        this.user = user;
    }

    // qui est connecté ?
    @Override
    public Principal getUserPrincipal() {
        return user::getUsername;
    }

//    @Override
//    public Principal getUserPrincipal() {
//        return new Principal() {
//            @Override
//            public String getName() {
//                return user.getUsername();
//            }
//        };
//    }

    @Override
    public boolean isUserInRole(String role) {
        return user.getRole().name().equals(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return "BASIC";
    }
}
