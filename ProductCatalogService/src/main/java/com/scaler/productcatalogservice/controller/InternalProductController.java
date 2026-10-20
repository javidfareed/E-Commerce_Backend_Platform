package com.scaler.productcatalogservice.controller;

import com.scaler.productcatalogservice.service.ProductImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

    private final ProductImportService productImportService;

    public InternalProductController(ProductImportService productImportService) {
        this.productImportService = productImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts()
    {
        int count = productImportService.importProducts();

        return ResponseEntity.ok( "Imported " + count + " Products");
    }
}
