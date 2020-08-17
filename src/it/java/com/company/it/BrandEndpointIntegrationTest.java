package com.company.it;

import com.company.group.Integration;
import com.company.mock.BrandData;
import com.company.model.Brand;
import com.company.repository.BrandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Category(Integration.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class BrandEndpointIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BrandRepository brandRepository;

    @After
    @Before
    public void setup() {
        brandRepository.deleteAll();
    }

    @Test
    public void givenAnEmptyDatabaseWhenGettingAllBrandsThenAEmptyListIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/brands")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @Test
    public void givenANonEmptyDatabaseWhenGettingAllBrandsthenAListIsReturned() throws Exception {

        List<Brand> brands = BrandData.getBrandsMock();
        brandRepository.insert(brands);

        mvc.perform(MockMvcRequestBuilders.get("/brands")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(brands.size())))
                .andReturn();
    }
}
