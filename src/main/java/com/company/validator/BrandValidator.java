package com.company.validator;

import com.company.exception.DuplicatedNameException;
import com.company.exception.EntityInUseException;
import com.company.exception.ServiceException;
import com.company.model.Brand;
import com.company.repository.BrandRepository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class BrandValidator extends ModelValidator<Brand> {

    public BrandValidator(BrandRepository repository) {
        super(repository);
    }

    @Override
    public void customInsertValidation(Brand brand) throws ServiceException {
        BrandRepository repository = (BrandRepository) getRepository();
        if (repository.findByName(brand.getName()).isPresent())
            throw new DuplicatedNameException(String.format("Brand [%s] already exist", brand.getName()));
    }

    @Override
    public Object customUpdateValidation(Brand brand, Brand retrievedBrand) throws ServiceException {
        if ( !brand.getName().equals( retrievedBrand.getName()) )
        customInsertValidation(brand);
        if ( Objects.nonNull(brand.getName()) )
            retrievedBrand.setName(brand.getName());
        return retrievedBrand;
    }

    public void customDeleteValidation(List<Brand> brands) throws ServiceException {
        if (CollectionUtils.isEmpty(brands))
            throw new EntityInUseException(String.format("You may not delete this record once it is in use"));
    }
}
