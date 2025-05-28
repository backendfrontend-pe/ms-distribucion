package pe.edu.vallegrande.distribution_microservice.application.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.vallegrande.distribution_microservice.application.services.RateService;
import pe.edu.vallegrande.distribution_microservice.domain.enums.Constants;
import pe.edu.vallegrande.distribution_microservice.domain.models.Rate;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.RateCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.RateResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.exception.CustomException;
import pe.edu.vallegrande.distribution_microservice.infrastructure.repository.RateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RateServiceImpl implements RateService {

    @Autowired
    private RateRepository rateRepository;


    @Override
    public Flux<Rate> getAll() {
        return rateRepository.findAll()
                .doOnNext(rate -> log.info("Rate retrieved: {}", rate));
    }

    @Override
    public Flux<Rate> getAllActive() {
        return rateRepository.findAllByStatus(Constants.ACTIVE.name());
    }

    @Override
    public Flux<Rate> getAllInactive() {
        return rateRepository.findAllByStatus(Constants.INACTIVE.name());
    }

    @Override
    public Mono<Rate> getById(String id) {
        return rateRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Rate not found",
                        "The requested rate with id " + id + " was not found")));
    }

    @Override
    public Mono<RateResponse> save(RateCreateRequest request) {
        return rateRepository.existsByZoneId(request.getZoneId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomException(
                                HttpStatus.BAD_REQUEST.value(),
                                "Zone already exists",
                                "The zone " + request.getZoneId() + " is already registered"));
                    }

                    Rate rate = new Rate();
                    rate.setZoneId(request.getZoneId());
                    rate.setAmount(request.getAmount());
                    rate.setDescription(request.getDescription());
                    rate.setRateType(request.getRateType());
                    rate.setStartDate(request.getStartDate());
                    rate.setEndDate(request.getEndDate());
                    rate.setStatus(Constants.ACTIVE.name());
                    rate.setZoneName(request.getZoneName());

                    return rateRepository.save(rate)
                            .map(savedRate -> {
                                RateResponse response = new RateResponse();
                                response.setId(savedRate.getRateId());
                                response.setZoneId(savedRate.getZoneId());
                                response.setAmount(savedRate.getAmount());
                                response.setDescription(savedRate.getDescription());
                                response.setStartDate(savedRate.getStartDate());
                                response.setEndDate(savedRate.getEndDate());
                                response.setStatus(savedRate.getStatus());
                                response.setDateRecord(savedRate.getDateRecord());
                                response.setZoneName(savedRate.getZoneName());
                                return response;
                            });
                });
    }

    @Override
    public Mono<Rate> update(String id, Rate rate) {
        return rateRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Rate not found",
                        "Cannot update non-existent rate with id " + id)))
                .flatMap(existingRate -> {
                    existingRate.setZoneId(rate.getZoneId());
                    existingRate.setAmount(rate.getAmount());
                    existingRate.setDescription(rate.getDescription());
                    existingRate.setRateType(rate.getRateType());
                    existingRate.setStartDate(rate.getStartDate());
                    existingRate.setEndDate(rate.getEndDate());
                    existingRate.setZoneName(rate.getZoneName());
                    return rateRepository.save(existingRate);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return rateRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Rate not found",
                        "Cannot delete non-existent rate with id " + id)))
                .flatMap(rateRepository::delete);
    }

    @Override
    public Mono<Rate> activate(String id) {
        return changeStatus(id, Constants.ACTIVE.name());
    }

    @Override
    public Mono<Rate> desactivate(String id) {
        return changeStatus(id, Constants.INACTIVE.name());
    }

    private Mono<Rate> changeStatus(String id, String status) {
        return rateRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Rate not found",
                        "Cannot change status of non-existent rate with id " + id)))
                .flatMap(rate -> {
                    rate.setStatus(status);
                    return rateRepository.save(rate);
                });
    }
}