package com.company.service;

import com.company.exception.EntityNotFoundException;
import com.company.exception.InvalidFieldException;
import com.company.mock.BrandData;
import com.company.model.Brand;
import com.company.repository.BrandRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BrandCompanyServiceTest {

    @Mock
    private BrandRepository repository;

    @InjectMocks
    private BrandService service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void givenAnyBrandWhenInsertingBrandThenBrandIsInsertedAndReturned() throws Exception {

        Brand brand = BrandData.getBrandMock();
        when(repository.insert(eq(brand))).thenReturn(brand);

        Brand returnedBrand = service.insertBrand(brand);

        assertNotNull(returnedBrand);
        assertEquals(brand, returnedBrand);
        verify(repository, times(1)).insert(eq(brand));
    }

    @Test(expected = InvalidFieldException.class)
    public void givenABrandWithIdSetWhenInsertingBrandThenExceptionIsThrown() throws Exception {

        Brand brand = BrandData.getBrandMock("id");

        service.insertBrand(brand);

        expectedException.expectMessage("Id is an invalid parameter for the insert action");
        expectedException.expect(InvalidFieldException.class);
        verify(repository, times(0)).insert(eq(brand));
    }

    @Test
    public void whenFindingAllBrandsThenAllBrandsAreReturned() throws Exception {
        List<Brand> brands = BrandData.getBrandsMock();
        when(repository.findAll()).thenReturn(brands);

        List<Brand> returnedBrands = service.findAll();

        assertNotNull(returnedBrands);
        assertEquals(brands, returnedBrands);
        verify(repository, times(1)).findAll();
    }

    @Test
    public void givenAnBrandIdWhenFindingABrandWithIdThenABrandIsReturned() throws Exception {

        String id = "id";
        Brand brand = BrandData.getBrandMock(id);
        when(repository.findById(eq(id))).thenReturn(Optional.of(brand));

        Brand returnedBrand = service.findById(id);

        assertNotNull(returnedBrand);
        assertEquals(brand, returnedBrand);
        verify(repository, times(1)).findById(eq(id));
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenAnInexistentBrandIdWhenFindingABrandWithIdThenExceptionIsThrown() throws Exception {
        String id = "non-existent";
        when(repository.findById(eq(id))).thenReturn(Optional.empty());

        service.findById(id);

        expectedException.expectMessage(String.format("Brand %s not found.", id));
        expectedException.expect(EntityNotFoundException.class);
        verify(repository, times(1)).findById(eq(id));
    }
}