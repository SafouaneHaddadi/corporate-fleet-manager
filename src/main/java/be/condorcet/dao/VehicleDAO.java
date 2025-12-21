package be.condorcet.dao;

import be.condorcet.model.Vehicle;

import java.util.List;

public interface VehicleDAO extends GenericDAO<Vehicle, Long> {

    List<Vehicle> findAvailableVehicles();
    List<Vehicle> findByBrand(String brand);
    public boolean existsByLicensePlate(String licensePlate);

}
