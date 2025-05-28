package pe.edu.vallegrande.distribution_microservice.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private boolean status;
    private T data;
    private ErrorMessage error;

    public ResponseDto(boolean status, T data) {
        this.status = status;
        this.data = data;
        this.error = null;
    }

    public ResponseDto(boolean status, ErrorMessage error) {
        this.status = status;
        this.data = null;
        this.error = error;
    }
}