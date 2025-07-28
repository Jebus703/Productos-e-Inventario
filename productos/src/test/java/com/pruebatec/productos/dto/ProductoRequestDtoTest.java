package com.pruebatec.productos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoRequestDto Tests")
class ProductoRequestDtoTest {

    private Validator validator;
    private ProductoRequestDto dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        dto = new ProductoRequestDto();
    }

    @Nested
    @DisplayName("Constructor y Getters/Setters Tests")
    class ConstructorAndAccessorsTests {

        @Test
        @DisplayName("Debería crear instancia con constructor por defecto")
        void deberiaCrearInstanciaConConstructorPorDefecto() {
            // Act
            ProductoRequestDto nuevoDto = new ProductoRequestDto();
            
            // Assert
            assertNotNull(nuevoDto);
            assertNull(nuevoDto.getNombre());
            assertNull(nuevoDto.getPrecio());
            assertNull(nuevoDto.getDescripcion());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener nombre")
        void deberiaPermitirEstablecerYObtenerNombre() {
            // Arrange
            String nombre = "iPhone 15 Pro";
            
            // Act
            dto.setNombre(nombre);
            
            // Assert
            assertEquals(nombre, dto.getNombre());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener precio")
        void deberiaPermitirEstablecerYObtenerPrecio() {
            // Arrange
            BigDecimal precio = new BigDecimal("1299.99");
            
            // Act
            dto.setPrecio(precio);
            
            // Assert
            assertEquals(precio, dto.getPrecio());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener descripción")
        void deberiaPermitirEstablecerYObtenerDescripcion() {
            // Arrange
            String descripcion = "El último iPhone con chip A17 Pro";
            
            // Act
            dto.setDescripcion(descripcion);
            
            // Assert
            assertEquals(descripcion, dto.getDescripcion());
        }
    }

    @Nested
    @DisplayName("Validación de Nombre Tests")
    class ValidacionNombreTests {

        @Test
        @DisplayName("Debería ser válido con nombre correcto")
        void deberiaSerValidoConNombreCorrecto() {
            // Arrange
            dto.setNombre("iPhone 15 Pro");
            dto.setPrecio(new BigDecimal("1299.99"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre es null")
        void deberiaFallarValidacionCuandoNombreEsNull() {
            // Arrange
            dto.setNombre(null);
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre está vacío")
        void deberiaFallarValidacionCuandoNombreEstaVacio() {
            // Arrange
            dto.setNombre("");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre solo tiene espacios")
        void deberiaFallarValidacionCuandoNombreSoloTieneEspacios() {
            // Arrange
            dto.setNombre("   ");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando nombre excede 255 caracteres")
        void deberiaFallarValidacionCuandoNombreExcede255Caracteres() {
            // Arrange
            String nombreLargo = "a".repeat(256);
            dto.setNombre(nombreLargo);
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("255 caracteres")));
        }

        @Test
        @DisplayName("Debería ser válido con nombre de exactamente 255 caracteres")
        void deberiaSerValidoConNombreDeExactamente255Caracteres() {
            // Arrange
            String nombreLimite = "a".repeat(255);
            dto.setNombre(nombreLimite);
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con nombre de un solo carácter")
        void deberiaSerValidoConNombreDeUnSoloCaracter() {
            // Arrange
            dto.setNombre("A");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Validación de Precio Tests")
    class ValidacionPrecioTests {

        @Test
        @DisplayName("Debería ser válido con precio correcto")
        void deberiaSerValidoConPrecioCorrecto() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("99.99"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es null")
        void deberiaFallarValidacionCuandoPrecioEsNull() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(null);
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obligatorio")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es cero")
        void deberiaFallarValidacionCuandoPrecioEsCero() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(BigDecimal.ZERO);
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("mayor a 0")));
        }

        @Test
        @DisplayName("Debería fallar validación cuando precio es negativo")
        void deberiaFallarValidacionCuandoPrecioEsNegativo() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("-10.00"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("mayor a 0")));
        }

        @Test
        @DisplayName("Debería ser válido con precio mínimo permitido")
        void deberiaSerValidoConPrecioMinimoPermitido() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("0.01"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con precio con muchos decimales")
        void deberiaSerValidoConPrecioConMuchosDecimales() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("99.999999"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con precio entero")
        void deberiaSerValidoConPrecioEntero() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("100"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Validación de Descripción Tests")
    class ValidacionDescripcionTests {

        @Test
        @DisplayName("Debería ser válido con descripción null")
        void deberiaSerValidoConDescripcionNull() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("100.00"));
            dto.setDescripcion(null);
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con descripción vacía")
        void deberiaSerValidoConDescripcionVacia() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("100.00"));
            dto.setDescripcion("");
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con descripción normal")
        void deberiaSerValidoConDescripcionNormal() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("100.00"));
            dto.setDescripcion("Esta es una descripción válida del producto");
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería ser válido con descripción larga")
        void deberiaSerValidoConDescripcionLarga() {
            // Arrange
            dto.setNombre("Producto Test");
            dto.setPrecio(new BigDecimal("100.00"));
            dto.setDescripcion("Esta es una descripción muy larga que contiene muchos detalles sobre el producto ".repeat(10));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Casos Extremos Tests")
    class CasosExtremosTests {

        @Test
        @DisplayName("Debería fallar validación con múltiples errores")
        void deberiaFallarValidacionConMultiplesErrores() {
            // Arrange - DTO completamente inválido
            dto.setNombre(null);
            dto.setPrecio(null);
            dto.setDescripcion("Descripción válida");
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertEquals(2, violations.size()); // nombre y precio
        }

        @Test
        @DisplayName("Debería ser válido con datos mínimos requeridos")
        void deberiaSerValidoConDatosMimimosRequeridos() {
            // Arrange
            dto.setNombre("A");
            dto.setPrecio(new BigDecimal("0.01"));
            // descripción se queda null
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería manejar nombres con caracteres especiales")
        void deberiaManejarNombresConCaracteresEspeciales() {
            // Arrange
            dto.setNombre("Café \"Premium\" 100% (Orgánico) - #1 ¡Excelente!");
            dto.setPrecio(new BigDecimal("25.99"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Debería manejar precios con precisión alta")
        void deberiaManejarPreciosConPrecisionAlta() {
            // Arrange
            dto.setNombre("Producto Precisión");
            dto.setPrecio(new BigDecimal("99.123456789"));
            
            // Act
            Set<ConstraintViolation<ProductoRequestDto>> violations = validator.validate(dto);
            
            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Debería ser igual a sí mismo")
        void deberiaSerIgualASiMismo() {
            // Arrange
            dto.setNombre("Test");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act & Assert
            assertEquals(dto, dto);
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        @DisplayName("Debería ser igual a otro DTO con los mismos valores")
        void deberiaSerIgualAOtroDtoConLosMismosValores() {
            // Arrange
            dto.setNombre("Test");
            dto.setPrecio(new BigDecimal("100.00"));
            dto.setDescripcion("Descripción");
            
            ProductoRequestDto otroDto = new ProductoRequestDto();
            otroDto.setNombre("Test");
            otroDto.setPrecio(new BigDecimal("100.00"));
            otroDto.setDescripcion("Descripción");
            
            // Act & Assert
            assertEquals(dto, otroDto);
            assertEquals(dto.hashCode(), otroDto.hashCode());
        }

        @Test
        @DisplayName("No debería ser igual a null")
        void noDeberiaSerIgualANull() {
            // Arrange
            dto.setNombre("Test");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act & Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("No debería ser igual a objeto de diferente clase")
        void noDeberiaSerIgualAObjetoDeDiferenteClase() {
            // Arrange
            dto.setNombre("Test");
            dto.setPrecio(new BigDecimal("100.00"));
            
            // Act & Assert
            assertNotEquals(dto, "String diferente");
        }

        @Test
        @DisplayName("No debería ser igual cuando nombre es diferente")
        void noDeberiaSerIgualCuandoNombreEsDiferente() {
            // Arrange
            dto.setNombre("Test1");
            dto.setPrecio(new BigDecimal("100.00"));
            
            ProductoRequestDto otroDto = new ProductoRequestDto();
            otroDto.setNombre("Test2");
            otroDto.setPrecio(new BigDecimal("100.00"));
            
            // Act & Assert
            assertNotEquals(dto, otroDto);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Debería generar toString con todos los campos")
        void deberiaGenerarToStringConTodosLosCampos() {
            // Arrange
            dto.setNombre("iPhone 15 Pro");
            dto.setPrecio(new BigDecimal("1299.99"));
            dto.setDescripcion("El último iPhone");
            
            // Act
            String resultado = dto.toString();
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("iPhone 15 Pro"));
            assertTrue(resultado.contains("1299.99"));
            assertTrue(resultado.contains("El último iPhone"));
        }

        @Test
        @DisplayName("Debería generar toString con campos null")
        void deberiaGenerarToStringConCamposNull() {
            // Arrange - DTO con valores null
            
            // Act
            String resultado = dto.toString();
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("ProductoRequestDto"));
        }
    }
}