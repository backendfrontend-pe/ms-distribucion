package pe.edu.vallegrande.distribution_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.distribution_microservice.application.services.StreeService;
import pe.edu.vallegrande.distribution_microservice.domain.models.Stree;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeUpdateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.StreeResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.distribution_microservice.infrastructure.repository.StreeRepository;
import pe.edu.vallegrande.distribution_microservice.infrastructure.repository.ZoneRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StreeServiceImpl implements StreeService {

    @Autowired
    private StreeRepository streeRepository;

    @Override
    public Flux<Stree> getAll() {
        return streeRepository.findAll()
                .doOnNext(stree -> log.info("Stree retrieved: {}", stree));
    }

    @Override
public Flux<StreeResponse> getAllActive() {
    return streeRepository.findAllByStatus(true)
        .flatMap(stree -> zoneRepository.findById(stree.getZoneId())
            .map(zone -> new StreeResponse(
                stree.getStreeId(),
                stree.getName(),
                stree.getZoneId(),
                zone.getName(),
                stree.isStatus(),
                stree.getDateRecord()
            ))
            .defaultIfEmpty(new StreeResponse(  // por si no encuentra la zona
                stree.getStreeId(),
                stree.getName(),
                stree.getZoneId(),
                null,
                stree.isStatus(),
                stree.getDateRecord()
            ))
        );
}


   @Override
public Flux<Stree> getAllInactive() {
    return streeRepository.findAllByStatus(false);
}


   @Override
public Mono<Stree> getById(String id) {
    return streeRepository.findById(id)
        .flatMap(stree -> {
            return zoneRepository.findById(stree.getZoneId())  // Buscar la zona por zoneId
                .map(zone -> {
                    stree.setZoneName(zone.getName());  // Asignar el nombre de la zona a la calle
                    return stree;
                });
        })
        .switchIfEmpty(Mono.error(new CustomException(
            HttpStatus.NOT_FOUND.value(),
            "Stree not found",
            "La calle con ID " + id + " no fue encontrada")));
}


   @Autowired
private ZoneRepository zoneRepository; // asegúrate de tenerlo

@Override
public Mono<StreeResponse> save(StreeCreateRequest request) {
    return zoneRepository.findById(request.getZoneId())
        .switchIfEmpty(Mono.error(new CustomException(
                HttpStatus.NOT_FOUND.value(),
                "Zone not found",
                "Zone with ID " + request.getZoneId() + " does not exist")))
        .flatMap(zone -> {
            Stree stree = new Stree();
            stree.setName(request.getName());
            stree.setZoneId(zone.getZoneId());
            stree.setZoneName(zone.getName()); // AQUÍ se llena automáticamente
            stree.setStatus(true);
            stree.setDateRecord(LocalDateTime.now());

            return streeRepository.save(stree);
        })
        .map(saved -> {
            StreeResponse response = new StreeResponse();
            response.setStreeId(saved.getStreeId());
            response.setName(saved.getName());
            response.setZoneId(saved.getZoneId());
            response.setZoneName(saved.getZoneName());
            response.setStatus(saved.isStatus());
            response.setDateRecord(saved.getDateRecord());
            return response;
        });
}


@Override
public Mono<Stree> update(String id, StreeUpdateRequest request) {
    return streeRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomException(
            HttpStatus.NOT_FOUND.value(),
            "Stree not found",
            "Calle no encontrada con ID " + id)))
        .flatMap(existingStree -> {
            if (request.getName() != null) {
                existingStree.setName(request.getName());
            }
            if (request.getZoneId() != null) {
                return zoneRepository.findById(request.getZoneId())
                    .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Zone not found",
                        "Zona no encontrada con ID " + request.getZoneId())))
                    .map(zone -> {
                        existingStree.setZoneId(zone.getZoneId());
                        existingStree.setZoneName(zone.getName());
                        return existingStree;
                    });
            } else {
                return Mono.just(existingStree);
            }
        })
        .flatMap(streeRepository::save);
}


   @Override
public Mono<Void> delete(String id) {
    return streeRepository.findById(id)
            .flatMap(stree -> {
                stree.setStatus(false);  // ← Borrado lógico
                return streeRepository.save(stree);
            })
            .then(); // Devuelve Mono<Void>
}


    @Override
    public Mono<Stree> activate(String id) {
        return streeRepository.findById(id)
                .flatMap(stree -> {
                    stree.setStatus(true);
                    return streeRepository.save(stree);
                })
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Stree not found",
                        "Cannot activate stree with id " + id)));
    }

@Override
public Mono<Stree> deactivate(String id) {
    return streeRepository.findById(id)
            .flatMap(stree -> {
                stree.setStatus(false);
                return streeRepository.save(stree);
            })
            .switchIfEmpty(Mono.error(new CustomException(
                    HttpStatus.NOT_FOUND.value(),
                    "Stree not found",
                    "Cannot deactivate stree with id " + id)));
}
}