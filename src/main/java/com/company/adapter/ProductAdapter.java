package com.company.adapter;

import com.company.controller.dto.BrandDTO;
import com.company.controller.dto.ProductDTO;
import com.company.model.Brand;
import com.company.model.Product;

import java.math.BigDecimal;
import java.util.Objects;


// TODO colocar @Componet e adicionar o @Autowired do brand service

public class ProductAdapter {

    public static Product dtoToEntity(ProductDTO dto){
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description((dto.getDescription()))
                .price((Objects.nonNull(dto.getPrice()) ? new BigDecimal(dto.getPrice()) : null))
                .brand( Brand.builder()
                        .id(Objects.nonNull(dto.getBrand()) ? dto.getBrand().getId() : null)
                        .name(Objects.nonNull(dto.getBrand()) ? dto.getBrand().getName() : null)
                        .build())
            .build();
    }

    public static ProductDTO entityToDto(Product entity){
        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description((entity.getDescription()))
                .price(Objects.nonNull(entity.getPrice()) ? entity.getPrice().toString() : null)
                .brand( BrandDTO.builder()
                        .id(Objects.nonNull(entity.getBrand()) ? entity.getBrand().getId() : null)
                        .name(Objects.nonNull(entity.getBrand()) ? entity.getBrand().getName() : null)
                    .build())
            .build();
    }
}
