package com.scaler.productcatalogservice.controller;


import com.scaler.productcatalogservice.dto.CategoryDto;
import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.dto.ProductPitchDto;
import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.service.IProductService;
import com.scaler.productcatalogservice.service.PitchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {


    private final IProductService productService;
    private final PitchService pitchService;
    private final IProductService supplierService;

    public ProductController(IProductService productService, PitchService pitchService, @Qualifier("dummyProductService") IProductService supplierService) {
        this.productService = productService;
        this.pitchService = pitchService;
        this.supplierService = supplierService;
    }

    //API to get all products
    @GetMapping
    ResponseEntity<List<ProductDto>> getAllProducts()
    {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products)
        {
            productDtos.add(from(product));
        }

        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id)
    {
        if(id < 0)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product product = productService.getProductById(id);
        if(product ==  null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @GetMapping("/{id}/pitch")
    ResponseEntity<ProductPitchDto> generatePitch(@PathVariable("id") Long id)
    {
       if(id<0)
       {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       Product product = productService.getProductById(id);
       if(product == null)
       {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

       ProductPitchDto productPitchDto = new ProductPitchDto();
       productPitchDto.setId(product.getId());
       productPitchDto.setPitch(pitchService.generatePitch(product));

       return new ResponseEntity<>(productPitchDto,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
       Product product = productService.createProduct(productDto);
       return new ResponseEntity<>(from(product),HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProductById(@PathVariable Long id)
    {
        boolean isDeleted = productService.deleteProductById(id);

        if(isDeleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable Long id)
    {
        try {
            Product product = productService.updateProduct(productDto, id);
            if (product == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(from(product), HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{id}")
    ResponseEntity<ProductDto> updateProductField(@RequestBody ProductDto productDto,@PathVariable Long id)
    {
        try {
            Product product = productService.updateProductField(productDto, id);
            if (product == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(from(product), HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/search")
    PagedModel<ProductDto> getProductsInPriceRange(@RequestParam(required = false) BigDecimal minPrice,
                                                   @RequestParam(required = false) BigDecimal maxPrice, Pageable pageable)
    {
            Page<ProductDto> products = productService.getProductsInPriceRange(minPrice,maxPrice,pageable)
                    .map(x -> from(x));

            return new PagedModel<>(products);
    }


    private ProductDto from(Product product)
    {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());

        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }

}
