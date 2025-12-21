package be.condorcet.dao.impl;

import be.condorcet.dao.VehicleDAO;
import be.condorcet.model.Vehicle;
import be.condorcet.model.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.util.List;

// Une seule instance pour toute l'appli
@ApplicationScoped
public class VehicleDAOImpl extends GenericDAOImpl<Vehicle, Long> implements VehicleDAO {

    // indique qu'on gère Vehicle.class. on transmet l’entity au GenericDAOImpl
    public VehicleDAOImpl() {
        super(Vehicle.class);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {

        return em.createQuery(
                "SELECT v FROM Vehicle v WHERE v.status = :status", Vehicle.class)
                .setParameter("status", VehicleStatus.AVAILABLE)
                .getResultList();
    }

    @Override
    public List<Vehicle> findByBrand(String brand) {
        TypedQuery<Vehicle> query = em.createQuery(
                "SELECT v FROM Vehicle v WHERE LOWER(v.brand) LIKE :brand", Vehicle.class)
                .setParameter("brand", "%" + brand.toLowerCase() + "%");   // %brand% pour dire "contient cette marque"

        return query.getResultList();
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        Long count = em.createQuery(
                        "SELECT COUNT(v) FROM Vehicle v WHERE v.licensePlate = :plate",
                        Long.class)
                .setParameter("plate", licensePlate)
                .getSingleResult();

        return count > 0;
    }
}
