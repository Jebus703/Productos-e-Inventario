package com.pruebatec.productos.service.impl;


import com.pruebatec.productos.dto.ProductoRequestDto;
import com.pruebatec.productos.dto.ProductoResponseDto;
import com.pruebatec.productos.entity.Producto;
import com.pruebatec.productos.exception.ProductoException;
import com.pruebatec.productos.repository.ProductoRepository;
import com.pruebatec.productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok genera constructor con campos final
@Slf4j // Lombok genera logger automáticamente
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> obtenerTodosLosProductos() {
        log.debug("🔍 Obteniendo todos los productos activos");
        
        // Usa naming convention: findByActivoOrderById
        List<Producto> productos = productoRepository.findByActivoOrderById("Y");
        
        log.info("✅ Se encontraron {} productos activos", productos.size());
        
        return productos.stream()
                .map(ProductoResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerProductoPorId(Long id) {
        log.debug("🔍 Buscando producto con ID: {}", id);
        
        if (id == null || id <= 0) {
            throw ProductoException.validacion("id", id, "El ID del producto debe ser un número positivo");
        }
        
        // Usa naming convention: findByIdAndActivo
        Producto producto = productoRepository.findByIdAndActivo(id, "Y")
                .orElseThrow(() -> {
                    log.warn("❌ Producto con ID {} no encontrado", id);
                    return ProductoException.noEncontrado(id);
                });
        
        log.info("✅ Producto encontrado: {}", producto.getNombre());
        return new ProductoResponseDto(producto);
    }

    @Override
    public ProductoResponseDto crearProducto(ProductoRequestDto productoRequest) {
        log.debug("🆕 Creando nuevo producto: {}", productoRequest.getNombre());
        
        // Validar datos de entrada
        validarProductoRequest(productoRequest);
        
        // Verificar nombre único usando naming convention
        if (productoRepository.existsByNombreIgnoreCaseAndActivo(productoRequest.getNombre(), "Y")) {
            log.warn("❌ Ya existe un producto con el nombre: {}", productoRequest.getNombre());
            throw ProductoException.duplicado(productoRequest.getNombre());
        }
        
        // Crear entidad usando setters (Lombok)
        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setActivo("Y");
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());
        
        // Guardar en base de datos
        Producto productoGuardado = productoRepository.save(producto);
        
        log.info("✅ Producto creado exitosamente con ID: {} - {}", 
                productoGuardado.getId(), productoGuardado.getNombre());
        
        return new ProductoResponseDto(productoGuardado);
    }
    
    /**
     * Valida los datos del producto request
     */
    private void validarProductoRequest(ProductoRequestDto request) {
        if (request == null) {
            throw ProductoException.validacion("El producto no puede ser nulo");
        }
        
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw ProductoException.validacion("nombre", request.getNombre(), 
                    "El nombre del producto es obligatorio");
        }
        
        if (request.getPrecio() == null || request.getPrecio().doubleValue() <= 0) {
            throw ProductoException.validacion("precio", request.getPrecio(), 
                    "El precio debe ser mayor a 0");
        }
    }
}