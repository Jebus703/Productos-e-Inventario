-- Secuencia para productos
CREATE SEQUENCE seq_productos 
    START WITH 1 
    INCREMENT BY 1 
    CACHE 20 
    NOCYCLE;
-- Tabla de productos
CREATE TABLE productos (
    id NUMBER PRIMARY KEY,
    nombre VARCHAR2(255) NOT NULL,
    precio NUMBER(10,2) NOT NULL CHECK (precio > 0),
    descripcion CLOB,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo CHAR(1) DEFAULT 'Y' CHECK (activo IN ('Y', 'N'))
);
-- Índices para optimizar consultas
CREATE INDEX idx_productos_nombre ON productos(nombre);
CREATE INDEX idx_productos_activo ON productos(activo);
-- Trigger para auto-incrementar ID y actualizar fecha
CREATE OR REPLACE TRIGGER trg_productos_bi
    BEFORE INSERT ON productos
    FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_productos.NEXTVAL;
    END IF;
END;
/
CREATE OR REPLACE TRIGGER trg_productos_bu
    BEFORE UPDATE ON productos
    FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := CURRENT_TIMESTAMP;
END;
/

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++-------------------------Tabla Inventarios-------------------------- ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Secuencia para inventario
CREATE SEQUENCE seq_inventario 
    START WITH 1 
    INCREMENT BY 1 
    CACHE 20 
    NOCYCLE;
-- Tabla de inventario 
CREATE TABLE inventario (
    id NUMBER PRIMARY KEY,
    producto_id NUMBER NOT NULL,
    cantidad NUMBER DEFAULT 0 CHECK (cantidad >= 0),
    cantidad_minima NUMBER DEFAULT 0 CHECK (cantidad_minima >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Índices para optimizar consultas
CREATE INDEX idx_inventario_producto_id ON inventario(producto_id);
CREATE UNIQUE INDEX idx_inventario_producto_unique ON inventario(producto_id);

-- Triggers para inventario
CREATE OR REPLACE TRIGGER trg_inventario_bi
    BEFORE INSERT ON inventario
    FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_inventario.NEXTVAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_inventario_bu
    BEFORE UPDATE ON inventario
    FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := CURRENT_TIMESTAMP;
END;
/
-- Secuencia para historial de compras
CREATE SEQUENCE seq_historial_compras 
    START WITH 1 
    INCREMENT BY 1 
    CACHE 50 
    NOCYCLE;
    
-- Tabla para registro de compras (opcional según requisitos)
CREATE TABLE historial_compras (
    id NUMBER PRIMARY KEY,
    producto_id NUMBER NOT NULL,
    cantidad_comprada NUMBER NOT NULL CHECK (cantidad_comprada > 0),
    precio_unitario NUMBER(10,2) NOT NULL CHECK (precio_unitario > 0),
    total NUMBER(10,2) NOT NULL CHECK (total > 0),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR2(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('COMPLETADA', 'CANCELADA', 'PENDIENTE'))
);

-- Índices para el historial
CREATE INDEX idx_historial_producto_id ON historial_compras(producto_id);
CREATE INDEX idx_historial_fecha ON historial_compras(fecha_compra);
CREATE INDEX idx_historial_estado ON historial_compras(estado);

-- Trigger para historial
CREATE OR REPLACE TRIGGER trg_historial_compras_bi
    BEFORE INSERT ON historial_compras
    FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_historial_compras.NEXTVAL;
    END IF;
END;
/

-- Insertar productos de ejemplo (los IDs se generan automáticamente)
INSERT INTO productos (nombre, precio, descripcion) VALUES 
('Laptop Dell XPS 13', 1299.99, 'Laptop ultrabook con procesador Intel i7');

INSERT INTO productos (nombre, precio, descripcion) VALUES 
('Mouse Logitech MX Master', 89.99, 'Mouse inalámbrico ergonómico');

INSERT INTO productos (nombre, precio, descripcion) VALUES 
('Teclado Mecánico Keychron K2', 79.95, 'Teclado mecánico inalámbrico compacto');

-- Insertar inventario inicial (usando los IDs generados)
INSERT INTO inventario (producto_id, cantidad, cantidad_minima) VALUES 
(1, 25, 5);

INSERT INTO inventario (producto_id, cantidad, cantidad_minima) VALUES 
(2, 50, 10);

INSERT INTO inventario (producto_id, cantidad, cantidad_minima) VALUES 
(3, 30, 8);

-- Confirmar cambios
COMMIT;

SELECT sequence_name, last_number FROM user_sequences;
SELECT table_name FROM user_tables WHERE table_name IN ('PRODUCTOS', 'INVENTARIO', 'HISTORIAL_COMPRAS');

SELECT 'Productos:' as tabla, COUNT(*) as registros FROM productos
UNION ALL
SELECT 'Inventario:', COUNT(*) FROM inventario;

