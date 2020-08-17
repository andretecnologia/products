package com.company.controller;

import com.company.controller.dto.BrandDTO;
import com.company.model.Brand;
import com.company.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.company.adapter.BrandAdapter.dtoToEntity;
import static com.company.adapter.BrandAdapter.entityToDto;

@RestController
@RequestMapping(path = "/brands")
public class BrandController {
    @Autowired
    private BrandService service;

    public BrandController(@Autowired BrandService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandDTO> insert(@RequestBody BrandDTO brand) throws Exception {
        return ResponseEntity.ok(entityToDto(service.insertBrand(dtoToEntity(brand))));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandDTO> getById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(entityToDto(service.findById(id)));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrandDTO> update(@RequestBody BrandDTO brand, @PathVariable String id) throws Exception {
        brand.setId(id);
        return ResponseEntity.ok(entityToDto(service.updateBrand(dtoToEntity(brand))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Brand>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
