package be.condorcet.dao.impl;

import be.condorcet.dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    @PersistenceContext(unitName = "fleet-pu") // EntityManager injecté autom par WildFly
    protected EntityManager em; //objet JPA qui parle à la DB

    private final Class<T> entityClass; // on conserve la classe en mémoire

    //le const prend en paramètre la classe de l’entité pour pouvoir faire des opérations spécifiques à cette entité
    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T findById(ID id) {
        return em.find(entityClass, id); //la méthode a besoin de connaître la classe de l'entité
    }

//    @Override
//    public void update(T entity) {
//        em.merge(entity);
//    }
    @Override
    public T update(T entity) {
        return em.merge(entity); // ← renvoie l'entité mergée
    }

    //on charge d'abord l'entité puis on la supprime
    @Override
    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }
   /* @Override
    public void delete(T entity) {
        // Le merge est nécessaire si l'objet n'est plus attaché à la session
        em.remove(em.merge(entity));
    }*/

    @Override
    public List<T> findAll() {
        TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass //type de résultat attendu
        );
        return query.getResultList(); //retourne une liste de T
    }
}
