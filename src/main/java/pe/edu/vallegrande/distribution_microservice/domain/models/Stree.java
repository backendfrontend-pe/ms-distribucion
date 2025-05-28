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
@Document(collection = "calles")
public class Stree {
    @Id
    private String streeId;
    private String name;
    private String zoneId;
    private String zoneName;
    private boolean status = true; // Activo por defecto
    private LocalDateTime dateRecord = LocalDateTime.now();
}
