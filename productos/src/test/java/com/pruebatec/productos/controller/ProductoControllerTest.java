package com.pruebatec.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatec.productos.dto.ProductoRequestDto;
import com.pruebatec.productos.dto.ProductoResponseDto;
import com.pruebatec.productos.exception.ProductoException;
import com.pruebatec.productos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@DisplayName("ProductoController Tests")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDto producto1;
    private ProductoResponseDto producto2;
    private ProductoRequestDto productoRequestDto;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        producto1 = new ProductoResponseDto();
        producto1.setId(1L);
        producto1.setNombre("iPhone 15 Pro");
        producto1.setPrecio(new BigDecimal("1299.99"));
        producto1.setDescripcion("El último iPhone con chip A17 Pro");
        producto1.setActivo("Y");
        producto1.setFechaCreacion(LocalDateTime.now());
        producto1.setFechaActualizacion(LocalDateTime.now());

        producto2 = new ProductoResponseDto();
        producto2.setId(2L);
        producto2.setNombre("MacBook Air M3");
        producto2.setPrecio(new BigDecimal("1899.99"));
        producto2.setDescripcion("Laptop ultradelgada con chip M3");
        producto2.setActivo("Y");
        producto2.setFechaCreacion(LocalDateTime.now());
        producto2.setFechaActualizacion(LocalDateTime.now());

        productoRequestDto = new ProductoRequestDto();
        productoRequestDto.setNombre("Dell XPS 13");
        productoRequestDto.setPrecio(new BigDecimal("1299.00"));
        productoRequestDto.setDescripcion("Ultrabook premium con pantalla InfinityEdge");
    }

    @Nested
    @DisplayName("GET /productos - Obtener todos los productos")
    class ObtenerTodosLosProductosTests {

        @Test
        @DisplayName("Debería retornar lista de productos exitosamente")
        void deberiaRetornarListaDeProductosExitosamente() throws Exception {
            // Given
            List<ProductoResponseDto> productos = Arrays.asList(producto1, producto2);
            when(productoService.obtenerTodosLosProductos()).thenReturn(productos);

            // When & Then
            mockMvc.perform(get("/productos")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Productos obtenidos exitosamente"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].nombre").value("iPhone 15 Pro"))
                    .andExpect(jsonPath("$.data[0].precio").value(1299.99))
                    .andExpect(jsonPath("$.data[1].id").value(2))
                    .andExpect(jsonPath("$.data[1].nombre").value("MacBook Air M3"))
                    .andExpect(jsonPath("$.meta.total").value(2))
                    .andExpect(jsonPath("$.meta.count").value(2))
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(productoService).obtenerTodosLosProductos();
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay productos")
        void deberiaRetornarListaVaciaCuandoNoHayProductos() throws Exception {
            // Given
            when(productoService.obtenerTodosLosProductos()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/productos"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Productos obtenidos exitosamente"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(0)))
                    .andExpect(jsonPath("$.meta.total").value(0))
                    .andExpect(jsonPath("$.meta.count").value(0));

            verify(productoService).obtenerTodosLosProductos();
        }

        @Test
        @DisplayName("Debería manejar excepción del servicio")
        void deberiaManejarExcepcionDelServicio() throws Exception {
            // Given
            when(productoService.obtenerTodosLosProductos())
                    .thenThrow(new RuntimeException("Error de base de datos"));

            // When & Then
            mockMvc.perform(get("/productos"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());

            verify(productoService).obtenerTodosLosProductos();
        }
    }

    @Nested
    @DisplayName("GET /productos/{id} - Obtener producto por ID")
    class ObtenerProductoPorIdTests {

        @Test
        @DisplayName("Debería retornar producto por ID exitosamente")
        void deberiaRetornarProductoPorIdExitosamente() throws Exception {
            // Given
            when(productoService.obtenerProductoPorId(1L)).thenReturn(producto1);

            // When & Then
            mockMvc.perform(get("/productos/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Producto encontrado"))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.nombre").value("iPhone 15 Pro"))
                    .andExpect(jsonPath("$.data.precio").value(1299.99))
                    .andExpect(jsonPath("$.data.descripcion").value("El último iPhone con chip A17 Pro"))
                    .andExpect(jsonPath("$.data.activo").value("Y"))
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(productoService).obtenerProductoPorId(1L);
        }

        @Test
        @DisplayName("Debería retornar 404 cuando producto no existe")
        void deberiaRetornar404CuandoProductoNoExiste() throws Exception {
            // Given
            when(productoService.obtenerProductoPorId(999L))
                    .thenThrow(ProductoException.noEncontrado(999L));

            // When & Then
            mockMvc.perform(get("/productos/999"))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(productoService).obtenerProductoPorId(999L);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando ID es inválido")
        void deberiaRetornar400CuandoIdEsInvalido() throws Exception {
            // Given
            when(productoService.obtenerProductoPorId(0L))
                    .thenThrow(ProductoException.validacion("id", 0L, "El ID del producto debe ser un número positivo"));

            // When & Then
            mockMvc.perform(get("/productos/0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(productoService).obtenerProductoPorId(0L);
        }

        @Test
        @DisplayName("Debería manejar ID con formato incorrecto")
        void deberiaManejarIdConFormatoIncorrecto() throws Exception {
            // When & Then
            mockMvc.perform(get("/productos/abc"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }
    }

    @Nested
    @DisplayName("POST /productos - Crear producto")
    class CrearProductoTests {

        @Test
        @DisplayName("Debería crear producto exitosamente")
        void deberiaCrearProductoExitosamente() throws Exception {
            // Given
            ProductoResponseDto productoCreado = new ProductoResponseDto();
            productoCreado.setId(3L);
            productoCreado.setNombre("Dell XPS 13");
            productoCreado.setPrecio(new BigDecimal("1299.00"));
            productoCreado.setDescripcion("Ultrabook premium con pantalla InfinityEdge");
            productoCreado.setActivo("Y");
            productoCreado.setFechaCreacion(LocalDateTime.now());
            productoCreado.setFechaActualizacion(LocalDateTime.now());

            when(productoService.crearProducto(any(ProductoRequestDto.class))).thenReturn(productoCreado);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Producto creado exitosamente"))
                    .andExpect(jsonPath("$.data.id").value(3))
                    .andExpect(jsonPath("$.data.nombre").value("Dell XPS 13"))
                    .andExpect(jsonPath("$.data.precio").value(1299.00))
                    .andExpect(jsonPath("$.data.descripcion").value("Ultrabook premium con pantalla InfinityEdge"))
                    .andExpect(jsonPath("$.data.activo").value("Y"))
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(productoService).crearProducto(any(ProductoRequestDto.class));
        }

        @Test
        @DisplayName("Debería retornar 409 cuando producto ya existe")
        void deberiaRetornar409CuandoProductoYaExiste() throws Exception {
            // Given
            when(productoService.crearProducto(any(ProductoRequestDto.class)))
                    .thenThrow(ProductoException.duplicado("Dell XPS 13"));

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());

            verify(productoService).crearProducto(any(ProductoRequestDto.class));
        }

        @Test
        @DisplayName("Debería retornar 400 cuando nombre está vacío")
        void deberiaRetornar400CuandoNombreEstaVacio() throws Exception {
            // Given
            productoRequestDto.setNombre("");

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando nombre es nulo")
        void deberiaRetornar400CuandoNombreEsNulo() throws Exception {
            // Given
            productoRequestDto.setNombre(null);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando precio es nulo")
        void deberiaRetornar400CuandoPrecioEsNulo() throws Exception {
            // Given
            productoRequestDto.setPrecio(null);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando precio es negativo")
        void deberiaRetornar400CuandoPrecioEsNegativo() throws Exception {
            // Given
            productoRequestDto.setPrecio(new BigDecimal("-10.00"));

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando precio es cero")
        void deberiaRetornar400CuandoPrecioEsCero() throws Exception {
            // Given
            productoRequestDto.setPrecio(BigDecimal.ZERO);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería retornar 400 cuando JSON es inválido")
        void deberiaRetornar400CuandoJsonEsInvalido() throws Exception {
            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nombre\": \"Test\", \"precio\": }"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(productoService);
        }

        @Test
        @DisplayName("Debería crear producto sin descripción")
        void deberiaCrearProductoSinDescripcion() throws Exception {
            // Given
            productoRequestDto.setDescripcion(null);
            
            ProductoResponseDto productoCreado = new ProductoResponseDto();
            productoCreado.setId(3L);
            productoCreado.setNombre("Dell XPS 13");
            productoCreado.setPrecio(new BigDecimal("1299.00"));
            productoCreado.setDescripcion(null);
            productoCreado.setActivo("Y");
            productoCreado.setFechaCreacion(LocalDateTime.now());
            productoCreado.setFechaActualizacion(LocalDateTime.now());

            when(productoService.crearProducto(any(ProductoRequestDto.class))).thenReturn(productoCreado);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.descripcion").doesNotExist());

            verify(productoService).crearProducto(any(ProductoRequestDto.class));
        }
    }

    @Nested
    @DisplayName("GET /productos/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Debería retornar estado saludable")
        void deberiaRetornarEstadoSaludable() throws Exception {
            // When & Then
            mockMvc.perform(get("/productos/health"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("✅ Productos Service is UP and running"))
                    .andExpect(jsonPath("$.data").value("Service is healthy"))
                    .andExpect(jsonPath("$.timestamp").exists());

            verifyNoInteractions(productoService);
        }
    }

    @Nested
    @DisplayName("Casos Edge y Validaciones Adicionales")
    class CasosEdgeTests {

        @Test
        @DisplayName("Debería manejar nombres con caracteres especiales")
        void deberiaManejarNombresConCaracteresEspeciales() throws Exception {
            // Given
            productoRequestDto.setNombre("Producto-Especial_123 ñáéíóú");
            
            ProductoResponseDto productoCreado = new ProductoResponseDto();
            productoCreado.setId(3L);
            productoCreado.setNombre("Producto-Especial_123 ñáéíóú");
            productoCreado.setPrecio(new BigDecimal("1299.00"));
            productoCreado.setDescripcion("Descripción con ñáéíóú");
            productoCreado.setActivo("Y");
            productoCreado.setFechaCreacion(LocalDateTime.now());
            productoCreado.setFechaActualizacion(LocalDateTime.now());

            when(productoService.crearProducto(any(ProductoRequestDto.class))).thenReturn(productoCreado);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.nombre").value("Producto-Especial_123 ñáéíóú"));

            verify(productoService).crearProducto(any(ProductoRequestDto.class));
        }

        @Test
        @DisplayName("Debería manejar precios con muchos decimales")
        void deberiaManejarPreciosConMuchosDecimales() throws Exception {
            // Given
            productoRequestDto.setPrecio(new BigDecimal("1299.999999"));
            
            ProductoResponseDto productoCreado = new ProductoResponseDto();
            productoCreado.setId(3L);
            productoCreado.setNombre("Dell XPS 13");
            productoCreado.setPrecio(new BigDecimal("1299.999999"));
            productoCreado.setDescripcion("Ultrabook premium");
            productoCreado.setActivo("Y");
            productoCreado.setFechaCreacion(LocalDateTime.now());
            productoCreado.setFechaActualizacion(LocalDateTime.now());

            when(productoService.crearProducto(any(ProductoRequestDto.class))).thenReturn(productoCreado);

            // When & Then
            mockMvc.perform(post("/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productoRequestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            verify(productoService).crearProducto(any(ProductoRequestDto.class));
        }

        @Test
        @DisplayName("Debería verificar headers CORS")
        void deberiaVerificarHeadersCors() throws Exception {
            // When & Then
            mockMvc.perform(options("/productos")
                    .header("Origin", "http://localhost:3000")
                    .header("Access-Control-Request-Method", "POST")
                    .header("Access-Control-Request-Headers", "Content-Type"))
                    .andDo(print())
                    .andExpect(header().string("Access-Control-Allow-Origin", "*"));
        }

        @Test
        @DisplayName("Debería manejar ID muy grande")
        void deberiaManejarIdMuyGrande() throws Exception {
            // Given
            Long idGrande = Long.MAX_VALUE;
            when(productoService.obtenerProductoPorId(idGrande))
                    .thenThrow(ProductoException.noEncontrado(idGrande));

            // When & Then
            mockMvc.perform(get("/productos/" + idGrande))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(productoService).obtenerProductoPorId(idGrande);
        }

        @Test
        @DisplayName("Debería verificar que se use el MediaType correcto")
        void deberiaVerificarQueSeUseElMediaTypeCorrecto() throws Exception {
            // Given
            when(productoService.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto1));

            // When & Then
            mockMvc.perform(get("/productos")
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(productoService).obtenerTodosLosProductos();
        }

        @Test
        @DisplayName("Debería validar estructura completa de respuesta")
        void deberiaValidarEstructuraCompletaDeRespuesta() throws Exception {
            // Given
            when(productoService.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto1));

            // When & Then
            mockMvc.perform(get("/productos"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.data").exists())
                    .andExpect(jsonPath("$.meta").exists())
                    .andExpect(jsonPath("$.meta.total").exists())
                    .andExpect(jsonPath("$.meta.count").exists())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.errors").doesNotExist());

            verify(productoService).obtenerTodosLosProductos();
        }
    }
}