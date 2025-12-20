package be.condorcet.model;
import jakarta.persistence.*;

@Entity
@Table(name="VEHICLE")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment dans la DB
    private Long id;

    @Column(nullable = false, length = 20)
    private String brand;

    @Column(nullable = false, length = 30)
    private String model;

    @Column(nullable = false, unique = true, length = 20) // deux véhicules ne peuvent pas avoir la même plaque
    private String licensePlate;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private int mileage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.AVAILABLE; //par défaut disponible

    public Vehicle() {} //obligatoire pr JPA

    public Vehicle(String brand, String model, String licensePlate,
                   int year, int mileage, VehicleStatus status) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.mileage = mileage;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
