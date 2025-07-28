package com.pruebatec.productos.exception;

import lombok.Getter;

/**
 * Excepción unificada para todos los errores relacionados con productos
 * Responsabilidad: Manejar todos los tipos de errores de productos
 */
@Getter
public class ProductoException extends RuntimeException {
    
    private final Long productoId;
    private final String nombreProducto;
    private final String campo;
    private final Object valor;
    private final TipoError tipoError;
    
    /**
     * Enum para clasificar el tipo de error
     */
    public enum TipoError {
        NOT_FOUND,
        DUPLICADO,
        VALIDATION_ERROR,
        GENERAL_ERROR
    }
    
    // Constructor para errores de producto no encontrado
    public ProductoException(Long productoId) {
        super(String.format("Producto con ID %d no fue encontrado", productoId));
        this.productoId = productoId;
        this.nombreProducto = null;
        this.campo = null;
        this.valor = null;
        this.tipoError = TipoError.NOT_FOUND;
    }
    
    // Constructor para errores de producto duplicado
    public ProductoException(String nombreProducto, boolean isDuplicado) {
        super(String.format("Ya existe un producto con el nombre: %s", nombreProducto));
        this.productoId = null;
        this.nombreProducto = nombreProducto;
        this.campo = null;
        this.valor = null;
        this.tipoError = TipoError.DUPLICADO;
    }
    
    // Constructor para errores de validación
    public ProductoException(String campo, Object valor, String message) {
        super(message);
        this.productoId = null;
        this.nombreProducto = null;
        this.campo = campo;
        this.valor = valor;
        this.tipoError = TipoError.VALIDATION_ERROR;
    }
    
    // Constructor genérico
    public ProductoException(String message) {
        super(message);
        this.productoId = null;
        this.nombreProducto = null;
        this.campo = null;
        this.valor = null;
        this.tipoError = TipoError.GENERAL_ERROR;
    }
    
    // Constructor con tipo específico
    public ProductoException(String message, TipoError tipoError) {
        super(message);
        this.productoId = null;
        this.nombreProducto = null;
        this.campo = null;
        this.valor = null;
        this.tipoError = tipoError;
    }
    
    // Métodos de conveniencia para crear excepciones específicas
    public static ProductoException noEncontrado(Long id) {
        return new ProductoException(id);
    }
    
    public static ProductoException duplicado(String nombre) {
        return new ProductoException(nombre, true);
    }
    
    public static ProductoException validacion(String campo, Object valor, String mensaje) {
        return new ProductoException(campo, valor, mensaje);
    }
    
    public static ProductoException validacion(String mensaje) {
        return new ProductoException(mensaje, TipoError.VALIDATION_ERROR);
    }
}