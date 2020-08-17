package com.company.service;

import com.company.exception.EntityNotFoundException;
import com.company.exception.ServiceException;
import com.company.model.Product;
import com.company.repository.ProductRepository;
import com.company.validator.ModelValidator;
import com.company.validator.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    private ProductRepository productRepository;
    private ModelValidator validator;

    public ProductService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.validator = ValidatorFactory.getValidator(Product.class, productRepository);
    }

    public Product insert(Product product) throws ServiceException {
        validator.validateInsert(product);
        return productRepository.insert(product);
    }

    public List<Product> insert(List<Product> products) throws ServiceException {
        validator.validateInsert(products);
        return productRepository.insert(products);
    }

    public Product update(Product product) throws ServiceException {
        validator.validateId(product.getId());
        Product retrievedProduct = productRepository.findById( product.getId() ).get();
        return productRepository.save( (Product) validator.customUpdateValidation(product, retrievedProduct));
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(String id) throws ServiceException {
        String message = String.format("Product %s not found", id);
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));
    }

    public void delete(String id) throws ServiceException {
        validator.validateId(id);
        productRepository.deleteById(id);
    }

    public Page<Product> findByName(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable);
    }
}
