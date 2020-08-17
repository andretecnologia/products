package com.company.service;

import com.company.exception.EntityNotFoundException;
import com.company.exception.ServiceException;
import com.company.model.Brand;
import com.company.repository.BrandRepository;
import com.company.repository.ProductRepository;
import com.company.validator.ModelValidator;
import com.company.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private BrandRepository brandRepository;
    private ProductRepository productRepository;
    private ModelValidator validator;

    public BrandService(@Autowired BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
        this.validator = ValidatorFactory.getValidator(Brand.class, brandRepository);
    }

    public Brand insertBrand(Brand brand) throws Exception {
        validator.validateInsert(brand);
        return brandRepository.insert(brand);
    }

    public Brand updateBrand(Brand brand) throws ServiceException {
        validator.validateId(brand.getId());
        Brand retrievedBrand = brandRepository.findById( brand.getId() ).get();
        return brandRepository.save( (Brand) validator.customUpdateValidation(brand, retrievedBrand));
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(String id) throws ServiceException {
        return brandRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Brand %s not found.", id);
            return new EntityNotFoundException(message);
        });
    }

    public void delete(String id) throws ServiceException {
        validator.validateId(id);
        brandRepository.deleteById(id);
    }
}
