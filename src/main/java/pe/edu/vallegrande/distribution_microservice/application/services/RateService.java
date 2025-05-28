package pe.edu.vallegrande.distribution_microservice.application.services;

import pe.edu.vallegrande.distribution_microservice.domain.models.Rate;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.RateCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.RateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RateService {
    Flux<Rate> getAll();
    Flux<Rate> getAllActive();
    Flux<Rate> getAllInactive();
    Mono<Rate> getById(String id);
    Mono<RateResponse> save(RateCreateRequest rateRequest);
    Mono<Rate> update(String id, Rate rate);
    Mono<Void> delete(String id);
    Mono<Rate> activate(String id);
    Mono<Rate> desactivate(String id);
}