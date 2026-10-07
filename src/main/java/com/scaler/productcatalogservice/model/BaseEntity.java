package com.scaler.productcatalogservice.model;

import com.scaler.productcatalogservice.model.enums.State;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public abstract class BaseEntity {

    private int id;

    private Date createdAt;

    private Date lastUpdatedAt;

    private State state;
}
