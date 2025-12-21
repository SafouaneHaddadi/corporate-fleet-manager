package be.condorcet.service;

import be.condorcet.dao.VehicleDAO;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.Vehicle;
import be.condorcet.model.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class VehicleService {

    @Inject
    private VehicleDAO vehicleDAO;

    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    public Vehicle createVehicle(Vehicle v) {

        if (v.getBrand() == null || v.getBrand().isBlank()) {
            throw new BusinessException("brand is required");
        }
        if (v.getModel() == null || v.getModel().isBlank()) {
            throw new BusinessException("model is required");
        }
        if (v.getLicensePlate() == null || v.getLicensePlate().isBlank()) {
            throw new BusinessException("licensePlate is required");
        }
        String plateRegex = "^[12]-[A-Z]{3}-[0-9]{3}$";
        if (!v.getLicensePlate().matches(plateRegex)) {
            throw new BusinessException("Invalid Belgian license plate format");
        }
        if (vehicleDAO.existsByLicensePlate(v.getLicensePlate())) {
            throw new BusinessException("License plate already exists");
        }
        int currentYear = java.time.Year.now().getValue();
        if (v.getYear() == null) {
            throw new BusinessException("Year is required");
        }
        if (v.getYear() < 1900 || v.getYear() > currentYear + 1) {
            throw new BusinessException("Invalid year");
        }
        if (v.getMileage() < 0) {
            throw new BusinessException("Mileage cannot be negative");
        }

        v.setStatus(VehicleStatus.AVAILABLE);

        return vehicleDAO.create(v);
    }

    //modif partielles
    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = vehicleDAO.findById(id); //recup le véhicle existant
        if (existing == null) {
            throw new BusinessException("Vehicle not found");
        }
        // mise à jour SEULEMENT si le champ est fourni et non vide
        if (updated.getBrand() != null || !updated.getBrand().isBlank()) {
            existing.setBrand(updated.getBrand());
        }
        if (updated.getModel() != null && !updated.getModel().isBlank()) {
            existing.setModel(updated.getModel());
        }
        if (updated.getYear() != null) {
            int currentYear = java.time.Year.now().getValue();
            if (updated.getYear() < 1900 || updated.getYear() > currentYear + 1) {
                throw new BusinessException("Invalid year");
            }
            existing.setYear(updated.getYear());
        }
        if (updated.getMileage() >= 0) {
            existing.setMileage(updated.getMileage());
        }

        // immatriculation et statut non modifiables -> On ignore complètement si fournis dans le JSON

        return vehicleDAO.update(existing); // <- renvoie l'entité mise à jour
    }
}
