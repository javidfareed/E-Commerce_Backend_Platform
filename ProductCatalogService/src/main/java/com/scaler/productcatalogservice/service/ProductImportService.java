package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.CategoryDto;
import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImportService {

    private IProductService storageService;
    private IProductService supplierService;

    public ProductImportService(IProductService storageService, @Qualifier("dummyProductService") IProductService supplierService) {
        this.storageService = storageService;
        this.supplierService = supplierService;
    }

    public int importProducts() {

       int count = 0;
       List<Product> supplierProducts = supplierService.getAllProducts();
       for(Product product : supplierProducts)
       {
          ProductDto dto = from(product);
          storageService.createProduct(dto);
          count++;
       }

       return count;
    }

    private ProductDto from(Product product)
    {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());

        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }
}
