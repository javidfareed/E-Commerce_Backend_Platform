package com.scaler.productcatalogservice.controller;


import com.scaler.productcatalogservice.dto.CategoryDto;
import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.dto.ProductPitchDto;
import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.service.IProductService;
import com.scaler.productcatalogservice.service.PitchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {


    private IProductService productService;
    private PitchService pitchService;

    public ProductController(IProductService productService, PitchService pitchService) {
        this.productService = productService;
        this.pitchService = pitchService;
    }

    //API to get all products
    @GetMapping
    List<Product> getAllProducts()
    {
        return List.of();
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





    private ProductDto from(Product product)
    {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(product.getCategory().getName());
        categoryDto.setDescription(product.getCategory().getDescription());

        productDto.setCategory(categoryDto);

        return productDto;
    }

}
