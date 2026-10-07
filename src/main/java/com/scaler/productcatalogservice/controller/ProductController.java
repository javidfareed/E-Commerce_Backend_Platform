package com.scaler.productcatalogservice.controller;


import com.scaler.productcatalogservice.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    //API to get all products

    @GetMapping("/products")
    List<Product> getAllProducts()
    {
        return List.of();
    }
}
