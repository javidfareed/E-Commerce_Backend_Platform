package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.DummyJsonProductDto;
import com.scaler.productcatalogservice.dto.DummyJsonProductListDto;
import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.model.Category;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DummyProductService implements IProductService {


    private final RestClient restClient;

    public DummyProductService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    //Below method will not work as we changed from Long to UUID - returns 404 always
    //dummyjson only knows its own numbers, not our UUIDs, so it cannot look a product up by our id
    @Override
    public Product getProductById(UUID id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {

        String url = "https://dummyjson.com/products?limit=0";

        DummyJsonProductListDto response = restClient.get()
                .uri(url)
                .retrieve()
                .body(DummyJsonProductListDto.class);

        List<Product> products = new ArrayList<>();

        List<DummyJsonProductDto> dummyJsonProductDtos = response.getProducts();

        for(DummyJsonProductDto dto : dummyJsonProductDtos)
        {
            products.add(from(dto));
        }

        return products;

    }

    @Override
    public Product createProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public boolean deleteProductById(UUID id) {
        return false;
    }

    @Override
    public Product updateProduct(ProductDto productDto, UUID id) {
        return null;
    }

    @Override
    public Product updateProductField(ProductDto productDto, UUID id) {
        return null;
    }

    @Override
    public Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return null;
    }

    private Product from(DummyJsonProductDto dto)
    {
        Product product = new Product();
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
