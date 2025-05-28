package pe.edu.vallegrande.distribution_microservice.domain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tarifas")
public class Rate {
    @Id
    private String rateId;
    private String zoneId;
    private double amount;
    private String description;
    private String rateType;
    private Date startDate;
    private Date endDate;
    private String status = "ACTIVE";
    private LocalDateTime dateRecord = LocalDateTime.now();
    private String zoneName;
}