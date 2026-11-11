package com.scaler.productcatalogservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductPitchDto {

    private UUID id;
    private String pitch;
}
