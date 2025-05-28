package pe.edu.vallegrande.distribution_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.vallegrande.distribution_microservice.application.services.StreeService;
import pe.edu.vallegrande.distribution_microservice.domain.models.Stree;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.StreeUpdateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.StreeResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.exception.CustomException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calles")
@CrossOrigin("*")
@AllArgsConstructor
public class StreeRest {

    private final StreeService streeService;

    /** Listar todas las calles (activas e inactivas) */
    @GetMapping
    public Mono<ResponseDto<List<Stree>>> getAll() {
        return streeService.getAll()
                .collectList()
                .map(strees -> new ResponseDto<>(true, strees));
    }

    /** Listar calles activas */
    @GetMapping("/active")
    public Mono<ResponseDto<List<StreeResponse>>> getAllActive(){
        return streeService.getAllActive()
                .collectList()
                .map(strees -> new ResponseDto<>(true, strees));
    }

    /** Listar calles inactivas */
@GetMapping("/inactive")
public Mono<ResponseDto<List<Stree>>> getAllInactive() {
    return streeService.getAllInactive()
            .collectList()
            .map(strees -> new ResponseDto<>(true, strees))
            .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                    new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching inactive streets", e.getMessage()))));
}


    /** Obtener calle por ID */
    @GetMapping("/{id}")
    public Mono<ResponseDto<Stree>> getById(@PathVariable String id) {
        return streeService.getById(id)
                .map(stree -> new ResponseDto<>(true, stree))  // Devuelve la calle con zoneName incluido
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Get by ID failed", e.getMessage()))));
    }

    /** Crear calle */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<StreeResponse>> create(@RequestBody StreeCreateRequest request) {
        return streeService.save(request)
                .map(savedStree -> new ResponseDto<>(true, savedStree))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Validation error", e.getMessage()))));
    }

    /** Actualizar calle */
    @PutMapping("/{id}")
public Mono<ResponseEntity<StreeResponse>> updateStree(@PathVariable String id, @RequestBody StreeUpdateRequest request) {
    return streeService.update(id, request)
        .map(updated -> {
            StreeResponse response = new StreeResponse(
                updated.getStreeId(),
                updated.getName(),
                updated.getZoneId(),
                updated.getZoneName(),
                updated.isStatus(),
                updated.getDateRecord()
            );
            return ResponseEntity.ok(response);
        });
}


    /** Eliminar calle (borrado lógico) */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return streeService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage()))));
    }

    /** Activar calle */
    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Stree>> activate(@PathVariable String id) {
        return streeService.activate(id)
                .map(stree -> new ResponseDto<>(true, stree))
                .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Activation failed", e.getMessage()))));
    }

   /** Desactivar calle */
@PatchMapping("/{id}/deactivate")
public Mono<ResponseDto<Stree>> deactivate(@PathVariable String id) {
    return streeService.deactivate(id) // o renombra aquí y en servicio a deactivate
        .map(stree -> new ResponseDto<>(true, stree))
        .onErrorResume(e -> {
            System.err.println("Error al desactivar calle: " + e.getMessage());
            return Mono.just(new ResponseDto<>(false,
                    new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Deactivation failed", e.getMessage())));
        });
}

}
