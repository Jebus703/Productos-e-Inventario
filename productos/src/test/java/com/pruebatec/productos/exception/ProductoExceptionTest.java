package com.pruebatec.productos.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoException Tests")
class ProductoExceptionTest {

    @Nested
    @DisplayName("Constructor Tests - Not Found")
    class ConstructorNotFoundTests {

        @Test
        @DisplayName("Debería crear excepción para producto no encontrado con ID")
        void deberiaCrearExcepcionParaProductoNoEncontradoConId() {
            // Arrange
            Long productoId = 123L;
            
            // Act
            ProductoException exception = new ProductoException(productoId);
            
            // Assert
            assertEquals(productoId, exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.NOT_FOUND, exception.getTipoError());
            assertEquals("Producto con ID 123 no fue encontrado", exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción para producto no encontrado con ID null")
        void deberiaCrearExcepcionParaProductoNoEncontradoConIdNull() {
            // Arrange
            Long productoId = null;
            
            // Act
            ProductoException exception = new ProductoException(productoId);
            
            // Assert
            assertNull(exception.getProductoId());
            assertEquals(ProductoException.TipoError.NOT_FOUND, exception.getTipoError());
            assertEquals("Producto con ID null no fue encontrado", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor Tests - Duplicado")
    class ConstructorDuplicadoTests {

        @Test
        @DisplayName("Debería crear excepción para producto duplicado")
        void deberiaCrearExcepcionParaProductoDuplicado() {
            // Arrange
            String nombreProducto = "Laptop Dell";
            
            // Act
            ProductoException exception = new ProductoException(nombreProducto, true);
            
            // Assert
            assertNull(exception.getProductoId());
            assertEquals(nombreProducto, exception.getNombreProducto());
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.DUPLICADO, exception.getTipoError());
            assertEquals("Ya existe un producto con el nombre: Laptop Dell", exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción para producto duplicado con nombre null")
        void deberiaCrearExcepcionParaProductoDuplicadoConNombreNull() {
            // Arrange
            String nombreProducto = null;
            
            // Act
            ProductoException exception = new ProductoException(nombreProducto, true);
            
            // Assert
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertEquals(ProductoException.TipoError.DUPLICADO, exception.getTipoError());
            assertEquals("Ya existe un producto con el nombre: null", exception.getMessage());
        }

        @Test
        @DisplayName("Debería usar flag isDuplicado correctamente")
        void deberiaUsarFlagIsDuplicadoCorrectamente() {
            // Arrange
            String nombreProducto = "Producto Test";
            
            // Act
            ProductoException exception = new ProductoException(nombreProducto, false);
            
            // Assert - El flag no afecta el tipo de error, siempre es DUPLICADO
            assertEquals(ProductoException.TipoError.DUPLICADO, exception.getTipoError());
        }
    }

    @Nested
    @DisplayName("Constructor Tests - Validation Error")
    class ConstructorValidationErrorTests {

        @Test
        @DisplayName("Debería crear excepción de validación con campo y valor")
        void deberiaCrearExcepcionDeValidacionConCampoYValor() {
            // Arrange
            String campo = "precio";
            Object valor = -100.0;
            String mensaje = "El precio no puede ser negativo";
            
            // Act
            ProductoException exception = new ProductoException(campo, valor, mensaje);
            
            // Assert
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertEquals(campo, exception.getCampo());
            assertEquals(valor, exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción de validación con valores null")
        void deberiaCrearExcepcionDeValidacionConValoresNull() {
            // Arrange
            String campo = null;
            Object valor = null;
            String mensaje = "Error de validación genérico";
            
            // Act
            ProductoException exception = new ProductoException(campo, valor, mensaje);
            
            // Assert
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor Tests - General Error")
    class ConstructorGeneralErrorTests {

        @Test
        @DisplayName("Debería crear excepción genérica con mensaje")
        void deberiaCrearExcepcionGenericaConMensaje() {
            // Arrange
            String mensaje = "Error general en el procesamiento";
            
            // Act
            ProductoException exception = new ProductoException(mensaje);
            
            // Assert
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.GENERAL_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción genérica con mensaje null")
        void deberiaCrearExcepcionGenericaConMensajeNull() {
            // Arrange
            String mensaje = null;
            
            // Act
            ProductoException exception = new ProductoException(mensaje);
            
            // Assert
            assertEquals(ProductoException.TipoError.GENERAL_ERROR, exception.getTipoError());
            assertNull(exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor Tests - Con Tipo Específico")
    class ConstructorConTipoEspecificoTests {

        @Test
        @DisplayName("Debería crear excepción con tipo específico NOT_FOUND")
        void deberiaCrearExcepcionConTipoEspecificoNotFound() {
            // Arrange
            String mensaje = "Producto no encontrado por criterio específico";
            ProductoException.TipoError tipo = ProductoException.TipoError.NOT_FOUND;
            
            // Act
            ProductoException exception = new ProductoException(mensaje, tipo);
            
            // Assert
            assertEquals(tipo, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
        }

        @Test
        @DisplayName("Debería crear excepción con tipo específico VALIDATION_ERROR")
        void deberiaCrearExcepcionConTipoEspecificoValidationError() {
            // Arrange
            String mensaje = "Error de validación específico";
            ProductoException.TipoError tipo = ProductoException.TipoError.VALIDATION_ERROR;
            
            // Act
            ProductoException exception = new ProductoException(mensaje, tipo);
            
            // Assert
            assertEquals(tipo, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción con tipo específico DUPLICADO")
        void deberiaCrearExcepcionConTipoEspecificoDuplicado() {
            // Arrange
            String mensaje = "Duplicado detectado";
            ProductoException.TipoError tipo = ProductoException.TipoError.DUPLICADO;
            
            // Act
            ProductoException exception = new ProductoException(mensaje, tipo);
            
            // Assert
            assertEquals(tipo, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debería crear excepción con tipo específico GENERAL_ERROR")
        void deberiaCrearExcepcionConTipoEspecificoGeneralError() {
            // Arrange
            String mensaje = "Error general específico";
            ProductoException.TipoError tipo = ProductoException.TipoError.GENERAL_ERROR;
            
            // Act
            ProductoException exception = new ProductoException(mensaje, tipo);
            
            // Assert
            assertEquals(tipo, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Methods Tests")
    class StaticFactoryMethodsTests {

        @Test
        @DisplayName("noEncontrado() debería crear excepción NOT_FOUND")
        void noEncontradoDeberiaCrearExcepcionNotFound() {
            // Arrange
            Long id = 456L;
            
            // Act
            ProductoException exception = ProductoException.noEncontrado(id);
            
            // Assert
            assertEquals(id, exception.getProductoId());
            assertEquals(ProductoException.TipoError.NOT_FOUND, exception.getTipoError());
            assertEquals("Producto con ID 456 no fue encontrado", exception.getMessage());
        }

        @Test
        @DisplayName("noEncontrado() debería manejar ID null")
        void noEncontradoDeberiaManejarIdNull() {
            // Arrange
            Long id = null;
            
            // Act
            ProductoException exception = ProductoException.noEncontrado(id);
            
            // Assert
            assertNull(exception.getProductoId());
            assertEquals(ProductoException.TipoError.NOT_FOUND, exception.getTipoError());
        }

        @Test
        @DisplayName("duplicado() debería crear excepción DUPLICADO")
        void duplicadoDeberiaCrearExcepcionDuplicado() {
            // Arrange
            String nombre = "Producto Duplicado";
            
            // Act
            ProductoException exception = ProductoException.duplicado(nombre);
            
            // Assert
            assertEquals(nombre, exception.getNombreProducto());
            assertEquals(ProductoException.TipoError.DUPLICADO, exception.getTipoError());
            assertEquals("Ya existe un producto con el nombre: Producto Duplicado", exception.getMessage());
        }

        @Test
        @DisplayName("duplicado() debería manejar nombre null")
        void duplicadoDeberiaManejarNombreNull() {
            // Arrange
            String nombre = null;
            
            // Act
            ProductoException exception = ProductoException.duplicado(nombre);
            
            // Assert
            assertNull(exception.getNombreProducto());
            assertEquals(ProductoException.TipoError.DUPLICADO, exception.getTipoError());
        }

        @Test
        @DisplayName("validacion() con campo y valor debería crear excepción VALIDATION_ERROR")
        void validacionConCampoYValorDeberiaCrearExcepcionValidationError() {
            // Arrange
            String campo = "stock";
            Object valor = -5;
            String mensaje = "El stock no puede ser negativo";
            
            // Act
            ProductoException exception = ProductoException.validacion(campo, valor, mensaje);
            
            // Assert
            assertEquals(campo, exception.getCampo());
            assertEquals(valor, exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("validacion() con solo mensaje debería crear excepción VALIDATION_ERROR")
        void validacionConSoloMensajeDeberiaCrearExcepcionValidationError() {
            // Arrange
            String mensaje = "Error de validación genérico";
            
            // Act
            ProductoException exception = ProductoException.validacion(mensaje);
            
            // Assert
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Enum TipoError Tests")
    class EnumTipoErrorTests {

        @Test
        @DisplayName("Debería tener todos los tipos de error definidos")
        void deberiaTenerTodosLosTiposDeErrorDefinidos() {
            // Act & Assert
            assertEquals(4, ProductoException.TipoError.values().length);
            
            // Verificar que todos los tipos existen
            assertNotNull(ProductoException.TipoError.NOT_FOUND);
            assertNotNull(ProductoException.TipoError.DUPLICADO);
            assertNotNull(ProductoException.TipoError.VALIDATION_ERROR);
            assertNotNull(ProductoException.TipoError.GENERAL_ERROR);
        }

        @Test
        @DisplayName("Debería poder convertir enum a string")
        void deberiaPoderConvertirEnumAString() {
            // Act & Assert
            assertEquals("NOT_FOUND", ProductoException.TipoError.NOT_FOUND.toString());
            assertEquals("DUPLICADO", ProductoException.TipoError.DUPLICADO.toString());
            assertEquals("VALIDATION_ERROR", ProductoException.TipoError.VALIDATION_ERROR.toString());
            assertEquals("GENERAL_ERROR", ProductoException.TipoError.GENERAL_ERROR.toString());
        }

        @Test
        @DisplayName("Debería poder obtener enum por nombre")
        void deberiaPoderObtenerEnumPorNombre() {
            // Act & Assert
            assertEquals(ProductoException.TipoError.NOT_FOUND, 
                        ProductoException.TipoError.valueOf("NOT_FOUND"));
            assertEquals(ProductoException.TipoError.DUPLICADO, 
                        ProductoException.TipoError.valueOf("DUPLICADO"));
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, 
                        ProductoException.TipoError.valueOf("VALIDATION_ERROR"));
            assertEquals(ProductoException.TipoError.GENERAL_ERROR, 
                        ProductoException.TipoError.valueOf("GENERAL_ERROR"));
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("Debería retornar todos los valores de getters correctamente")
        void deberiaRetornarTodosLosValoresDeGettersCorrectamente() {
            // Arrange
            String campo = "nombre";
            Object valor = "Test Product";
            String mensaje = "Error de validación de nombre";
            
            // Act
            ProductoException exception = new ProductoException(campo, valor, mensaje);
            
            // Assert - verificar todos los getters
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertEquals(campo, exception.getCampo());
            assertEquals(valor, exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debería heredar getMessage() de RuntimeException")
        void deberiaHeredarGetMessageDeRuntimeException() {
            // Arrange
            String mensaje = "Mensaje de prueba";
            
            // Act
            ProductoException exception = new ProductoException(mensaje);
            
            // Assert
            assertEquals(mensaje, exception.getMessage());
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debería manejar todos los campos null")
        void deberiaManejarTodosLosCamposNull() {
            // Arrange & Act
            ProductoException exception = new ProductoException(null, null, null);
            
            // Assert
            assertNull(exception.getProductoId());
            assertNull(exception.getNombreProducto());
            assertNull(exception.getCampo());
            assertNull(exception.getValor());
            assertEquals(ProductoException.TipoError.VALIDATION_ERROR, exception.getTipoError());
            assertNull(exception.getMessage());
        }

        @Test
        @DisplayName("Debería manejar valores de diferentes tipos")
        void deberiaManejarValoresDeDiferentesTipos() {
            // Test con Integer
            ProductoException ex1 = new ProductoException("campo1", 123, "mensaje1");
            assertEquals(123, ex1.getValor());
            
            // Test con Double
            ProductoException ex2 = new ProductoException("campo2", 45.67, "mensaje2");
            assertEquals(45.67, ex2.getValor());
            
            // Test con String
            ProductoException ex3 = new ProductoException("campo3", "valor_string", "mensaje3");
            assertEquals("valor_string", ex3.getValor());
            
            // Test con Boolean
            ProductoException ex4 = new ProductoException("campo4", true, "mensaje4");
            assertEquals(true, ex4.getValor());
        }

        @Test
        @DisplayName("Debería manejar mensajes largos")
        void deberiaManejarMensajesLargos() {
            // Arrange
            String mensajeLargo = "Este es un mensaje muy largo que contiene muchos caracteres para probar el manejo de strings extensos en la excepción ProductoException y verificar que no hay limitaciones en la longitud del mensaje de error que se puede almacenar";
            
            // Act
            ProductoException exception = new ProductoException(mensajeLargo);
            
            // Assert
            assertEquals(mensajeLargo, exception.getMessage());
            assertEquals(ProductoException.TipoError.GENERAL_ERROR, exception.getTipoError());
        }
    }
}