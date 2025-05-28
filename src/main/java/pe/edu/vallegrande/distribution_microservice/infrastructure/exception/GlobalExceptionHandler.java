package pe.edu.vallegrande.distribution_microservice.infrastructure.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.distribution_microservice.infrastructure.dto.ResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Object>> handleCustomException(CustomException ex) {
        ErrorMessage errorMessage = ex.getErrorMessage();
        ResponseDto<Object> response = new ResponseDto<>(false, errorMessage);
        return ResponseEntity.status(errorMessage.getErrorCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleGenericException(Exception ex) {
        int statusCode = (ex instanceof RuntimeException) ? 500 : 400;

        ErrorMessage errorMessage = new ErrorMessage(
                statusCode,
                "Error interno del servidor",
                ex.getMessage()
        );
        ResponseDto<Object> response = new ResponseDto<>(false, errorMessage);
        return ResponseEntity.status(statusCode).body(response);
    }

}