package com.company.controller;

import com.company.controller.dto.ProductDTO;
import com.company.model.Product;
import com.company.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.company.adapter.ProductAdapter.dtoToEntity;
import static com.company.adapter.ProductAdapter.entityToDto;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    @Autowired
    ProductService service;

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO product) throws Exception {
        return ResponseEntity.ok(entityToDto(service.insert(dtoToEntity(product))));
        // TODO 201 Created
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO product, @PathVariable String id) throws Exception {
        // TODO Ver se existe, retornar 404 not found
        product.setId(id);
        return ResponseEntity.ok(entityToDto(service.update(dtoToEntity(product))));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(entityToDto(service.findById(id)));
        // TODO 404 Not Found
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> insert(@RequestBody List<Product> products) throws Exception {
        return ResponseEntity.ok(service.insert(products));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Stream<Product>> getAll(@RequestParam(name = "name", required = false) String name,
                                                  @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.ASC)}) Pageable pageable) {
        if(Objects.nonNull(name))
            return ResponseEntity.ok(service.findByName(name, pageable).get());
        return ResponseEntity.ok(service.findAll(pageable).get());
    }
}
