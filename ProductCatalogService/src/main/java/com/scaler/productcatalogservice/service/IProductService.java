package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Product;

public interface IProductService {

    Product getProductById(Long id);

    Product createProduct(ProductDto productDto);

    boolean deleteProductById(Long id);

    Product updateProduct(ProductDto productDto, Long id);

    Product updateProductField(ProductDto productDto, Long id);
}
