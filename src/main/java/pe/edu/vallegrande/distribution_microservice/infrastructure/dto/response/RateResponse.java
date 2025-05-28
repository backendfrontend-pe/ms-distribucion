package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateResponse {
    private String id; 
    private String zoneId; 
    private double amount; 
    private String description; 
    private String rateType; 
    private Date startDate; 
    private Date endDate; 
    private String status; 
    private LocalDateTime dateRecord; 
    private String zoneName; 
}
