package com.pruebatec.productos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clase base para respuestas estándar de la API
 * Implementa el formato JSON API estándar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Meta meta;
    private List<Error> errors;
    private LocalDateTime timestamp;
    
    /**
     * Clase interna para metadatos
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Integer total;
        private Integer count;
        private Integer page;
        private Integer pageSize;
        private Map<String, Object> additional;
    }
    
    /**
     * Clase interna para errores
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String code;
        private String title;
        private String detail;
        private String source;
        private Map<String, Object> meta;
    }
    
    // Constructor para respuestas exitosas
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor para respuestas exitosas con metadatos
    public ApiResponse(boolean success, String message, T data, Meta meta) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = meta;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor para respuestas de error
    public ApiResponse(boolean success, String message, List<Error> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
