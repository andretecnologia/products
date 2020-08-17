package com.company.mock;

import com.company.model.Brand;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BrandData {

    public static final String BRAND_ONE = "brand1";

    public static Brand getBrandMock() {
        return Brand.builder()
                .name(BRAND_ONE)
                .build();
    }

    public static Brand getBrandMock(String id) {
        return Brand.builder()
                .id(id)
                .name(BRAND_ONE)
                .build();
    }

    public static List<Brand> getBrandsMock() {
        return Arrays.asList(getBrandMock(UUID.randomUUID().toString()),
                getBrandMock(UUID.randomUUID().toString()),
                getBrandMock(UUID.randomUUID().toString()));
    }
}
