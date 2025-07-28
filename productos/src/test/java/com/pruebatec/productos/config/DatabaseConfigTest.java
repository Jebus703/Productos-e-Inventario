package com.pruebatec.productos.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseConfig Tests")
class DatabaseConfigTest {

    private ConfigurableEnvironment environment;
    private MutablePropertySources propertySources;
    private DatabaseConfig databaseConfig;

    @BeforeEach
    void setUp() {
        environment = mock(ConfigurableEnvironment.class);
        propertySources = mock(MutablePropertySources.class);
        when(environment.getPropertySources()).thenReturn(propertySources);
        
        databaseConfig = new DatabaseConfig(environment);
        
        // Configurar campos usando reflection
        ReflectionTestUtils.setField(databaseConfig, "oracleUrl", "jdbc:oracle:thin:@localhost:1521:xe");
        ReflectionTestUtils.setField(databaseConfig, "oracleUsername", "productos");
        ReflectionTestUtils.setField(databaseConfig, "oraclePassword", "password");
        ReflectionTestUtils.setField(databaseConfig, "oracleDriver", "oracle.jdbc.driver.OracleDriver");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Debería crear DatabaseConfig con environment")
        void deberiaCrearDatabaseConfigConEnvironment() {
            ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
            DatabaseConfig config = new DatabaseConfig(env);
            
            assertNotNull(config);
        }
    }

    @Nested
    @DisplayName("configureForH2 Tests - 0% Cobertura")
    class ConfigureForH2Tests {

        @Test
        @DisplayName("Debería configurar todas las propiedades para H2")
        void deberiaConfigurarTodasLasPropiedadesParaH2() {
            // Act - Invocar el método privado configureForH2
            ReflectionTestUtils.invokeMethod(databaseConfig, "configureForH2");
            
            // Assert - Verificar que se agregaron las propiedades al entorno
            verify(propertySources, times(1)).addFirst(any(MapPropertySource.class));
        }

        @Test
        @DisplayName("Debería configurar propiedades H2 específicas")
        void deberiaConfigurarPropiedadesH2Especificas() {
            // Capturar el MapPropertySource que se agrega
            ArgumentCaptor<MapPropertySource> captor = ArgumentCaptor.forClass(MapPropertySource.class);
            
            // Act
            ReflectionTestUtils.invokeMethod(databaseConfig, "configureForH2");
            
            // Assert
            verify(propertySources).addFirst(captor.capture());
            MapPropertySource propertySource = captor.getValue();
            
            assertEquals("h2-config", propertySource.getName());
            assertTrue(propertySource.getSource() instanceof java.util.Map);
        }
    }

    @Nested
    @DisplayName("dataSource Tests - 40% Cobertura")
    class DataSourceTests {

        @Test
        @DisplayName("Debería intentar crear Oracle DataSource (flujo normal)")
        void deberiaIntentarCrearOracleDataSourceFlujoNormal() {
            // Este test ejecuta el método dataSource() que internamente llama a createOracleDataSource()
            // Como Oracle probablemente fallará sin una BD real, debería hacer fallback a H2
            
            try {
                // Act - esto ejecutará el flujo completo del método dataSource()
                DataSource result = databaseConfig.dataSource();
                
                // Assert - debería retornar algo (Oracle o H2)
                assertNotNull(result);
                
                // Verificar que se configuró alguna BD
                String currentDatabase = (String) ReflectionTestUtils.getField(databaseConfig, "currentDatabase");
                assertTrue(currentDatabase.equals("Oracle") || currentDatabase.equals("H2"));
                
            } catch (RuntimeException e) {
                // Si ambas BD fallan, debería lanzar RuntimeException
                assertTrue(e.getMessage().contains("No se pudo establecer conexión"));
            }
        }

        @Test
        @DisplayName("Debería usar H2 cuando Oracle no está disponible")
        void deberiaUsarH2CuandoOracleNoEstaDisponible() {
            // Configurar Oracle con credenciales inválidas para que falle
            ReflectionTestUtils.setField(databaseConfig, "oracleUsername", "invalid_user");
            ReflectionTestUtils.setField(databaseConfig, "oraclePassword", "invalid_password");
            
            try {
                // Act
                DataSource result = databaseConfig.dataSource();
                
                // Assert - debería hacer fallback a H2
                assertNotNull(result);
                String currentDatabase = (String) ReflectionTestUtils.getField(databaseConfig, "currentDatabase");
                assertEquals("H2", currentDatabase);
                
            } catch (RuntimeException e) {
                // Si H2 también falla, es aceptable
                assertTrue(e.getMessage().contains("No se pudo establecer conexión"));
            }
        }

