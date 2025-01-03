package com.ve.inventory_billing.controllers;

import com.ve.inventory_billing.entities.Product;
import com.ve.inventory_billing.exchange.dto.CreateProductDto;
import com.ve.inventory_billing.exchange.dto.PaginationResponseDto;
import com.ve.inventory_billing.exchange.dto.UpdateProductDto;
import com.ve.inventory_billing.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@CacheEvict
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    CreateProductDto save(@RequestBody CreateProductDto createProductDto) throws BadRequestException {
        return this.productService.save(createProductDto);
    }

    @PutMapping("/{id}")
    UpdateProductDto update(@RequestBody UpdateProductDto updateProductDto, @PathVariable Integer id) throws BadRequestException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return this.productService.update(updateProductDto, id);
    }

    @GetMapping
    PaginationResponseDto<Product> pagination(
            @RequestParam(required = false) String search,
            @RequestParam(required = true) Integer page,
            @RequestParam(required = true) Integer showRows
    ) {
        return this.productService.findAll(
                search, page, showRows
        );
    }

    @GetMapping("/{id}")
    Optional<Product> findById(@PathVariable Integer id) {
        return this.productService.findById(id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        this.productService.deleteById(id);
    }
}
