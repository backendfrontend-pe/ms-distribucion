package pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request;

public class StreeUpdateRequest {
    private String name;      // Nombre de la calle (puede ser null si no se cambia)
    private String zoneId;    // ID de la zona (puede ser null si no se cambia)
    private Boolean status;   // Estado de la calle (puede ser null si no se cambia)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
