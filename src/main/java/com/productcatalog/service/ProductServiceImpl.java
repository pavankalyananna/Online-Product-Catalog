package com.productcatalog.service;

import com.productcatalog.dto.ProductDTO;
import com.productcatalog.entity.Product;
import com.productcatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public Product createProduct(ProductDTO productDTO) {
      
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty()) {
            if (!isSkuUnique(productDTO.getSku())) {
                throw new IllegalArgumentException("SKU must be unique. Product with SKU '" + productDTO.getSku() + "' already exists.");
            }
        }
        
        Product product = new Product();
        mapDTOToEntity(productDTO, product);
        
        return productRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsWithMinimumStock(Integer minStock) {
        return productRepository.findByStockQuantityGreaterThan(minStock);
    }
    
    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        
        Product existingProduct = existingProductOpt.get();
        
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty()) {
            if (!isSkuUniqueForUpdate(productDTO.getSku(), id)) {
                throw new IllegalArgumentException("SKU must be unique. Another product with SKU '" + productDTO.getSku() + "' already exists.");
            }
        }
        
        mapDTOToEntity(productDTO, existingProduct);
        return productRepository.save(existingProduct);
    }
    
    @Override
    public Product updateProductStock(Long id, Integer newStock) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        
        Product product = productOpt.get();
        product.setStockQuantity(newStock);
        return productRepository.save(product);
    }
    
    @Override
    public Product activateProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        
        Product product = productOpt.get();
        product.setIsActive(true);
        return productRepository.save(product);
    }
    
    @Override
    public Product deactivateProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        
        Product product = productOpt.get();
        product.setIsActive(false);
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    @Override
    public boolean isSkuUnique(String sku) {
        if (sku == null || sku.isEmpty()) {
            return true; 
        }
        return !productRepository.existsBySku(sku);
    }
    
    @Override
    public boolean isSkuUniqueForUpdate(String sku, Long id) {
        if (sku == null || sku.isEmpty()) {
            return true;
        }
        return !productRepository.existsBySkuAndIdNot(sku, id);
    }
    
    private void mapDTOToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setCategory(dto.getCategory());
        entity.setBrand(dto.getBrand());
        entity.setSku(dto.getSku());
        
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
    }
}