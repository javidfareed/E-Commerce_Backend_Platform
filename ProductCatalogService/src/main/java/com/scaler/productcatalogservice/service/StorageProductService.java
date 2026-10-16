package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@Slf4j
public class StorageProductService implements IProductService{

    private ProductRepository productRepository;

    public StorageProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(Long id) {

        log.info("Reading from DB " + id);
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
}
