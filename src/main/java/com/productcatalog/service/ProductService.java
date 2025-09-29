package com.productcatalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.productcatalog.dto.ProductDTO;
import com.productcatalog.entity.Product;

public interface ProductService {
    
    Product createProduct(ProductDTO productDTO);
    
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
    Optional<Product> getProductById(Long id);
    Optional<Product> getProductBySku(String sku);
    List<Product> getProductsByCategory(String category);
    List<Product> getActiveProducts();
    List<Product> searchProducts(String keyword);
    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);
    List<Product> getProductsWithMinimumStock(Integer minStock);
    
    Product updateProduct(Long id, ProductDTO productDTO);
    Product updateProductStock(Long id, Integer newStock);
    Product activateProduct(Long id);
    Product deactivateProduct(Long id);
    
    void deleteProduct(Long id);
    
    boolean isSkuUnique(String sku);
    boolean isSkuUniqueForUpdate(String sku, Long id);
}