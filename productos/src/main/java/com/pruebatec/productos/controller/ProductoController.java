package com.pruebatec.productos.controller;

import com.pruebatec.productos.dto.ProductoRequestDto;
import com.pruebatec.productos.dto.ProductoResponseDto;
import com.pruebatec.productos.response.ApiResponse;
import com.pruebatec.productos.response.ApiResponseBuilder;
import com.pruebatec.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(
        summary = "Obtener todos los productos",
        description = """
            Retorna una lista de todos los productos activos en el sistema.
            Los productos se ordenan por ID de forma ascendente.
            """,
        tags = {"Productos"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                          "success": true,
                          "message": "Productos obtenidos exitosamente",
                          "data": [
                            {
                              "id": 1,
                              "nombre": "iPhone 15 Pro",
                              "precio": 1299.99,
                              "descripcion": "El último iPhone con chip A17 Pro",
                              "fechaCreacion": "2025-07-27T10:30:00",
                              "fechaActualizacion": "2025-07-27T10:30:00",
                              "activo": "Y"
                            }
                          ],
                          "meta": {
                            "total": 1,
                            "count": 1
                          },
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDto>>> obtenerTodosLosProductos() {
        log.info("🔍 GET /productos - Obteniendo todos los productos");
        
        List<ProductoResponseDto> productos = productoService.obtenerTodosLosProductos();
        
        ApiResponse<List<ProductoResponseDto>> response = ApiResponseBuilder.success(
            "Productos obtenidos exitosamente", 
            productos, 
            productos.size()
        );
        
        log.info("✅ Se retornaron {} productos", productos.size());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = """
            Busca y retorna un producto específico basado en su ID único.
            Solo retorna productos que estén activos en el sistema.
            """,
        tags = {"Productos"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Producto encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Producto encontrado",
                    value = """
                        {
                          "success": true,
                          "message": "Producto encontrado",
                          "data": {
                            "id": 1,
                            "nombre": "iPhone 15 Pro",
                            "precio": 1299.99,
                            "descripcion": "El último iPhone con chip A17 Pro",
                            "fechaCreacion": "2025-07-27T10:30:00",
                            "fechaActualizacion": "2025-07-27T10:30:00",
                            "activo": "Y"
                          },
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Producto no encontrado",
                    value = """
                        {
                          "success": false,
                          "message": "Producto con ID 999 no fue encontrado",
                          "errors": [
                            {
                              "code": "NOT_FOUND",
                              "title": "Recurso no encontrado",
                              "detail": "Producto con ID 999 no fue encontrado"
                            }
                          ],
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "ID inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> obtenerProductoPorId(
            @Parameter(
                description = "ID único del producto",
                required = true,
                example = "1",
                schema = @Schema(type = "integer", format = "int64", minimum = "1")
            )
            @PathVariable Long id) {
        log.info("🔍 GET /productos/{} - Obteniendo producto por ID", id);
        
        ProductoResponseDto producto = productoService.obtenerProductoPorId(id);
        
        ApiResponse<ProductoResponseDto> response = ApiResponseBuilder.success(
            "Producto encontrado", 
            producto
        );
        
        log.info("✅ Producto encontrado: {}", producto.getNombre());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Crear nuevo producto",
        description = """
            Crea un nuevo producto en el sistema.
            
            **Validaciones:**
            - El nombre es obligatorio y no puede estar vacío
            - El precio debe ser mayor a 0.01
            - El nombre debe ser único (no puede existir otro producto activo con el mismo nombre)
            - La descripción es opcional
            """,
        tags = {"Productos"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Producto creado",
                    value = """
                        {
                          "success": true,
                          "message": "Producto creado exitosamente",
                          "data": {
                            "id": 5,
                            "nombre": "MacBook Air M3",
                            "precio": 1899.99,
                            "descripcion": "Laptop ultradelgada con chip M3",
                            "fechaCreacion": "2025-07-27T15:45:30",
                            "fechaActualizacion": "2025-07-27T15:45:30",
                            "activo": "Y"
                          },
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Errores de validación",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = """
                        {
                          "success": false,
                          "message": "Errores de validación en los datos enviados",
                          "errors": [
                            {
                              "code": "VALIDATION_ERROR",
                              "title": "Error de validación",
                              "detail": "El nombre del producto es obligatorio"
                            }
                          ],
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Producto duplicado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Producto duplicado",
                    value = """
                        {
                          "success": false,
                          "message": "Ya existe un producto con el nombre: iPhone 15 Pro",
                          "errors": [
                            {
                              "code": "DUPLICATE_ERROR",
                              "title": "Recurso duplicado",
                              "detail": "Ya existe un producto con el nombre: iPhone 15 Pro"
                            }
                          ],
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDto>> crearProducto(
            @Parameter(
                description = "Datos del producto a crear",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoRequestDto.class),
                    examples = {
                        @ExampleObject(
                            name = "iPhone",
                            summary = "Ejemplo de iPhone",
                            value = """
                                {
                                  "nombre": "iPhone 15 Pro Max",
                                  "precio": 1399.99,
                                  "descripcion": "El iPhone más avanzado con pantalla de 6.7 pulgadas"
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Laptop",
                            summary = "Ejemplo de Laptop",
                            value = """
                                {
                                  "nombre": "Dell XPS 13",
                                  "precio": 1299.00,
                                  "descripcion": "Ultrabook premium con pantalla InfinityEdge"
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductoRequestDto productoRequest) {
        log.info("🆕 POST /productos - Creando nuevo producto: {}", productoRequest.getNombre());
        
        ProductoResponseDto productoCreado = productoService.crearProducto(productoRequest);
        
        ApiResponse<ProductoResponseDto> response = ApiResponseBuilder.success(
            "Producto creado exitosamente", 
            productoCreado
        );
        
        log.info("✅ Producto creado con ID: {}", productoCreado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Health check del servicio",
        description = """
            Verifica que el servicio de productos esté funcionando correctamente.
            Útil para monitoreo y verificaciones de estado.
            """,
        tags = {"Health Check"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Servicio funcionando correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Servicio activo",
                    value = """
                        {
                          "success": true,
                          "message": "✅ Productos Service is UP and running",
                          "data": "Service is healthy",
                          "timestamp": "2025-07-27T15:45:30"
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = ApiResponseBuilder.success(
            "✅ Productos Service is UP and running",
            "Service is healthy"
        );
        
        return ResponseEntity.ok(response);
    }
}