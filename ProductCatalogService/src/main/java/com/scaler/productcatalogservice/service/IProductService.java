package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product createProduct(ProductDto productDto);

    boolean deleteProductById(Long id);

    Product updateProduct(ProductDto productDto, Long id);

    Product updateProductField(ProductDto productDto, Long id);

    Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

}
