package com.company.adapter;

import com.company.controller.dto.BrandDTO;
import com.company.model.Brand;

public class BrandAdapter {

    public static BrandDTO entityToDto(Brand entity) {
        return BrandDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static Brand dtoToEntity(BrandDTO dto){
        return Brand.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
