package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateCreateRequest {
    private String zoneId; 
    private double amount; 
    private String description; 
    private String rateType; 
    private Date startDate; 
    private Date endDate; 
    private String zoneName; 
}
