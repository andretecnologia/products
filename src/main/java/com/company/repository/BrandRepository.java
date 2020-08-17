package com.company.repository;

import com.company.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BrandRepository extends MongoRepository<Brand, String> {

    Optional<Brand> findByName(String name);

    Optional<Brand> findByNameIgnoreCase(String name);

    Optional<Brand> findById(String s);
}
