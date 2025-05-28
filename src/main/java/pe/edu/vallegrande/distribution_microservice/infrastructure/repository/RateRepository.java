package pe.edu.vallegrande.distribution_microservice.infrastructure.repository;

import pe.edu.vallegrande.distribution_microservice.domain.models.Rate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RateRepository extends ReactiveMongoRepository<Rate, String> {
    // Método para encontrar tarifas por zonaId
    Flux<Rate> findAllByZoneId(String zoneId);

    // Método para encontrar tarifas activas
    Flux<Rate> findAllByStatus(String status);

    // Método para verificar si existe una tarifa con un ID específico
    Mono<Boolean> existsByZoneId(String zoneId);
}