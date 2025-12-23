package be.condorcet.dao;

import be.condorcet.model.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
