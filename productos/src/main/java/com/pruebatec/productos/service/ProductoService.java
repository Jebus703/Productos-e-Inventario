package com.pruebatec.productos.service;


import com.pruebatec.productos.dto.ProductoRequestDto;
import com.pruebatec.productos.dto.ProductoResponseDto;

import java.util.List;

/**
 * Interface para el servicio de productos
 * Define las operaciones básicas del negocio
 */
public interface ProductoService {

    /**
     * Obtener todos los productos activos
     * @return Lista de productos activos
     */
    List<ProductoResponseDto> obtenerTodosLosProductos();

    /**
     * Obtener producto por ID
     * @param id ID del producto
     * @return Producto encontrado
     * @throws com.pruebatec.productos.exception.ProductoException si no se encuentra el producto
     */
    ProductoResponseDto obtenerProductoPorId(Long id);

    /**
     * Crear nuevo producto
     * @param productoRequest Datos del producto a crear
     * @return Producto creado
     * @throws com.pruebatec.productos.exception.ProductoException si hay errores de validación o duplicidad
     */
    ProductoResponseDto crearProducto(ProductoRequestDto productoRequest);
}