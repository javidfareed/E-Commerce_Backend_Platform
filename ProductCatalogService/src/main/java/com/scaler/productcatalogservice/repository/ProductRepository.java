package com.scaler.productcatalogservice.repository;

import com.scaler.productcatalogservice.model.Product;
import com.scaler.productcatalogservice.model.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByIdAndState(Long id, State state);

}
