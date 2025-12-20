package be.condorcet.dao;

import java.util.List;

// Interface générique pour les opérations CRUD de base sur n'importe quelle entité JPA

public interface GenericDAO<T, ID> { //T = type de l'entité. ID = type de la clé primaire
    void create(T entity);
    T findById(ID id);
    void update(T entity);
    void delete(ID id);
    List<T> findAll();
}
