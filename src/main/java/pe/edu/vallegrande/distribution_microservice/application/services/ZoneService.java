package pe.edu.vallegrande.distribution_microservice.application.services;

import java.util.List;

import pe.edu.vallegrande.distribution_microservice.domain.models.Zone;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneUpdateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.ZoneResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.rest.ApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ZoneService {
    Flux<Zone> getAll();
   Mono<ApiResponse<List<Zone>>> getAllActive();
Mono<ApiResponse<List<Zone>>> getAllInactive();
    Mono<Zone> getById(String id);
    Mono<ZoneResponse> save(ZoneCreateRequest zoneRequest);
    Mono<Zone> update(String id, ZoneUpdateRequest request);
    Mono<Void> delete(String id);
    Mono<Zone> activate(String id);
    Mono<Zone> desactivate(String id);
}
