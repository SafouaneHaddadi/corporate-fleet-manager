package be.condorcet.service;

import be.condorcet.dao.MaintenanceDAO;
import be.condorcet.dao.ReservationDAO;
import be.condorcet.dao.UserDAO;
import be.condorcet.dao.VehicleDAO;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.Maintenance;
import be.condorcet.model.Vehicle;
import be.condorcet.model.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class MaintenanceService {

    @Inject
    private ReservationDAO reservationDAO;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private MaintenanceDAO maintenanceDAO;

    public List<Maintenance> getAllMaintenances() {
        return maintenanceDAO.findAll();
    }

    @Transactional
    public Maintenance createMaintenance(Maintenance m) {

        if (m.getStartDate() == null || m.getEndDate() == null) {
            throw new BusinessException("Start and end date are required");
        }

        if (!m.getEndDate().isAfter(m.getStartDate())) {
            throw new BusinessException("End date must be after start date");
        }

        if (m.getVehicle() == null || m.getVehicle().getId() == null) {
            throw new BusinessException("Vehicle is required");
        }

        Vehicle v = vehicleDAO.findById(m.getVehicle().getId());
        if (v == null) {
            throw new BusinessException("Vehicle not found");
        }

        if (v.getStatus() == VehicleStatus.MAINTENANCE) {
            throw new BusinessException("This vehicle is already in maintenance");
        }

        boolean overlap = reservationDAO.hasOverlapping(
                v.getId(), m.getStartDate(), m.getEndDate()
        );

        if (overlap) {
            throw new BusinessException("Cannot create maintenance: a validated reservation exists for this period");
        }

        m.setVehicle(v);

        Maintenance created = maintenanceDAO.create(m);

        v.setStatus(VehicleStatus.MAINTENANCE);
        vehicleDAO.update(v);

        return created;
    }


}
