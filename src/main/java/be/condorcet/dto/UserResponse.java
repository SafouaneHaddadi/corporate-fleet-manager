package be.condorcet.dto;

import be.condorcet.model.UserRole;
import lombok.Getter;
import lombok.Setter;

//DTO pr controler ce que l'user reçoit

public class UserResponse {

    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String email;
    private UserRole role;

    public UserResponse(String username, String email, UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
