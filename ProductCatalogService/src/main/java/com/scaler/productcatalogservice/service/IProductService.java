package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.model.Product;

public interface IProductService {

  Product getProductById(Long id);
}
