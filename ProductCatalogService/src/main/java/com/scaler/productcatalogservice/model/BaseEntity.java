package com.scaler.productcatalogservice.model;

import com.scaler.productcatalogservice.model.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    private Date createdAt;

    private Date lastUpdatedAt;

    @Enumerated(value = EnumType.STRING)
    private State state;
}
