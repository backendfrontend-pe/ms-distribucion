package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreeCreateRequest {
    private String name;
    private String zoneId;
}
