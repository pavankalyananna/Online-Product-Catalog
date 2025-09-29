package com.productcatalog.controller;

import com.productcatalog.dto.ApiResponse;
import com.productcatalog.dto.ProductDTO;
import com.productcatalog.entity.Product;
import com.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            Product createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdProduct, "Product created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating product: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Product> products = productService.getAllProducts(pageable);
            return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(product.get(), "Product retrieved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(@PathVariable String sku) {
        try {
            Optional<Product> product = productService.getProductBySku(sku);
            if (product.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(product.get(), "Product retrieved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found with SKU: " + sku));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Product>>> getActiveProducts() {
        try {
            List<Product> products = productService.getActiveProducts();
            return ResponseEntity.ok(ApiResponse.success(products, "Active products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving active products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String keyword) {
        try {
            List<Product> products = productService.searchProducts(keyword);
            return ResponseEntity.ok(ApiResponse.success(products, "Search results retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error searching products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        try {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving products: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating product: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Product>> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        try {
            Product updatedProduct = productService.updateProductStock(id, stock);
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product stock updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating product stock: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Product>> activateProduct(@PathVariable Long id) {
        try {
            Product updatedProduct = productService.activateProduct(id);
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product activated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error activating product: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Product>> deactivateProduct(@PathVariable Long id) {
        try {
            Product updatedProduct = productService.deactivateProduct(id);
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product deactivated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deactivating product: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/validate-sku")
    public ResponseEntity<ApiResponse<Boolean>> validateSku(
            @RequestParam String sku,
            @RequestParam(required = false) Long id) {
        try {
            boolean isUnique;
            if (id != null) {
                isUnique = productService.isSkuUniqueForUpdate(sku, id);
            } else {
                isUnique = productService.isSkuUnique(sku);
            }
            return ResponseEntity.ok(ApiResponse.success(isUnique, "SKU validation completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error validating SKU: " + e.getMessage()));
        }
    }
}