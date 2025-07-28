package com.pruebatec.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApplication.class, args);
		 System.out.println("🚀 Microservicio de Productos iniciado correctamente!");
	     System.out.println("📡 API disponible en: http://localhost:8080/productos");
	     System.out.println("🏥 Health check: http://localhost:8080/productos/health");
	     System.out.println("📘 Info de la App: http://localhost:8080/actuator/info");
	     System.out.println("📕 Swagger disponible en: http://localhost:8080/swagger-ui.html");

	}

}
