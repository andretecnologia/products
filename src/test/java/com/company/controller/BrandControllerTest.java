package com.company.controller;

import com.company.exception.InvalidFieldException;
import com.company.mock.BrandData;
import com.company.model.Brand;
import com.company.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BrandController.class)
@AutoConfigureMockMvc
public class BrandControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BrandService service;

    @Test
    public void insertBrand() throws Exception {
        String id = "id";
        Brand brand = BrandData.getBrandMock();
        Brand returnedBrand = BrandData.getBrandMock(id);
        when(service.insertBrand(eq(brand))).thenReturn(returnedBrand);

        mvc.perform(MockMvcRequestBuilders.post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(brand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(returnedBrand.getName())))
                .andReturn();

    }

    @Test
    public void insertBrandWithId() throws Exception {
        String id = "id";
        Brand brand = BrandData.getBrandMock(id);
        when(service.insertBrand(eq(brand))).thenThrow(InvalidFieldException.class);

        mvc.perform(MockMvcRequestBuilders.post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(brand)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getAll() throws Exception {
        List<Brand> brands = BrandData.getBrandsMock();
        when(service.findAll()).thenReturn(brands);

        mvc.perform(MockMvcRequestBuilders.get("/brands")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(brands.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(brands.get(0).getName())))
                .andExpect(jsonPath("$[1].id", is(brands.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(brands.get(1).getName())))
                .andExpect(jsonPath("$[2].id", is(brands.get(2).getId())))
                .andExpect(jsonPath("$[2].name", is(brands.get(2).getName())))
                .andReturn();

    }

    @Test
    public void getById() throws Exception {
        String id = "id";
        Brand brand = BrandData.getBrandMock(id);
        when(service.findById(eq(id))).thenReturn(brand);

        mvc.perform(MockMvcRequestBuilders.get("/brands/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brand.getId())))
                .andExpect(jsonPath("$.name", is(brand.getName())))
                .andReturn();
    }
}