package be.condorcet.dao;

import be.condorcet.model.Reservation;
import be.condorcet.model.ReservationStatus;
import be.condorcet.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDAO extends GenericDAO<Reservation, Long> {

    boolean hasOverlapping(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    List<Reservation> findByEmployee(User employee);

    List<Reservation> findByStatus(ReservationStatus status);

}
