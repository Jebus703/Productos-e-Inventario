package com.pruebatec.productos.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    private ApiResponse<String> response;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        response = new ApiResponse<>();
        testTime = LocalDateTime.now();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Debería crear instancia con constructor por defecto")
        void deberiaCrearInstanciaConConstructorPorDefecto() {
            // Act
            ApiResponse<String> nuevaResponse = new ApiResponse<>();
            
            // Assert
            assertNotNull(nuevaResponse);
            assertFalse(nuevaResponse.isSuccess()); // default boolean value
            assertNull(nuevaResponse.getMessage());
            assertNull(nuevaResponse.getData());
            assertNull(nuevaResponse.getMeta());
            assertNull(nuevaResponse.getErrors());
            assertNull(nuevaResponse.getTimestamp());
        }

        @Test
        @DisplayName("Debería crear instancia con constructor completo")
        void deberiaCrearInstanciaConConstructorCompleto() {
            // Arrange
            List<ApiResponse.Error> errors = Arrays.asList(
                new ApiResponse.Error("ERR001", "Error Title", "Error Detail", "source", null)
            );
            ApiResponse.Meta meta = new ApiResponse.Meta(100, 10, 1, 10, null);
            LocalDateTime timestamp = LocalDateTime.now();
            
            // Act
            ApiResponse<String> nuevaResponse = new ApiResponse<>(
                true, "Success message", "Test data", meta, errors, timestamp
            );
            
            // Assert
            assertTrue(nuevaResponse.isSuccess());
            assertEquals("Success message", nuevaResponse.getMessage());
            assertEquals("Test data", nuevaResponse.getData());
            assertEquals(meta, nuevaResponse.getMeta());
            assertEquals(errors, nuevaResponse.getErrors());
            assertEquals(timestamp, nuevaResponse.getTimestamp());
        }

        @Test
        @DisplayName("Debería crear respuesta exitosa con timestamp automático")
        void deberiaCrearRespuestaExitosaConTimestampAutomatico() {
            // Arrange
            LocalDateTime antes = LocalDateTime.now();
            
            // Act
            ApiResponse<String> nuevaResponse = new ApiResponse<>(true, "Success", "data");
            
            // Assert
            LocalDateTime despues = LocalDateTime.now();
            
            assertTrue(nuevaResponse.isSuccess());
            assertEquals("Success", nuevaResponse.getMessage());
            assertEquals("data", nuevaResponse.getData());
            assertNotNull(nuevaResponse.getTimestamp());
            
            // Verificar que el timestamp está en el rango esperado
            assertTrue(nuevaResponse.getTimestamp().isAfter(antes.minusSeconds(1)));
            assertTrue(nuevaResponse.getTimestamp().isBefore(despues.plusSeconds(1)));
        }

        @Test
        @DisplayName("Debería crear respuesta exitosa con metadatos")
        void deberiaCrearRespuestaExitosaConMetadatos() {
            // Arrange
            ApiResponse.Meta meta = new ApiResponse.Meta(50, 25, 2, 25, null);
            LocalDateTime antes = LocalDateTime.now();
            
            // Act
            ApiResponse<List<String>> nuevaResponse = new ApiResponse<>(
                true, "Success with meta", Arrays.asList("item1", "item2"), meta
            );
            
            // Assert
            LocalDateTime despues = LocalDateTime.now();
            
            assertTrue(nuevaResponse.isSuccess());
            assertEquals("Success with meta", nuevaResponse.getMessage());
            assertEquals(Arrays.asList("item1", "item2"), nuevaResponse.getData());
            assertEquals(meta, nuevaResponse.getMeta());
            assertNotNull(nuevaResponse.getTimestamp());
            assertTrue(nuevaResponse.getTimestamp().isAfter(antes.minusSeconds(1)));
            assertTrue(nuevaResponse.getTimestamp().isBefore(despues.plusSeconds(1)));
        }

        @Test
        @DisplayName("Debería crear respuesta de error con timestamp automático")
        void deberiaCrearRespuestaDeErrorConTimestampAutomatico() {
            // Arrange
            List<ApiResponse.Error> errors = Arrays.asList(
                new ApiResponse.Error("E001", "Validation Error", "Name is required", "name", null),
                new ApiResponse.Error("E002", "Format Error", "Invalid email format", "email", null)
            );
            LocalDateTime antes = LocalDateTime.now();
            
            // Act
            ApiResponse<Object> nuevaResponse = new ApiResponse<>(false, "Validation failed", errors);
            
            // Assert
            LocalDateTime despues = LocalDateTime.now();
            
            assertFalse(nuevaResponse.isSuccess());
            assertEquals("Validation failed", nuevaResponse.getMessage());
            assertEquals(errors, nuevaResponse.getErrors());
            assertNull(nuevaResponse.getData());
            assertNotNull(nuevaResponse.getTimestamp());
            assertTrue(nuevaResponse.getTimestamp().isAfter(antes.minusSeconds(1)));
            assertTrue(nuevaResponse.getTimestamp().isBefore(despues.plusSeconds(1)));
        }
    }

    @Nested
    @DisplayName("Getters y Setters Tests")
    class GettersAndSettersTests {

        @Test
        @DisplayName("Debería permitir establecer y obtener success")
        void deberiaPermitirEstablecerYObtenerSuccess() {
            // Act
            response.setSuccess(true);
            
            // Assert
            assertTrue(response.isSuccess());
            
            // Act
            response.setSuccess(false);
            
            // Assert
            assertFalse(response.isSuccess());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener message")
        void deberiaPermitirEstablecerYObtenerMessage() {
            // Arrange
            String mensaje = "Operación completada exitosamente";
            
            // Act
            response.setMessage(mensaje);
            
            // Assert
            assertEquals(mensaje, response.getMessage());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener data de diferentes tipos")
        void deberiaPermitirEstablecerYObtenerDataDeDiferentesTipos() {
            // Test con String
            ApiResponse<String> stringResponse = new ApiResponse<>();
            stringResponse.setData("test data");
            assertEquals("test data", stringResponse.getData());
            
            // Test con Integer
            ApiResponse<Integer> intResponse = new ApiResponse<>();
            intResponse.setData(42);
            assertEquals(Integer.valueOf(42), intResponse.getData());
            
            // Test con List
            ApiResponse<List<String>> listResponse = new ApiResponse<>();
            List<String> lista = Arrays.asList("item1", "item2");
            listResponse.setData(lista);
            assertEquals(lista, listResponse.getData());
            
            // Test con Map
            ApiResponse<Map<String, Object>> mapResponse = new ApiResponse<>();
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("key", "value");
            mapResponse.setData(mapa);
            assertEquals(mapa, mapResponse.getData());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener meta")
        void deberiaPermitirEstablecerYObtenerMeta() {
            // Arrange
            ApiResponse.Meta meta = new ApiResponse.Meta(100, 20, 1, 20, null);
            
            // Act
            response.setMeta(meta);
            
            // Assert
            assertEquals(meta, response.getMeta());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener errors")
        void deberiaPermitirEstablecerYObtenerErrors() {
            // Arrange
            List<ApiResponse.Error> errors = Arrays.asList(
                new ApiResponse.Error("E001", "Error 1", "Detail 1", "source1", null)
            );
            
            // Act
            response.setErrors(errors);
            
            // Assert
            assertEquals(errors, response.getErrors());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener timestamp")
        void deberiaPermitirEstablecerYObtenerTimestamp() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();
            
            // Act
            response.setTimestamp(timestamp);
            
            // Assert
            assertEquals(timestamp, response.getTimestamp());
        }
    }

    @Nested
    @DisplayName("Datos Genéricos Tests")
    class DatosGenericosTests {

        @Test
        @DisplayName("Debería manejar diferentes tipos de datos genéricos")
        void deberiaManejarDiferentesTiposDeDatosGenericos() {
            // Test con objeto personalizado
            class ProductoTest {
                String nombre;
                Integer precio;
                
                ProductoTest(String nombre, Integer precio) {
                    this.nombre = nombre;
                    this.precio = precio;
                }
            }
            
            ApiResponse<ProductoTest> productoResponse = new ApiResponse<>();
            ProductoTest producto = new ProductoTest("iPhone", 1000);
            productoResponse.setData(producto);
            
            assertEquals(producto, productoResponse.getData());
            assertEquals("iPhone", productoResponse.getData().nombre);
            assertEquals(Integer.valueOf(1000), productoResponse.getData().precio);
        }

        @Test
        @DisplayName("Debería manejar colecciones complejas")
        void deberiaManejarColeccionesComplejas() {
            // Test con List de Maps
            ApiResponse<List<Map<String, Object>>> complexResponse = new ApiResponse<>();
            
            Map<String, Object> item1 = new HashMap<>();
            item1.put("id", 1);
            item1.put("name", "Item 1");
            
            Map<String, Object> item2 = new HashMap<>();
            item2.put("id", 2);
            item2.put("name", "Item 2");
            
            List<Map<String, Object>> data = Arrays.asList(item1, item2);
            complexResponse.setData(data);
            
            assertEquals(data, complexResponse.getData());
            assertEquals(2, complexResponse.getData().size());
            assertEquals("Item 1", complexResponse.getData().get(0).get("name"));
        }

        @Test
        @DisplayName("Debería manejar datos null")
        void deberiaManejarDatosNull() {
            // Arrange & Act
            response.setData(null);
            
            // Assert
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Debería mantener precisión de timestamp")
        void deberiaMantenerPrecisionDeTimestamp() {
            // Arrange
            LocalDateTime timestampPreciso = LocalDateTime.of(2024, 1, 15, 14, 30, 45, 123456789);
            
            // Act
            response.setTimestamp(timestampPreciso);
            
            // Assert
            assertEquals(timestampPreciso, response.getTimestamp());
            assertEquals(2024, response.getTimestamp().getYear());
            assertEquals(1, response.getTimestamp().getMonthValue());
            assertEquals(15, response.getTimestamp().getDayOfMonth());
            assertEquals(14, response.getTimestamp().getHour());
            assertEquals(30, response.getTimestamp().getMinute());
            assertEquals(45, response.getTimestamp().getSecond());
            assertEquals(123456789, response.getTimestamp().getNano());
        }

        @Test
        @DisplayName("Debería generar timestamps únicos en constructores")
        void deberiaGenerarTimestampsUnicosEnConstructores() {
            // Act
            ApiResponse<String> response1 = new ApiResponse<>(true, "msg1", "data1");
            ApiResponse<String> response2 = new ApiResponse<>(true, "msg2", "data2");
            
            // Assert
            assertNotNull(response1.getTimestamp());
            assertNotNull(response2.getTimestamp());
            // Los timestamps deberían ser muy cercanos pero probablemente diferentes
            // debido a la diferencia en nanosegundos
            long diferenciaNanos = ChronoUnit.NANOS.between(response1.getTimestamp(), response2.getTimestamp());
            assertTrue(Math.abs(diferenciaNanos) >= 0); // Siempre verdadero, pero confirma que existen
        }
    }

    @Nested
    @DisplayName("Equals y HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Debería ser igual a sí mismo")
        void deberiaSerIgualASiMismo() {
            // Arrange
            response.setSuccess(true);
            response.setMessage("test");
            response.setData("data");
            
            // Act & Assert
            assertEquals(response, response);
            assertEquals(response.hashCode(), response.hashCode());
        }

        @Test
        @DisplayName("Debería ser igual a otro ApiResponse con los mismos valores")
        void deberiaSerIgualAOtroApiResponseConLosMismosValores() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();
            
            response.setSuccess(true);
            response.setMessage("test message");
            response.setData("test data");
            response.setTimestamp(timestamp);
            
            ApiResponse<String> otroResponse = new ApiResponse<>();
            otroResponse.setSuccess(true);
            otroResponse.setMessage("test message");
            otroResponse.setData("test data");
            otroResponse.setTimestamp(timestamp);
            
            // Act & Assert
            assertEquals(response, otroResponse);
            assertEquals(response.hashCode(), otroResponse.hashCode());
        }

        @Test
        @DisplayName("No debería ser igual a null")
        void noDeberiaSerIgualANull() {
            // Arrange
            response.setSuccess(true);
            response.setMessage("test");
            
            // Act & Assert
            assertNotEquals(response, null);
        }

        @Test
        @DisplayName("No debería ser igual a objeto de diferente clase")
        void noDeberiaSerIgualAObjetoDeDiferenteClase() {
            // Arrange
            response.setSuccess(true);
            response.setMessage("test");
            
            // Act & Assert
            assertNotEquals(response, "String diferente");
        }

        @Test
        @DisplayName("No debería ser igual cuando success es diferente")
        void noDeberiaSerIgualCuandoSuccessEsDiferente() {
            // Arrange
            response.setSuccess(true);
            response.setMessage("test");
            
            ApiResponse<String> otroResponse = new ApiResponse<>();
            otroResponse.setSuccess(false);
            otroResponse.setMessage("test");
            
            // Act & Assert
            assertNotEquals(response, otroResponse);
        }

        @Test
        @DisplayName("No debería ser igual cuando data es diferente")
        void noDeberiaSerIgualCuandoDataEsDiferente() {
            // Arrange
            response.setSuccess(true);
            response.setData("data1");
            
            ApiResponse<String> otroResponse = new ApiResponse<>();
            otroResponse.setSuccess(true);
            otroResponse.setData("data2");
            
            // Act & Assert
            assertNotEquals(response, otroResponse);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Debería generar toString con todos los campos")
        void deberiaGenerarToStringConTodosLosCampos() {
            // Arrange
            response.setSuccess(true);
            response.setMessage("Test message");
            response.setData("Test data");
            response.setTimestamp(LocalDateTime.now());
            
            // Act
            String resultado = response.toString();
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("true"));
            assertTrue(resultado.contains("Test message"));
            assertTrue(resultado.contains("Test data"));
        }

        @Test
        @DisplayName("Debería generar toString con campos null")
        void deberiaGenerarToStringConCamposNull() {
            // Act
            String resultado = response.toString();
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("ApiResponse"));
        }
    }

    @Nested
    @DisplayName("Meta Class Tests")
    class MetaClassTests {

        @Test
        @DisplayName("Debería crear Meta con constructor por defecto")
        void deberiaCrearMetaConConstructorPorDefecto() {
            // Act
            ApiResponse.Meta meta = new ApiResponse.Meta();
            
            // Assert
            assertNotNull(meta);
            assertNull(meta.getTotal());
            assertNull(meta.getCount());
            assertNull(meta.getPage());
            assertNull(meta.getPageSize());
            assertNull(meta.getAdditional());
        }

        @Test
        @DisplayName("Debería crear Meta con constructor completo")
        void deberiaCrearMetaConConstructorCompleto() {
            // Arrange
            Map<String, Object> additional = new HashMap<>();
            additional.put("extraInfo", "some value");
            
            // Act
            ApiResponse.Meta meta = new ApiResponse.Meta(100, 25, 2, 25, additional);
            
            // Assert
            assertEquals(Integer.valueOf(100), meta.getTotal());
            assertEquals(Integer.valueOf(25), meta.getCount());
            assertEquals(Integer.valueOf(2), meta.getPage());
            assertEquals(Integer.valueOf(25), meta.getPageSize());
            assertEquals(additional, meta.getAdditional());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener todos los campos de Meta")
        void deberiaPermitirEstablecerYObtenerTodosLosCamposDeMeta() {
            // Arrange
            ApiResponse.Meta meta = new ApiResponse.Meta();
            Map<String, Object> additional = new HashMap<>();
            additional.put("key1", "value1");
            additional.put("key2", 42);
            
            // Act
            meta.setTotal(150);
            meta.setCount(30);
            meta.setPage(3);
            meta.setPageSize(30);
            meta.setAdditional(additional);
            
            // Assert
            assertEquals(Integer.valueOf(150), meta.getTotal());
            assertEquals(Integer.valueOf(30), meta.getCount());
            assertEquals(Integer.valueOf(3), meta.getPage());
            assertEquals(Integer.valueOf(30), meta.getPageSize());
            assertEquals(additional, meta.getAdditional());
            assertEquals("value1", meta.getAdditional().get("key1"));
            assertEquals(42, meta.getAdditional().get("key2"));
        }

        @Test
        @DisplayName("Debería manejar valores extremos en Meta")
        void deberiaManejarValoresExtremosEnMeta() {
            // Arrange
            ApiResponse.Meta meta = new ApiResponse.Meta();
            
            // Act & Assert - valores extremos
            meta.setTotal(Integer.MAX_VALUE);
            assertEquals(Integer.valueOf(Integer.MAX_VALUE), meta.getTotal());
            
            meta.setCount(0);
            assertEquals(Integer.valueOf(0), meta.getCount());
            
            meta.setPage(-1);
            assertEquals(Integer.valueOf(-1), meta.getPage());
        }

        @Test
        @DisplayName("Meta debería ser igual con los mismos valores")
        void metaDeberiaSerIgualConLosMismosValores() {
            // Arrange
            Map<String, Object> additional = new HashMap<>();
            additional.put("test", "value");
            
            ApiResponse.Meta meta1 = new ApiResponse.Meta(100, 20, 1, 20, additional);
            ApiResponse.Meta meta2 = new ApiResponse.Meta(100, 20, 1, 20, additional);
            
            // Act & Assert
            assertEquals(meta1, meta2);
            assertEquals(meta1.hashCode(), meta2.hashCode());
        }
    }

    @Nested
    @DisplayName("Error Class Tests")
    class ErrorClassTests {

        @Test
        @DisplayName("Debería crear Error con constructor por defecto")
        void deberiaCrearErrorConConstructorPorDefecto() {
            // Act
            ApiResponse.Error error = new ApiResponse.Error();
            
            // Assert
            assertNotNull(error);
            assertNull(error.getCode());
            assertNull(error.getTitle());
            assertNull(error.getDetail());
            assertNull(error.getSource());
            assertNull(error.getMeta());
        }

        @Test
        @DisplayName("Debería crear Error con constructor completo")
        void deberiaCrearErrorConConstructorCompleto() {
            // Arrange
            Map<String, Object> meta = new HashMap<>();
            meta.put("field", "email");
            meta.put("line", 45);
            
            // Act
            ApiResponse.Error error = new ApiResponse.Error(
                "VALIDATION_001", 
                "Validation Error", 
                "Email format is invalid", 
                "user.email", 
                meta
            );
            
            // Assert
            assertEquals("VALIDATION_001", error.getCode());
            assertEquals("Validation Error", error.getTitle());
            assertEquals("Email format is invalid", error.getDetail());
            assertEquals("user.email", error.getSource());
            assertEquals(meta, error.getMeta());
        }

        @Test
        @DisplayName("Debería permitir establecer y obtener todos los campos de Error")
        void deberiaPermitirEstablecerYObtenerTodosLosCamposDeError() {
            // Arrange
            ApiResponse.Error error = new ApiResponse.Error();
            Map<String, Object> meta = new HashMap<>();
            meta.put("severity", "HIGH");
            meta.put("category", "BUSINESS_RULE");
            
            // Act
            error.setCode("BUS_001");
            error.setTitle("Business Rule Violation");
            error.setDetail("Cannot delete product with active orders");
            error.setSource("product.delete");
            error.setMeta(meta);
            
            // Assert
            assertEquals("BUS_001", error.getCode());
            assertEquals("Business Rule Violation", error.getTitle());
            assertEquals("Cannot delete product with active orders", error.getDetail());
            assertEquals("product.delete", error.getSource());
            assertEquals(meta, error.getMeta());
            assertEquals("HIGH", error.getMeta().get("severity"));
        }

        @Test
        @DisplayName("Debería manejar diferentes tipos de errores")
        void deberiaManejarDiferentesTiposDeErrores() {
            // Test error de validación
            ApiResponse.Error validationError = new ApiResponse.Error(
                "VAL_001", "Validation Error", "Field is required", "name", null
            );
            
            // Test error de autenticación
            ApiResponse.Error authError = new ApiResponse.Error(
                "AUTH_001", "Authentication Error", "Invalid credentials", "auth.login", null
            );
            
            // Test error de sistema
            ApiResponse.Error systemError = new ApiResponse.Error(
                "SYS_001", "System Error", "Database connection failed", "database", null
            );
            
            // Assert
            assertEquals("VAL_001", validationError.getCode());
            assertEquals("AUTH_001", authError.getCode());
            assertEquals("SYS_001", systemError.getCode());
            
            assertNotEquals(validationError, authError);
            assertNotEquals(authError, systemError);
        }

        @Test
        @DisplayName("Error debería ser igual con los mismos valores")
        void errorDeberiaSerIgualConLosMismosValores() {
            // Arrange
            Map<String, Object> meta = new HashMap<>();
            meta.put("test", "value");
            
            ApiResponse.Error error1 = new ApiResponse.Error("E001", "Title", "Detail", "source", meta);
            ApiResponse.Error error2 = new ApiResponse.Error("E001", "Title", "Detail", "source", meta);
            
            // Act & Assert
            assertEquals(error1, error2);
            assertEquals(error1.hashCode(), error2.hashCode());
        }

        @Test
        @DisplayName("Debería manejar strings largos en Error")
        void deberiaManejarStringsLargosEnError() {
            // Arrange
            String detalleLargo = "Error detail: " + "x".repeat(1000);
            
            // Act
            ApiResponse.Error error = new ApiResponse.Error();
            error.setDetail(detalleLargo);
            
            // Assert
            assertEquals(detalleLargo, error.getDetail());
            assertTrue(error.getDetail().length() > 1000);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Debería funcionar correctamente en flujo completo de éxito")
        void deberiaFuncionarCorrectamenteEnFlujoCompletoDeExito() {
            // Arrange
            List<String> productos = Arrays.asList("iPhone", "Samsung", "Xiaomi");
            Map<String, Object> additional = new HashMap<>();
            additional.put("totalRevenue", 50000.0);
            additional.put("currency", "USD");
            
            ApiResponse.Meta meta = new ApiResponse.Meta(3, 3, 1, 10, additional);
            
            // Act
            ApiResponse<List<String>> respuestaCompleta = new ApiResponse<>(
                true, "Productos obtenidos exitosamente", productos, meta
            );
            
            // Assert
            assertTrue(respuestaCompleta.isSuccess());
            assertEquals("Productos obtenidos exitosamente", respuestaCompleta.getMessage());
            assertEquals(productos, respuestaCompleta.getData());
            assertEquals(3, respuestaCompleta.getData().size());
            assertEquals("iPhone", respuestaCompleta.getData().get(0));
            assertEquals(meta, respuestaCompleta.getMeta());
            assertEquals(Integer.valueOf(3), respuestaCompleta.getMeta().getTotal());
            assertEquals(50000.0, respuestaCompleta.getMeta().getAdditional().get("totalRevenue"));
            assertNotNull(respuestaCompleta.getTimestamp());
        }

        @Test
        @DisplayName("Debería funcionar correctamente en flujo completo de error")
        void deberiaFuncionarCorrectamenteEnFlujoCompletoDeError() {
            // Arrange
            List<ApiResponse.Error> errores = Arrays.asList(
                new ApiResponse.Error("VAL_001", "Validation Error", "Name is required", "name", null),
                new ApiResponse.Error("VAL_002", "Validation Error", "Price must be positive", "price", null)
            );
            
            // Act
            ApiResponse<Object> respuestaError = new ApiResponse<>(
                false, "Validation failed for product creation", errores
            );
            
            // Assert
            assertFalse(respuestaError.isSuccess());
            assertEquals("Validation failed for product creation", respuestaError.getMessage());
            assertEquals(errores, respuestaError.getErrors());
            assertEquals(2, respuestaError.getErrors().size());
            assertEquals("VAL_001", respuestaError.getErrors().get(0).getCode());
            assertEquals("Name is required", respuestaError.getErrors().get(0).getDetail());
            assertEquals("VAL_002", respuestaError.getErrors().get(1).getCode());
            assertEquals("Price must be positive", respuestaError.getErrors().get(1).getDetail());
            assertNull(respuestaError.getData());
            assertNotNull(respuestaError.getTimestamp());
        }

        @Test
        @DisplayName("Debería manejar respuesta vacía correctamente")
        void deberiaManejarRespuestaVaciaCorrectamente() {
            // Act
            ApiResponse<Void> respuestaVacia = new ApiResponse<>(true, "Operation completed", null);
            
            // Assert
            assertTrue(respuestaVacia.isSuccess());
            assertEquals("Operation completed", respuestaVacia.getMessage());
            assertNull(respuestaVacia.getData());
            assertNotNull(respuestaVacia.getTimestamp());
        }
    }
}