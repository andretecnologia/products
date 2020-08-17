package com.company.validator;

import com.company.model.BaseModel;
import com.company.model.Brand;
import com.company.model.Product;
import com.company.repository.BrandRepository;
import com.company.repository.ProductRepository;
import org.springframework.data.repository.CrudRepository;

public class ValidatorFactory {
    public static <T extends BaseModel> ModelValidator getValidator(Class<T> clazz, CrudRepository repository) {
        if (Product.class.equals(clazz))
            return new ProductValidator((ProductRepository) repository);

        if (Brand.class.equals(clazz))
            return new BrandValidator((BrandRepository) repository);

        return null;
    }
}
