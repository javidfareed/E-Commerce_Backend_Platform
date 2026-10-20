package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService{

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Double getAvgPriceForCategory(String category) {
       return categoryRepository.findAvgPriceForCategory(category);
    }
}
