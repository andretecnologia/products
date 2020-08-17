package com.company.mock;

import com.company.controller.dto.ProductDTO;
import com.company.model.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ProductData {

    public static final String NAME = "test";
    public static final String DESCRIPTION = "desc";
    public static final String PRICE = "0";

    public static Product getProductMock() {
        return Product.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .price(new BigDecimal( PRICE ))
                .build();
    }

    public static ProductDTO getProductDTOMock() {
        return ProductDTO.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();
    }

    public static ProductDTO getProductDTOMock(String id) {
        return ProductDTO.builder()
                .id(id)
                .name(NAME)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();
    }

    public static Product getProductMock(String id) {
        return Product.builder()
                .id(id)
                .name(NAME)
                .description(DESCRIPTION)
                .price(new BigDecimal( PRICE ))
                .build();
    }

    public static List<Product> getProductsMock() {
        return Arrays.asList(getProductMock(), getProductMock(), getProductMock());
    }

    public static List<ProductDTO> getProductsDTOMock() {
        return Arrays.asList(getProductDTOMock(), getProductDTOMock(), getProductDTOMock());
    }

    public static List<Product> getProductsMock(String id) {
        int i = 1;
        return Arrays.asList(getProductMock(id + i++), getProductMock(id + i++), getProductMock(id + i++));
    }
}
