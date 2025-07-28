package com.pruebatec.productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear productos (Request)
 * Responsabilidad única: Transferir datos de entrada
 */
@Data
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear un nuevo producto")
public class ProductoRequestDto {

    @Schema(
        description = "Nombre del producto",
        example = "iPhone 15 Pro",
        required = true,
        minLength = 1,
        maxLength = 255
    )
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;

    @Schema(
        description = "Precio del producto en USD",
        example = "1299.99",
        required = true,
        minimum = "0.01"
    )
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Schema(
        description = "Descripción detallada del producto",
        example = "El último iPhone con chip A17 Pro y cámara de 48MP",
        required = false
    )
    private String descripcion;
}