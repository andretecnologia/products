package com.company.repository;

import com.company.group.Integration;
import com.company.model.Product;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Category(Integration.class)
public class ProductRepositoryIntegrationTest {

    private static final String PRODUCT_ONE = "Product 1";
    private static final String NAME = "name1";
    private static final String PRICE = "0.00";
    
    private static final String NEW_PRODUCT = "New Product";

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    ProductRepository repository;

    private List<String> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        if (!mongoOperations.collectionExists(Product.class)) {
            mongoOperations.createCollection(Product.class);
        }
    }

    @After
    public void tearDown() {
        mongoOperations.remove(Query.query(Criteria.where("id").in(idsToDelete)), Product.class);
        idsToDelete.clear();
    }

    @Test
    public void whenInsertingProductThenProductIsInserted() {
        Product product = createProduct(PRODUCT_ONE, NAME, PRICE);

        String id = repository.save(product).getId();
        idsToDelete.add(id);

        product.setCreatedDate(product.getCreatedDate().withNano(0));
        product.setLastModifiedDate(product.getLastModifiedDate().withNano(0));

        Product expectedProduct = mongoOperations.findById(id, Product.class);
        expectedProduct.setCreatedDate(expectedProduct.getCreatedDate().withNano(0));
        expectedProduct.setLastModifiedDate(expectedProduct.getLastModifiedDate().withNano(0));
        assertEquals(expectedProduct, product);
    }

    @Test
    public void givenProductExistsWhenSavingProductThenProductIsUpdated() {
        Product product = createProduct(PRODUCT_ONE, NAME, PRICE);
        String id = mongoOperations.insert(product).getId();
        idsToDelete.add(id);

        product = repository.findByNameIgnoreCase("product 1").get();
        product.setName(NEW_PRODUCT);
        repository.save(product);

        Product savedProduct = mongoOperations.findById(id, Product.class);
        assertThat(NEW_PRODUCT, is(savedProduct.getName()));
    }

    @Test
    public void givenProductExistsWhenDeletingProductThenProductIsRemoved() {
        Product product = createProduct(PRODUCT_ONE, NAME, PRICE);
        String id = mongoOperations.insert(product).getId();

        repository.deleteById(id);

        Product dbProduct = mongoOperations.findById(id, Product.class);
        assertNull(dbProduct);
    }

    private Product createProduct(String name, String description, String price) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(new BigDecimal(price))
                .build();
        return product;
    }
}