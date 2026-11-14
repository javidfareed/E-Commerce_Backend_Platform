package com.scaler.productcatalogservice.ServiceTests;

import com.scaler.productcatalogservice.dto.ProductDto;
import com.scaler.productcatalogservice.mapper.ProductMapper;
import com.scaler.productcatalogservice.model.Category;
import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.repository.CategoryRepository;
import com.scaler.productcatalogservice.repository.ProductRepository;
import com.scaler.productcatalogservice.service.StorageProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StorageProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private StorageProductService service;

    @Test
    void createProduct_clientSendsAnId_savesWithoutItAndWithDates() {
        ProductDto input = new ProductDto();
        input.setId(UUID.randomUUID());
        input.setName("Mascara");

        Product product = new Product();
        product.setName("Mascara");
        product.setCategory(new Category());

        when(productRepository.save(any(Product.class)))
                .thenAnswer(call -> call.getArgument(0));

        when(productMapper.toEntity(any(ProductDto.class)))
                .thenReturn(product);

        Product result = service.createProduct(input);

        assertThat(result.getName()).isEqualTo("Mascara");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getLastUpdatedAt()).isNotNull();
    }
}

// Unit tests
// integration tests - E2E don't mock the externals calls

// search product - catch minPrice maxPrice bug
