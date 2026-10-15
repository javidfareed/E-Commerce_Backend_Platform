package com.scaler.productcatalogservice.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product extends BaseEntity{

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private Category category;
}
