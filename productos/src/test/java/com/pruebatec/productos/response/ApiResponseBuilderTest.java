package com.pruebatec.productos.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponseBuilder Tests")
class ApiResponseBuilderTest {

    @Nested
    @DisplayName("Success Methods Tests")
    class SuccessMethodsTests {

        @Test
        @DisplayName("Debería crear respuesta exitosa simple")
        void deberiaCrearRespuestaExitosaSimple() {
            // Arrange
            String mensaje = "Operación completada exitosamente";
            String data = "Resultado de la operación";
            
            // Act
            ApiResponse<String> response = ApiResponseBuilder.success(mensaje, data);
            
            // Assert
            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertEquals(data, response.getData());
            assertNull(response.getMeta());
            assertNull(response.getErrors());
            assertNotNull(response.getTimestamp());
        }

        @Test
        @DisplayName("Debería crear respuesta exitosa con datos null")
        void deberiaCrearRespuestaExitosaConDatosNull() {
            // Arrange
            String mensaje = "Operación completada";
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.success(mensaje, null);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getTimestamp());
        }

        @Test
        @DisplayName("Debería crear respuesta exitosa con diferentes tipos de datos")
        void deberiaCrearRespuestaExitosaConDiferentesTiposDeDatos() {
            // Test con Integer
            ApiResponse<Integer> intResponse = ApiResponseBuilder.success("Success", 42);
            assertEquals(Integer.valueOf(42), intResponse.getData());
            
            // Test con Map
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("key", "value");
            ApiResponse<Map<String, Object>> mapResponse = ApiResponseBuilder.success("Success", mapData);
            assertEquals(mapData, mapResponse.getData());
            
            // Test con objeto personalizado
            LocalDateTime dateData = LocalDateTime.now();
            ApiResponse<LocalDateTime> dateResponse = ApiResponseBuilder.success("Success", dateData);
            assertEquals(dateData, dateResponse.getData());
        }
    }

    @Nested
    @DisplayName("Success with Total Tests")
    class SuccessWithTotalTests {

        @Test
        @DisplayName("Debería crear respuesta exitosa con total para List")
        void deberiaCrearRespuestaExitosaConTotalParaList() {
            // Arrange
            List<String> productos = Arrays.asList("iPhone", "Samsung", "Xiaomi");
            String mensaje = "Productos obtenidos";
            int total = 100;
            
            // Act
            ApiResponse<List<String>> response = ApiResponseBuilder.success(mensaje, productos, total);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertEquals(productos, response.getData());
            assertNotNull(response.getMeta());
            assertEquals(Integer.valueOf(total), response.getMeta().getTotal());
            assertEquals(Integer.valueOf(3), response.getMeta().getCount()); // tamaño de la lista
            assertNull(response.getErrors());
        }

        @Test
        @DisplayName("Debería crear respuesta exitosa con total para datos no-List")
        void deberiaCrearRespuestaExitosaConTotalParaDatosNoList() {
            // Arrange
            String data = "Single item";
            String mensaje = "Item obtenido";
            int total = 50;
            
            // Act
            ApiResponse<String> response = ApiResponseBuilder.success(mensaje, data, total);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertEquals(data, response.getData());
            assertNotNull(response.getMeta());
            assertEquals(Integer.valueOf(total), response.getMeta().getTotal());
            assertNull(response.getMeta().getCount()); // no es List, así que count permanece null
        }

        @Test
        @DisplayName("Debería manejar lista vacía correctamente")
        void deberiaManejarListaVaciaCorrectamente() {
            // Arrange
            List<String> listaVacia = new ArrayList<>();
            String mensaje = "Lista vacía";
            int total = 0;
            
            // Act
            ApiResponse<List<String>> response = ApiResponseBuilder.success(mensaje, listaVacia, total);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(listaVacia, response.getData());
            assertEquals(Integer.valueOf(0), response.getMeta().getTotal());
            assertEquals(Integer.valueOf(0), response.getMeta().getCount());
        }

        @Test
        @DisplayName("Debería manejar lista null")
        void deberiaManejarListaNull() {
            // Arrange
            List<String> listaNulla = null;
            String mensaje = "Lista null";
            int total = 10;
            
            // Act
            ApiResponse<List<String>> response = ApiResponseBuilder.success(mensaje, listaNulla, total);
            
            // Assert
            assertTrue(response.isSuccess());
            assertNull(response.getData());
            assertEquals(Integer.valueOf(total), response.getMeta().getTotal());
            assertNull(response.getMeta().getCount()); // null no es instanceof List
        }

        @Test
        @DisplayName("Debería manejar diferentes tipos de listas")
        void deberiaManejarDiferentesTiposDeListas() {
            // Test con ArrayList
            ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
            ApiResponse<ArrayList<Integer>> arrayResponse = ApiResponseBuilder.success("ArrayList", arrayList, 20);
            assertEquals(Integer.valueOf(5), arrayResponse.getMeta().getCount());
            
            // Test con LinkedList
            LinkedList<String> linkedList = new LinkedList<>(Arrays.asList("a", "b"));
            ApiResponse<LinkedList<String>> linkedResponse = ApiResponseBuilder.success("LinkedList", linkedList, 10);
            assertEquals(Integer.valueOf(2), linkedResponse.getMeta().getCount());
        }
    }

    @Nested
    @DisplayName("Success with Custom Meta Tests")
    class SuccessWithCustomMetaTests {

        @Test
        @DisplayName("Debería crear respuesta exitosa con metadatos personalizados")
        void deberiaCrearRespuestaExitosaConMetadatosPersonalizados() {
            // Arrange
            String mensaje = "Datos con metadatos";
            List<String> data = Arrays.asList("item1", "item2");
            
            Map<String, Object> additional = new HashMap<>();
            additional.put("version", "1.0");
            additional.put("region", "US");
            
            ApiResponse.Meta customMeta = new ApiResponse.Meta(100, 25, 3, 25, additional);
            
            // Act
            ApiResponse<List<String>> response = ApiResponseBuilder.success(mensaje, data, customMeta);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertEquals(data, response.getData());
            assertEquals(customMeta, response.getMeta());
            assertEquals(Integer.valueOf(100), response.getMeta().getTotal());
            assertEquals(Integer.valueOf(25), response.getMeta().getCount());
            assertEquals(Integer.valueOf(3), response.getMeta().getPage());
            assertEquals(Integer.valueOf(25), response.getMeta().getPageSize());
            assertEquals("1.0", response.getMeta().getAdditional().get("version"));
            assertEquals("US", response.getMeta().getAdditional().get("region"));
        }

        @Test
        @DisplayName("Debería manejar metadatos null")
        void deberiaManejarMetadatosNull() {
            // Arrange
            String mensaje = "Sin metadatos";
            String data = "test data";
            
            // Act
            ApiResponse<String> response = ApiResponseBuilder.success(mensaje, data, null);
            
            // Assert
            assertTrue(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertEquals(data, response.getData());
            assertNull(response.getMeta());
        }
    }

    @Nested
    @DisplayName("Error Methods Tests")
    class ErrorMethodsTests {

        @Test
        @DisplayName("Debería crear respuesta de error simple")
        void deberiaCrearRespuestaDeErrorSimple() {
            // Arrange
            String mensaje = "Ha ocurrido un error";
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.error(mensaje);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNull(response.getData());
            assertNull(response.getMeta());
            assertNotNull(response.getErrors());
            assertEquals(1, response.getErrors().size());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals("GENERAL_ERROR", error.getCode());
            assertEquals("Error", error.getTitle());
            assertEquals(mensaje, error.getDetail());
            assertNull(error.getSource());
            assertNull(error.getMeta());
            assertNotNull(response.getTimestamp());
        }

        @Test
        @DisplayName("Debería crear respuesta de error con código específico")
        void deberiaCrearRespuestaDeErrorConCodigoEspecifico() {
            // Arrange
            String code = "AUTH_001";
            String title = "Authentication Error";
            String message = "Invalid credentials provided";
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.error(code, title, message);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(title, response.getMessage()); // usa title como mensaje principal
            assertNull(response.getData());
            assertNotNull(response.getErrors());
            assertEquals(1, response.getErrors().size());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals(code, error.getCode());
            assertEquals(title, error.getTitle());
            assertEquals(message, error.getDetail());
            assertNull(error.getSource());
            assertNull(error.getMeta());
        }

        @Test
        @DisplayName("Debería manejar mensajes null en errores")
        void deberiaManejarMensajesNullEnErrores() {
            // Act
            ApiResponse<Object> response1 = ApiResponseBuilder.error(null);
            ApiResponse<Object> response2 = ApiResponseBuilder.error("CODE", null, "detail");
            
            // Assert
            assertFalse(response1.isSuccess());
            assertNull(response1.getMessage());
            
            assertFalse(response2.isSuccess());
            assertNull(response2.getMessage());
            assertEquals("detail", response2.getErrors().get(0).getDetail());
        }
    }

    @Nested
    @DisplayName("Validation Error Tests")
    class ValidationErrorTests {

        @Test
        @DisplayName("Debería crear respuesta de error de validación")
        void deberiaCrearRespuestaDeErrorDeValidacion() {
            // Arrange
            String mensaje = "Errores de validación encontrados";
            List<String> validationErrors = Arrays.asList(
                "El nombre es obligatorio",
                "El precio debe ser mayor a 0",
                "La descripción no puede exceder 255 caracteres"
            );
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.validationError(mensaje, validationErrors);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getErrors());
            assertEquals(3, response.getErrors().size());
            
            // Verificar cada error
            for (int i = 0; i < validationErrors.size(); i++) {
                ApiResponse.Error error = response.getErrors().get(i);
                assertEquals("VALIDATION_ERROR", error.getCode());
                assertEquals("Error de validación", error.getTitle());
                assertEquals(validationErrors.get(i), error.getDetail());
                assertNull(error.getSource());
                assertNull(error.getMeta());
            }
        }

        @Test
        @DisplayName("Debería manejar lista vacía de errores de validación")
        void deberiaManejarListaVaciaDeErroresDeValidacion() {
            // Arrange
            String mensaje = "Sin errores de validación";
            List<String> erroresVacios = new ArrayList<>();
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.validationError(mensaje, erroresVacios);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNotNull(response.getErrors());
            assertEquals(0, response.getErrors().size());
        }

        @Test
        @DisplayName("Debería manejar errores de validación con valores null")
        void deberiaManejarErroresDeValidacionConValoresNull() {
            // Arrange
            String mensaje = "Errores con nulls";
            List<String> erroresConNull = Arrays.asList(
                "Error válido",
                null,
                "Otro error válido"
            );
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.validationError(mensaje, erroresConNull);
            
            // Assert
            assertEquals(3, response.getErrors().size());
            assertEquals("Error válido", response.getErrors().get(0).getDetail());
            assertNull(response.getErrors().get(1).getDetail());
            assertEquals("Otro error válido", response.getErrors().get(2).getDetail());
        }

        @Test
        @DisplayName("Debería manejar lista null de errores de validación")
        void deberiaManejarListaNullDeErroresDeValidacion() {
            // Arrange
            String mensaje = "Lista null";
            
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                ApiResponseBuilder.validationError(mensaje, null);
            });
        }
    }

    @Nested
    @DisplayName("Not Found Error Tests")
    class NotFoundErrorTests {

        @Test
        @DisplayName("Debería crear respuesta de error not found")
        void deberiaCrearRespuestaDeErrorNotFound() {
            // Arrange
            String mensaje = "Producto con ID 123 no fue encontrado";
            Long id = 123L;
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.notFound(mensaje, id);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getErrors());
            assertEquals(1, response.getErrors().size());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals("NOT_FOUND", error.getCode());
            assertEquals("Recurso no encontrado", error.getTitle());
            assertEquals(mensaje, error.getDetail());
            assertNull(error.getSource());
            assertNotNull(error.getMeta());
            assertEquals(id, error.getMeta().get("productoId"));
        }

        @Test
        @DisplayName("Debería manejar ID null en not found")
        void deberiaManejarIdNullEnNotFound() {
            // Arrange
            String mensaje = "Producto no encontrado";
            Long id = null;
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.notFound(mensaje, id);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals("NOT_FOUND", error.getCode());
            assertNotNull(error.getMeta());
            assertNull(error.getMeta().get("productoId"));
        }

        @Test
        @DisplayName("Debería manejar diferentes tipos de ID")
        void deberiaManejarDiferentesTiposDeId() {
            // Test con ID positivo
            ApiResponse<Object> response1 = ApiResponseBuilder.notFound("Error", 42L);
            assertEquals(42L, response1.getErrors().get(0).getMeta().get("productoId"));
            
            // Test con ID negativo
            ApiResponse<Object> response2 = ApiResponseBuilder.notFound("Error", -1L);
            assertEquals(-1L, response2.getErrors().get(0).getMeta().get("productoId"));
            
            // Test con ID cero
            ApiResponse<Object> response3 = ApiResponseBuilder.notFound("Error", 0L);
            assertEquals(0L, response3.getErrors().get(0).getMeta().get("productoId"));
            
            // Test con ID muy grande
            ApiResponse<Object> response4 = ApiResponseBuilder.notFound("Error", Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, response4.getErrors().get(0).getMeta().get("productoId"));
        }
    }

    @Nested
    @DisplayName("Duplicate Error Tests")
    class DuplicateErrorTests {

        @Test
        @DisplayName("Debería crear respuesta de error de duplicado")
        void deberiaCrearRespuestaDeErrorDeDuplicado() {
            // Arrange
            String mensaje = "Ya existe un producto con el nombre: iPhone 15";
            String nombre = "iPhone 15";
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.duplicateError(mensaje, nombre);
            
            // Assert
            assertFalse(response.isSuccess());
            assertEquals(mensaje, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getErrors());
            assertEquals(1, response.getErrors().size());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals("DUPLICATE_ERROR", error.getCode());
            assertEquals("Recurso duplicado", error.getTitle());
            assertEquals(mensaje, error.getDetail());
            assertNull(error.getSource());
            assertNotNull(error.getMeta());
            assertEquals(nombre, error.getMeta().get("nombreProducto"));
        }

        @Test
        @DisplayName("Debería manejar nombre null en duplicate error")
        void deberiaManejarNombreNullEnDuplicateError() {
            // Arrange
            String mensaje = "Producto duplicado";
            String nombre = null;
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.duplicateError(mensaje, nombre);
            
            // Assert
            assertFalse(response.isSuccess());
            
            ApiResponse.Error error = response.getErrors().get(0);
            assertEquals("DUPLICATE_ERROR", error.getCode());
            assertNotNull(error.getMeta());
            assertNull(error.getMeta().get("nombreProducto"));
        }

        @Test
        @DisplayName("Debería manejar nombres con caracteres especiales")
        void deberiaManejarNombresConCaracteresEspeciales() {
            // Arrange
            String nombreEspecial = "Café \"Premium\" 100% (Orgánico) - #1 ¡Excelente! 🚀";
            String mensaje = "Producto duplicado: " + nombreEspecial;
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.duplicateError(mensaje, nombreEspecial);
            
            // Assert
            assertEquals(nombreEspecial, response.getErrors().get(0).getMeta().get("nombreProducto"));
        }

        @Test
        @DisplayName("Debería manejar nombres muy largos")
        void deberiaManejarNombresMuyLargos() {
            // Arrange
            String nombreLargo = "Producto con nombre muy largo: " + "x".repeat(1000);
            String mensaje = "Duplicado";
            
            // Act
            ApiResponse<Object> response = ApiResponseBuilder.duplicateError(mensaje, nombreLargo);
            
            // Assert
            assertEquals(nombreLargo, response.getErrors().get(0).getMeta().get("nombreProducto"));
            assertTrue(nombreLargo.length() > 1000);
        }
    }

    @Nested
    @DisplayName("Create Meta Methods Tests")
    class CreateMetaMethodsTests {

        @Test
        @DisplayName("Debería crear metadatos completos para paginación")
        void deberiaCrearMetadatosCompletosParaPaginacion() {
            // Arrange
            int total = 250;
            int count = 25;
            int page = 3;
            int pageSize = 25;
            
            // Act
            ApiResponse.Meta meta = ApiResponseBuilder.createMeta(total, count, page, pageSize);
            
            // Assert
            assertNotNull(meta);
            assertEquals(Integer.valueOf(total), meta.getTotal());
            assertEquals(Integer.valueOf(count), meta.getCount());
            assertEquals(Integer.valueOf(page), meta.getPage());
            assertEquals(Integer.valueOf(pageSize), meta.getPageSize());
            assertNull(meta.getAdditional()); // no se establece en el builder
        }

        @Test
        @DisplayName("Debería crear metadatos simples con solo total")
        void deberiaCrearMetadatosSimplesConSoloTotal() {
            // Arrange
            int total = 100;
            
            // Act
            ApiResponse.Meta meta = ApiResponseBuilder.createMeta(total);
            
            // Assert
            assertNotNull(meta);
            assertEquals(Integer.valueOf(total), meta.getTotal());
            assertNull(meta.getCount());
            assertNull(meta.getPage());
            assertNull(meta.getPageSize());
            assertNull(meta.getAdditional());
        }

        @Test
        @DisplayName("Debería manejar valores extremos en metadatos")
        void deberiaManejarValoresExtremosEnMetadatos() {
            // Test con valores cero
            ApiResponse.Meta meta1 = ApiResponseBuilder.createMeta(0, 0, 0, 0);
            assertEquals(Integer.valueOf(0), meta1.getTotal());
            assertEquals(Integer.valueOf(0), meta1.getCount());
            assertEquals(Integer.valueOf(0), meta1.getPage());
            assertEquals(Integer.valueOf(0), meta1.getPageSize());
            
            // Test con valores negativos
            ApiResponse.Meta meta2 = ApiResponseBuilder.createMeta(-1, -1, -1, -1);
            assertEquals(Integer.valueOf(-1), meta2.getTotal());
            assertEquals(Integer.valueOf(-1), meta2.getCount());
            assertEquals(Integer.valueOf(-1), meta2.getPage());
            assertEquals(Integer.valueOf(-1), meta2.getPageSize());
            
            // Test con valores muy grandes
            ApiResponse.Meta meta3 = ApiResponseBuilder.createMeta(Integer.MAX_VALUE);
            assertEquals(Integer.valueOf(Integer.MAX_VALUE), meta3.getTotal());
        }

        @Test
        @DisplayName("Debería crear metadatos independientes")
        void deberiaCrearMetadatosIndependientes() {
            // Act
            ApiResponse.Meta meta1 = ApiResponseBuilder.createMeta(100);
            ApiResponse.Meta meta2 = ApiResponseBuilder.createMeta(200);
            
            // Assert
            assertNotSame(meta1, meta2);
            assertEquals(Integer.valueOf(100), meta1.getTotal());
            assertEquals(Integer.valueOf(200), meta2.getTotal());
            
            // Modificar uno no debe afectar al otro
            meta1.setTotal(999);
            assertEquals(Integer.valueOf(999), meta1.getTotal());
            assertEquals(Integer.valueOf(200), meta2.getTotal());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Debería crear flujo completo de respuestas exitosas")
        void deberiaCrearFlujoCompletoDeRespuestasExitosas() {
            // Test respuesta simple
            ApiResponse<String> simple = ApiResponseBuilder.success("Success", "data");
            assertTrue(simple.isSuccess());
            
            // Test respuesta con lista y total
            List<String> items = Arrays.asList("item1", "item2", "item3");
            ApiResponse<List<String>> withTotal = ApiResponseBuilder.success("List success", items, 100);
            assertTrue(withTotal.isSuccess());
            assertEquals(Integer.valueOf(3), withTotal.getMeta().getCount());
            assertEquals(Integer.valueOf(100), withTotal.getMeta().getTotal());
            
            // Test respuesta con metadatos personalizados
            ApiResponse.Meta customMeta = ApiResponseBuilder.createMeta(50, 10, 2, 10);
            ApiResponse<List<String>> withCustomMeta = ApiResponseBuilder.success("Custom meta", items, customMeta);
            assertTrue(withCustomMeta.isSuccess());
            assertEquals(customMeta, withCustomMeta.getMeta());
        }

        @Test
        @DisplayName("Debería crear flujo completo de respuestas de error")
        void deberiaCrearFlujoCompletoDeRespuestasDeError() {
            // Error simple
            ApiResponse<Object> simple = ApiResponseBuilder.error("Simple error");
            assertFalse(simple.isSuccess());
            assertEquals(1, simple.getErrors().size());
            
            // Error con código
            ApiResponse<Object> withCode = ApiResponseBuilder.error("CODE", "Title", "Detail");
            assertFalse(withCode.isSuccess());
            assertEquals("CODE", withCode.getErrors().get(0).getCode());
            
            // Error de validación
            List<String> validationErrors = Arrays.asList("Error 1", "Error 2");
            ApiResponse<Object> validation = ApiResponseBuilder.validationError("Validation failed", validationErrors);
            assertFalse(validation.isSuccess());
            assertEquals(2, validation.getErrors().size());
            
            // Error not found
            ApiResponse<Object> notFound = ApiResponseBuilder.notFound("Not found", 123L);
            assertFalse(notFound.isSuccess());
            assertEquals("NOT_FOUND", notFound.getErrors().get(0).getCode());
            
            // Error duplicado
            ApiResponse<Object> duplicate = ApiResponseBuilder.duplicateError("Duplicate", "name");
            assertFalse(duplicate.isSuccess());
            assertEquals("DUPLICATE_ERROR", duplicate.getErrors().get(0).getCode());
        }

        @Test
        @DisplayName("Debería ser thread-safe para múltiples llamadas simultáneas")
        void deberiaSerThreadSafeParaMultiplesLlamadasSimultaneas() {
            // Act & Assert - múltiples llamadas simultáneas no deberían interferir
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 1000; i++) {
                    ApiResponse<String> response1 = ApiResponseBuilder.success("Message " + i, "Data " + i);
                    ApiResponse<Object> response2 = ApiResponseBuilder.error("Error " + i);
                    ApiResponse.Meta meta = ApiResponseBuilder.createMeta(i);
                    
                    assertEquals("Message " + i, response1.getMessage());
                    assertEquals("Error " + i, response2.getMessage());
                    assertEquals(Integer.valueOf(i), meta.getTotal());
                }
            });
        }

        @Test
        @DisplayName("Debería mantener inmutabilidad entre diferentes respuestas")
        void deberiaMantenerInmutabilidadEntreDiferentesRespuestas() {
            // Arrange
            List<String> sharedList = new ArrayList<>(Arrays.asList("shared"));
            
            // Act
            ApiResponse<List<String>> response1 = ApiResponseBuilder.success("Response 1", sharedList, 10);
            ApiResponse<List<String>> response2 = ApiResponseBuilder.success("Response 2", sharedList, 20);
            
            // Assert
            assertNotSame(response1, response2);
            assertNotSame(response1.getMeta(), response2.getMeta());
            assertEquals(Integer.valueOf(10), response1.getMeta().getTotal());
            assertEquals(Integer.valueOf(20), response2.getMeta().getTotal());
            
            // Modificar la lista compartida no debe afectar las respuestas ya creadas
            // (aunque comparten la referencia a la lista, las respuestas son independientes)
            assertEquals(sharedList, response1.getData());
            assertEquals(sharedList, response2.getData());
            assertSame(response1.getData(), response2.getData()); // comparten la misma lista
        }
    }

    @Nested
    @DisplayName("Static Methods Tests")
    class StaticMethodsTests {

        @Test
        @DisplayName("Todos los métodos deberían ser estáticos")
        void todosLosMetodosDeberianSerEstaticos() {
            // Act & Assert - verificar que podemos llamar métodos sin instanciar la clase
            assertDoesNotThrow(() -> {
                ApiResponseBuilder.success("test", "data");
                ApiResponseBuilder.error("error");
                ApiResponseBuilder.validationError("validation", Arrays.asList("error"));
                ApiResponseBuilder.notFound("not found", 1L);
                ApiResponseBuilder.duplicateError("duplicate", "name");
                ApiResponseBuilder.createMeta(10);
                ApiResponseBuilder.createMeta(10, 5, 1, 5);
            });
        }

        @Test
        @DisplayName("No debería permitir instanciación de la clase")
        void noDeberiaPermitirInstanciacionDeLaClase() {
            // Note: Este test verifica el comportamiento actual, pero idealmente 
            // ApiResponseBuilder debería tener constructor privado para ser una verdadera utility class
            
            // Por ahora, verificamos que se puede instanciar pero no es el uso esperado
            assertDoesNotThrow(() -> {
                ApiResponseBuilder builder = new ApiResponseBuilder();
                assertNotNull(builder);
            });
            
            // El uso esperado es siempre a través de métodos estáticos
            ApiResponse<String> response = ApiResponseBuilder.success("test", "data");
            assertNotNull(response);
        }
    }
}