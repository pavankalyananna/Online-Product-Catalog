package com.productcatalog.repository;

import com.productcatalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    List<Product> findByCategory(String category);
    
    List<Product> findByIsActiveTrue();
    
    List<Product> findByCategoryAndIsActiveTrue(String category);
    
    List<Product> findByBrand(String brand);
    
    Optional<Product> findBySku(String sku);
    List<Product> findByPriceLessThanEqual(Double maxPrice);
    
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    List<Product> findByStockQuantityGreaterThan(Integer minStock);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
}