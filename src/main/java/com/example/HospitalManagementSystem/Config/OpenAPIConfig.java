package com.example.HospitalManagementSystem.Config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
@Configuration
public class OpenAPIConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(new Info()
                        .title("HOSPITAL MANAGEMENT SYSTEM-REST API")
                        .version("1.0.0")
                        .description("A fully featured Hospital Management System built using Spring Boot. \n" +
                                "Includes JWT Authentication, Role-Based Access Control (RBAC), and modules for Admin, Doctor, Patient, Receptionist, Lab Technician, and Pharmacist.")
                        .contact(new Contact().name("HIRTHIKHA K").email("hirthi26@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://apache.org"))
                );
    }

}
