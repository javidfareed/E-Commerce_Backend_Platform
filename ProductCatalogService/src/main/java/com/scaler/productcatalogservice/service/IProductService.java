package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IProductService {

    Product getProductById(UUID id);

    List<Product> getAllProducts();

    Product createProduct(ProductDto productDto);

    boolean deleteProductById(UUID id);

    Product updateProduct(ProductDto productDto, UUID id);

    Product updateProductField(ProductDto productDto, UUID id);

    Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

}
