package pe.edu.vallegrande.distribution_microservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "zonas")
public class Zone {
    @Id
    private String zoneId;
    private String name;
    private String description;
    private String officeId; //es 'sedeId'
    private String status = "ACTIVE";
    private LocalDateTime dateRecord = LocalDateTime.now();
}
