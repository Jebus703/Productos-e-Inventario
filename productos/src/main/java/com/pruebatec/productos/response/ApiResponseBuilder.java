package com.pruebatec.productos.response;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder para crear respuestas estándar de la API
 * Patrón Builder para facilitar la creación de respuestas
 */
public class ApiResponseBuilder {
    
    /**
     * Crear respuesta exitosa simple
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * Crear respuesta exitosa con total de elementos
     */
    public static <T> ApiResponse<T> success(String message, T data, int total) {
        ApiResponse.Meta meta = new ApiResponse.Meta();
        meta.setTotal(total);
        
        if (data instanceof List) {
            meta.setCount(((List<?>) data).size());
        }
        
        return new ApiResponse<>(true, message, data, meta);
    }
    
    /**
     * Crear respuesta exitosa con metadatos personalizados
     */
    public static <T> ApiResponse<T> success(String message, T data, ApiResponse.Meta meta) {
        return new ApiResponse<>(true, message, data, meta);
    }
    
    /**
     * Crear respuesta de error simple
     */
    public static <T> ApiResponse<T> error(String message) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        errors.add(new ApiResponse.Error("GENERAL_ERROR", "Error", message, null, null));
        return new ApiResponse<>(false, message, errors);
    }
    
    /**
     * Crear respuesta de error con código específico
     */
    public static <T> ApiResponse<T> error(String code, String title, String message) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        errors.add(new ApiResponse.Error(code, title, message, null, null));
        return new ApiResponse<>(false, title, errors);
    }
    
    /**
     * Crear respuesta de error de validación
     */
    public static <T> ApiResponse<T> validationError(String message, List<String> validationErrors) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        
        for (String validationError : validationErrors) {
            errors.add(new ApiResponse.Error(
                "VALIDATION_ERROR", 
                "Error de validación", 
                validationError, 
                null, 
                null
            ));
        }
        
        return new ApiResponse<>(false, message, errors);
    }
    
    /**
     * Crear respuesta de error de producto no encontrado
     */
    public static <T> ApiResponse<T> notFound(String message, Long id) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("productoId", id);
        
        errors.add(new ApiResponse.Error(
            "NOT_FOUND", 
            "Recurso no encontrado", 
            message, 
            null, 
            meta
        ));
        
        return new ApiResponse<>(false, message, errors);
    }
    
    /**
     * Crear respuesta de error de producto duplicado
     */
    public static <T> ApiResponse<T> duplicateError(String message, String nombre) {
        List<ApiResponse.Error> errors = new ArrayList<>();
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("nombreProducto", nombre);
        
        errors.add(new ApiResponse.Error(
            "DUPLICATE_ERROR", 
            "Recurso duplicado", 
            message, 
            null, 
            meta
        ));
        
        return new ApiResponse<>(false, message, errors);
    }
    
    /**
     * Crear metadatos para listados con paginación
     */
    public static ApiResponse.Meta createMeta(int total, int count, int page, int pageSize) {
        ApiResponse.Meta meta = new ApiResponse.Meta();
        meta.setTotal(total);
        meta.setCount(count);
        meta.setPage(page);
        meta.setPageSize(pageSize);
        return meta;
    }
    
    /**
     * Crear metadatos simples con total
     */
    public static ApiResponse.Meta createMeta(int total) {
        ApiResponse.Meta meta = new ApiResponse.Meta();
        meta.setTotal(total);
        return meta;
    }
}
