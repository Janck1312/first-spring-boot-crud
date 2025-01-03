package com.ve.inventory_billing.repositories;

import com.ve.inventory_billing.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository <Product, Integer> {
    Optional<Product> findByCode(String code);

    @Query(value = "SELECT * FROM products WHERE name LIKE '%:search%' OR code LIKE '%:search%' AND deleted_at IS NULL LIMIT :offset, :showRows", nativeQuery = true)
    List<Product> pagination(@Param("search") String search, @Param("offset") Integer offset, @Param("showRows") Integer showRows);

    @Query(value = "SELECT * FROM products WHERE deleted_at IS NULL LIMIT :offset, :showRows", nativeQuery = true)
    List<Product> pagination(@Param("offset") Integer offset, @Param("showRows") Integer showRows);
}
