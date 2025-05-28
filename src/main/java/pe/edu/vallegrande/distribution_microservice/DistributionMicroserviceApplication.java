package pe.edu.vallegrande.distribution_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class DistributionMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributionMicroserviceApplication.class, args);
    }
}