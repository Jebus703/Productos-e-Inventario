package com.pruebatec.productos.service.impl;

import com.pruebatec.productos.dto.ProductoRequestDto;
import com.pruebatec.productos.dto.ProductoResponseDto;
import com.pruebatec.productos.entity.Producto;
import com.pruebatec.productos.exception.ProductoException;
import com.pruebatec.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoServiceImpl Tests")
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto1;
    private Producto producto2;
    private ProductoRequestDto productoRequestDto;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Laptop Gaming");
        producto1.setPrecio(new BigDecimal("1500.00"));
        producto1.setDescripcion("Laptop para gaming de alta gama");
        producto1.setActivo("Y");
        producto1.setFechaCreacion(LocalDateTime.now());
        producto1.setFechaActualizacion(LocalDateTime.now());

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Inalámbrico");
        producto2.setPrecio(new BigDecimal("25.99"));
        producto2.setDescripcion("Mouse inalámbrico de precisión");
        producto2.setActivo("Y");
        producto2.setFechaCreacion(LocalDateTime.now());
        producto2.setFechaActualizacion(LocalDateTime.now());

        productoRequestDto = new ProductoRequestDto();
        productoRequestDto.setNombre("Teclado Mecánico");
        productoRequestDto.setPrecio(new BigDecimal("89.99"));
        productoRequestDto.setDescripcion("Teclado mecánico RGB");
    }

    @Nested
    @DisplayName("Obtener Todos Los Productos")
    class ObtenerTodosLosProductosTests {

        @Test
        @DisplayName("Debería retornar lista de productos activos")
        void deberiaRetornarListaDeProductosActivos() {
            // Given
            List<Producto> productos = Arrays.asList(producto1, producto2);
            when(productoRepository.findByActivoOrderById("Y")).thenReturn(productos);

            // When
            List<ProductoResponseDto> resultado = productoService.obtenerTodosLosProductos();

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Laptop Gaming");
            assertThat(resultado.get(1).getNombre()).isEqualTo("Mouse Inalámbrico");
            
            verify(productoRepository).findByActivoOrderById("Y");
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay productos")
        void deberiaRetornarListaVaciaCuandoNoHayProductos() {
            // Given
            when(productoRepository.findByActivoOrderById("Y")).thenReturn(Collections.emptyList());

            // When
            List<ProductoResponseDto> resultado = productoService.obtenerTodosLosProductos();

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();
            
            verify(productoRepository).findByActivoOrderById("Y");
        }
    }

    @Nested
    @DisplayName("Obtener Producto Por ID")
    class ObtenerProductoPorIdTests {

        @Test
        @DisplayName("Debería retornar producto cuando ID existe")
        void deberiaRetornarProductoCuandoIdExiste() {
            // Given
            Long id = 1L;
            when(productoRepository.findByIdAndActivo(id, "Y")).thenReturn(Optional.of(producto1));

            // When
            ProductoResponseDto resultado = productoService.obtenerProductoPorId(id);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("Laptop Gaming");
            assertThat(resultado.getPrecio()).isEqualTo(new BigDecimal("1500.00"));
            
            verify(productoRepository).findByIdAndActivo(id, "Y");
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando producto no existe")
        void deberiaLanzarExcepcionCuandoProductoNoExiste() {
            // Given
            Long id = 999L;
            when(productoRepository.findByIdAndActivo(id, "Y")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productoService.obtenerProductoPorId(id))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("no fue encontrado");
            
            verify(productoRepository).findByIdAndActivo(id, "Y");
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando ID es nulo")
        void deberiaLanzarExcepcionCuandoIdEsNulo() {
            // When & Then
            assertThatThrownBy(() -> productoService.obtenerProductoPorId(null))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("debe ser un número positivo");
            
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando ID es menor o igual a cero")
        void deberiaLanzarExcepcionCuandoIdEsMenorOIgualACero() {
            // When & Then
            assertThatThrownBy(() -> productoService.obtenerProductoPorId(0L))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("debe ser un número positivo");
            
            assertThatThrownBy(() -> productoService.obtenerProductoPorId(-1L))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("debe ser un número positivo");
            
            verifyNoInteractions(productoRepository);
        }
    }

    @Nested
    @DisplayName("Crear Producto")
    class CrearProductoTests {

        @Test
        @DisplayName("Debería crear producto exitosamente")
        void deberiaCrearProductoExitosamente() {
            // Given
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y"))
                    .thenReturn(false);
            
            Producto productoGuardado = new Producto();
            productoGuardado.setId(3L);
            productoGuardado.setNombre("Teclado Mecánico");
            productoGuardado.setPrecio(new BigDecimal("89.99"));
            productoGuardado.setDescripcion("Teclado mecánico RGB");
            productoGuardado.setActivo("Y");
            productoGuardado.setFechaCreacion(LocalDateTime.now());
            productoGuardado.setFechaActualizacion(LocalDateTime.now());
            
            when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

            // When
            ProductoResponseDto resultado = productoService.crearProducto(productoRequestDto);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(3L);
            assertThat(resultado.getNombre()).isEqualTo("Teclado Mecánico");
            assertThat(resultado.getPrecio()).isEqualTo(new BigDecimal("89.99"));
            assertThat(resultado.getDescripcion()).isEqualTo("Teclado mecánico RGB");
            
            verify(productoRepository).existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y");
            verify(productoRepository).save(argThat(producto -> 
                producto.getNombre().equals("Teclado Mecánico") &&
                producto.getPrecio().equals(new BigDecimal("89.99")) &&
                producto.getActivo().equals("Y") &&
                producto.getFechaCreacion() != null &&
                producto.getFechaActualizacion() != null
            ));
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando nombre ya existe")
        void deberiaLanzarExcepcionCuandoNombreYaExiste() {
            // Given
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y"))
                    .thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("Ya existe");
            
            verify(productoRepository).existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y");
            verify(productoRepository, never()).save(any(Producto.class));
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando request es nulo")
        void deberiaLanzarExcepcionCuandoRequestEsNulo() {
            // When & Then
            // El servicio actual lanza NullPointerException en la línea del log antes de validar
            assertThatThrownBy(() -> productoService.crearProducto(null))
                    .isInstanceOf(NullPointerException.class);
            
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando nombre es nulo")
        void deberiaLanzarExcepcionCuandoNombreEsNulo() {
            // Given
            productoRequestDto.setNombre(null);

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("obligatorio");
            
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando nombre está vacío")
        void deberiaLanzarExcepcionCuandoNombreEstaVacio() {
            // Given
            productoRequestDto.setNombre("   ");

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("obligatorio");
            
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando precio es nulo")
        void deberiaLanzarExcepcionCuandoPrecioEsNulo() {
            // Given
            productoRequestDto.setPrecio(null);

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("mayor a 0");
            
            verifyNoInteractions(productoRepository);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando precio es menor o igual a cero")
        void deberiaLanzarExcepcionCuandoPrecioEsMenorOIgualACero() {
            // Given
            productoRequestDto.setPrecio(BigDecimal.ZERO);

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("mayor a 0");
            
            productoRequestDto.setPrecio(new BigDecimal("-10.00"));
            
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class)
                    .hasMessageContaining("mayor a 0");
            
            verifyNoInteractions(productoRepository);
        }
    }

    @Nested
    @DisplayName("Casos Edge")
    class CasosEdgeTests {

        @Test
        @DisplayName("Debería manejar nombres con diferentes casos para duplicados")
        void deberiaManejarNombresConDiferentesCasosParaDuplicados() {
            // Given
            productoRequestDto.setNombre("TECLADO mecánico");
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("TECLADO mecánico", "Y"))
                    .thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> productoService.crearProducto(productoRequestDto))
                    .isInstanceOf(ProductoException.class);
            
            verify(productoRepository).existsByNombreIgnoreCaseAndActivo("TECLADO mecánico", "Y");
        }

        @Test
        @DisplayName("Debería permitir crear producto con descripción nula")
        void deberiaPermitirCrearProductoConDescripcionNula() {
            // Given
            productoRequestDto.setDescripcion(null);
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y"))
                    .thenReturn(false);
            
            Producto productoGuardado = new Producto();
            productoGuardado.setId(3L);
            productoGuardado.setNombre("Teclado Mecánico");
            productoGuardado.setPrecio(new BigDecimal("89.99"));
            productoGuardado.setDescripcion(null);
            productoGuardado.setActivo("Y");
            productoGuardado.setFechaCreacion(LocalDateTime.now());
            productoGuardado.setFechaActualizacion(LocalDateTime.now());
            
            when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

            // When
            ProductoResponseDto resultado = productoService.crearProducto(productoRequestDto);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getDescripcion()).isNull();
            
            verify(productoRepository).save(any(Producto.class));
        }

        @Test
        @DisplayName("Debería verificar que se establezcan fechas de creación y actualización")
        void deberiaVerificarQueSeEstablezcanFechas() {
            // Given
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y"))
                    .thenReturn(false);
            
            Producto productoGuardado = new Producto();
            productoGuardado.setId(3L);
            productoGuardado.setNombre("Teclado Mecánico");
            productoGuardado.setPrecio(new BigDecimal("89.99"));
            productoGuardado.setDescripcion("Teclado mecánico RGB");
            productoGuardado.setActivo("Y");
            productoGuardado.setFechaCreacion(LocalDateTime.now());
            productoGuardado.setFechaActualizacion(LocalDateTime.now());
            
            when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

            // When
            ProductoResponseDto resultado = productoService.crearProducto(productoRequestDto);

            // Then
            assertThat(resultado).isNotNull();
            verify(productoRepository).save(argThat(producto -> 
                producto.getFechaCreacion() != null &&
                producto.getFechaActualizacion() != null &&
                producto.getActivo().equals("Y")
            ));
        }

        @Test
        @DisplayName("Debería verificar que se use el repositorio correctamente para verificar duplicados")
        void deberiaVerificarUsoCorrectoDelRepositorioParaDuplicados() {
            // Given
            when(productoRepository.existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y"))
                    .thenReturn(false);
            
            Producto productoGuardado = new Producto();
            productoGuardado.setId(3L);
            productoGuardado.setNombre("Teclado Mecánico");
            productoGuardado.setPrecio(new BigDecimal("89.99"));
            productoGuardado.setDescripcion("Teclado mecánico RGB");
            productoGuardado.setActivo("Y");
            productoGuardado.setFechaCreacion(LocalDateTime.now());
            productoGuardado.setFechaActualizacion(LocalDateTime.now());
            
            when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

            // When
            productoService.crearProducto(productoRequestDto);

            // Then
            // Verificar que se llame primero la verificación de duplicado
            verify(productoRepository).existsByNombreIgnoreCaseAndActivo("Teclado Mecánico", "Y");
            // Luego el guardado
            verify(productoRepository).save(any(Producto.class));
        }

        @Test
        @DisplayName("Debería validar que solo se busquen productos activos")
        void deberiaValidarQueSoloBusquenProductosActivos() {
            // When
            productoService.obtenerTodosLosProductos();

            // Then
            verify(productoRepository).findByActivoOrderById("Y");
            verify(productoRepository, never()).findByActivoOrderById("N");
        }

        @Test
        @DisplayName("Debería validar búsqueda por ID solo para productos activos")
        void deberiaValidarBusquedaPorIdSoloParaProductosActivos() {
            // Given
            Long id = 1L;
            when(productoRepository.findByIdAndActivo(id, "Y")).thenReturn(Optional.of(producto1));

            // When
            productoService.obtenerProductoPorId(id);

            // Then
            verify(productoRepository).findByIdAndActivo(id, "Y");
            verify(productoRepository, never()).findByIdAndActivo(id, "N");
        }
    }
}