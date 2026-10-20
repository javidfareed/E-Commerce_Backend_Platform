package com.scaler.productcatalogservice.repository;

import com.scaler.productcatalogservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);

    @Query(value = "select avg(p.price) from Product p where p.category.name =:categoryName")
    Double findAvgPriceForCategory(String categoryName);
}
