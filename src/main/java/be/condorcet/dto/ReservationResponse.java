package be.condorcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;
    private String status;
    private VehicleResponse vehicle;
    private String employeeUsername; // juste le username
}
