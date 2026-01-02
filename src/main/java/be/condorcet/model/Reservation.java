package be.condorcet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING; // par défaut EN_ATTENTE

    @Column(nullable = false)
    private String refusalReason;

    @Column
    private LocalDateTime approvedAt;

    // champ pour le gestionnaire qui a validé
    @ManyToOne
    @JoinColumn(name = "approved_by_id", nullable = true)
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
