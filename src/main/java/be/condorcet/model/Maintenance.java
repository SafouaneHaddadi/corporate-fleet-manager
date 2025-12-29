package be.condorcet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", // le propriétaire est ce côté -> on crée la clé étrangère maintenance.vehicle_id
            nullable = false)
    private Vehicle vehicle;
}
