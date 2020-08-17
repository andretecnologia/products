package com.company.repository;

import com.company.group.Integration;
import com.company.model.Brand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Category(Integration.class)
public class BrandRepositoryIntegrationTest {

    private static final String BRAND_ONE = "Brand 1";
    private static final String NEW_BRAND = "New Brand";

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    BrandRepository repository;

    private List<String> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        if (!mongoOperations.collectionExists(Brand.class)) {
            mongoOperations.createCollection(Brand.class);
        }
    }

    @After
    public void tearDown() {
        mongoOperations.remove(Query.query(Criteria.where("id").in(idsToDelete)), Brand.class);
        idsToDelete.clear();
    }

    @Test
    public void whenInsertingBrandThenBrandIsInserted() {
        Brand brand = createBrand(BRAND_ONE);

        String id = repository.save(brand).getId();
        idsToDelete.add(id);

        Brand expectedBrand = mongoOperations.findById(id, Brand.class);
        assertEquals(expectedBrand.getName(), brand.getName());
        assertEquals(expectedBrand.getId(), brand.getId());
    }

    @Test
    public void givenBrandExistsWhenSavingBrandThenBrandIsUpdated() {
        Brand brand = createBrand(BRAND_ONE);
        String id = mongoOperations.insert(brand).getId();
        idsToDelete.add(id);

        brand = repository.findByNameIgnoreCase("brand 1").get();
        brand.setName(NEW_BRAND);
        repository.save(brand);

        Brand savedBrand = mongoOperations.findById(id, Brand.class);
        assertThat(NEW_BRAND, is(savedBrand.getName()));
    }

    @Test
    public void givenBrandExistsWhenDeletingBrandThenBrandIsRemoved() {
        Brand brand = createBrand(BRAND_ONE);
        String id = mongoOperations.insert(brand).getId();

        repository.deleteById(id);

        Brand dbBrand = mongoOperations.findById(id, Brand.class);
        assertNull(dbBrand);
    }

    private Brand createBrand(String name) {
        Brand brand = Brand.builder()
                .name(name)
                .build();
        return brand;
    }
}