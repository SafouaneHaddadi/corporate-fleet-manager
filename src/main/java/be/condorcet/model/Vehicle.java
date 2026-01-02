package be.condorcet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VEHICLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false, unique = true)
    private String licensePlate;
    
    @Column(name = "manufacture_year", nullable = false)
    private Integer year;
    
    private Integer mileage;

    // la val de l’enum sera convertie en String dans la JSP
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<Maintenance> maintenances = new ArrayList<>();}


