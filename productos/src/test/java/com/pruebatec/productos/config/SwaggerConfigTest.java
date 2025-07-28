package com.pruebatec.productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SwaggerConfig Tests")
class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Nested
    @DisplayName("OpenAPI Configuration Tests")
    class OpenAPIConfigurationTests {

        @Test
        @DisplayName("Debería crear OpenAPI configuración completa")
        void deberiaCrearOpenAPIConfiguracionCompleta() {
            OpenAPI openAPI = swaggerConfig.customOpenAPI();

            assertNotNull(openAPI);
            assertNotNull(openAPI.getInfo());
            assertNotNull(openAPI.getServers());
            assertNotNull(openAPI.getTags());
            assertFalse(openAPI.getServers().isEmpty());
            assertFalse(openAPI.getTags().isEmpty());
        }

        @Test
        @DisplayName("Debería configurar información de API correctamente")
        void deberiaConfigurarInformacionDeApiCorrectamente() {
            OpenAPI openAPI = swaggerConfig.customOpenAPI();
            Info info = openAPI.getInfo();

            assertEquals("Microservicio de Productos API", info.getTitle());
            assertEquals("1.0.0", info.getVersion());
            assertNotNull(info.getDescription());
            assertTrue(info.getDescription().contains("API REST"));
            assertTrue(info.getDescription().contains("Spring Boot"));
            assertNotNull(info.getContact());
            assertNotNull(info.getLicense());
        }
    }

    @Nested
    @DisplayName("API Info Tests")
    class ApiInfoTests {

        @Test
        @DisplayName("Debería crear información de API con todos los campos")
        void deberiaCrearInformacionDeApiConTodosLosCampos() {
            Info info = (Info) ReflectionTestUtils.invokeMethod(swaggerConfig, "createApiInfo");

            assertNotNull(info);
            assertEquals("Microservicio de Productos API", info.getTitle());
            assertEquals("1.0.0", info.getVersion());
            assertNotNull(info.getDescription());
            assertNotNull(info.getContact());
            assertNotNull(info.getLicense());
        }

        @Test
        @DisplayName("Debería incluir tecnologías en la descripción")
        void deberiaIncluirTecnologiasEnLaDescripcion() {
            Info info = (Info) ReflectionTestUtils.invokeMethod(swaggerConfig, "createApiInfo");

            String description = info.getDescription();
            assertTrue(description.contains("Spring Boot"));
            assertTrue(description.contains("Java 21"));
            assertTrue(description.contains("Oracle Database"));
            assertTrue(description.contains("Spring Data JPA"));
            assertTrue(description.contains("Lombok"));
            assertTrue(description.contains("Maven"));
        }

        @Test
        @DisplayName("Debería incluir características principales en la descripción")
        void deberiaIncluirCaracteristicasPrincipalesEnLaDescripcion() {
            Info info = (Info) ReflectionTestUtils.invokeMethod(swaggerConfig, "createApiInfo");

            String description = info.getDescription();
            assertTrue(description.contains("CRUD"));
            assertTrue(description.contains("Validaciones"));
            assertTrue(description.contains("JSON API"));
            assertTrue(description.contains("Oracle"));
            assertTrue(description.contains("SQLite"));
        }
    }

    @Nested
    @DisplayName("Contact Tests")
    class ContactTests {

        @Test
        @DisplayName("Debería crear contacto con información completa")
        void deberiaCrearContactoConInformacionCompleta() {
            Contact contact = (Contact) ReflectionTestUtils.invokeMethod(swaggerConfig, "createContact");

            assertNotNull(contact);
            assertEquals("Jesus Antonio Suarez Duarte", contact.getName());
            assertEquals("Jebus702@hotmail.com", contact.getEmail());
            assertNotNull(contact.getUrl());
            assertTrue(contact.getUrl().contains("github.com"));
        }

        @Test
        @DisplayName("Debería tener nombre válido")
        void deberiaTenerNombreValido() {
            Contact contact = (Contact) ReflectionTestUtils.invokeMethod(swaggerConfig, "createContact");

            assertNotNull(contact.getName());
            assertFalse(contact.getName().trim().isEmpty());
        }

        @Test
        @DisplayName("Debería tener email válido")
        void deberiaTenerEmailValido() {
            Contact contact = (Contact) ReflectionTestUtils.invokeMethod(swaggerConfig, "createContact");

            assertNotNull(contact.getEmail());
            assertTrue(contact.getEmail().contains("@"));
            assertTrue(contact.getEmail().contains("."));
        }

        @Test
        @DisplayName("Debería tener URL válida")
        void deberiaTenerUrlValida() {
            Contact contact = (Contact) ReflectionTestUtils.invokeMethod(swaggerConfig, "createContact");

            assertNotNull(contact.getUrl());
            assertTrue(contact.getUrl().startsWith("http"));
        }
    }

    @Nested
    @DisplayName("License Tests")
    class LicenseTests {

        @Test
        @DisplayName("Debería crear licencia MIT")
        void deberiaCrearLicenciaMit() {
            License license = (License) ReflectionTestUtils.invokeMethod(swaggerConfig, "createLicense");

            assertNotNull(license);
            assertEquals("MIT License", license.getName());
            assertEquals("https://opensource.org/licenses/MIT", license.getUrl());
        }

        @Test
        @DisplayName("Debería tener nombre de licencia válido")
        void deberiaTenerNombreDeLicenciaValido() {
            License license = (License) ReflectionTestUtils.invokeMethod(swaggerConfig, "createLicense");

            assertNotNull(license.getName());
            assertFalse(license.getName().trim().isEmpty());
        }

        @Test
        @DisplayName("Debería tener URL de licencia válida")
        void deberiaTenerUrlDeLicenciaValida() {
            License license = (License) ReflectionTestUtils.invokeMethod(swaggerConfig, "createLicense");

            assertNotNull(license.getUrl());
            assertTrue(license.getUrl().startsWith("https://"));
        }
    }

    @Nested
    @DisplayName("Servers Tests")
    class ServersTests {

        @Test
        @DisplayName("Debería crear lista de servidores")
        void deberiaCrearListaDeServidores() {
            @SuppressWarnings("unchecked")
            List<Server> servers = (List<Server>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createServers");

            assertNotNull(servers);
            assertEquals(2, servers.size());
        }

        @Test
        @DisplayName("Debería incluir servidor local")
        void deberiaIncluirServidorLocal() {
            @SuppressWarnings("unchecked")
            List<Server> servers = (List<Server>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createServers");

            Server localServer = servers.stream()
                .filter(server -> server.getUrl().contains("localhost"))
                .findFirst()
                .orElse(null);

            assertNotNull(localServer);
            assertEquals("http://localhost:8080", localServer.getUrl());
            assertTrue(localServer.getDescription().contains("desarrollo"));
        }

        @Test
        @DisplayName("Debería incluir servidor de producción")
        void deberiaIncluirServidorDeProduccion() {
            @SuppressWarnings("unchecked")
            List<Server> servers = (List<Server>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createServers");

            Server prodServer = servers.stream()
                .filter(server -> server.getUrl().contains("api-productos"))
                .findFirst()
                .orElse(null);

            assertNotNull(prodServer);
            assertTrue(prodServer.getUrl().startsWith("https://"));
            assertTrue(prodServer.getDescription().contains("producción"));
        }

        @Test
        @DisplayName("Todos los servidores deben tener URL y descripción")
        void todosLosServidoresDebenTenerUrlYDescripcion() {
            @SuppressWarnings("unchecked")
            List<Server> servers = (List<Server>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createServers");

            for (Server server : servers) {
                assertNotNull(server.getUrl());
                assertNotNull(server.getDescription());
                assertFalse(server.getUrl().trim().isEmpty());
                assertFalse(server.getDescription().trim().isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("Tags Tests")
    class TagsTests {

        @Test
        @DisplayName("Debería crear lista de tags")
        void deberiaCrearListaDeTags() {
            @SuppressWarnings("unchecked")
            List<Tag> tags = (List<Tag>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createTags");

            assertNotNull(tags);
            assertEquals(2, tags.size());
        }

        @Test
        @DisplayName("Debería incluir tag de Productos")
        void deberiaIncluirTagDeProductos() {
            @SuppressWarnings("unchecked")
            List<Tag> tags = (List<Tag>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createTags");

            Tag productosTag = tags.stream()
                .filter(tag -> "Productos".equals(tag.getName()))
                .findFirst()
                .orElse(null);

            assertNotNull(productosTag);
            assertEquals("Productos", productosTag.getName());
            assertTrue(productosTag.getDescription().contains("productos"));
        }

        @Test
        @DisplayName("Debería incluir tag de Health Check")
        void deberiaIncluirTagDeHealthCheck() {
            @SuppressWarnings("unchecked")
            List<Tag> tags = (List<Tag>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createTags");

            Tag healthTag = tags.stream()
                .filter(tag -> "Health Check".equals(tag.getName()))
                .findFirst()
                .orElse(null);

            assertNotNull(healthTag);
            assertEquals("Health Check", healthTag.getName());
            assertTrue(healthTag.getDescription().contains("estado"));
        }

        @Test
        @DisplayName("Todos los tags deben tener nombre y descripción")
        void todosLosTagsDebenTenerNombreYDescripcion() {
            @SuppressWarnings("unchecked")
            List<Tag> tags = (List<Tag>) ReflectionTestUtils.invokeMethod(swaggerConfig, "createTags");

            for (Tag tag : tags) {
                assertNotNull(tag.getName());
                assertNotNull(tag.getDescription());
                assertFalse(tag.getName().trim().isEmpty());
                assertFalse(tag.getDescription().trim().isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("Bean Creation Tests")
    class BeanCreationTests {

        @Test
        @DisplayName("Debería crear bean OpenAPI válido")
        void deberiaCrearBeanOpenApiValido() {
            OpenAPI openAPI = swaggerConfig.customOpenAPI();

            assertNotNull(openAPI);
            
            // Verificar que todos los componentes están presentes
            assertNotNull(openAPI.getInfo());
            assertNotNull(openAPI.getServers());
            assertNotNull(openAPI.getTags());
            
            // Verificar que las listas no están vacías
            assertFalse(openAPI.getServers().isEmpty());
            assertFalse(openAPI.getTags().isEmpty());
        }

        @Test
        @DisplayName("Bean debería ser configurable")
        void beanDeberiaSerConfigurable() {
            OpenAPI openAPI1 = swaggerConfig.customOpenAPI();
            OpenAPI openAPI2 = swaggerConfig.customOpenAPI();

            // Los beans deben ser equivalentes pero instancias diferentes
            assertNotSame(openAPI1, openAPI2);
            assertEquals(openAPI1.getInfo().getTitle(), openAPI2.getInfo().getTitle());
            assertEquals(openAPI1.getInfo().getVersion(), openAPI2.getInfo().getVersion());
        }
    }

    @Nested
    @DisplayName("Configuration Validation Tests")
    class ConfigurationValidationTests {

        @Test
        @DisplayName("Configuración debe ser completa y consistente")
        void configuracionDebeSerCompletaYConsistente() {
            OpenAPI openAPI = swaggerConfig.customOpenAPI();

            // Validar información básica
            Info info = openAPI.getInfo();
            assertNotNull(info.getTitle());
            assertNotNull(info.getVersion());
            assertNotNull(info.getDescription());

            // Validar contacto
            Contact contact = info.getContact();
            assertNotNull(contact.getName());
            assertNotNull(contact.getEmail());
            assertNotNull(contact.getUrl());

            // Validar licencia
            License license = info.getLicense();
            assertNotNull(license.getName());
            assertNotNull(license.getUrl());

            // Validar servidores
            List<Server> servers = openAPI.getServers();
            assertTrue(servers.size() >= 1);
            servers.forEach(server -> {
                assertNotNull(server.getUrl());
                assertNotNull(server.getDescription());
            });

            // Validar tags
            List<Tag> tags = openAPI.getTags();
            assertTrue(tags.size() >= 1);
            tags.forEach(tag -> {
                assertNotNull(tag.getName());
                assertNotNull(tag.getDescription());
            });
        }
    }
}