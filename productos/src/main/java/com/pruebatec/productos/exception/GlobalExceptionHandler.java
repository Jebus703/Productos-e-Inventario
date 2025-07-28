package com.pruebatec.productos.exception;

import com.pruebatec.productos.response.ApiResponse;
import com.pruebatec.productos.response.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones con formato JSON API estándar
 * Responsabilidad: Capturar excepciones y devolver respuestas consistentes
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de Bean Validation (@Valid)
     * Ejemplo: @NotBlank, @DecimalMin, etc.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("🚫 Error de validación: {}", ex.getMessage());
        
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        ApiResponse<Object> response = ApiResponseBuilder.validationError(
            "Errores de validación en los datos enviados", 
            errors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja errores de ProductoException personalizadas
     */
    @ExceptionHandler(ProductoException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductoException(ProductoException ex) {
        log.warn("⚠️ Error de producto: {} - Tipo: {}", ex.getMessage(), ex.getTipoError());
        
        ApiResponse<Object> response = switch (ex.getTipoError()) {
            case NOT_FOUND -> ApiResponseBuilder.notFound(ex.getMessage(), ex.getProductoId());
            case DUPLICADO -> ApiResponseBuilder.duplicateError(ex.getMessage(), ex.getNombreProducto());
            case VALIDATION_ERROR -> ApiResponseBuilder.error("VALIDATION_ERROR", "Error de validación", ex.getMessage());
            default -> ApiResponseBuilder.error("GENERAL_ERROR", "Error general", ex.getMessage());
        };
        
        HttpStatus status = switch (ex.getTipoError()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICADO -> HttpStatus.CONFLICT;
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };
        
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Maneja errores de JSON malformado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonErrors(HttpMessageNotReadableException ex) {
        log.warn("🚫 Error de JSON: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponseBuilder.error(
            "JSON_ERROR", 
            "Error en formato JSON", 
            "El formato del JSON enviado no es válido"
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja errores de tipo de parámetro incorrecto
     * Ejemplo: enviar "abc" en lugar de un número para el ID
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("🚫 Error de tipo de parámetro: {}", ex.getMessage());
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        ApiResponse<Object> response = ApiResponseBuilder.error(
            "TYPE_MISMATCH", 
            "Error de tipo de parámetro", 
            message
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja cualquier otra excepción no contemplada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("❌ Error interno no controlado: {} - Clase: {}", 
                 ex.getMessage(), ex.getClass().getSimpleName(), ex);
        
        // En desarrollo: mostrar más detalles
        // En producción: mensaje genérico
        String message = isDevelopmentEnvironment() 
            ? String.format("Error interno: %s (%s)", ex.getMessage(), ex.getClass().getSimpleName())
            : "Ha ocurrido un error interno. Por favor contacte al administrador.";
        
        ApiResponse<Object> response = ApiResponseBuilder.error(
            "INTERNAL_ERROR", 
            "Error interno del servidor", 
            message
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Detectar si estamos en ambiente de desarrollo
     */
    private boolean isDevelopmentEnvironment() {
        String[] activeProfiles = org.springframework.core.env.Environment.class.isInstance(this) 
            ? new String[0] : new String[]{"dev", "development", "local"};
        // Simplificado: siempre retorna false para producción
        return false; // Cambiar según configuración
    }
}