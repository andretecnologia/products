package com.company.service;

import com.company.exception.DuplicatedNameException;
import com.company.exception.EntityNotFoundException;
import com.company.exception.InvalidFieldException;
import com.company.mock.ProductData;
import com.company.model.Product;
import com.company.repository.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductCompanyServiceTest {

    @Mock
    private ProductRepository repository;

    private ProductService service;

    @Before
    public void setup() {
        service = new ProductService(repository);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void givenAnyProductWhenSavingProductThenProductIsSavedAndReturned() throws Exception {
        Product product = ProductData.getProductMock();
        when(repository.findByNameContaining(eq(product.getPrice().toString()))).thenReturn(Optional.empty());
        when(repository.insert(eq(product))).thenReturn(product);

        Product returnedProduct = service.insert(product);

        assertNotNull(returnedProduct);
        assertEquals(product, returnedProduct);
        verify(repository, times(1)).findByNameContaining(eq(product.getName()));
        verify(repository, times(1)).insert(eq(product));
    }

    @Test(expected = DuplicatedNameException.class)
    public void givenAProductWithAnExistentProductnameWhenInsertingProductThenExceptionIsThrown() throws Exception {
        Product product = ProductData.getProductMock();
        when(repository.findByNameContaining(anyString())).thenReturn(Optional.of(product));

        service.insert(product);

        expectedException.expect(DuplicatedNameException.class);
        expectedException.expectMessage(String.format("Productname %s already exist", product.getPrice()));
        verify(repository, times(1)).findByNameContaining(eq(product.getPrice().toString()));
        verify(repository, times(0)).insert(eq(product));
    }

    @Test
    public void givenAProductListWhenSavingProductsThenProductsAreSavedAndReturned() throws Exception {
        List<Product> products = ProductData.getProductsMock();
        when(repository.findByNameContaining(eq(products.get(0).getPrice().toString()))).thenReturn(Optional.empty());
        when(repository.insert(eq(products))).thenReturn(products);

        List<Product> returnedProducts = service.insert(products);

        assertNotNull(returnedProducts);
        assertEquals(products, returnedProducts);
        verify(repository, times(3)).findByNameContaining(eq(products.get(0).getName()));
        verify(repository, times(1)).insert(eq(products));
    }

    @Test(expected = InvalidFieldException.class)
    public void givenAProductWithAnIdSetWhenInsertingProductThenExceptionIsThrown() throws Exception {
        Product product = ProductData.getProductMock();
        product.setId("test");

        service.insert(product);

        expectedException.expect(InvalidFieldException.class);
        expectedException.expectMessage("Id is an invalid parameter for the insert action");
        verify(repository, times(0)).findByNameContaining(eq(product.getPrice().toString()));
        verify(repository, times(0)).insert(eq(product));
    }

    @Test
    public void givenAnyProductWhenUpdatingProductThenProductIsUpdatedAndReturned() throws Exception {
        String id = "Id";
        Product product = ProductData.getProductMock();
        product.setId(id);

        when(repository.findById(eq(product.getId()))).thenReturn(Optional.of(product));
        when(repository.save(eq(product))).thenReturn(product);

        Product returnedProduct = service.update(product);

        assertNotNull(returnedProduct);
        assertEquals(product, returnedProduct);
        verify(repository, times(2)).findById(eq(product.getId()));
        verify(repository, times(1)).save(eq(product));
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenANonExistentProductWhenSavingProductThenExceptionIsThrown() throws Exception {
        Product product = ProductData.getProductMock();

        service.update(product);

        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("Product %s not found", product.getId()));
        verify(repository, times(1)).findById(eq(product.getId()));
        verify(repository, times(0)).save(eq(product));
    }

    @Test
    public void givenAProductIdWhenSearchingProductByIdThenReturnFoundProduct() throws Exception {
        String id = "id";

        Product product = ProductData.getProductMock();
        product.setId(id);
        when(repository.findById(eq(id))).thenReturn(Optional.of(product));

        Product returnedProduct = service.findById(id);

        verify(repository, times(1)).findById(id);
        assertEquals(returnedProduct, product);
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenAnInexistentProductIdWhenSearchingProductByIdThenThrowAndException() throws Exception {
        String id = "id";

        when(repository.findById(eq(id))).thenReturn(Optional.empty());

        service.findById(id);

        verify(repository, times(1)).findById(id);
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("Product %s not found", id));
    }

    @Test
    public void givenAnProductIdWhenDeletingProductWithIdThenProductIsDeleted() throws Exception {
        String id = "id";

        Product product = ProductData.getProductMock();
        product.setId(id);
        when(repository.findById(eq(id))).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(eq(id));

        service.delete(id);

        verify(repository, times(1)).findById(eq(id));
        verify(repository, times(1)).deleteById(eq(id));
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenAnInvalidProductIdWhenDeletingProductWithIdThenExceptionIsThrown() throws Exception {
        String id = "id";

        when(repository.findById(eq(id))).thenReturn(Optional.empty());

        service.delete(id);

        verify(repository, times(1)).findById(eq(id));
        verify(repository, times(0)).deleteById(eq(id));

        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(String.format("Product %s not found", id));
    }
}