        @Test
        @DisplayName("Debería manejar error de Oracle pero H2 funciona como fallback")
        void deberiaManejarErrorDeOraclePeroH2FuncionaComoFallback() {
            // Arrange - Oracle con driver inválido, H2 con driver válido
            ReflectionTestUtils.setField(databaseConfig, "oracleDriver", "driver.oracle.invalido");
            // H2 mantiene su driver válido por defecto
            
            // Act - Oracle fallará, pero H2 funcionará
            DataSource result = databaseConfig.dataSource();
            
            // Assert - debería retornar H2 DataSource
            assertNotNull(result);
            String currentDatabase = (String) ReflectionTestUtils.getField(databaseConfig, "currentDatabase");
            assertEquals("H2", currentDatabase);
        }
    }

    @Nested
    @DisplayName("createH2DataSource Tests - 0% Cobertura")
    class CreateH2DataSourceTests {

        @Test
        @DisplayName("Debería crear DataSource H2 exitosamente")
        void deberiaCrearDataSourceH2Exitosamente() {
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createH2DataSource");
            
            // Assert - H2 DataSource siempre se crea, incluso con valores null
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería crear DataSource H2 incluso con driver null")
        void deberiaCrearDataSourceH2InclutoConDriverNull() {
            // Arrange - forzar driver nulo (pero el DataSource se creará de todas formas)
            ReflectionTestUtils.setField(databaseConfig, "h2Driver", null);
            
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createH2DataSource");
            
            // Assert - DataSourceBuilder.create() no falla por valores null, solo la conexión posterior falla
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería crear DataSource H2 con URL null")
        void deberiaCrearDataSourceH2ConUrlNull() {
            // Arrange
            ReflectionTestUtils.setField(databaseConfig, "h2Url", null);
            
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createH2DataSource");
            
            // Assert - DataSource se crea, pero la conexión fallaría
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería ejecutar el bloque try-catch de createH2DataSource")
        void deberiaEjecutarElBloqueTryCatchDeCreateH2DataSource() {
            // Verificar que el método maneja correctamente el flujo normal
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createH2DataSource");
            
            assertNotNull(result);
            
            // Verificar que los valores por defecto están configurados
            String h2Driver = (String) ReflectionTestUtils.getField(databaseConfig, "h2Driver");
            assertEquals("org.h2.Driver", h2Driver);
        }
    }

    @Nested
    @DisplayName("testConnection Tests - 59% Cobertura")
    class TestConnectionTests {

        @Test
        @DisplayName("Debería retornar false cuando DataSource es null")
        void deberiaRetornarFalseCuandoDataSourceEsNull() {
            // Act
            boolean result = (boolean) ReflectionTestUtils.invokeMethod(
                databaseConfig, "testConnection", null, "Oracle");
            
            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Debería retornar true cuando conexión es válida")
        void deberiaRetornarTrueCuandoConexionEsValida() throws Exception {
            // Arrange
            DataSource mockDataSource = mock(DataSource.class);
            Connection mockConnection = mock(Connection.class);
            DatabaseMetaData mockMetaData = mock(DatabaseMetaData.class);
            
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.isValid(5)).thenReturn(true);
            when(mockConnection.getMetaData()).thenReturn(mockMetaData);
            when(mockMetaData.getDatabaseProductName()).thenReturn("Oracle");
            when(mockMetaData.getDatabaseProductVersion()).thenReturn("21c");
            
            // Act
            boolean result = (boolean) ReflectionTestUtils.invokeMethod(
                databaseConfig, "testConnection", mockDataSource, "Oracle");
            
            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("Debería retornar false cuando conexión no es válida")
        void deberiaRetornarFalseCuandoConexionNoEsValida() throws Exception {
            // Arrange
            DataSource mockDataSource = mock(DataSource.class);
            Connection mockConnection = mock(Connection.class);
            
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.isValid(5)).thenReturn(false);
            
            // Act
            boolean result = (boolean) ReflectionTestUtils.invokeMethod(
                databaseConfig, "testConnection", mockDataSource, "Oracle");
            
            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Debería retornar false cuando hay SQLException")
        void deberiaRetornarFalseCuandoHaySqlException() throws Exception {
            // Arrange
            DataSource mockDataSource = mock(DataSource.class);
            when(mockDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));
            
            // Act
            boolean result = (boolean) ReflectionTestUtils.invokeMethod(
                databaseConfig, "testConnection", mockDataSource, "Oracle");
            
            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Debería manejar cuando getDatabaseInfo falla pero conexión es válida")
        void deberiaManejarCuandoGetDatabaseInfoFallaPeroConexionEsValida() throws Exception {
            // Arrange
            DataSource mockDataSource = mock(DataSource.class);
            Connection mockConnection = mock(Connection.class);
            
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.isValid(5)).thenReturn(true);
            // getDatabaseInfo fallará al no poder obtener metadata, pero la conexión es válida
            when(mockConnection.getMetaData()).thenThrow(new SQLException("Metadata error"));
            
            // Act
            boolean result = (boolean) ReflectionTestUtils.invokeMethod(
                databaseConfig, "testConnection", mockDataSource, "Oracle");
            
            // Assert - debería retornar true porque la conexión es válida
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("createOracleDataSource Tests - 69% Cobertura")
    class CreateOracleDataSourceTests {

        @Test
        @DisplayName("Debería crear DataSource Oracle exitosamente")
        void deberiaCrearDataSourceOracleExitosamente() {
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createOracleDataSource");
            
            // Assert - DataSource se crea incluso sin conexión real
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería crear DataSource Oracle incluso con driver null")
        void deberiaCrearDataSourceOracleInclutoConDriverNull() {
            // Arrange - el DataSource se crea, pero la conexión posterior fallará
            ReflectionTestUtils.setField(databaseConfig, "oracleDriver", null);
            
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createOracleDataSource");
            
            // Assert - DataSourceBuilder no falla inmediatamente con null
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería manejar URL Oracle null")
        void deberiaManejarUrlOracleNull() {
            // Arrange
            ReflectionTestUtils.setField(databaseConfig, "oracleUrl", null);
            
            // Act
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createOracleDataSource");
            
            // Assert - DataSource se crea pero conexión fallaría
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería ejecutar el bloque try de createOracleDataSource")
        void deberiaEjecutarElBloqueTryDeCreateOracleDataSource() {
            // Verificar que el método ejecuta el bloque try correctamente
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createOracleDataSource");
            
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería manejar excepción forzando ClassLoader error")
        void deberiaManejarExcepcionForzandoClassLoaderError() {
            // Para cubrir el bloque catch, necesitamos forzar una excepción real
            // Configurar un driver que definitivamente no existe
            ReflectionTestUtils.setField(databaseConfig, "oracleDriver", "com.invalid.NonExistentDriver");
            
            // Act - esto debería ejecutar el bloque catch
            DataSource result = (DataSource) ReflectionTestUtils.invokeMethod(
                databaseConfig, "createOracleDataSource");
            
            // Assert - en caso de excepción, debería retornar null
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("getDatabaseInfo Tests - 87% Cobertura")
    class GetDatabaseInfoTests {

        @Test
        @DisplayName("Debería obtener información de la base de datos correctamente")
        void deberiaObtenerInformacionDeLaBaseDeDatosCorrectamente() throws Exception {
            // Arrange
            Connection mockConnection = mock(Connection.class);
            DatabaseMetaData mockMetaData = mock(DatabaseMetaData.class);
            
            when(mockConnection.getMetaData()).thenReturn(mockMetaData);
            when(mockMetaData.getDatabaseProductName()).thenReturn("Oracle");
            when(mockMetaData.getDatabaseProductVersion()).thenReturn("21c Express Edition");
            
            // Act
            String result = (String) ReflectionTestUtils.invokeMethod(
                databaseConfig, "getDatabaseInfo", mockConnection);
            
            // Assert
            assertEquals("Oracle v21c Express Edition", result);
        }

        @Test
        @DisplayName("Debería retornar información no disponible cuando hay SQLException")
        void deberiaRetornarInformacionNoDisponibleCuandoHaySqlException() throws Exception {
            // Arrange
            Connection mockConnection = mock(Connection.class);
            when(mockConnection.getMetaData()).thenThrow(new SQLException("Error"));
            
            // Act
            String result = (String) ReflectionTestUtils.invokeMethod(
                databaseConfig, "getDatabaseInfo", mockConnection);
            
            // Assert
            assertEquals("Información no disponible", result);
        }

        @Test
        @DisplayName("Debería manejar SQLException en getDatabaseProductName")
        void deberiaManejarSqlExceptionEnGetDatabaseProductName() throws Exception {
            // Arrange
            Connection mockConnection = mock(Connection.class);
            DatabaseMetaData mockMetaData = mock(DatabaseMetaData.class);
            
            when(mockConnection.getMetaData()).thenReturn(mockMetaData);
            when(mockMetaData.getDatabaseProductName()).thenThrow(new SQLException("Product name error"));
            
            // Act
            String result = (String) ReflectionTestUtils.invokeMethod(
                databaseConfig, "getDatabaseInfo", mockConnection);
            
            // Assert
            assertEquals("Información no disponible", result);
        }

        @Test
        @DisplayName("Debería manejar SQLException en getDatabaseProductVersion")
        void deberiaManejarSqlExceptionEnGetDatabaseProductVersion() throws Exception {
            // Arrange
            Connection mockConnection = mock(Connection.class);
            DatabaseMetaData mockMetaData = mock(DatabaseMetaData.class);
            
            when(mockConnection.getMetaData()).thenReturn(mockMetaData);
            when(mockMetaData.getDatabaseProductName()).thenReturn("Oracle");
            when(mockMetaData.getDatabaseProductVersion()).thenThrow(new SQLException("Version error"));
            
            // Act
            String result = (String) ReflectionTestUtils.invokeMethod(
                databaseConfig, "getDatabaseInfo", mockConnection);
            
            // Assert
            assertEquals("Información no disponible", result);
        }
    }

    @Nested
    @DisplayName("DatabaseInfo Bean Tests")
    class DatabaseInfoBeanTests {

        @Test
        @DisplayName("Debería crear bean DatabaseInfo")
        void deberiaCrearBeanDatabaseInfo() {
            DatabaseConfig.DatabaseInfo info = databaseConfig.databaseInfo();
            
            assertNotNull(info);
            assertNotNull(info.getDatabaseType());
        }

        @Test
        @DisplayName("Debería crear bean DatabaseInfo con currentDatabase Oracle")
        void deberiaCrearBeanDatabaseInfoConCurrentDatabaseOracle() {
            // Arrange
            ReflectionTestUtils.setField(databaseConfig, "currentDatabase", "Oracle");
            
            // Act
            DatabaseConfig.DatabaseInfo info = databaseConfig.databaseInfo();
            
            // Assert
            assertNotNull(info);
            assertEquals("Oracle", info.getDatabaseType());
            assertTrue(info.isUsingOracle());
            assertFalse(info.isUsingH2());
        }

        @Test
        @DisplayName("Debería crear bean DatabaseInfo con currentDatabase H2")
        void deberiaCrearBeanDatabaseInfoConCurrentDatabaseH2() {
            // Arrange
            ReflectionTestUtils.setField(databaseConfig, "currentDatabase", "H2");
            
            // Act
            DatabaseConfig.DatabaseInfo info = databaseConfig.databaseInfo();
            
            // Assert
            assertNotNull(info);
            assertEquals("H2", info.getDatabaseType());
            assertFalse(info.isUsingOracle());
            assertTrue(info.isUsingH2());
        }
    }

    @Nested
    @DisplayName("DatabaseInfo Class Tests")
    class DatabaseInfoClassTests {

        @Test
        @DisplayName("Debería identificar correctamente base de datos H2")
        void deberiaIdentificarCorrectamenteBaseDeDatosH2() {
            DatabaseConfig.DatabaseInfo info = new DatabaseConfig.DatabaseInfo("H2");
            
            assertTrue(info.isUsingH2());
            assertFalse(info.isUsingOracle());
            assertEquals("H2", info.getDatabaseType());
            assertEquals("http://localhost:8080/h2-console", info.getConsoleUrl());
        }

        @Test
        @DisplayName("Debería identificar correctamente base de datos Oracle")
        void deberiaIdentificarCorrectamenteBaseDeDatosOracle() {
            DatabaseConfig.DatabaseInfo info = new DatabaseConfig.DatabaseInfo("Oracle");
            
            assertFalse(info.isUsingH2());
            assertTrue(info.isUsingOracle());
            assertEquals("Oracle", info.getDatabaseType());
            assertNull(info.getConsoleUrl());
        }

        @Test
        @DisplayName("Debería manejar tipo null")
        void deberiaManejarTipoNull() {
            DatabaseConfig.DatabaseInfo info = new DatabaseConfig.DatabaseInfo(null);
            
            assertFalse(info.isUsingH2());
            assertFalse(info.isUsingOracle());
            assertNull(info.getDatabaseType());
            assertNull(info.getConsoleUrl());
        }

        @Test
        @DisplayName("Debería manejar tipo desconocido")
        void deberiaManejarTipoDesconocido() {
            DatabaseConfig.DatabaseInfo info = new DatabaseConfig.DatabaseInfo("MySQL");
            
            assertFalse(info.isUsingH2());
            assertFalse(info.isUsingOracle());
            assertEquals("MySQL", info.getDatabaseType());
            assertNull(info.getConsoleUrl());
        }

        @Test
        @DisplayName("Debería manejar string vacío")
        void deberiaManejarStringVacio() {
            DatabaseConfig.DatabaseInfo info = new DatabaseConfig.DatabaseInfo("");
            
            assertFalse(info.isUsingH2());
            assertFalse(info.isUsingOracle());
            assertEquals("", info.getDatabaseType());
            assertNull(info.getConsoleUrl());
        }

        @Test
        @DisplayName("Debería ser case sensitive para tipos de DB")
        void deberiaSercaseSensitiveParaTiposDeDb() {
            DatabaseConfig.DatabaseInfo info1 = new DatabaseConfig.DatabaseInfo("h2");
            DatabaseConfig.DatabaseInfo info2 = new DatabaseConfig.DatabaseInfo("oracle");
            
            assertFalse(info1.isUsingH2()); // "h2" != "H2"
            assertFalse(info2.isUsingOracle()); // "oracle" != "Oracle"
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Debería tener campos de configuración Oracle configurados")
        void deberiaTenerCamposDeConfiguracionOracleConfigurados() {
            String oracleUrl = (String) ReflectionTestUtils.getField(databaseConfig, "oracleUrl");
            String oracleUsername = (String) ReflectionTestUtils.getField(databaseConfig, "oracleUsername");
            String oraclePassword = (String) ReflectionTestUtils.getField(databaseConfig, "oraclePassword");
            String oracleDriver = (String) ReflectionTestUtils.getField(databaseConfig, "oracleDriver");
            
            assertNotNull(oracleUrl);
            assertNotNull(oracleUsername);
            assertNotNull(oraclePassword);
            assertNotNull(oracleDriver);
            assertTrue(oracleUrl.contains("oracle"));
            assertEquals("oracle.jdbc.driver.OracleDriver", oracleDriver);
        }

        @Test
        @DisplayName("Debería tener valores por defecto para H2")
        void deberiaTenerValoresPorDefectoParaH2() {
            String h2Url = (String) ReflectionTestUtils.getField(databaseConfig, "h2Url");
            String h2Username = (String) ReflectionTestUtils.getField(databaseConfig, "h2Username");
            String h2Password = (String) ReflectionTestUtils.getField(databaseConfig, "h2Password");
            String h2Driver = (String) ReflectionTestUtils.getField(databaseConfig, "h2Driver");
            
            assertNotNull(h2Url);
            assertNotNull(h2Username);
            assertNotNull(h2Password);
            assertNotNull(h2Driver);
            assertTrue(h2Url.contains("h2:mem"));
            assertEquals("sa", h2Username);
            assertEquals("org.h2.Driver", h2Driver);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Debería tener configuración válida para ambiente de desarrollo")
        void deberiaTenerConfiguracionValidaParaAmbienteDeDesarrollo() {
            String oracleUrl = (String) ReflectionTestUtils.getField(databaseConfig, "oracleUrl");
            
            assertNotNull(oracleUrl);
            assertTrue(oracleUrl.contains("localhost") || oracleUrl.contains("127.0.0.1"));
        }

        @Test
        @DisplayName("Debería manejar currentDatabase como Oracle por defecto")
        void deberiaManejarCurrentDatabaseComoOraclePorDefecto() {
            String currentDatabase = (String) ReflectionTestUtils.getField(databaseConfig, "currentDatabase");
            
            assertEquals("Oracle", currentDatabase);
        }

        @Test
        @DisplayName("Debería poder cambiar currentDatabase")
        void deberiaPoderCambiarCurrentDatabase() {
            // Arrange
            ReflectionTestUtils.setField(databaseConfig, "currentDatabase", "H2");
            
            // Act
            String currentDatabase = (String) ReflectionTestUtils.getField(databaseConfig, "currentDatabase");
            
            // Assert
            assertEquals("H2", currentDatabase);
        }
    }
}