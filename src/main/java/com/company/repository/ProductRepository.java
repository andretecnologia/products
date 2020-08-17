package com.company.repository;

import com.company.model.Brand;
import com.company.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findByNameContaining(String name, Pageable pageable);

    Optional<Product> findByNameContaining(String name);

    Optional<Product> findByNameIgnoreCase(String name);

    List<Product> findByBrand(Brand brand);
}
