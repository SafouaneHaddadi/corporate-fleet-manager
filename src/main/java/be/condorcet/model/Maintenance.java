package be.condorcet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MAINTENANCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "vehicle")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Clé primaire auto-incrémentée

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime plannedEndDate;

    @Column
    private LocalDateTime actualEndDate;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", // le propriétaire est ce côté -> on crée la clé étrangère maintenance.vehicle_id
            nullable = false)
    private Vehicle vehicle;
}
