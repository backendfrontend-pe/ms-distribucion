package pe.edu.vallegrande.distribution_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.distribution_microservice.application.services.ZoneService;
import pe.edu.vallegrande.distribution_microservice.domain.enums.Constants;
import pe.edu.vallegrande.distribution_microservice.domain.models.Zone;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.ZoneResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.distribution_microservice.infrastructure.repository.ZoneRepository;
import pe.edu.vallegrande.distribution_microservice.infrastructure.rest.ApiResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ZoneServiceImpl implements ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Override
    public Flux<Zone> getAll() {
        return zoneRepository.findAll()
                .doOnNext(zone -> log.info("Zone retrieved: {}", zone));
    }

  @Override
public Mono<ApiResponse<List<Zone>>> getAllActive() {
    return zoneRepository.findAllByStatus(Constants.ACTIVE.name())
        .collectList()
        .map(zones -> new ApiResponse<>(true, zones));
}


  @Override
public Mono<ApiResponse<List<Zone>>> getAllInactive() {
    return zoneRepository.findAllByStatus(Constants.INACTIVE.name())
        .collectList()
        .map(zones -> new ApiResponse<>(true, zones));
}

    @Override
    public Mono<Zone> getById(String id) {
        return zoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Zone not found",
                        "The requested zone with id " + id + " was not found")));
    }

  @Override
public Mono<ZoneResponse> save(ZoneCreateRequest request) {
    Zone zone = new Zone();
    zone.setName(request.getName());
    zone.setDescription(request.getDescription());
    zone.setStatus(Constants.ACTIVE.name());
    zone.setDateRecord(LocalDateTime.now());

    return zoneRepository.count()
        .map(count -> String.format("%02d", count + 1))
        .flatMap(generatedOfficeId -> {
            zone.setOfficeId(generatedOfficeId);
            return zoneRepository.save(zone);
        })
        .map(savedZone -> {
            ZoneResponse response = new ZoneResponse();
            response.setZoneId(savedZone.getZoneId());
            response.setName(savedZone.getName());
            response.setDescription(savedZone.getDescription());
            return response;
        });
}


@Override
public Mono<Zone> update(String id, ZoneUpdateRequest request) {
    return zoneRepository.findById(id)
            .switchIfEmpty(Mono.error(new CustomException(
                    HttpStatus.NOT_FOUND.value(),
                    "Zone not found",
                    "Zone with id " + id + " not found")))
            .flatMap(existingZone -> {
                existingZone.setName(request.getName());
                existingZone.setDescription(request.getDescription());
                return zoneRepository.save(existingZone);
            });
}

  @Override
public Mono<Void> delete(String id) {
    return zoneRepository.findById(id)
        .doOnNext(z -> log.info("🔍 Zona encontrada para eliminar: {}", z))
        .switchIfEmpty(Mono.error(new CustomException(
            HttpStatus.NOT_FOUND.value(),
            "Zone not found",
            "The zone with ID " + id + " was not found")))
        .flatMap(zone -> {
            zone.setStatus(Constants.INACTIVE.name());
            return zoneRepository.save(zone);
        })
        .doOnSuccess(v -> log.info("✅ Zona marcada como INACTIVA"))
        .then();
}



   @Override
public Mono<Zone> activate(String id) {
    return zoneRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(
            HttpStatus.NOT_FOUND.value(),
            "Zone not found",
            "The zone with ID " + id + " was not found")))
        .flatMap(zone -> {
            zone.setStatus(Constants.ACTIVE.name());
            return zoneRepository.save(zone);
        });
}


   @Override
public Mono<Zone> desactivate(String id) {
    return zoneRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(
            HttpStatus.NOT_FOUND.value(),
            "Zone not found",
            "The zone with ID " + id + " was not found")))
        .flatMap(zone -> {
            zone.setStatus(Constants.INACTIVE.name());
            return zoneRepository.save(zone);
        });
}

}