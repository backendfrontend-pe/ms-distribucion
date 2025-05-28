package pe.edu.vallegrande.distribution_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import pe.edu.vallegrande.distribution_microservice.application.services.RateService;
import pe.edu.vallegrande.distribution_microservice.domain.models.Rate;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.RateCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.RateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarifas")
@CrossOrigin("*")
@AllArgsConstructor
public class RateRest {

    private final RateService rateService;

    @GetMapping
    public Mono<ResponseDto<List<Rate>>> getAll() {
        return rateService.getAll()
                .collectList()
                .map(rates -> new ResponseDto<>(true, rates));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<Rate>>> getAllActive() {
        return rateService.getAllActive()
                .collectList()
                .map(rates -> new ResponseDto<>(true, rates));
    }

    @GetMapping("/inactive")
    public Mono<ResponseDto<List<Rate>>> getAllInactive() {
        return rateService.getAllInactive()
                .collectList()
                .map(rates -> new ResponseDto<>(true, rates));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<Rate>> getById(@PathVariable String id) {
        return rateService.getById(id)
                .map(rate -> new ResponseDto<>(true, rate))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "Rate not found",
                                        e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<RateResponse>> create(@RequestBody RateCreateRequest request) {
        return rateService.save(request)
                .map(savedRate -> new ResponseDto<>(true, savedRate))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Validation error",
                                        e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseDto<Rate>> update(@PathVariable String id, @RequestBody Rate rate) {
        return rateService.update(id, rate)
                .map(updatedRate -> new ResponseDto<>(true, updatedRate))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Update failed",
                                        e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return rateService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Delete failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Rate>> activate(@PathVariable String id) {
        return rateService.activate(id)
                .map(rate -> new ResponseDto<>(true, rate))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Activation failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/desactivate")
    public Mono<ResponseDto<Rate>> desactivate(@PathVariable String id) {
        return rateService.desactivate(id)
                .map(rate -> new ResponseDto<>(true, rate))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Desactivation failed",
                                        e.getMessage()))));
    }


}