package com.pruebatec.productos.dto;

import com.pruebatec.productos.entity.Producto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de productos (Response)
 * Responsabilidad única: Transferir datos de salida
 */
@Data
@NoArgsConstructor
public class ProductoResponseDto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String activo;

    /**
     * Constructor desde Entity (Mapper pattern)
     */
    public ProductoResponseDto(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.descripcion = producto.getDescripcion();
        this.fechaCreacion = producto.getFechaCreacion();
        this.fechaActualizacion = producto.getFechaActualizacion();
        this.activo = producto.getActivo();
    }
}