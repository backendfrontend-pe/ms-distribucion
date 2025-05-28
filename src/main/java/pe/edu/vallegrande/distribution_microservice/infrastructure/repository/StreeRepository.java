package pe.edu.vallegrande.distribution_microservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.distribution_microservice.domain.models.Stree;
import reactor.core.publisher.Flux;

@Repository
public interface StreeRepository extends ReactiveMongoRepository<Stree, String> {
   
    Flux<Stree> findAllByStatus(boolean status);
    Flux<Stree> findAllByZoneId(String zoneId);    // Para obtener calles por zona
}
