package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Category;
import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.model.enums.State;
import com.scaler.productcatalogservice.repository.CategoryRepository;
import com.scaler.productcatalogservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Primary
@Slf4j
public class StorageProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public StorageProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long id) {

        log.info("Reading from DB " + id);
        return productRepository.findByIdAndState(id,State.ACTIVE).orElse(null);
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        //Dto --> entity
        //Category check and save to repo

        Product product = from(productDto);
        product.setCategory(findOrCreateCategory(product.getCategory()));
        product.setCreatedAt(new Date());
        product.setLastUpdatedAt(new Date());
        product.setState(State.ACTIVE);

        return productRepository.save(product);

    }

    @Override
    public boolean deleteProductById(Long id) {

        Product product = productRepository.findById(id).orElse(null);
        if(product == null)
        {
            return false;
        }
        product.setState(State.INACTIVE);
        product.setLastUpdatedAt(new Date());
        productRepository.save(product);
        return true;

    }

    private Category findOrCreateCategory(Category category) {
        if(category == null)
        {
            return null;
        }

        Optional<Category> existing = categoryRepository.findByName(category.getName());
        if(existing.isPresent())
        {
            return existing.get();
        }

        category.setCreatedAt(new Date());
        category.setLastUpdatedAt(new Date());
        category.setState(State.ACTIVE);

        return category;
    }


    private Product from(ProductDto dto)
    {
         Product product = new Product();
         product.setName(dto.getName());
         product.setDescription(dto.getDescription());
         product.setPrice(dto.getPrice());
         product.setImageUrl(dto.getImageUrl());

         if(dto.getCategory() != null)
         {
             Category category = new Category();
             category.setName(dto.getCategory().getName());
             category.setDescription(dto.getCategory().getDescription());
             product.setCategory(category);
         }

         return product;
    }
}
