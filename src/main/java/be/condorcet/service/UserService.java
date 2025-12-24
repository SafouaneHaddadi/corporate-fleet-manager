package be.condorcet.service;

import be.condorcet.dao.UserDAO;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.User;
import be.condorcet.model.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt; // librairie pour hash du mot de passe

@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    private UserDAO userDAO;

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User findById(Long id) {
        User u = userDAO.findById(id);
        if (u == null) {
            throw new BusinessException("User not found with id " + id);
        }
        return u;
    }

    public User registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BusinessException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BusinessException("Password is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new BusinessException("Invalid email format");
        }
        if (userDAO.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        String hashedpwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedpwd);

        if (user.getRole() == null) {
            user.setRole(UserRole.EMPLOYEE);
        }
        return userDAO.create(user);
    }

    public User authenticate(String username, String password) {
        Optional<User> optUser = userDAO.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new BusinessException("Invalid username or password"); //sécurité
        }

        User user = optUser.get(); //pr obtenir l’objet User stocké
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }
        return user;
    }
}
