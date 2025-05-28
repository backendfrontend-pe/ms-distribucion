package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreeResponse {
    private String streeId;
    private String name;
    private String zoneId;
    private String zoneName;
    private boolean status; 
    private LocalDateTime dateRecord;
}
