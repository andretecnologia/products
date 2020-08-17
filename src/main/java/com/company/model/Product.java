package com.company.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseModel {

    private String name;
    private String description;
    private BigDecimal price;

    @DBRef
    private Brand brand;

    @Builder
    public Product(String id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String name,
                   String description, BigDecimal price, Brand brand) {
        super(id, createdDate, lastModifiedDate);
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
    }
}
