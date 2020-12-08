package com.company.validator;

import com.company.exception.DuplicatedNameException;
import com.company.exception.ServiceException;
import com.company.model.Product;
import com.company.repository.ProductRepository;

import java.util.Objects;

public class ProductValidator extends ModelValidator<Product> {

    public ProductValidator(ProductRepository repository) {
        super(repository);
    }

    // TODO remover Agora eu uso o @Indexed(unique = true)

    @Override
    public void customInsertValidation(Product product) throws ServiceException {
        ProductRepository repository = (ProductRepository) getRepository();
        if (repository.findByNameContaining(product.getName()).isPresent())
            throw new DuplicatedNameException(String.format("Name [%s] already exist", product.getName()));
    }

    @Override
    public Product customUpdateValidation(Product product, Product retrievedProduct) throws ServiceException {
        if ( Objects.nonNull(product.getName() ) && !product.getName().equals( retrievedProduct.getName()) )
            customInsertValidation(product);
        if ( Objects.nonNull(product.getName()) )
            retrievedProduct.setName(product.getName());
        if ( Objects.nonNull(product.getBrand()) )
            retrievedProduct.setBrand(product.getBrand());
        if ( Objects.nonNull(product.getDescription()) )
            retrievedProduct.setDescription( product.getDescription());
        if ( Objects.nonNull(product.getPrice()) )
            retrievedProduct.setPrice( product.getPrice());
        return retrievedProduct;
    }
}
