-- =====================================================
-- SCRIPT DE INICIALIZACIÓN PARA H2 DATABASE
-- =====================================================
-- Este archivo se ejecuta DESPUÉS de que JPA cree las tablas
-- gracias a spring.jpa.defer-datasource-initialization=true

-- =====================================================
-- INSERTAR DATOS DE PRUEBA - PRECIOS EN PESOS COLOMBIANOS (COP)
-- =====================================================

INSERT INTO productos (nombre, precio, descripcion, fecha_creacion, fecha_actualizacion, activo) VALUES 
('Laptop HP Pavilion 15', 5299000.00, 'Laptop con procesador Intel i5-1235U, 8GB RAM DDR4, 256GB SSD NVMe, pantalla 15.6" Full HD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Mouse Logitech MX Master 3', 399000.00, 'Mouse inalámbrico ergonómico de alta precisión, sensor 4000 DPI, batería recargable', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Teclado Mecánico Corsair K70', 599000.00, 'Teclado mecánico RGB con switches Cherry MX Blue, estructura de aluminio', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Monitor Samsung 24" F24T450FQL', 1199000.00, 'Monitor Full HD IPS de 24", 75Hz, conectividad HDMI/DisplayPort, ajustable', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Audífonos Sony WH-1000XM4', 1399000.00, 'Audífonos inalámbricos con cancelación de ruido activa, 30h batería, Bluetooth 5.0', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('SSD Samsung 980 PRO 1TB', 799000.00, 'Disco sólido NVMe M.2, velocidades hasta 7000 MB/s lectura, 5000 MB/s escritura', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Webcam Logitech C920 HD Pro', 319000.00, 'Cámara web Full HD 1080p, micrófono estéreo integrado, enfoque automático', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Smartphone Samsung Galaxy S23', 3599000.00, 'Smartphone 5G, pantalla Dynamic AMOLED 6.1", 128GB, cámara 50MP, Android 13', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Tablet iPad Air 5ta Gen', 2399000.00, 'Tablet con chip M1, pantalla Liquid Retina 10.9", 64GB, compatible con Apple Pencil', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Impresora HP LaserJet Pro M404n', 1199000.00, 'Impresora láser monocromática, velocidad 38 ppm, conectividad USB/Ethernet', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Router ASUS AX6000 RT-AX88U', 999000.00, 'Router WiFi 6 dual-band, 8 puertos Gigabit, velocidades hasta 6000 Mbps', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Disco Duro WD Black 2TB', 359000.00, 'Disco duro interno SATA 3.5", 7200 RPM, 64MB cache, optimizado para gaming', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Tarjeta Gráfica RTX 4060 Ti', 1799000.00, 'GPU NVIDIA GeForce RTX 4060 Ti, 8GB GDDR6, Ray Tracing, DLSS 3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Procesador AMD Ryzen 7 5800X', 1199000.00, 'CPU de 8 núcleos/16 hilos, frecuencia base 3.8GHz, boost hasta 4.7GHz, socket AM4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Memoria RAM Corsair 32GB DDR4', 559000.00, 'Kit 2x16GB DDR4-3200, latencia CL16, compatible con Intel/AMD, disipador aluminio', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Cable HDMI 4K Premium', 89000.00, 'Cable HDMI 2.1 de 2 metros, soporte 4K@120Hz, HDR, compatible con PS5/Xbox Series X', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Parlantes Logitech Z623', 459000.00, 'Sistema de parlantes 2.1 con subwoofer, 200W RMS, certificación THX', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y'),
('Micrófono Blue Yeti', 679000.00, 'Micrófono condensador USB, patrón cardioide, ideal para streaming y podcasting', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Y');

-- =====================================================
-- CONFIRMACIÓN DE INSERCIÓN
-- =====================================================
-- Los productos han sido insertados exitosamente
-- Total: 18 productos con precios en pesos colombianos
-- Rango de precios: $89,000 - $5,299,000 COP