package pe.edu.vallegrande.distribution_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.distribution_microservice.application.services.ZoneService;
import pe.edu.vallegrande.distribution_microservice.domain.models.Zone;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneCreateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.request.ZoneUpdateRequest;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.response.ZoneResponse;
import pe.edu.vallegrande.distribution_microservice.infrastructure.rest.ApiResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zonas")
@CrossOrigin("*")
@AllArgsConstructor
public class ZoneRest {

    private final ZoneService zoneService;

    /** Listar todas las zonas (act. e inact.) */
    @GetMapping
    public Mono<ResponseDto<List<Zone>>> getAll() {
        return zoneService.getAll()
                .collectList()
                .map(zonas -> new ResponseDto<>(true, zonas));
    }

    /** Listar zonas activas */
    @GetMapping("/activas")
    public Mono<ApiResponse<List<Zone>>> getAllActive() {
        return zoneService.getAllActive();
    }

/** Listar zonas inactivas */
@GetMapping("/inactivas")
public Mono<ResponseEntity<ApiResponse<List<Zone>>>> getInactivas() {
    return zoneService.getAllInactive()
        .map(ResponseEntity::ok);
}

/** Obtener por ID */
@GetMapping("/{id}")
public Mono<ResponseDto<Zone>> getById(@PathVariable String id) {
    return zoneService.getById(id)
        .map(z -> new ResponseDto<>(true, z))
        .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
            new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Get by ID failed", e.getMessage()))));
}

    /** Crear zona */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<ZoneResponse>> create(@RequestBody ZoneCreateRequest request) {
        return zoneService.save(request)
            .map(zr -> new ResponseDto<>(true, zr))
            .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Validation error", e.getMessage()))));
    }

    /** Actualizar zona */
    @PutMapping("/{id}")
    public Mono<ResponseDto<Zone>> update(@PathVariable String id,
                                          @RequestBody ZoneUpdateRequest request) {
        return zoneService.update(id, request)
            .map(z -> new ResponseDto<>(true, z))
            .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Update failed", e.getMessage()))));
    }

    /** Eliminar (lógico) zona */
    @DeleteMapping("/{id}")
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return zoneService.delete(id)
            .then(Mono.just(new ResponseDto<>(true, null)))
            .onErrorResume(e -> {
                e.printStackTrace();
                return Mono.just(new ResponseDto<>(false,
                    new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Delete failed", e.getMessage())));
            });
    }

    /** Reactivar zona */
    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<Zone>> activate(@PathVariable String id) {
        return zoneService.activate(id)
            .map(z -> new ResponseDto<>(true, z))
            .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Activation failed", e.getMessage()))));
    }

    /** Desactivar zona */
    @PatchMapping("/{id}/desactivate")
    public Mono<ResponseDto<Zone>> desactivate(@PathVariable String id) {
        return zoneService.desactivate(id)
            .map(z -> new ResponseDto<>(true, z))
            .onErrorResume(e -> Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Desactivation failed", e.getMessage()))));
    }
}
