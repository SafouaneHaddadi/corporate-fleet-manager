package be.condorcet.dao.impl;

import be.condorcet.dao.ReservationDAO;
import be.condorcet.model.Reservation;
import be.condorcet.model.ReservationStatus;
import be.condorcet.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReservationDAOImpl extends GenericDAOImpl<Reservation, Long> implements ReservationDAO {

    public ReservationDAOImpl() {
        super(Reservation.class);
    }

    @Override
    public boolean hasOverlapping(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        Long count = em.createQuery(
                        "SELECT COUNT(r) FROM Reservation r " +
                                "WHERE r.vehicle.id = :vehicleId " +
                                "AND r.status = :status " +
                                "AND r.startDate <= :endDate " +
                                "AND r.endDate >= :startDate", Long.class)
                .setParameter("vehicleId", vehicleId)
                .setParameter("status", ReservationStatus.APPROVED)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();

        return count > 0; // true = il existe un chevauchement
    }

    @Override
    public List<Reservation> findByEmployee(User employee) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.employee = :employee ORDER BY r.startDate DESC",
                        Reservation.class)
                .setParameter("employee", employee)
                .getResultList();
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.status = :status ORDER BY r.startDate ASC",
                        Reservation.class)
                .setParameter("status", status)
                .getResultList();
    }


    @Override
    public void approve(Long reservationId, User manager) {
        Reservation r = em.find(Reservation.class, reservationId);
        if (r != null) {
            r.setStatus(ReservationStatus.APPROVED);
            r.setApprovedBy(manager);
            r.setApprovedAt(LocalDateTime.now());
            em.merge(r);
        }
    }

    @Override
    public void decline(Long reservationId, User manager, String refusalReason) {
        Reservation r = em.find(Reservation.class, reservationId);
        if (r != null) {
            r.setStatus(ReservationStatus.REFUSED);
            r.setApprovedBy(manager);
            r.setRefusalReason(refusalReason);
            r.setApprovedAt(LocalDateTime.now());
            em.merge(r);
        }
    }
}