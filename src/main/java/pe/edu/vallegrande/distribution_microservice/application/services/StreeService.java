package pe.edu.vallegrande.distribution_microservice.application.services;

import pe.edu.vallegrande.distribution_microservice.domain.models.Stree;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeUpdateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.StreeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StreeService {
    Flux<Stree> getAll();
    Flux<StreeResponse> getAllActive();
   Flux<Stree> getAllInactive();
    Mono<Stree> getById(String id);
    Mono<StreeResponse> save(StreeCreateRequest streeRequest);
    Mono<Stree> update(String id, StreeUpdateRequest request);
    Mono<Void> delete(String id);
    Mono<Stree> activate(String id);
    Mono<Stree> deactivate(String id);
}
