package be.condorcet.service;

import be.condorcet.dao.ReservationDAO;
import be.condorcet.dao.UserDAO;
import be.condorcet.dao.VehicleDAO;
import be.condorcet.exception.BusinessException;
import be.condorcet.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ReservationService {

    @Inject
    private ReservationDAO reservationDAO;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    public Reservation findById(Long id) {
        Reservation r = reservationDAO.findById(id);
        if (r == null) {
            throw new BusinessException("Reservation not found with id " + id);
        }
        return r;
    }

    public List<Reservation> getReservationsByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BusinessException("status is required");
        }

        ReservationStatus reservationStatus;
        try {
            reservationStatus = ReservationStatus.valueOf(status.toUpperCase()); //convertit le string en enum
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Unknown status: " + status);
        }

        return reservationDAO.findByStatus(reservationStatus);
    }


    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public List<Reservation> getReservationsByUser(String username) {

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        return reservationDAO.findByEmployee(user);
    }


    public Reservation createReservation(Reservation r, String connectUser) {

        if (r.getStartDate() == null) {
            throw new BusinessException("Start date is required");
        }
        if (r.getEndDate() == null) {
            throw new BusinessException("End date is required");
        }
        if (!r.getEndDate().isAfter(r.getStartDate())) {
            throw new BusinessException("End date must be after start date");
        }
        if (r.getReason() == null || r.getReason().isBlank()) {
            throw new BusinessException("Reason is required");
        }
        if (r.getVehicle() == null || r.getVehicle().getId() == null) {
            throw new BusinessException("Vehicle id is required");
        }

        Long vehicleId = r.getVehicle().getId();

        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException("Vehicle not found");
        }
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new BusinessException("Vehicle not available");
        }

        if (reservationDAO.hasOverlapping(vehicleId, r.getStartDate(), r.getEndDate())) {
            throw new BusinessException("This vehicle is already reserved for the requested period");
        }

        User employee = userDAO.findByUsername(connectUser)
                .orElseThrow(() -> new BusinessException("Connected user not found"));

        r.setVehicle(vehicle);
        r.setEmployee(employee);
        r.setStatus(ReservationStatus.PENDING);
        r.setApprovedAt(null);
        r.setApprovedBy(null);
        r.setRefusalReason(null);

        return reservationDAO.create(r);
    }

    public Reservation approveReservation(Long id, String managerUsername) {

        Reservation reservation = reservationDAO.findById(id);
        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }

        User manager = userDAO.findByUsername(managerUsername)
                .orElseThrow(() -> new BusinessException("Manager not found"));

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            throw new BusinessException("Reservation is already approved");
        }

        if (reservationDAO.hasOverlapping(
                reservation.getVehicle().getId(),
                reservation.getStartDate(),
                reservation.getEndDate())) {
            throw new BusinessException("Vehicle already reserved on this period");
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setApprovedBy(manager);
        reservation.setApprovedAt(LocalDateTime.now());

        return reservationDAO.update(reservation);
    }

    public Reservation declineReservation(Long reservationId, String managerUsername, String reason) {

        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Refusal reason is required");
        }

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            throw new BusinessException("Reservation is already approved");
        }

        if (reservation.getStatus() == ReservationStatus.REFUSED) {
            throw new BusinessException("Reservation is already declined");
        }

        User manager = userDAO.findByUsername(managerUsername)
                .orElseThrow(() -> new BusinessException("Manager not found"));

        reservation.setStatus(ReservationStatus.REFUSED);
        reservation.setApprovedBy(manager); //l'approbation signifie le refus ici
        reservation.setApprovedAt(LocalDateTime.now());
        reservation.setRefusalReason(reason);

        return reservationDAO.update(reservation);
    }

    public Reservation cancelApprovedReservation(Long reservationId) {
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new BusinessException("Reservation not found");
        }
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new BusinessException("Only APPROVED reservations can be cancelled by the manager");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setRefusalReason("Cancelled by manager due to unplanned maintenance");

        return reservationDAO.update(reservation);
    }

    public List<Vehicle> findAvailableVehiclesForPeriod(LocalDateTime start, LocalDateTime end) {

        List<Vehicle> candidates = vehicleDAO.findAvailableVehicles();

        return candidates.stream()
                .filter(v -> !reservationDAO.hasOverlapping(v.getId(), start, end))
                .toList();
    }




}
