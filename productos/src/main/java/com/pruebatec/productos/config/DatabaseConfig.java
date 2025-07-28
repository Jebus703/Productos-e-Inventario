package com.pruebatec.productos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración de base de datos con Oracle como principal
 * H2 como fallback (si Oracle no está disponible)
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String oracleUrl;
    
    @Value("${spring.datasource.username}")
    private String oracleUsername;
    
    @Value("${spring.datasource.password}")
    private String oraclePassword;
    
    @Value("${spring.datasource.driver-class-name}")
    private String oracleDriver;

    // Configuración H2 para fallback
    private final String h2Url = "jdbc:h2:mem:productos_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private final String h2Username = "sa";
    private final String h2Password = "password";
    private final String h2Driver = "org.h2.Driver";

    private final ConfigurableEnvironment environment;
    private String currentDatabase = "Oracle";

    public DatabaseConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    /**
     * DataSource principal - Oracle con fallback a H2
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        // Intentar Oracle primero (BASE DE DATOS PRINCIPAL)
        DataSource oracleDataSource = createOracleDataSource();
        if (testConnection(oracleDataSource, "Oracle")) {
            log.info("🟢 CONECTADO A ORACLE DATABASE (PRINCIPAL)");
            log.info("📊 Base de datos: Oracle - Esquema: {}", oracleUsername.toUpperCase());
            currentDatabase = "Oracle";
            configureForOracle();
            return oracleDataSource;
        }
        
        // Fallback a H2 si Oracle falla
        log.warn("🔴 Oracle no disponible, cambiando a H2 (FALLBACK)...");
        DataSource h2DataSource = createH2DataSource();
        if (testConnection(h2DataSource, "H2")) {
            log.info("🟡 CONECTADO A H2 DATABASE (FALLBACK)");
            log.info("📊 Base de datos: H2 - URL: {}", h2Url);
            log.info("🌐 Consola H2 disponible en: http://localhost:8080/h2-console");
            currentDatabase = "H2";
            configureForH2();
            return h2DataSource;
        }
        
        // Si ambas fallan
        log.error("❌ NO SE PUDO CONECTAR A NINGUNA BASE DE DATOS");
        throw new RuntimeException("Error: No se pudo establecer conexión con Oracle ni H2");
    }

    /**
     * Configurar propiedades dinámicamente para Oracle
     */
    private void configureForOracle() {
        Map<String, Object> oracleProps = new HashMap<>();
        oracleProps.put("spring.jpa.hibernate.ddl-auto", "update"); // Cambio temporal
        oracleProps.put("spring.jpa.database-platform", "org.hibernate.dialect.OracleDialect");
        oracleProps.put("spring.sql.init.mode", "never");
        oracleProps.put("spring.h2.console.enabled", "false");
        
        addPropertiesToEnvironment(oracleProps, "oracle-config");
        log.info("⚙️ Configuración aplicada para Oracle");
    }

    /**
     * Configurar propiedades dinámicamente para H2
     */
    private void configureForH2() {
        Map<String, Object> h2Props = new HashMap<>();
        
        // Configuración específica para H2
        h2Props.put("spring.jpa.hibernate.ddl-auto", "create-drop");
        h2Props.put("spring.jpa.database-platform", "org.hibernate.dialect.H2Dialect");
        h2Props.put("spring.sql.init.mode", "always");
        h2Props.put("spring.sql.init.data-locations", "classpath:data.sql");
        h2Props.put("spring.sql.init.continue-on-error", "true");
        h2Props.put("spring.h2.console.enabled", "true");
        
        // Configuración de pool optimizada para desarrollo
        h2Props.put("spring.datasource.hikari.maximum-pool-size", "5");
        h2Props.put("spring.datasource.hikari.minimum-idle", "1");
        h2Props.put("spring.datasource.hikari.connection-timeout", "30000");
        h2Props.put("spring.datasource.hikari.idle-timeout", "600000");
        
        // Configuraciones específicas de Hibernate para H2
        h2Props.put("spring.jpa.properties.hibernate.connection.provider_disables_autocommit", "false");
        h2Props.put("spring.jpa.properties.hibernate.connection.autocommit", "true");
        h2Props.put("spring.jpa.show-sql", "true");
        
        // Forzar ejecución de data.sql
        h2Props.put("spring.jpa.defer-datasource-initialization", "true");
        
        addPropertiesToEnvironment(h2Props, "h2-config");
        log.info("⚙️ Configuración aplicada para H2");
        log.info("📋 Precargando datos iniciales desde data.sql");
    }

    /**
     * Agregar propiedades al entorno de Spring
     */
    private void addPropertiesToEnvironment(Map<String, Object> properties, String sourceName) {
        MapPropertySource propertySource = new MapPropertySource(sourceName, properties);
        environment.getPropertySources().addFirst(propertySource);
    }

    /**
     * Crear DataSource para Oracle
     */
    private DataSource createOracleDataSource() {
        try {
            log.info("🔧 Configurando Oracle Database (Principal)");
            
            return DataSourceBuilder.create()
                    .driverClassName(oracleDriver)
                    .url(oracleUrl)
                    .username(oracleUsername)
                    .password(oraclePassword)
                    .build();
        } catch (Exception e) {
            log.warn("⚠️ Error creando DataSource de Oracle: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Crear DataSource para H2
     */
    private DataSource createH2DataSource() {
        try {
            log.info("🔧 Configurando H2 Database (Fallback)");
            
            return DataSourceBuilder.create()
                    .driverClassName(h2Driver)
                    .url(h2Url)
                    .username(h2Username)
                    .password(h2Password)
                    .build();
        } catch (Exception e) {
            log.warn("⚠️ Error creando DataSource de H2: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Probar conexión a la base de datos
     */
    private boolean testConnection(DataSource dataSource, String dbType) {
        if (dataSource == null) {
            return false;
        }
        
        try (Connection connection = dataSource.getConnection()) {
            boolean isValid = connection.isValid(5);
            
            if (isValid) {
                String dbInfo = getDatabaseInfo(connection);
                log.info("✅ Conexión exitosa a {}: {}", dbType, dbInfo);
                return true;
            } else {
                log.warn("⚠️ Conexión no válida a {}", dbType);
                return false;
            }
            
        } catch (SQLException e) {
            log.warn("🔴 Error conectando a {}: {}", dbType, e.getMessage());
            return false;
        }
    }

    /**
     * Obtener información de la base de datos
     */
    private String getDatabaseInfo(Connection connection) {
        try {
            String productName = connection.getMetaData().getDatabaseProductName();
            String version = connection.getMetaData().getDatabaseProductVersion();
            return String.format("%s v%s", productName, version);
        } catch (SQLException e) {
            return "Información no disponible";
        }
    }

    /**
     * Bean para saber qué base de datos se está usando
     */
    @Bean
    public DatabaseInfo databaseInfo() {
        return new DatabaseInfo(currentDatabase);
    }

    /**
     * Clase para exponer información de la base de datos
     */
    public static class DatabaseInfo {
        private final String databaseType;
        
        public DatabaseInfo(String databaseType) {
            this.databaseType = databaseType;
        }
        
        public boolean isUsingH2() {
            return "H2".equals(databaseType);
        }
        
        public boolean isUsingOracle() {
            return "Oracle".equals(databaseType);
        }
        
        public String getDatabaseType() {
            return databaseType;
        }
        
        public String getConsoleUrl() {
            return isUsingH2() ? "http://localhost:8080/h2-console" : null;
        }
    }
}