package com.scaler.productcatalogservice.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseEntity{

    private String name;

    private String description;

    private double price;

    private String imageUrl;

    private Category category;
}
