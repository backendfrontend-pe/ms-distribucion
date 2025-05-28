package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {
    private String zoneId;
    private String name;
    private String description;
    private String officeId;
    private String status;
    private LocalDateTime dateRecord;
}
