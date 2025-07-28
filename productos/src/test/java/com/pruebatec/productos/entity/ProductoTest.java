package com.pruebatec.productos.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producto Entity Tests")
class ProductoTest {

    private Validator validator;
    private Producto producto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Producto válido por defecto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(new BigDecimal("10.50"));
        producto.setDescripcion("Descripción del producto test");
        producto.setActivo("Y");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Debería crear producto con constructor vacío")
        void deberiaCrearProductoConConstructorVacio() {
            Producto nuevoProducto = new Producto();
            
            assertNotNull(nuevoProducto);
            assertNull(nuevoProducto.getId());
            assertNull(nuevoProducto.getNombre());
            assertNull(nuevoProducto.getPrecio());
            assertNull(nuevoProducto.getDescripcion());
        }

        @Test
        @DisplayName("Debería crear producto con constructor completo")
        void deberiaCrearProductoConConstructorCompleto() {
            Long id = 1L;
            String nombre = "Producto Test";
            BigDecimal precio = new BigDecimal("15.99");
            String descripcion = "Descripción test";
            LocalDateTime fechaCreacion = LocalDateTime.now();
            LocalDateTime fechaActualizacion = LocalDateTime.now();
            String activo = "Y";

            Producto nuevoProducto = new Producto(id, nombre, precio, descripcion, 
                                                fechaCreacion, fechaActualizacion, activo);

            assertEquals(id, nuevoProducto.getId());
            assertEquals(nombre, nuevoProducto.getNombre());
            assertEquals(precio, nuevoProducto.getPrecio());
            assertEquals(descripcion, nuevoProducto.getDescripcion());
            assertEquals(fechaCreacion, nuevoProducto.getFechaCreacion());
            assertEquals(fechaActualizacion, nuevoProducto.getFechaActualizacion());
            assertEquals(activo, nuevoProducto.getActivo());
        }

