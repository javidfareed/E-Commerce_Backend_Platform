package com.scaler.productcatalogservice.model;


//category will be later highly modifiable, hence better to create them as a separate table instead of enum

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category extends BaseEntity {

    private String name;

    private String description;

    private List<Product> products;
}
