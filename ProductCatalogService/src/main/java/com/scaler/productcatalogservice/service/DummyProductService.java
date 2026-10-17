package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.DummyJsonProductDto;
import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Category;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DummyProductService implements IProductService {


    private RestTemplate restTemplate;

    public DummyProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Product getProductById(Long id) {

        try {
            String url = "https://dummyjson.com/products/{id}";
            ResponseEntity<DummyJsonProductDto> response = restTemplate.getForEntity(url, DummyJsonProductDto.class,id);
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody())
            {
                return from(response.getBody());
            }

            return null;
        } catch (HttpClientErrorException.NotFound e) {

            return null;

        }


    }

    @Override
    public Product createProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public boolean deleteProductById(Long id) {
        return false;
    }

    @Override
    public Product updateProduct(ProductDto productDto, Long id) {
        return null;
    }

    @Override
    public Product updateProductField(ProductDto productDto, Long id) {
        return null;
    }

    private Product from(DummyJsonProductDto dto)
    {
        Product product = new Product();
        product.setId(dto.getId());
        product.setDescription(dto.getDescription());
        product.setName(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getThumbnail());

        Category category = new Category();
        category.setName(dto.getCategory());
        product.setCategory(category);

        return product;
    }
}
