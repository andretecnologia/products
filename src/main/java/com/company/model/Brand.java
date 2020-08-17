package com.company.model;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Brand extends BaseModel {
    
    @NonNull
    private String name;

    @Builder
    public Brand(String id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String name) {
        super(id, createdDate, lastModifiedDate);
        this.name = name;
    }
}
