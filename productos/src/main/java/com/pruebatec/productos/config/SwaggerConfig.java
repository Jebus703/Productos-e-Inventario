package com.pruebatec.productos.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API
 * Genera documentación interactiva accesible en /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers())
                .tags(createTags());
    }

    /**
     * Información general de la API
     */
    private Info createApiInfo() {
        return new Info()
                .title("Microservicio de Productos API")
                .description("""
                    API REST para la gestión de productos en un sistema de microservicios.
                    
                    **Características principales:**
                    - Gestión completa de productos (CRUD)
                    - Validaciones robustas de datos
                    - Manejo de errores estandarizado
                    - Respuestas en formato JSON API
                    - Soporte para Oracle y SQLite
                    
                    **Tecnologías utilizadas:**
                    - Spring Boot 3.5.4
                    - Java 21
                    - Oracle Database 21c XE
                    - Spring Data JPA
                    - Lombok
                    - Maven
                    """)
                .version("1.0.0")
                .contact(createContact())
                .license(createLicense());
    }

    /**
     * Información de contacto del desarrollador
     */
    private Contact createContact() {
        return new Contact()
                .name("Jesus Antonio Suarez Duarte")
                .email("Jebus702@hotmail.com")
                .url("https://github.com/tu-usuario"); // Cambiar por tu GitHub
    }

    /**
     * Información de licencia
     */
    private License createLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * Servidores disponibles
     */
    private List<Server> createServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de desarrollo local");

        Server prodServer = new Server()
                .url("https://api-productos.tudominio.com")
                .description("Servidor de producción");

        return List.of(localServer, prodServer);
    }

    /**
     * Tags para agrupar endpoints
     */
    private List<Tag> createTags() {
        Tag productosTag = new Tag()
                .name("Productos")
                .description("Operaciones relacionadas con la gestión de productos");

        Tag healthTag = new Tag()
                .name("Health Check")
                .description("Endpoints para verificar el estado del servicio");

        return List.of(productosTag, healthTag);
    }
}