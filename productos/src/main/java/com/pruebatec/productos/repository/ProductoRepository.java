package com.pruebatec.productos.repository;


import com.pruebatec.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para productos
 * Usa Spring Data JPA naming conventions (no necesita @Query)
 * Patrón Repository: Encapsula la lógica de acceso a datos
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Spring Data genera automáticamente la query:
     * SELECT * FROM productos WHERE activo = ?1 ORDER BY id
     */
    List<Producto> findByActivoOrderById(String activo);

    /**
     * Spring Data genera automáticamente:
     * SELECT * FROM productos WHERE id = ?1 AND activo = ?2
     */
    Optional<Producto> findByIdAndActivo(Long id, String activo);

    /**
     * Spring Data genera automáticamente:
     * SELECT COUNT(*) > 0 FROM productos WHERE UPPER(nombre) = UPPER(?1) AND activo = ?2
     */
    boolean existsByNombreIgnoreCaseAndActivo(String nombre, String activo);
}