        @Test
        @DisplayName("Debería crear producto con constructor personalizado")
        void deberiaCrearProductoConConstructorPersonalizado() {
            String nombre = "Producto Personalizado";
            BigDecimal precio = new BigDecimal("25.50");
            String descripcion = "Descripción personalizada";

            Producto nuevoProducto = new Producto(nombre, precio, descripcion);

            assertNull(nuevoProducto.getId());
            assertEquals(nombre, nuevoProducto.getNombre());
            assertEquals(precio, nuevoProducto.getPrecio());
            assertEquals(descripcion, nuevoProducto.getDescripcion());
            assertEquals("Y", nuevoProducto.getActivo());
            assertNull(nuevoProducto.getFechaCreacion());
            assertNull(nuevoProducto.getFechaActualizacion());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Debería validar producto válido sin errores")
        void deberiaValidarProductoValidoSinErrores() {
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre es null")
        void deberiaFallarValidacionCuandoNombreEsNull() {
            producto.setNombre(null);
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El nombre del producto es obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre está vacío")
        void deberiaFallarValidacionCuandoNombreEstaVacio() {
            producto.setNombre("");
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El nombre del producto es obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre es solo espacios")
        void deberiaFallarValidacionCuandoNombreEsSoloEspacios() {
            producto.setNombre("   ");
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El nombre del producto es obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre excede 255 caracteres")
        void deberiaFallarValidacionCuandoNombreExcede255Caracteres() {
            String nombreLargo = "a".repeat(256);
            producto.setNombre(nombreLargo);
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El nombre no puede exceder 255 caracteres")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es null")
        void deberiaFallarValidacionCuandoPrecioEsNull() {
            producto.setPrecio(null);
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El precio es obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es cero")
        void deberiaFallarValidacionCuandoPrecioEsCero() {
            producto.setPrecio(BigDecimal.ZERO);
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El precio debe ser mayor a 0")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es negativo")
        void deberiaFallarValidacionCuandoPrecioEsNegativo() {
            producto.setPrecio(new BigDecimal("-5.00"));
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertEquals(1, violations.size());
            assertTrue(violations.stream().anyMatch(v -> 
                v.getMessage().equals("El precio debe ser mayor a 0")));
        }

        @Test
        @DisplayName("Debería aceptar precio mínimo válido")
        void deberiaAceptarPrecioMinimoValido() {
            producto.setPrecio(new BigDecimal("0.01"));
            
            Set<ConstraintViolation<Producto>> violations = validator.validate(producto);
            
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Debería establecer y obtener ID correctamente")
        void deberiaEstablecerYObtenerIdCorrectamente() {
            Long id = 999L;
            producto.setId(id);
            
            assertEquals(id, producto.getId());
        }

        @Test
        @DisplayName("Debería establecer y obtener nombre correctamente")
        void deberiaEstablecerYObtenerNombreCorrectamente() {
            String nombre = "Nuevo Producto";
            producto.setNombre(nombre);
            
            assertEquals(nombre, producto.getNombre());
        }

        @Test
        @DisplayName("Debería establecer y obtener precio correctamente")
        void deberiaEstablecerYObtenerPrecioCorrectamente() {
            BigDecimal precio = new BigDecimal("99.99");
            producto.setPrecio(precio);
            
            assertEquals(precio, producto.getPrecio());
        }

        @Test
        @DisplayName("Debería establecer y obtener descripción correctamente")
        void deberiaEstablecerYObtenerDescripcionCorrectamente() {
            String descripcion = "Nueva descripción";
            producto.setDescripcion(descripcion);
            
            assertEquals(descripcion, producto.getDescripcion());
        }

        @Test
        @DisplayName("Debería establecer y obtener fecha de creación correctamente")
        void deberiaEstablecerYObtenerFechaCreacionCorrectamente() {
            LocalDateTime fecha = LocalDateTime.now();
            producto.setFechaCreacion(fecha);
            
            assertEquals(fecha, producto.getFechaCreacion());
        }

        @Test
        @DisplayName("Debería establecer y obtener fecha de actualización correctamente")
        void deberiaEstablecerYObtenerFechaActualizacionCorrectamente() {
            LocalDateTime fecha = LocalDateTime.now();
            producto.setFechaActualizacion(fecha);
            
            assertEquals(fecha, producto.getFechaActualizacion());
        }

        @Test
        @DisplayName("Debería establecer y obtener estado activo correctamente")
        void deberiaEstablecerYObtenerEstadoActivoCorrectamente() {
            String activo = "N";
            producto.setActivo(activo);
            
            assertEquals(activo, producto.getActivo());
        }
    }

    @Nested
    @DisplayName("Callback Method Tests")
    class CallbackMethodTests {

        @Test
        @DisplayName("Debería ejecutar onCreate correctamente")
        void deberiaEjecutarOnCreateCorrectamente() {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Test");
            nuevoProducto.setPrecio(new BigDecimal("10.00"));
            
            // Simular @PrePersist
            nuevoProducto.onCreate();
            
            assertNotNull(nuevoProducto.getFechaCreacion());
            assertNotNull(nuevoProducto.getFechaActualizacion());
            assertEquals("Y", nuevoProducto.getActivo());
        }

        @Test
        @DisplayName("Debería mantener activo existente en onCreate")
        void deberiaMantenerActivoExistenteEnOnCreate() {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Test");
            nuevoProducto.setPrecio(new BigDecimal("10.00"));
            nuevoProducto.setActivo("N");
            
            // Simular @PrePersist
            nuevoProducto.onCreate();
            
            assertEquals("N", nuevoProducto.getActivo());
        }

        @Test
        @DisplayName("Debería ejecutar onUpdate correctamente")
        void deberiaEjecutarOnUpdateCorrectamente() {
            LocalDateTime fechaInicialCreacion = LocalDateTime.now().minusDays(1);
            LocalDateTime fechaInicialActualizacion = LocalDateTime.now().minusDays(1);
            
            producto.setFechaCreacion(fechaInicialCreacion);
            producto.setFechaActualizacion(fechaInicialActualizacion);
            
            // Simular @PreUpdate
            producto.onUpdate();
            
            assertEquals(fechaInicialCreacion, producto.getFechaCreacion());
            assertTrue(producto.getFechaActualizacion().isAfter(fechaInicialActualizacion));
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Debería ser igual a sí mismo")
        void deberiaSerIgualASiMismo() {
            assertEquals(producto, producto);
        }

        @Test
        @DisplayName("Debería ser igual a otro producto con los mismos valores")
        void deberiaSerIgualAOtroProductoConLosMismosValores() {
            Producto otroProducto = new Producto();
            otroProducto.setId(producto.getId());
            otroProducto.setNombre(producto.getNombre());
            otroProducto.setPrecio(producto.getPrecio());
            otroProducto.setDescripcion(producto.getDescripcion());
            otroProducto.setActivo(producto.getActivo());
            
            assertEquals(producto, otroProducto);
            assertEquals(producto.hashCode(), otroProducto.hashCode());
        }

        @Test
        @DisplayName("No debería ser igual a null")
        void noDeberiaSerIgualANull() {
            assertNotEquals(producto, null);
        }

        @Test
        @DisplayName("No debería ser igual a objeto de otra clase")
        void noDeberiaSerIgualAObjetoDeOtraClase() {
            String otroObjeto = "No soy un producto";
            assertNotEquals(producto, otroObjeto);
        }

        @Test
        @DisplayName("No debería ser igual a producto con ID diferente")
        void noDeberiaSerIgualAProductoConIdDiferente() {
            Producto otroProducto = new Producto();
            otroProducto.setId(999L);
            otroProducto.setNombre(producto.getNombre());
            otroProducto.setPrecio(producto.getPrecio());
            
            assertNotEquals(producto, otroProducto);
        }
    }

    @Nested
    @DisplayName("ToString Test")
    class ToStringTest {

        @Test
        @DisplayName("Debería generar toString correctamente")
        void deberiaGenerarToStringCorrectamente() {
            String toString = producto.toString();
            
            assertNotNull(toString);
            assertTrue(toString.contains("Producto"));
            assertTrue(toString.contains("id=" + producto.getId()));
            assertTrue(toString.contains("nombre=" + producto.getNombre()));
            assertTrue(toString.contains("precio=" + producto.getPrecio()));
        }
    }
}