package com.company.controller;

import com.company.adapter.ProductAdapter;
import com.company.controller.dto.ProductDTO;
import com.company.exception.DuplicatedNameException;
import com.company.exception.EntityNotFoundException;
import com.company.mock.ProductData;
import com.company.model.Product;
import com.company.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    private ProductService service;

    @Test
    public void insertProduct() throws Exception {
        String productId = "Id";
        ProductDTO product = ProductData.getProductDTOMock();
        Product returnedProduct = ProductData.getProductMock(productId);
        when(service.insert(eq(ProductAdapter.dtoToEntity(product)))).thenReturn(returnedProduct);

        mvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is(returnedProduct.getName())))
                .andExpect(jsonPath("$.description", is(returnedProduct.getDescription())))
                .andExpect(jsonPath("$.price", is(returnedProduct.getPrice().toString())))
                .andReturn();
    }

    @Test
    public void insertExistentProduct() throws Exception {
        ProductDTO product = ProductData.getProductDTOMock();
        when(service.insert(eq(ProductAdapter.dtoToEntity(product)))).thenThrow(DuplicatedNameException.class);

        mvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isConflict())
                .andReturn();
    }


    @Test
    public void updateProduct() throws Exception {
        String productId = "Id";
        Product returnedProduct = ProductData.getProductMock(productId);
        ProductDTO product = ProductData.getProductDTOMock(productId);
        when(service.update(eq(ProductAdapter.dtoToEntity(product)))).thenReturn(returnedProduct);

        mvc.perform(MockMvcRequestBuilders.put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.price", is(product.getPrice().toString())))
                .andReturn();
    }

    @Test
    public void updateNonExistentProduct() throws Exception {
        String productId = "Id";
        ProductDTO product = ProductData.getProductDTOMock(productId);
        when(service.update(eq(ProductAdapter.dtoToEntity(product)))).thenThrow(EntityNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateThrowsException() throws Exception {
        String productId = "Id";
        Product product = ProductData.getProductMock(productId);
        when(service.update(eq(product))).thenThrow(RuntimeException.class);

        mvc.perform(MockMvcRequestBuilders.put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    public void getProductsWithDefaultPaging() throws Exception {
        List<Product> products = ProductData.getProductsMock("id");
        Page page = new PageImpl(new ArrayList(), PageRequest.of(0, 1), 0);

        when(service.findAll(any())).thenReturn(page);

        mvc.perform(MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(service, times(1)).findAll(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        Assert.assertThat(pageable.getPageSize(), is(20));
        Assert.assertThat(pageable.getPageNumber(), is(0));
    }

    @Test
    public void getProductsWithCustomPaging() throws Exception {
        List<Product> products = ProductData.getProductsMock("id");
        Page page = new PageImpl(new ArrayList(), PageRequest.of(1, 2), 0);

        when(service.findAll(any())).thenReturn(page);

        mvc.perform(MockMvcRequestBuilders.get("/products?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(service, times(1)).findAll(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        Assert.assertThat(pageable.getPageSize(), is(2));
        Assert.assertThat(pageable.getPageNumber(), is(1));
    }

    @Test
    public void deleteProduct() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/products/123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void deleteInexistentProduct() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).delete(anyString());

        mvc.perform(MockMvcRequestBuilders.delete("/products/123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}