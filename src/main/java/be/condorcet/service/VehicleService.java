package be.condorcet.service;

import be.condorcet.dao.VehicleDAO;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.Vehicle;
import be.condorcet.model.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class VehicleService {

    @Inject
    private VehicleDAO vehicleDAO;

    public Vehicle createVehicle(Vehicle v) {

        if (v.getBrand() == null || v.getBrand().isBlank() ){
            throw new BusinessException("brand is required");
        }
        if (v.getModel() == null || v.getModel().isBlank() ){
            throw new BusinessException("model is required");
        }
        if (v.getLicensePlate() == null || v.getLicensePlate().isBlank() ){
            throw new BusinessException("licensePlate is required");
        }
        String plateRegex = "^[12]-[A-Z]{3}-[0-9]{3}$";
        if(!v.getLicensePlate().matches(plateRegex)) {
             throw new BusinessException("Invalid Belgian license plate format");
        }
        if (vehicleDAO.existsByLicensePlate(v.getLicensePlate())) {
            throw new BusinessException("License plate already exists");
        }
        int currentYear = java.time.Year.now().getValue();
        if (v.getYear() == null) {
            throw new BusinessException("Year is required");
        }
        if(v.getYear() < 1900 || v.getYear() > currentYear + 1) {
            throw new BusinessException("Invalid year");
        }
        if(v.getMileage() < 0) {
            throw new BusinessException("Mileage cannot be negative");
        }

        v.setStatus(VehicleStatus.AVAILABLE);
        vehicleDAO.create(v);

        return v;

    }
}
