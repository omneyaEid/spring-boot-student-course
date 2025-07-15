package com.example.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "School API", version = "1.0", description = "Manage students and courses with authentication"),
        servers = @Server(url = "http://localhost:8080")
)
public class SchoolApp {
    public static void main(String[] args) {
        SpringApplication.run(SchoolApp.class, args);
    }
}
