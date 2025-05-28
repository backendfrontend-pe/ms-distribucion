package pe.edu.vallegrande.distribution_microservice.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.distribution_microservice.domain.models.Zone;
import reactor.core.publisher.Flux;

@Repository

public interface ZoneRepository extends ReactiveMongoRepository<Zone, String> {
    
    Flux<Zone> findAllByStatus(String status);
    
}

