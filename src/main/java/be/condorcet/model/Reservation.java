package be.condorcet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "vehicle")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING; // par défaut EN_ATTENTE

    @Column
    private String refusalReason;

    @Column
    private LocalDateTime approvedAt;

    // champ pour le gestionnaire qui a validé
    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @ManyToOne     // Une réservation concerne un seul véhicule
    @JoinColumn(name = "vehicle_id",
            nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "employee_id",
            nullable = false)
    private User employee;
}
